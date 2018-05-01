package com.test.dao.file_dir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.test.db.DBManage;
import com.test.tools.Tools;

public class UploadTableManager  {
	// ���ݿ����
	private DBManage dbmanage ;
	private Connection connection ;
	private PreparedStatement preparedStatement ;
	private ResultSet resultSet ;
	
	public FileManager fileManager ;
	private HdfsTools hdfsTool ;
	
	public UploadTableManager() {
		this.dbmanage = new DBManage() ;
		this.fileManager = new FileManager() ;
		this.hdfsTool = new HdfsTools() ;
	}
	public boolean reupload(String filename) {
		int tempFileId = -1;
		try {
			String queryReuploadFileId = "select tf_id,tf_state from temp_upload_file where u_id = ? and tf_name = ? " ;
			
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.connection.prepareStatement(Tools.QUERY_REUPLOAD_FILE_ID) ;
			this.preparedStatement = this.connection.prepareStatement(queryReuploadFileId) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			if(this.resultSet.next()) {
				tempFileId = this.resultSet.getInt("tf_id") ;
				String uploadState = this.resultSet.getString("tf_state") ;
		    	//�������ݿ���ʱ�ļ���״̬Ϊ���ϴ��С�
		    	this.fileManager.updateTempUploadFileState(tempFileId,"�ϴ���") ;
				if(uploadState.equals("ʧ��")) {
					// ɾ��ԭ�м�¼�������µĿ��ϴ���¼
					System.out.println("ʧ���ش�������δʵ��");
				}else {
					System.out.println("���ļ����ϴ��ɹ��������ϴ��У�����Ҫ�����ش�");
				}
			}
		}catch(Exception e) {
			e.printStackTrace() ;
			return false ;
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
			
		return true ;
	}
	/**
	 * ɾ���ϴ�����
	 * @param filename 
	 * @return
	 */
	public boolean deleteUploadAssignment(String filename) {
		try {
			// ɾ��HDFS�ļ�
			String deleteHdfsErrorFile = "select b_name from " +
			"(select f_id from " +
			"(select tf_name , parent_id " +
			"from temp_upload_file " +
			"where u_id = ? and tf_name = ? and tf_state = ?) a , file " +
			"where a.tf_name = file.f_name and a.parent_id = file.parent_id ) b ,fileblock " +
			"where b.f_id = fileblock.f_id" ;
			this.connection = this.dbmanage.getConnection() ;
			this.preparedStatement = this.connection.prepareStatement(deleteHdfsErrorFile) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.preparedStatement.setString(3, "ʧ��") ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			while(this.resultSet.next()) {
				String blockName = this.resultSet.getString("b_name") ;
				System.out.println("ɾ���ļ� " + blockName);
				this.hdfsTool.deleteHdfsFile("/" + blockName) ;
			}
			// ɾ�����ݿ��¼
			String deleteUploadAssignment = "delete from temp_upload_file where u_id = ? and tf_name = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.connection.prepareStatement(Tools.DELETE_UPLOAD_ASSIGNMENT) ;
			this.preparedStatement = this.connection.prepareStatement(deleteUploadAssignment) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.preparedStatement.execute() ;
			
			// ��ȡ�ļ��ϴ�ʧ�ܺ���Ҫɾ������ʱHDFS�ļ�
//			String getFilenameAndParentId = "select tf_name , parent_id from temp_upload_file where u_id = ? and tf_name = ?" ;
//			String getFileId = "select f_id from file where f_name = ? and parent_id = ?" ;
//			String getHdfsFileName = "select b_name from fileblock where f_id = ?" ;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false ;
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
		return true ;
	}
	
	/**
	 * ����ϴ��б����Ƿ�������������
	 */
	public boolean checkUploadTable(String filename) {
		try {
			String checkUploadTable = "select tf_name from temp_upload_file where u_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.connection.prepareStatement(Tools.CKECK_UPLOAD_TABLE) ;
			this.preparedStatement = this.connection.prepareStatement(checkUploadTable) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			while(this.resultSet.next()) {
				String tempFileName = this.resultSet.getString("tf_name") ;
				if(tempFileName.equals(filename)) {
					return false ;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false ;
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
		return true ;
	}
	
	/**
	 * ���ش�ǰɾ��ԭ�п�ļ�¼
	 */
//	public boolean deleteBlockRecord(int blockId) {
//		try {
//			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.connection.prepareStatement(Tools.DELETE_BLOCK_UPLOAD_RECORD) ;
//			this.preparedStatement.setInt(1, blockId) ;
//			this.preparedStatement.execute() ;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return false ;
//		}finally {
//			// �ر�����
//			this.dbmanage.closeLink() ;
//		}
//		return true ;
//	}

}
