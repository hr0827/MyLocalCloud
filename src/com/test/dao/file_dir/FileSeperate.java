package com.test.dao.file_dir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.test.db.DBManage;
import com.test.tools.Tools;
/**
 * �ļ��ָ���
 * @author asus
 *
 */
public class FileSeperate {
	private String fileName ;
	private String fileDictionary ;
	private String savePath ;
	private int fileBlockNum ;
	@SuppressWarnings("unused")
	private long fileSize ;
	private long[] fileBlockSizes ;
	private File file ;
	private File fileMkdir ;
	private String foldername;
	private int tempfileid;
	// �ļ�����
	private FileManager fileManager ;
	
	// ���ݿ����
	private DBManage dbmanage ;
	@SuppressWarnings("unused")
	private Connection connection ;
	private PreparedStatement preparedStatement ;
	private ResultSet resultSet ;
	
	public FileSeperate() {
		this.dbmanage = new DBManage() ;
		this.fileManager = new FileManager(this.dbmanage) ;
	}
	/**
	 * @param fileDictionary
	 * 			Ҫ�ָ���ļ��Ĵ�ŵ�ַ
	 */			
	public FileSeperate(String fileDictionary,String foldernam , int tempfileid , String savePath) {
		this.foldername = foldernam; 	
		this.dbmanage = new DBManage() ;
		this.fileManager = new FileManager(this.dbmanage) ;
		this.tempfileid = tempfileid ;
		this.savePath = savePath ;
		fileSeperateInit(fileDictionary) ;
	}
	/**
	 * �ļ��ָ��ʼ��
	 * @param fileDictionary
	 * 			Ҫ�ָ���ļ��Ĵ�ŵ�ַ
	 */
	public void fileSeperateInit(String fileDictionary) {
		this.fileDictionary = fileDictionary ;
		this.file = new File(fileDictionary) ;
//		this.parentDictionary = "E:\\SafeCloudFiles\\uploadtemp" ;
		this.fileName = this.getFileName() ;
		this.fileSize = this.getFileSize() ;
		this.fileBlockNum = this.getFileBlockNum() ;
		//��Ŀ¼���ڴ�ŷָ����ļ���
//		fileMkdir = new File(this.parentDictionary + "\\"+foldername) ;
		fileMkdir = new File(this.savePath + "\\"+foldername) ;
		fileMkdir.mkdirs() ;
	}
	
	/**
	 * ��ȡ�ļ���
	 * @return
	 */
	public String getFileName() {
		return this.file.getName() ;
	}
	/**
	 * ��ȡ�ļ���С
	 */
	public long getFileSize() {
		return this.file.length() ;
	}
	/**
	 * �����ļ�����
	 */
	public int getFileBlockNum() {
		int count = 0;
		try {
			String getHdfsNodes = "select * from hdfs where u_id = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.HDFS_NODES) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(getHdfsNodes) ;
			this.preparedStatement.setInt(1 ,Tools.userId) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			while(this.resultSet.next()) {
				count ++ ;
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
		return count ;
	}
	
	/**
	 * ����ÿ��Ĵ�С
	 */
	public long[] getFileBlockSize() {
		int count = (int)this.fileBlockNum ;
		long[] fileBlockSizes = new long[count] ;
		
		System.out.println("�ļ���С��" + getFileSize());
		/**
		 * ���÷���
		 */
		if(this.fileBlockNum != 0) {
			if(getFileSize() % getFileBlockNum() == 0 ) {
				for (int i =0 ; i < count ;i++ ) {
					fileBlockSizes[i] = getFileSize() / getFileBlockNum() ;
				}
			}else {
				// �ֲ���ȫ
				for(int i = 0 ; i < count - 1 ; i++ ) {
					fileBlockSizes[i] = getFileSize() / getFileBlockNum() ;
				}
				fileBlockSizes[count - 1] = getFileSize() - (count-1) * ( getFileSize() / getFileBlockNum() ) ;
			}
			for(int i=0;i<fileBlockSizes.length ;i++) {
				System.out.println("�ļ����С��" + fileBlockSizes[i]);
			}
		}

		return fileBlockSizes ;
	}
	/**
	 * �����ļ��������
	 */
	public String getFileBlockName(int blockCount) {
		return this.savePath + "\\"+foldername+"\\" + this.fileName + ".part" + blockCount ;
	}
	
	/**
	 * �ļ��ֿ�д����
	 */
	  public boolean writeFile(String fileDictionary ,String fileBlockName,long fileBlockSize[],long beginPos)//��Ӳ��д�ļ�
	  {
	 
	    RandomAccessFile raf=null;
	    FileOutputStream fos=null;
	    byte[] bt=new byte[1024];
	    long writeByte=0;
	    int len=0;
	    try
	    {
	      raf = new RandomAccessFile(fileDictionary,"r");
	      raf.seek(beginPos);
	      fos = new FileOutputStream(fileBlockName);
	      for(int i = 0 ; i< fileBlockSize.length ; i ++) {
		      while((len=raf.read(bt))>0)
		      {       
		        if(writeByte<fileBlockSize[i])//�����ǰ�黹û��д��
		        {
		          writeByte=writeByte+len;
		          if(writeByte<=fileBlockSize[i])
		            fos.write(bt,0,len);
		          else
		          {
		            len=len-(int)(writeByte-fileBlockSize[i]);
		            fos.write(bt,0,len);
		          }
		        }       
		      }
	      }


	      fos.close();
	      raf.close();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      try
	      {
	        if(fos!=null)
	          fos.close();
	        if(raf!=null)
	          raf.close();
	      }
	      catch(Exception f)
	      {
	        f.printStackTrace();
	      }
	      return false;
	    }
	    return true;
	  }
	
	/**
	 * �ļ��ֿ�
	 */
	public boolean seperateFile() {
		System.out.println("enter function >>>>>> seperateFile");
		//������ʱ�ļ�״̬Ϊ���ļ��ֿ��С�
		if(this.fileBlockNum != 0) {
			this.fileManager.updateTempUploadFileState(tempfileid,"�ֿ���") ;
			this.fileBlockSizes = getFileBlockSize() ;
		    long writeSize=0;//ÿ��д����ֽ�
		    long writeTotal=0;//�Ѿ�д�˵��ֽ�
		    String FileCurrentNameAndPath = null;
		    System.out.println("�ļ��ֿ���......");
		    for(int i=0 ; i< this.fileBlockNum ; i++)
		    {
		      if(i<this.fileBlockNum)
		        writeSize = fileBlockSizes[i];//ȡ��ÿһ��Ҫд����ļ���С
		      if(this.fileBlockNum == 1)
//		        FileCurrentNameAndPath = this.fileDictionary + ".bak";
		    	  FileCurrentNameAndPath = this.savePath + "//"+foldername+"//" + this.fileName + ".part" ;	      
		      else
		        FileCurrentNameAndPath=getFileBlockName(i) ;
		      if(!writeFile(this.fileDictionary , FileCurrentNameAndPath , fileBlockSizes , writeTotal))//ѭ����Ӳ��д�ļ�
		      {
		    	  System.out.println("�ļ��ֿ�ʧ��");
		    	  return false;
		      }
		      writeTotal=writeTotal+writeSize;
		    }
		    System.out.println("�ļ��ֿ����");
		    return true;
		}else {
			JOptionPane.showMessageDialog(null, "�����HDFS�ڵ㣡����", "�ϴ���ʾ", 1);
			return false ;
		}
		

	}
}
