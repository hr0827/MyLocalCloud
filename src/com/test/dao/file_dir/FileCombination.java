package com.test.dao.file_dir;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.StringTokenizer;
/**
 * �ļ���ϲ���
 * @author asus
 *
 */
public class FileCombination {

	String saveDictionary=null;//����ļ���ŵ�Ŀ¼
	String[] separatedFiles;//������в���ļ���
	String[][] separatedFilesAndSize;//������в���ļ������ּ���С
	int fileBlockNum=0;//ȷ���ļ�����
	String fileRealName="";//�ݲ���ļ���ȷ������ԭ�ļ���
	// �յ���͹��췽��
	public FileCombination() {
		
	}
	/**
	 * @param saveDictionary
	 * 				
	 */
	public FileCombination(String saveDictionary)
	{
		this.saveDictionary = saveDictionary ;
		getFileAttribute() ;
		combinationFile() ;
	}
	/**
	 *�����ļ��飬�õ��ļ���
	 */
    private String getRealName(String fileName)
    {
    	StringTokenizer st = new StringTokenizer(fileName , ".");
    	return st.nextToken()+"."+st.nextToken();
    }
    /**
     * ��ȡ�ļ���С
     */
    private long getFileSize(String fileName)
    {
    	fileName = saveDictionary + fileName;
    	return (new File(fileName).length());
    }
    /**
     * ����һЩ���ԣ�����ʹ��
     * @param drictory ����ļ�Ŀ¼
     */
    private void getFileAttribute()
    {
    	//��λ��saveDictionaryĿ¼��
    	File file=new File(this.saveDictionary);
    	// ����saveDictionaryĿ¼���ļ���ĳ��ȶ�������
    	separatedFiles=new String[file.list().length];
    	// ��ȡsaveDictionaryĿ¼�����е��ļ��������
    	separatedFiles=file.list();
    	//���ļ���Ŀ��̬���ɶ�ά���飬�����ļ������ļ���С
    	//��һάװ�ļ������ڶ�άΪ���ļ����ֽڴ�С
    	separatedFilesAndSize=new String[separatedFiles.length][2];
    	// �ļ��鰴������
    	Arrays.sort(separatedFiles);
    	fileBlockNum=separatedFiles.length;//��ǰ�ļ��������ж��ٸ��ļ�
    	for(int i=0;i<fileBlockNum;i++)
    	{
    		separatedFilesAndSize[i][0]=separatedFiles[i];//�ļ���
    		separatedFilesAndSize[i][1]=String.valueOf(getFileSize(separatedFiles[i]));//�ļ�����
    	}
    	fileRealName=getRealName(separatedFiles[fileBlockNum-1]);//ȡ���ļ��ָ�ǰ��ԭ�ļ���
    }
    /**
     * �ϲ��ļ�����������ļ���д
     * @return trueΪ�ɹ��ϲ��ļ�
     */
    private boolean combinationFile()
    {
    	RandomAccessFile raf=null;
    	long alreadyWrite=0;
    	FileInputStream fis=null;
    	int len=0;
    	byte[] bt=new byte[1024];
    	try
    	{
//    		raf = new RandomAccessFile(this.saveDictionary+fileRealName,"rw");
    		File file = new File("E:\\SafeCloudFiles\\") ;
    		if(!file.exists()) {
    			file.mkdirs() ;
    		}
//    		System.out.println("this.saveDictionary " + this.saveDictionary);
    		raf = new RandomAccessFile("E:\\SafeCloudFiles\\"+fileRealName,"rw");
    		for(int i=0;i<fileBlockNum;i++)
    		{
    			raf.seek(alreadyWrite);
    			fis=new FileInputStream(this.saveDictionary+separatedFilesAndSize[i][0]);
    			while((len=fis.read(bt))>0)
    			{
    				raf.write(bt,0,len);
    			}
    			fis.close();
    			alreadyWrite=alreadyWrite+Long.parseLong(separatedFilesAndSize[i][1]);
    		}
    		raf.close();     
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		try
    		{
    			if(raf!=null)
    				raf.close();
    			if(fis!=null)
    				fis.close();
    		}
    		catch (IOException f)
    		{
    			f.printStackTrace();
    		}
    		System.out.println("�ļ��ϲ�ʧ��");
    		return false;
    	}
    	System.out.println("�ļ��ϲ����");
    	return true;
    }
}
