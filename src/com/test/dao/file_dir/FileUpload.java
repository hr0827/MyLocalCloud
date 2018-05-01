package com.test.dao.file_dir;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;

import com.test.db.DBManage;
import com.test.tools.Tools;


public class FileUpload extends HdfsTools implements Runnable{
	private File file;
	private String filePath;
	private String savePath ;
	private String filename;
	public int tempfileId;
	public Map<Integer , String > blockMap ;
	//��ʱ�����ļ�����
	private String filefoldername;
	//������Կ��������ʱ�ļ����
	private String secretString;
	//����ļ�����ʵ�ļ���
	private String[] covernamelist;
	// �����ļ�����
	public FileManager fileManager ;
	// �ļ���С
	public long fileSize = 0 ;
	
	private int fileID = -1 ;
	
	// ���ݿ����
	private DBManage dbmanage ;
	private Connection connection ;
	private PreparedStatement preparedStatement ;
	private ResultSet resultSet ;

	public FileUpload(String filePath , String savePath){
		file = new File(filePath);
		this.filePath = filePath;
		this.savePath = savePath ;
		this.dbmanage = new DBManage() ;
		this.fileManager = new FileManager(this.dbmanage) ;
		this.fileSize = file.length() ;
		init();
	}
	public FileUpload() {
		this.dbmanage = new DBManage() ;
		this.fileManager = new FileManager(this.dbmanage) ;
	}
	private void init(){
		filename = file.getName();
		System.out.println(filename);
		StringTokenizer st = new StringTokenizer(filename , ".");
		filefoldername = st.nextToken();
	}
	
	/**
	 * �ļ����ϴ�
	 * @param fileBlockPath �ļ����ش�ŵ�ַ
	 * @param dstPath Զ��HDFS��ַ
	 * @return
	 * @throws IOException
	 */
	public boolean upload(String fileBlockPath , String dstPath) throws IOException {
		//�������ݿ���ʱ�ļ���״̬Ϊ���ϴ��С�
    	this.fileManager.updateTempUploadFileState(tempfileId,"�ϴ���") ; 
    	// ��ȡ����Ŀ¼�µ����м����ļ��飬������ϴ�
    	File file = new File(fileBlockPath) ;
    	File[] fileList = file.listFiles() ;
    	for(int i = 0 ; i < fileList.length ; i ++) {
    		this.uploadFile2Hdfs(fileList[i].getAbsolutePath(), dstPath) ;
    	}
    	this.fileManager.updateTempUploadFileState(tempfileId,"���") ;
    	// �ļ��ϴ��ɹ�
    	return true ;
	}
	
	public boolean saveFileBlockInfo(FileBlockEncrypt fileBlockEncrypt ,String fileBlockPath , String secretKey) {
		File file = new File(fileBlockPath) ;
		File[] fileList = file.listFiles() ;
		
		try {
			String queryTempFileUploadFileInfo = "select * from temp_upload_file where tf_id = ?" ;
        	connection = this.dbmanage.getConnection() ;
//        	preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_TEMP_FILE_UPLOAD_FILE_INFO) ;
        	preparedStatement = this.dbmanage.getPreparedStatement(queryTempFileUploadFileInfo) ;
			preparedStatement.setInt(1, tempfileId) ;
			resultSet = preparedStatement.executeQuery() ;
			while(resultSet.next()) {
				// ȡ���ļ�����Ϣ
				String fileName = resultSet.getString("tf_name") ;
				String filedate = resultSet.getString("tf_date") ;
				int parentid = resultSet.getInt("parent_id") ;
				this.fileManager.saveFile(fileName, filedate,this.fileSize , parentid) ;
				// ����õ��ϴ����ļ�ID��
				fileID = this.fileManager.queryFileIdByFileName(fileName,parentid) ;
			}
			
			String saveFileBlockInfo = "insert into fileblock(f_id ,b_name , b_covername , f_key) values(?,?,?,?)" ;
			connection = this.dbmanage.getConnection() ;
//        	preparedStatement = this.dbmanage.getPreparedStatement(Tools.SAVE_FILE_BLICK_INFO) ;
        	preparedStatement = this.dbmanage.getPreparedStatement(saveFileBlockInfo) ;
        	
        	String[] coverNameList = fileBlockEncrypt.getCovernamelist() ;
        	String[] fileNameList = fileBlockEncrypt.getRondomnamelist() ;
        	for(int i = 0 ; i < coverNameList.length ; i ++) {
        		String filename = fileList[i].getName() ;
    			preparedStatement.setInt(1, fileID) ;
    			preparedStatement.setString(2, fileNameList[i]) ;
    			preparedStatement.setString(3, coverNameList[i]) ;
    			preparedStatement.setString(4, secretKey) ;
    			preparedStatement.execute() ;
        	}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally {
			// �ر�����
			if(resultSet != null) {
				try {
					resultSet.close() ;
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
	 * �ļ��ϴ��ɹ�����к�������
	 * @return
	 */
	public boolean afterUploadSuccess(String blockUploadPath , long fileSize) {
		/**
		 * �ļ��ϴ���ɺ�ɾ���ļ���
		 */
		this.fileManager.deleteDirectory(blockUploadPath) ;		

		try {
			String queryTempFileUploadFileInfo = "select * from temp_upload_file where tf_id = ?" ;
        	connection = this.dbmanage.getConnection() ;
//        	preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_TEMP_FILE_UPLOAD_FILE_INFO) ;
        	preparedStatement = this.dbmanage.getPreparedStatement(queryTempFileUploadFileInfo) ;
			preparedStatement.setInt(1, tempfileId) ;
			resultSet = preparedStatement.executeQuery() ;
			while(resultSet.next()) {
				// ȡ���ļ�����Ϣ
				String fileName = resultSet.getString("tf_name") ;
				String filedate = resultSet.getString("tf_date") ;
				int parentid = resultSet.getInt("parent_id") ;
//				// �����ļ���¼
//				this.fileManager.saveFile(fileName, filedate,this.fileSize , parentid) ;
//				// ����õ��ϴ����ļ�ID��
//				fileID = this.fileManager.queryFileIdByFileName(fileName,parentid) ;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally {
			// �ر�����
			if(resultSet != null) {
				try {
					resultSet.close() ;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.dbmanage.closeLink() ;
		}
		// �������ݿ����ļ��еĴ�С
		this.fileManager.updateFileSize(fileID , fileSize) ;
		
		//�ļ��ϴ���ɺ�ˢ�½����ϵ��ļ��б�
		Tools.loginSuccess.fileBottomPanel.removeAll() ;
		this.fileManager.showFileList(Tools.parentIdStack.lastElement(), Tools.userId) ;
		Tools.loginSuccess.repaint() ;
		return true ;
	}
	
	@Override
	public void run() {
		if(this.fileManager.checkFileName(filename)) {
			// ��ȡ���ϴ��ļ���parent_id
			int parentid = Tools.parentIdStack.lastElement();	
			
			// ��ȡ���ϴ��ļ���HDFSĿ¼
			String hdfsPath = "/" ;
			try {
				for(int i = 0 ; i < Tools.parentIdStack.size() ; i ++) {
					int fileId = Tools.parentIdStack.get(i) ;
					
					String getFileNameById = "select f_name from file where f_id = ?" ;
					this.connection = this.dbmanage.getConnection() ;
//					this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.GET_FILE_NAME_BY_ID) ;
					this.preparedStatement = this.dbmanage.getPreparedStatement(getFileNameById) ;
					this.preparedStatement.setInt(1, fileId) ;
					this.resultSet = this.preparedStatement.executeQuery() ;
					while(this.resultSet.next()) {
						hdfsPath = hdfsPath + this.resultSet.getString("f_name") + "/";
					}
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
			System.out.println("�ļ���Ӧ��HDFSĿ¼ -> " + hdfsPath);

			// ���ܺ���ļ����ŵ�ַ���ļ���Ŀ¼��ַ + \\test\\encrypt��
			String encryptPath = this.savePath + "\\"+filefoldername ;			
			// ��ȡ�ļ��ϴ�����
			Date filedate = new Date() ;
			if(!(new File(savePath + "\\" + filefoldername).exists())) {
				// ��ʱ���д����ļ��ϴ���¼
				this.fileManager.saveTempUploadFile(filename, filedate.toString(),parentid) ;
				// ����õ��ϴ����ļ�ID��
				tempfileId = -1;
				tempfileId = this.fileManager.queryUploadFileIdByTempFileName(filename,parentid) ;			
				this.fileManager.fileSeperate = new FileSeperate(filePath,filefoldername ,tempfileId,savePath) ;
				if(this.fileManager.fileSeperate.seperateFile()) {
					// �����ַ���
					secretString = this.fileManager.generateSecretString() ;
					// �Էָ����ļ�����м���
					FileBlockEncrypt fileBlockEncrypt = new FileBlockEncrypt(encryptPath , secretString ,tempfileId) ;				
					if(fileBlockEncrypt.crateEncryptFile()) {
						System.out.println("�ļ�����ܳɹ�");
						// �ļ��ϴ�
						try {
							if(!upload(encryptPath + "\\encrypt\\" , "/")) {
								JOptionPane.showMessageDialog(null, "�����ļ��ϴ�����ʧ�ܣ������ԣ�", "��Ϣ��ʾ", 1);
							}else {
								saveFileBlockInfo(fileBlockEncrypt ,encryptPath + "\\encrypt\\" , secretString) ;
								afterUploadSuccess(encryptPath , this.fileSize) ;
								// �ļ��ϴ��ɹ�
								JOptionPane.showMessageDialog(null, "�����ļ��ϴ��ɹ���", "��Ϣ��ʾ", 1);
							}
						} catch (Exception e) {
							this.fileManager.updateTempUploadFileState(tempfileId,"ʧ��") ;
							JOptionPane.showMessageDialog(null, "�����쳣�����������������ӡ�", "��Ϣ��ʾ", 1);
							e.printStackTrace() ;
						}
					}
				}
			}else {
				JOptionPane.showMessageDialog(null, "���ļ������ϴ���", "�ϴ���ʾ", 1);
			}
		}else {
			System.out.println("�ļ�����");
			JOptionPane.showMessageDialog(null, "���ļ��Ѵ���", "��Ϣ��ʾ", 1);
		}
	}
}


