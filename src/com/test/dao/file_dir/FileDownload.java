package com.test.dao.file_dir;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.JOptionPane;
import com.test.db.DBManage;
import com.test.tools.Tools;

public class FileDownload implements Runnable {
	private String fileBlockPath;
	private String savePath;
	private String filename;
	private int fileid;
	private File fileMkdir ;
	//������Կ
	private String fileblockkey = " ";
	//��ʱ�ļ�ID
	public int tempFileId;
	//�ļ���С
	public int fileTotalSize;
	// �����ļ�����
	private FileManager fileManager ;	
	// ���ݿ����
	private DBManage dbmanage ;
	@SuppressWarnings("unused")
	private Connection connection ;
	private PreparedStatement preparedStatement ;
	private ResultSet resultSet ;
	
	private HdfsTools hdfsTool ;
	
	
	public FileDownload(int fileId,String filename ,String savePath) {
		fileid = fileId;
		this.savePath = savePath;
		this.filename = filename;
		this.dbmanage = new DBManage() ;
		this.fileManager = new FileManager(this.dbmanage) ;
		this.hdfsTool = new HdfsTools() ;
	}
	// �ļ�����
	@SuppressWarnings("unchecked")
	public boolean download(String savePath) throws Exception {
		//��������״̬Ϊ�������С�
		this.fileManager.updateTempDownloadFileState(tempFileId,"������") ;
		
		fileMkdir = new File(savePath) ;
		fileMkdir.mkdirs();
		
		//��ȡkey ��������
		String showFileBlockKey = "select * from fileblock where f_id = ?" ;
    	connection = this.dbmanage.getConnection() ;
//        	preparedStatement = this.dbmanage.getPreparedStatement(Tools.SHOW_FILE_BLOCK_KEY) ;
    	preparedStatement = this.dbmanage.getPreparedStatement(showFileBlockKey) ;
		preparedStatement.setInt(1, fileid) ;
		resultSet = preparedStatement.executeQuery() ;
		
		while(resultSet.next()) {
			String filename = resultSet.getString("b_name") ;
			String covername = resultSet.getString("b_covername") ;
			fileblockkey = Tools.loginSuccess.hdfs.getFileBlockKey(fileid);
			System.out.println("download file >>>>>> " + savePath + "/" + covername + " | " + "/" + filename);
			hdfsTool.downloadFile(savePath + "/" + covername, "/" + filename) ;
			hdfsTool.deleteCRCFile(savePath) ;
		}
		return true;
	}
	
	//��ʱ����·��
	public void getSavepath(){
		StringTokenizer st = new StringTokenizer(filename , ".");
		fileBlockPath = savePath + st.nextToken();
	}

	@Override
	public void run() {
		//��ʱ�ļ���������������¼,��������״̬Ϊ��׼����ʼ��
		int parentId = this.fileManager.saveTempDownloadFile(fileid) ;
		// ����õ��ϴ����ļ�ID��
		tempFileId = -1;
		tempFileId = this.fileManager.queryDownloadFileIdByTempFileName(filename,parentId) ;
		
		getSavepath();
		try {
			if(download(fileBlockPath)){
				//��������״̬Ϊ�������С�
        		this.fileManager.updateTempDownloadFileState(tempFileId,"������") ;
				new FileBlockDecrypt(fileBlockPath , fileblockkey) ;
				//��������״̬Ϊ���ϲ��С�
        		this.fileManager.updateTempDownloadFileState(tempFileId,"�ϲ���") ;
				//�ϲ�
			    new FileCombination(fileBlockPath+"\\decrypt\\" , this.fileid) ;
				/**
				 * �ļ�������ɺ�ɾ���ļ���
				 */
				//��������״̬Ϊ����ɡ�
        		this.fileManager.updateTempDownloadFileState(tempFileId,"���") ;
				this.fileManager.deleteDirectory(fileBlockPath) ;	
				JOptionPane.showMessageDialog(null, "�ļ����سɹ�����", "��Ϣ��ʾ", 1);
				
			}else {
				System.out.println("�ļ�����������ʧ��");
				this.fileManager.updateTempDownloadFileState(tempFileId,"ʧ��") ;
				JOptionPane.showMessageDialog(null, "�ļ�����������ʧ�ܣ������ԣ�", "��Ϣ��ʾ", 1);
			}
		}catch(ConnectException e) {
			this.fileManager.updateTempDownloadFileState(tempFileId,"ʧ��") ;
			JOptionPane.showMessageDialog(null, "HDFS��Ⱥ�����쳣����������HDFS״̬", "��Ϣ��ʾ", 1);
			e.printStackTrace() ;
		}catch(Exception e ) {
			this.fileManager.updateTempDownloadFileState(tempFileId,"ʧ��") ;
			e.printStackTrace() ;
		}
	}
}
