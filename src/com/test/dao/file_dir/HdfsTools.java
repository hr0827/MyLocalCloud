package com.test.dao.file_dir;

import java.io.IOException ;
import java.net.URI ;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList ;
import java.util.List ;
import java.io.File ;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;  
import org.apache.hadoop.fs.FileSystem;  
import org.apache.hadoop.fs.Path;  
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;

import com.test.db.DBManage;
import com.test.tools.Tools;

/**
 * ����HDFS�ļ�ϵͳ��
 * 1�������ļ���
 * 2��ɾ���ļ����ļ��У�
 * 3���ϴ��ļ�
 * 4�������ļ����ļ��У�
 * @author asus
 *
 */

public class HdfsTools {
	
	private DBManage dbmanage ;
	private Connection connection ;
	private PreparedStatement preparedStatement ;
	private ResultSet resultSet ;
//	public static String hdfsUri = "hdfs://192.168.204.130:9000";
	public String hdfsUri ;
	
	public HdfsTools() {
		// ���췽��������hadoop��������
		System.setProperty("hadoop.home.dir", "E:\\hadoop-2.6.0");
		hdfsUri = getHdfsUri() ;
	}
	
	
	public String getHdfsUri() {
		String hdfsIp = "" ;
		String queryHdfsNodeIp = "select hdfs_ip from hdfs where u_id = ?" ;
		
		try {
			// ���ݿ����
			dbmanage = new DBManage();
			connection = dbmanage.getConnection();
			preparedStatement = dbmanage.getPreparedStatement(queryHdfsNodeIp);
			preparedStatement.setInt(1, Tools.userId) ;
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				hdfsIp = resultSet.getString("hdfs_ip") ;
				hdfsIp = "hdfs://" + hdfsIp + ":9000" ;
			}
		}catch(Exception e) {
			e.printStackTrace() ;
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
			dbmanage.closeLink() ;
		}
		return hdfsIp ;
	}
	
	/**
	 * HDFS����Ŀ¼
	 * @param dir Ŀ¼��ַ
	 * @return bool
	 * @throws IOException
	 */
	public boolean mkdir(String dir) throws IOException {
		if(StringUtils.isBlank(dir)) {
			return false ;
		}
		dir = hdfsUri + dir ;
		Configuration conf = new Configuration() ;
		FileSystem fs = FileSystem.get(URI.create(dir) , conf) ;
		if(!fs.exists(new Path(dir))) {
			fs.mkdirs(new Path(dir)) ;
		}
		fs.close() ;
		return true ;
	}
	
	/**
	 * HDFS ɾ��Ŀ¼
	 * @param dir	Ŀ¼��ַ
	 * @return bool
	 * @throws IOException
	 */
	public boolean deleteDir(String dir) throws IOException{
		if(StringUtils.isBlank(dir)) {
			return false ;
		}
		dir = hdfsUri + dir ;
		Configuration conf = new Configuration() ;
		FileSystem fs = FileSystem.get(URI.create(dir) , conf) ;
		fs.delete(new Path(dir) , true) ;
		fs.close() ;
		return true ;
	}
	
	/**
	 * �г�Ŀ¼�µ������ļ����ļ���
	 * @param dir Ŀ¼��ַ
	 * @return	�����ļ��б�
	 * @throws IOException
	 */
	public List<String> listAll(String dir) throws IOException {
		if(StringUtils.isBlank(dir)) {
			return new ArrayList<String>() ;
		}
		dir = hdfsUri + dir ;
		Configuration conf = new Configuration() ;
		FileSystem fs = FileSystem.get(URI.create(dir) , conf) ;
		FileStatus[] stats = fs.listStatus(new Path(dir)) ;
		List<String> names = new ArrayList<String>() ;
		for(int i = 0 ; i < stats.length ; i ++) {
			if(stats[i].isFile()) {
				names.add(stats[i].getPath().toString()) ;
			}else if(stats[i].isDirectory()) {
				names.add(stats[i].getPath().toString()) ;
			}else {
				names.add(stats[i].getPath().toString()) ;
			}
		}
		fs.close() ;
		return names ;
	}
	
	public boolean uploadFile2Hdfs(String localFile , String hdfsFile) throws IOException {
		System.out.println("enter function >>>>>> uploadFile2Hdfs");
		System.out.println("localFile -> " + localFile + " hdfsFile -> " + hdfsFile);
		if(StringUtils.isBlank(localFile) || StringUtils.isBlank(hdfsFile)) {
			return false ;
		}
		hdfsFile = hdfsUri + hdfsFile ;
		Configuration conf = new Configuration() ;
		FileSystem fs = FileSystem.get(URI.create(hdfsFile) , conf) ;
		Path src = new Path(localFile) ;
		Path dst = new Path(hdfsFile) ;
		
		fs.copyFromLocalFile(src, dst);
		fs.close() ;
		return true ;
	}
	
	/**
	 * ɾ���ļ����غ���crc��β���ļ�
	 * @return
	 */
	public boolean deleteCRCFile(String dir) {
		File file = new File(dir) ;
		File[] fileList = file.listFiles() ;
		for(int i = 0 ; i < fileList.length ; i ++) {
			boolean isFile = fileList[i].isFile() ;
			boolean isDir = fileList[i].isDirectory() ;
			if(isFile && fileList[i].getName().endsWith(".crc")) {
				fileList[i].delete() ;
				System.out.println("delete file -> " + fileList[i].getName() + " isFile -> " + isFile);
			}else if(isDir) {
				deleteCRCFile(fileList[i].getAbsolutePath()) ;
			}
		}
		return true ;
	}
	
	/**
	 * ����HDFS�ļ�������Ŀ¼���ݹ�����
	 * @param localPath �����ļ�Ŀ¼
	 * @param hdfsPath	Զ���ļ�Ŀ¼
	 * @return
	 * @throws IOException
	 */
	public boolean downloadFileFromHdfs(String localPath , String hdfsPath) throws IOException {
		System.out.println("call function downloadFileFromHdfs");
		// �жϱ�������Ŀ¼�Ƿ����
		File file = new File(localPath) ;
		if(!file.exists()) {
			// Ŀ¼�������򴴽�
			file.mkdirs() ;
		}
		if(StringUtils.isBlank(localPath) || StringUtils.isBlank(hdfsPath)) {
			return false ;
		}
		
		String absHdfsPath = hdfsUri + hdfsPath ;
		Configuration conf = new Configuration() ;
		FileSystem fs = FileSystem.get(URI.create(absHdfsPath) , conf) ;
		FileStatus[] stats = fs.listStatus(new Path(absHdfsPath)) ;
		
		for(int i = 0 ; i < stats.length ; i++) {
			if(stats[i].isFile()) {
				// �����ļ�
				String filepath = stats[i].getPath().toString() ;
				fs.copyToLocalFile(new Path(filepath) , new Path(localPath));
			}else if(stats[i].isDirectory()) {
				// �����Ŀ¼���ݹ�����Ŀ¼�е�ʣ���ļ�
				String nextDir = stats[i].getPath().toString() ;
				String tempDir = nextDir.substring(absHdfsPath.length() , nextDir.length()) ;
				downloadFileFromHdfs(localPath + tempDir , hdfsPath + tempDir) ;
			}
		}
		return true ;
	}
	
	public boolean downloadFile(String localPath , String hdfsPath) throws IOException {
		System.out.println("call function downloadFileFromHdfs");
		// �жϱ�������Ŀ¼�Ƿ����
		if(StringUtils.isBlank(localPath) || StringUtils.isBlank(hdfsPath)) {
			return false ;
		}
		
		String absHdfsPath = hdfsUri + hdfsPath ;
		Configuration conf = new Configuration() ;
		FileSystem fs = FileSystem.get(URI.create(absHdfsPath) , conf) ;
		FileStatus[] stats = fs.listStatus(new Path(absHdfsPath)) ;
		
		for(int i = 0 ; i < stats.length ; i++) {
			if(stats[i].isFile()) {
				// �����ļ�
				String filepath = stats[i].getPath().toString() ;
				fs.copyToLocalFile(new Path(filepath) , new Path(localPath));
			}
		}
		return true ;
	}
	
	public boolean deleteHdfsFile(String hdfsPath) throws IOException {
		System.out.println("enter function deleteHdfsFile");
		if(StringUtils.isBlank(hdfsPath)) {
			return false ;
		}
		hdfsPath = hdfsUri + hdfsPath ;
		Configuration conf = new Configuration() ;
		FileSystem fs = FileSystem.get(URI.create(hdfsPath) , conf) ;
		Path path = new Path(hdfsPath) ;
		boolean isDeleted = fs.delete(path , true) ;
		fs.close() ;
		return true ;
	}
	
	/**
	 * ��ȡHDFS��Ⱥ�б���Ϣ
	 * @return
	 * @throws IOException
	 */
	public DatanodeInfo[] getDataNodeList() throws IOException {
		Configuration conf=new Configuration();
		FileSystem fs=FileSystem.get(URI.create(hdfsUri) ,conf);
		DistributedFileSystem hdfs = (DistributedFileSystem)fs;
		DatanodeInfo[] dataNodeStats = hdfs.getDataNodeStats();
		return dataNodeStats ;
	}
	
	/*
	public static void main(String[] args) throws IOException {
		// TODO �Զ����ɵķ������
		
		// ����HDFSĿ¼
		HdfsTools ht = new HdfsTools() ;
		boolean mkdir_res = ht.mkdir("/star/xing/cheng/") ;
		System.out.println(mkdir_res);
		
		// ɾ��HDFSĿ¼
		boolean deleteDir_res = ht.deleteDir("/star/xing/cheng/") ;
		
		// �г�Ŀ¼�µ��ļ����ļ���
		List<String> list_res = ht.listAll("/star/") ;
		for(int i = 0 ; i < list_res.size() ; i ++) {
			System.out.println(list_res.get(i));
		}
		
		// �ϴ������ļ���HDFS
		ht.uploadFile2Hdfs("E:\\MySQL�洢����.pdf", "/star/xing/") ;
		ht.uploadFile2Hdfs("F:\\��Ƶ�̳�\\javascript��Ƶ\\1.1.mov", "/star/xing/") ;
		ht.uploadFile2Hdfs("E:\\MySQL�洢����.pdf", "/star/xing/") ;
		
		// �����ļ����е�����
		System.out.println("download file");
		boolean download_res = ht.downloadFileFromHdfs("E:\\star\\", "/star/") ;
		System.out.println(download_res);
		
		// �����ļ�
		System.out.println("download file");
		boolean download_res_1 = ht.downloadFileFromHdfs("E:\\star\\", "/star/xing/MySQL�洢����.pdf") ;
		System.out.println(download_res_1);
		
		ht.deleteCRCFile("E:\\star\\") ;
		
		ht.deleteHdfsFile("/star/xing/MySQL�洢����.pdf") ;
		ht.deleteHdfsFile("/star/xing/") ;
	}
	*/
}