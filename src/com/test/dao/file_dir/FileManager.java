package com.test.dao.file_dir;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import com.test.db.DBManage;
import com.test.tools.Tools;
import com.test.ui.FilePanel;
/**
 * �ļ�����������
 * @author asus
 *
 */
public class FileManager {
	// �ļ��ָ�
	public FileSeperate fileSeperate ;
	// �ļ��ϲ�
	public FileCombination fileCombination ;
	// ɾ���ļ�ʱ�õ���ջ
	private Stack<Integer> deleteFileStack = new Stack<Integer>() ;
	
	// ���ݿ����
	public DBManage dbmanage ;
	public Connection connection ;
	public PreparedStatement preparedStatement ;
	public ResultSet resultSet ;
	public HdfsTools hdfsTool ;

	public FileManager(DBManage dbmanage) {
		this.dbmanage = dbmanage ;
		this.hdfsTool = new HdfsTools() ;
	}
	public FileManager() {
		this.dbmanage = new DBManage() ;
		this.hdfsTool = new HdfsTools() ;
	}
	// ��ʾ�ļ��б�
	public void showFile(Icon icon , String fileName) {
		Tools.loginSuccess.filePanel = new FilePanel(icon , fileName) ;
		Tools.loginSuccess.fileBottomPanel.add(Tools.loginSuccess.filePanel) ;
		Tools.loginSuccess.repaint() ;
	}
	// ��ʾ�ļ��б�
	public void showFileList(int parentId , int userId) {
		try {
			String showFile = "select * from file where parent_id = ? and u_id = ? order by f_type DESC" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.SHOW_FILE) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(showFile) ;
			this.preparedStatement.setInt(1, parentId) ;
			this.preparedStatement.setInt(2, userId) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			while(this.resultSet.next()) {
				// ȡ���ļ���
				String fileName = this.resultSet.getString("f_name") ;
				String filetype = this.resultSet.getString("f_type") ;
				// ��ʾ�ļ�ͼ��
				if(filetype.equals("�ļ�")) {
					this.showFile(new ImageIcon("image/file.png") , fileName) ;
				}else if(filetype.equals("�ļ���")) {
					this.showFile(new ImageIcon("image/dir.png") , fileName) ;
				}
			}
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
	// �����ļ���
	public void createDir() {
		//�������1000���ڵ���������
		String filename  = "�½��ļ���"+ (int)(Math.random()*1000);
		Date date = new Date();
		String type = "�ļ���";
		long size = 0 ;
		//����������ļ�������������������������֪��������Ϊֹ
		while (!checkFileName(filename)) {
			filename  = "�½��ļ���"+ (int)(Math.random()*1000);
		}
		try {
			String createDir = "insert into File (u_id , f_name , f_date , f_type , f_size , parent_id) values(? , ? , ? , ? , ? , ?)" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.CREATE_DIR) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(createDir) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.preparedStatement.setString(3, date.toString()) ;
			this.preparedStatement.setString(4, type) ;
			this.preparedStatement.setLong(5, size) ;
			this.preparedStatement.setInt(6, Tools.parentIdStack.lastElement()) ;
			this.preparedStatement.execute() ;
			// �½����ļ����ڽ�������ʾ����
			Tools.loginSuccess.filePanel = new FilePanel(new ImageIcon("image/dir.png") , filename) ;
			Tools.loginSuccess.fileBottomPanel.add(Tools.loginSuccess.filePanel) ;
			Tools.loginSuccess.repaint();
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
	}
	
	/**
	 * ����ļ��Ƿ�����,ͨ���ļ����ͺ��ļ�����ѯ
	 */
	public boolean checkFileName(String filename) {
		try {
			String checkFileName = "select * from File where u_id = ? and f_name = ? and parent_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.CHECK_FILE_NAME) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(checkFileName) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.preparedStatement.setInt(3, Tools.parentIdStack.lastElement()) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			while(this.resultSet.next()) {
				return false ;
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
	 * ����ļ��Ƿ�����,ͨ���ļ�ID�Ų�ѯ
	 */
	public boolean checkPasteFileName() {
		try {
			String checkPasteFileName = "select * from File where f_name = (select f_name from File where f_id = ?) and u_id = ? and parent_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.CHECK_PASTE_FILE_NAME) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(checkPasteFileName) ;
			this.preparedStatement.setInt(1, Tools.cutFileId) ;
			this.preparedStatement.setInt(2, Tools.userId) ;
			this.preparedStatement.setInt(3, Tools.parentIdStack.lastElement()) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			if(this.resultSet.next()) {
				JOptionPane.showMessageDialog(null, "���ļ����Ѵ��ڣ�����ճ��", "������Ϣ", 1);
				return false ;
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
	 * ���ļ���
	 */
	public boolean openDir(String filename) {
		try {
			String queryFileId = "select * from File where u_id = ? and f_name = ? and parent_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_FILE_ID) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(queryFileId) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.preparedStatement.setInt(3, Tools.parentIdStack.lastElement()) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			int fileId = -1;
			String filetype = null ;
			if(this.resultSet.next()) {
				fileId = this.resultSet.getInt("f_id") ;
				filetype = this.resultSet.getString("f_type") ;
			}
			// ˫�����ļ���
			if(filetype != null && filetype.equals("�ļ���")) {
				Tools.parentIdStack.add(fileId) ;
				Tools.loginSuccess.fileBottomPanel.removeAll() ;
				this.showFileList(Tools.parentIdStack.lastElement(), Tools.userId) ;
				Tools.loginSuccess.fileBottomPanel.repaint() ;
				// �����ļ�·��
				String context = Tools.loginSuccess.search.getText() ;
				Tools.loginSuccess.search.setText(context + "\\" + filename) ;
			}else if(filetype != null){
				// ˫���ļ���������
				int res = JOptionPane.showConfirmDialog(null, "ȷ�����ظ��ļ���\n(�ļ���ŵ�ַΪ��E:\\SafeCloudFiles)", "������ʾ", JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
				if(res == JOptionPane.OK_OPTION) {
					if(new DownloadTableManager().checkDownloadTable(filename)) {
						FileDownload filedownload = new FileDownload(fileId,filename,"E:\\SafeCloudFiles\\downloadtemp\\");
						new Thread(filedownload).start();
					}else {
    					JOptionPane.showMessageDialog(null , "���ļ����������б��У�����","������ʾ" , 1);
					}
				}else {
					System.out.println("������");
				}
				return false ;
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
	 * �����ļ���
	 */
	public boolean cutFile(String filename) {
		try {
			String queryFileId = "select * from File where u_id = ? and f_name = ? and parent_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_FILE_ID) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(queryFileId) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.preparedStatement.setInt(3, Tools.parentIdStack.lastElement()) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			if(this.resultSet.next()) {
				Tools.cutFileId = this.resultSet.getInt("f_id") ;
				Tools.copyFileSize = this.resultSet.getLong("f_size") ;
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
	 * ճ���ļ���
	 */
	public boolean pasteFile() {
		if(checkPasteFileName()) {
			try {
				// ���е��ļ�����
				if(Tools.cutFileId == -1 ) {
					System.out.println("ճ�����ļ�������");
					return false ;
				}else if(Tools.cutFileId == Tools.parentIdStack.lastElement() ){
					JOptionPane.showMessageDialog(null, "Ŀ���ļ�����ԭ�ļ��е����ļ���", "������Ϣ", 1);
					System.out.println("���ܼ���ճ���Լ�");
					return false ;
				}else {
					String pasteFile = "update File set parent_id = ? where f_id = ?" ;
					this.connection = this.dbmanage.getConnection() ;
//					this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.PASTE_FILE) ;
					this.preparedStatement = this.dbmanage.getPreparedStatement(pasteFile) ;
					this.preparedStatement.setInt(1, Tools.parentIdStack.lastElement()) ;
					this.preparedStatement.setInt(2, Tools.cutFileId) ;
					this.preparedStatement.executeUpdate() ;	
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
		}
		return true ;
	}
	
	/**
	 * �ļ�������
	 */
	public boolean rename(String new_filename , String old_filename) {
		if(checkFileName(new_filename)) {
			try {
				String renameFile = "update file set f_name = ? where u_id = ? and parent_id = ? and f_name = ?" ;
				this.connection = this.dbmanage.getConnection() ;
//				this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.RENAME_FILE) ;
				this.preparedStatement = this.dbmanage.getPreparedStatement(renameFile) ;
				this.preparedStatement.setString(1, new_filename) ;
				this.preparedStatement.setInt(2, Tools.userId) ;
				this.preparedStatement.setInt(3, Tools.parentIdStack.lastElement()) ;
				this.preparedStatement.setString(4, old_filename) ;
				this.preparedStatement.executeUpdate() ;
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
		}else {
			JOptionPane.showMessageDialog(null, "���ļ����Ѵ���", "������Ϣ", 1);
			return false ;
		}
		return true ;
	}
	
	/*
	 * �����ļ��������ļ�
	 * filename �ļ���
	 * ͨ���ļ�����ѯ��֮ƥ����ļ������鴦�ļ������ļ����ͣ��ļ����ڵ�ID���������ڵ�ID��ջ
	 */
	public Map<String,String> searchFiles(String filename) {
		Map<String,String> files = new HashMap<String,String>() ;
		try {
			String searchFile = "select f_name,f_type,parent_id from file where u_id = ? and f_name = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.SEARCH_FILE) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(searchFile) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			// �鵽���ļ�����¼�ļ��������ͣ����ڵ�ID
			if(this.resultSet.next()) {
				String name = this.resultSet.getString("f_name") ;
				String type = this.resultSet.getString("f_type") ;
				int id = this.resultSet.getInt("parent_id") ;
				// ��ε����ѯͬһ���ļ�ʱʹ��
				if(Tools.parentIdStack.lastElement() != id) {
					Tools.parentIdStack.add(id) ;
				}
				files.put(name, type) ;
			}else {
				// ��ѯʧ����ʹ�ã����ڷ�����һ��Ŀ¼
				if(Tools.parentIdStack.lastElement() != -1) {
					Tools.parentIdStack.add(-1) ;
				}
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
		// ���ز�ѯ�����ļ������֡����ͼ�ֵ��
		return files ;
	}
	
	/**
	 * ͨ���ļ�����ѯ�ļ�ID��
	 */
	public int queryFileIdByFileName(String filename ,int parentid) {
		int fileId = -1;
		try {
			String queryFileNameByFileId = "select f_id from File where u_id = ? and f_name = ? and parent_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_FILE_ID_BY_FILE_NAME) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(queryFileNameByFileId) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.preparedStatement.setInt(3, parentid) ;
//			Tools.preparedStatement.setInt(3, Tools.parentIdStack.lastElement()) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			if(this.resultSet.next()) {
				fileId = this.resultSet.getInt("f_id") ;
			}
		}catch(Exception ee) {
			ee.printStackTrace() ;
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
		return fileId ;
	}
	
	
	/** 
	  * ɾ���ļ����������ļ����ļ��� 
	  *  
	  * @param fileName 
	  *            Ҫɾ�����ļ��� 
	  * @return ɾ���ɹ�����true�����򷵻�false 
	  */  
	 public boolean delete(String fileName) {  
		 File file = new File(fileName);  
		 if (!file.exists()) {  
			 System.out.println("ɾ���ļ�ʧ��:" + fileName + "�����ڣ�");  
			 return false;  
		 } else {  
			 if (file.isFile())  
				 return deleteFile(fileName);  
			 else  
				 return deleteDirectory(fileName);  
		}  
	 }  
	  
	 /** 
	  * ɾ�������ļ� 
	  *  
	  * @param fileName 
	  *            Ҫɾ�����ļ����ļ��� 
	  * @return �����ļ�ɾ���ɹ�����true�����򷵻�false 
	  */  
	 public boolean deleteFile(String fileName) {  
		 File file = new File(fileName);  
		 // ����ļ�·������Ӧ���ļ����ڣ�������һ���ļ�����ֱ��ɾ��  
		 if (file.exists() && file.isFile()) {  
			 if (file.delete()) {  
				 System.out.println("ɾ�������ļ�" + fileName + "�ɹ���");  
				 return true;  
			 } else {  
				 System.out.println("ɾ�������ļ�" + fileName + "ʧ�ܣ�");  
				 return false;  
			 }  
		 } else {  
			 System.out.println("ɾ�������ļ�ʧ�ܣ�" + fileName + "�����ڣ�");  
			 return false;  
		 }  
	 }  
	  
	 /** 
	  * ɾ��Ŀ¼��Ŀ¼�µ��ļ� 
	  *  
	  * @param dir 
	  *            Ҫɾ����Ŀ¼���ļ�·�� 
	  * @return Ŀ¼ɾ���ɹ�����true�����򷵻�false 
	  */  
	 public boolean deleteDirectory(String dir) {  
		 // ���dir�����ļ��ָ�����β���Զ�����ļ��ָ���  
		 if (!dir.endsWith(File.separator))
			 dir = dir + File.separator;  
		 File dirFile = new File(dir);  
		 // ���dir��Ӧ���ļ������ڣ����߲���һ��Ŀ¼�����˳�  
		 if ((!dirFile.exists()) || (!dirFile.isDirectory())) {  
			 System.out.println("ɾ��Ŀ¼ʧ�ܣ�" + dir + "�����ڣ�");  
			 return false;  
		 }  
		 boolean flag = true;  
		 // ɾ���ļ����е������ļ�������Ŀ¼  
		 File[] files = dirFile.listFiles();  
		 for (int i = 0; i < files.length; i++) {  
			 // ɾ�����ļ�  
			 if (files[i].isFile()) {  
				 flag = deleteFile(files[i].getAbsolutePath());  
				 if (!flag)  
					 break;  
			 }  
			 // ɾ����Ŀ¼  
			 else if (files[i].isDirectory()) {  
				 flag = deleteDirectory(files[i].getAbsolutePath());  
				 if (!flag)  
					 break;  
			 }  
		 }  
		 if (!flag) {  
			 System.out.println("ɾ��Ŀ¼ʧ�ܣ�");  
			 return false;  
		 }  
		 // ɾ����ǰĿ¼  
		 if (dirFile.delete()) {  
			 System.out.println("ɾ��Ŀ¼" + dir + "�ɹ���");  
			 return true;  
		 } else {  
			 return false;  
		 }  
	 }
	
	
	
	
	/**
	 * ������������õ��ַ���
	 */
	 public String generateSecretString(){
			String str = "" ;
			String base = "abcdefghijklmnopqrstuvwxyzABCDEGHIJKLMNOPQRSTUVWSYZ0123456789"; 

		   SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//�������ڸ�ʽ
		   String currentDate = df.format(new Date());// new Date()Ϊ��ȡ��ǰϵͳʱ��
			
			System.out.println(currentDate);
			Random random = new Random(); 
			
			int fileNameLength = random.nextInt(base.length()) + 1;
			System.out.println(fileNameLength);
			while(fileNameLength < 20) {
				fileNameLength = random.nextInt(base.length()) + 1;
				System.out.println(fileNameLength);
			}
			
			StringBuffer sb = new StringBuffer(); 
		    for (int i = 0; i < fileNameLength; i++) {
		        int number = random.nextInt(base.length());
		        sb.append(base.charAt(number));
		        }
		    str = sb.toString() + currentDate; 
		    return str ;
		}
	
	/*
	 * �洢�ļ���¼
	 */
	public boolean saveFile(String filename ,String filedate , long filesize , int parentid) {
		try {
			String saveFile = "insert into file (u_id,f_name,f_date,f_type,f_size,parent_id) values(?,?,?,?,?,?)" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.SAVE_FILE) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(saveFile) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.preparedStatement.setString(3, filedate) ;
			this.preparedStatement.setString(4, "�ļ�") ;
			this.preparedStatement.setLong(5, filesize) ;
			this.preparedStatement.setInt(6, parentid) ;
//			Tools.preparedStatement.setInt(6, Tools.parentIdStack.lastElement()) ;
			this.preparedStatement.execute() ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// �ر����ݿ�����
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
	
	/*
	 * �洢�ļ����¼
	 */
	public boolean saveFileBlock(int fileId ,String fileBlockName ,String covername,int netDiskId , String secretKey) {
		try {
			String saveFileBlock = "insert into fileblock (f_id,b_name,b_covername,netdisk_id,f_key) values(?,?,?,?,?)" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.SAVE_FILE_BLOCK) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(saveFileBlock) ;
			this.preparedStatement.setInt(1, fileId) ;
			this.preparedStatement.setString(2, fileBlockName) ;
			this.preparedStatement.setString(3, covername) ;
			this.preparedStatement.setInt(4, netDiskId) ;
			this.preparedStatement.setString(5, secretKey) ;
			this.preparedStatement.execute() ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// �ر����ݿ�����
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
	 * �鿴�ļ�����
	 */
	public String getProperty(String filename) {
		String file_date = "" ;
		String file_type = "" ;
		long file_size = 0 ;
		try {
			String getFileProperty = "select * from file where u_id = ? and f_name = ? and parent_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.GET_FILE_PROPERTY) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(getFileProperty) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.preparedStatement.setInt(3, Tools.parentIdStack.lastElement()) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			
			if(this.resultSet.next()) {
				file_date = this.resultSet.getString("f_date") ;
				file_type = this.resultSet.getString("f_type") ;
				file_size = this.resultSet.getLong("f_size") ; 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "" ;
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
		return "�ļ�����" + filename + "\n�������ڣ�" + file_date + "\n�ļ����ͣ�" + file_type + "\n��С��" + (int)file_size/(1024*1024) + "M" ;
	}
	
	/**
	 * ͨ���ļ�����ѯ��ʱ�ϴ��ļ�ID��
	 */
	public int queryUploadFileIdByTempFileName(String filename ,int parentid) {
		int fileId = -1;
		try {
			String queryUploadFileIdByTempFileName = "select tf_id from temp_upload_file where u_id = ? and tf_name = ? and parent_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_UPLOAD_FILE_ID_BY_TEMP_FILE_NAME) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(queryUploadFileIdByTempFileName) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.preparedStatement.setInt(3, parentid) ;
//			Tools.preparedStatement.setInt(3, Tools.parentIdStack.lastElement()) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			if(this.resultSet.next()) {
				fileId = this.resultSet.getInt("tf_id") ;
			}
		}catch(Exception ee) {
			ee.printStackTrace() ;
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
		return fileId ;
	}
	
	/**
	 * ͨ���ļ�����ѯ��ʱ�����ļ�ID��
	 */
	public int queryDownloadFileIdByTempFileName(String filename ,int parentid) {
		int fileId = -1;
		try {
			String queryDownloadFileIdByTempFileName = "select tf_id from temp_download_file where u_id = ? and tf_name = ? and parent_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_DOWNLOAD_FILE_ID_BY_TEMP_FILE_NAME) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(queryDownloadFileIdByTempFileName) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.preparedStatement.setInt(3, parentid) ;
//			Tools.preparedStatement.setInt(3, Tools.parentIdStack.lastElement()) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			if(this.resultSet.next()) {
				fileId = this.resultSet.getInt("tf_id") ;
			}
		}catch(Exception ee) {
			ee.printStackTrace() ;
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
		return fileId ;
	}
	
	/*
	 * ��ʱ���д����ļ��ϴ���¼
	 */
	public boolean saveTempUploadFile(String filename ,String filedate , int parentid) {
		try {
			String saveTempUploadFile = "insert into temp_upload_file (u_id,tf_name,tf_date,tf_tasktype,tf_type,parent_id,tf_state) values(?,?,?,?,?,?,?)" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.SAVE_TEMP_UPLOAD_FILE) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(saveTempUploadFile) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.preparedStatement.setString(3, filedate) ;
			this.preparedStatement.setString(4, "�ϴ�����") ;
			this.preparedStatement.setString(5, "�ļ�") ;
			this.preparedStatement.setInt(6, parentid) ;
			this.preparedStatement.setString(7, "׼����ʼ") ;
			this.preparedStatement.execute() ;
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
	
	/*
	 * ��ʱ�ļ���������������¼
	 */
	public int saveTempDownloadFile(int fileid) {
		String f_name = "";
		long f_size = 0;
		int parent_id = 0;
		//��ȡ�ļ���Ϣ
		try {
			String queryFile = "select f_name,f_size,parent_id from file where f_id = ? " ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_FILE) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(queryFile) ;
			this.preparedStatement.setInt(1, fileid) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			while(this.resultSet.next()) {
				f_name = this.resultSet.getString("f_name") ;
				f_size = this.resultSet.getInt("f_size") ;
				parent_id = this.resultSet.getInt("parent_id") ;
			}
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
		System.out.println("��ѯ���������ļ�����Ϣ�ǣ� " + f_name + " " + f_size + " " + parent_id);
		//��ʱ������¼
		Date filedownloaddate = new Date() ;
		try {
			String saveTempDownloadFile = "insert into temp_download_file (u_id,tf_name,tf_date,tf_tasktype,tf_type,parent_id,tf_state) values(?,?,?,?,?,?,?)" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.SAVE_TEMP_DOWNLOAD_FILE) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(saveTempDownloadFile) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, f_name) ;
			this.preparedStatement.setString(3, filedownloaddate.toString()) ;
			this.preparedStatement.setString(4, "��������") ;
			this.preparedStatement.setString(5, "�ļ�") ;
			this.preparedStatement.setInt(6, parent_id) ;
			this.preparedStatement.setString(7, "׼����ʼ") ;
			this.preparedStatement.execute() ;
		} catch (Exception e) {
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
		return parent_id ;
	}
	
	/**
	 * �����ϴ���ʱ�ļ�״̬
	 */
	public void updateTempUploadFileState(int tempfileId, String state) {
		System.out.println("�ļ�ID " + tempfileId + " ״̬ " + state);
		try {
			String updateUploadTempFileState = "update temp_upload_file set tf_state = ? where tf_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.UPDATE_UPLOAD_TEMP_FILE_STATE) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(updateUploadTempFileState) ;
			this.preparedStatement.setString(1, state) ;
			this.preparedStatement.setInt(2, tempfileId) ;
			this.preparedStatement.executeUpdate() ;
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
	}
	
	/**
	 * ����������ʱ�ļ�״̬
	 */
	public void updateTempDownloadFileState(int tempfileId, String state) {
		try {
			String updateDownloadTempFileState = "update temp_download_file set tf_state = ? where tf_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.UPDATE_DOWNLOAD_TEMP_FILE_STATE) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(updateDownloadTempFileState) ;
			this.preparedStatement.setString(1, state) ;
			this.preparedStatement.setInt(2, tempfileId) ;
			this.preparedStatement.executeUpdate() ;
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
	}
	
	/**
	 * ������ʱ�ļ��ϴ��ٷֱ�
	 */
//	public void updateTempUploadFilePercent(int tempfileid, String percent) {
//		try {
//			String updateUploadTempFilePercent = "update temp_upload_file set tf_percent = ? where tf_id = ?" ;
//			this.connection = this.dbmanage.getConnection() ;
////			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.UPDATE_UPLOAD_TEMP_FILE_PERCENT) ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(updateUploadTempFilePercent) ;
//			this.preparedStatement.setString(1, percent) ;
//			this.preparedStatement.setInt(2, tempfileid) ;
//			this.preparedStatement.executeUpdate() ;
//			} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			} finally{
//				// �ر�����
//				if(this.resultSet != null) {
//					try {
//						this.resultSet.close() ;
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				this.dbmanage.closeLink() ;
//			}
//	}
	
	/**
	 * ������ʱ�ļ����ذٷֱ�
	 */
//	public void updateTempDownloadFilePercent(int tempfileid, String percent) {
//		try {
//			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.UPDATE_DOWNLOAD_TEMP_FILE_PERCENT) ;
//			this.preparedStatement.setString(1, percent) ;
//			this.preparedStatement.setInt(2, tempfileid) ;
//			this.preparedStatement.executeUpdate() ;
//			} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			} finally{
//				// �ر�����
//				if(this.resultSet != null) {
//					try {
//						this.resultSet.close() ;
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				this.dbmanage.closeLink() ;
//			}
//	}
	
	/**
	 * �����ļ��еĴ�С���������е����ļ��к��ļ�
	 */
	public void updateFileSize(int fileId ,long filesize) {
		int parentId = -1 ;
		try {
			String queryParentIdByFileId = "select f_size,parent_id from file where f_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_PARENT_ID_BY_FILE_ID) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(queryParentIdByFileId) ;
			this.preparedStatement.setInt(1, fileId) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			if(this.resultSet.next()) {
				parentId = this.resultSet.getInt("parent_id") ; 
				if(parentId == 0) {
					
				}else {
					long parentsize = 0 ;
					// ��ȡ���ļ��Ĵ�С
					this.connection = this.dbmanage.getConnection() ;
//					this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_PARENT_ID_BY_FILE_ID) ;
					this.preparedStatement = this.dbmanage.getPreparedStatement(queryParentIdByFileId) ;
					this.preparedStatement.setInt(1, parentId) ;
					this.resultSet = this.preparedStatement.executeQuery() ;
					if(this.resultSet.next()) {
						parentsize = this.resultSet.getLong("f_size") ;
						// ���¸��ļ��Ĵ�С
						String updateFileSize = "update file set f_size = ? where f_id = ?" ;
						this.connection = this.dbmanage.getConnection() ;
//						this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.UPDATE_FILE_SIZE) ;
						this.preparedStatement = this.dbmanage.getPreparedStatement(updateFileSize) ;
						this.preparedStatement.setLong(1, parentsize + filesize) ;
						this.preparedStatement.setInt(2, parentId) ;
						this.preparedStatement.executeUpdate() ;
					}
					// �ݹ����
					updateFileSize(parentId , filesize) ;
				}
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
	}
	
	/**
	 * ͨ���ļ�����ѯ�ļ�ID�ź��ļ���С
	 */
	public void queryFileIdAndSizeByName(String filename , int parentId) {
		try {
			String queryFileIdAndSizeByName = "select f_id,f_size,parent_id from file where u_id = ? and f_name = ? and parent_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_FILE_ID_AND_SIZE_BY_NAME) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(queryFileIdAndSizeByName) ;
			this.preparedStatement.setInt(1, Tools.userId) ;
			this.preparedStatement.setString(2, filename) ;
			this.preparedStatement.setInt(3, parentId) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			if(this.resultSet.next()) {
				Tools.deleteFileId = this.resultSet.getInt("f_id") ;
				Tools.deleteFileSize = this.resultSet.getLong("f_size") ;
			}
		}catch(Exception e) {
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



/**
 * ͨ����ʱ�ļ�ID��ѯ��С
 */
public int getTempFileSize(int tempfileid) {
	int size = 0;
	try {
		String queryTempFileSize = "select tf_size from temp_download_file where tf_id = ? " ;
		this.connection = this.dbmanage.getConnection() ;
//		this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_TEMP_FILE_SIZE) ;
		this.preparedStatement = this.dbmanage.getPreparedStatement(queryTempFileSize) ;
		this.preparedStatement.setInt(1, tempfileid) ;
		this.resultSet = this.preparedStatement.executeQuery() ;
		if(this.resultSet.next()) {
			size = this.resultSet.getInt("tf_size") ;
		}
	}catch(Exception e) {
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
	return size;
}
/**
 * ͨ����ʱ�ļ�ID��ѯ�ļ���
 */
public String getTempFileName(int tempfileid) {
	String name = " ";
	try {
		String queryTempFileName = "select tf_name from temp_download_file where tf_id = ? " ;
		this.connection = this.dbmanage.getConnection() ;
//		this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_TEMP_FILE_NAME) ;
		this.preparedStatement = this.dbmanage.getPreparedStatement(queryTempFileName) ;
		this.preparedStatement.setInt(1, tempfileid) ;
		this.resultSet = this.preparedStatement.executeQuery() ;
		if(this.resultSet.next()) {
			name = this.resultSet.getString("tf_name") ;
		}
	}catch(Exception e) {
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
	return name;
}
/**
 * ͨ����ʱ�ļ�ID��ѯ�ļ���
 */
public String getTempUploadFileName(int tempfileid) {
	String name = " ";
	try {
		String queryTempUploadFileName = "select tf_name from temp_upload_file where tf_id = ? " ;
		this.connection = this.dbmanage.getConnection() ;
//		this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_TEMP_UPLOADFILE_NAME) ;
		this.preparedStatement = this.dbmanage.getPreparedStatement(queryTempUploadFileName) ;
		this.preparedStatement.setInt(1, tempfileid) ;
		this.resultSet = this.preparedStatement.executeQuery() ;
		if(this.resultSet.next()) {
			name = this.resultSet.getString("tf_name") ;
		}
	}catch(Exception e) {
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
	return name;
}


}