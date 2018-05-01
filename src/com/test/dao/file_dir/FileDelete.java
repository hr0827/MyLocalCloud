package com.test.dao.file_dir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.test.db.DBManage;
import com.test.tools.Tools;

public class FileDelete implements Callable<Boolean>{
	// ���ݿ����
	public DBManage dbmanage ;
	public Connection connection ;
	public PreparedStatement preparedStatement ;
	public ResultSet resultSet ;

	private String fileName ;
	private int parentId ;
	// ɾ���ļ�ʱ�õ���ջ
	private Stack<Integer> deleteFileStack = new Stack<Integer>() ;
	
	private HdfsTools hdfsTool ;
	
	public FileDelete(String fileName , int parentId) {
		this.dbmanage = new DBManage() ;
		this.fileName = fileName ;
		this.parentId = parentId ;
		this.hdfsTool = new HdfsTools() ;
	}
	
	/**
	 * ɾ���ļ������ļ���
	 * filename���ļ���
	 * parentId�Ǳ�ɾ�����ļ��ĸ��ڵ�ID
	 */
	public boolean delete(String filename , int parentId) {
		String filetype = "";
		int fileId = -1 ;
		/**
		 * �����ļ����͸�ID��ѯ�ļ��������ͺ��ļ�ID��
		 */
		try {
			String queryFileType = "select f_id,f_type,f_size from file where u_id = ? and f_name = ? and parent_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_FILE_TYPE) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(queryFileType) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.preparedStatement.setInt(3, parentId) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			if(this.resultSet.next()) {
				filetype = this.resultSet.getString("f_type") ;
				fileId = this.resultSet.getInt("f_id") ;
			}
			// ���ļ���ֱ��ɾ�����ļ���Ҫ��������
			if(!filetype.equals("") && filetype.equals("�ļ�")) {
				// ����ɾ���̣߳�ɾ��HDFS�е��ļ�
				String getFileBlockNameInHdfs = "select b_name from fileblock where f_id = ?" ;
				this.connection = this.dbmanage.getConnection() ;
				this.preparedStatement = this.dbmanage.getPreparedStatement(getFileBlockNameInHdfs) ;
				this.preparedStatement.setInt(1, fileId) ;
				this.resultSet = this.preparedStatement.executeQuery() ;
				while(this.resultSet.next()) {
					String fileBlockName = this.resultSet.getString("b_name") ;
					this.hdfsTool.deleteHdfsFile("/" + fileBlockName) ;
					
					// ɾ�����ݿ��ļ���¼
					String deleteFileRecord = "delete from file where f_id = ?" ;
					this.preparedStatement = this.dbmanage.getPreparedStatement(deleteFileRecord) ;
					this.preparedStatement.setInt(1, fileId) ;
					this.preparedStatement.execute() ;
				}
			}else {
				// Ҫɾ�������ļ��У���������������
				if(fileId != -1) {
					deleteFileStack.add(fileId) ;
					querySubFile() ;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false ;
		} finally {
			// �ر�����
			if(this.resultSet != null) {
				try {
					this.resultSet.close() ;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.dbmanage.closeLink() ;
		}
		return true ;
	}
	/**
	 * ��ѯһ���ļ��İ��������е����ļ��к��ļ�
	 */
	public void querySubFile() {
		ResultSet rs = null ;
		int parentId = deleteFileStack.lastElement() ;
		try {
			String querySubFile = "select f_id,f_type,f_name from file where u_id = ? and parent_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_SUB_FILE) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(querySubFile) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setInt(2, parentId) ;
			rs = this.preparedStatement.executeQuery() ;
			System.out.println("ɾ��1");
			while(rs.next()) {
				System.out.println("ɾ��2");
				String filetype = rs.getString("f_type") ;
				int fileId = rs.getInt("f_id") ;
				String filename = rs.getString("f_name") ;
				/**
				 * ���ļ����ļ��о͵ݹ鴦��
				 */
				if(filetype.equals("�ļ���")) {
					System.out.println("�����ļ���");
					deleteFileStack.add(fileId) ;
					querySubFile() ;
				}else {
					/**
					 * ���ļ����ļ���ֱ��ɾ��
					 */
					System.out.println("�����ļ�");
					delete(filename , deleteFileStack.lastElement()) ;
				}
			}
			System.out.println("ɾ��3");
			// �ر����ݿ�����
			if(rs != null) {
				rs.close() ;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// �ر�����
			if(this.resultSet != null) {
				try {
					this.resultSet.close() ;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.dbmanage.closeLink() ;
		}
		try {
			int id = deleteFileStack.pop() ;
			this.resultSet = null ;
			String checkDeleteFile = "select * from file where parent_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.CHECK_DELETE_FILE) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(checkDeleteFile) ;
			this.preparedStatement.setInt(1, id) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			System.out.println(id + " ɾ��4 " + this.resultSet);
			if(!this.resultSet.next()) {
				System.out.println("ɾ��5");
				String deleteFileById = "delete from file where f_id = ?" ;
				this.connection = this.dbmanage.getConnection() ;
//				this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.DELETE_FILE_BY_ID) ;
				this.preparedStatement = this.dbmanage.getPreparedStatement(deleteFileById) ;
				this.preparedStatement.setInt(1, id) ;
				this.preparedStatement.execute() ;
				System.out.println("�ݹ����ɾ���ļ���");
				System.out.println("��ʱջ�е����ݣ� " + deleteFileStack);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			// �ر�����
			if(this.resultSet != null) {
				try {
					this.resultSet.close() ;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.dbmanage.closeLink() ;
		}
	}


	@Override
	public Boolean call() {
		// TODO Auto-generated method stub
		return delete(fileName , parentId) ;
	}
	

}
