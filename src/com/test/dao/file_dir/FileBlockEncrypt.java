package com.test.dao.file_dir;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
/**
 * ������
 * @author asus
 *
 */
public class FileBlockEncrypt {
	/**
	 * αװ�ļ���
	 */
	private String[] covernamelist; 
	/**
	 * ������ļ���
	 */
	private String[] randomnamelist; 
	/**
	 * �����ܵ��ļ�·��
	 */
	private String filePath ;
	/**
	 * ���ܺ��ļ��Ĵ��·��
	 */
    public String outPath[] ; 
    /**
     * ��·�����ļ����а������ļ�
     */
    private String fileBlockPath[] ;
	/**
	 * ������Կ�ַ���
	 */
	@SuppressWarnings("unused")
	private String encryptKey ;
	/**
	 * ��ʱ�ļ�ID
	 */
	private int tempfileid ;
	/**
	 * ���ݼ�����Կ�ַ����õ��ļ�����Կ
	 */
	private Key key =null;
	/**
	 * ���ܶ���
	 */
	private Cipher fileBlockEncrypt ;
	/**
	 * ���ݸ������ļ�·������һ��File����
	 */
	private File file ;
	/**
	 * ���ڴ���һ��Ŀ¼��ż��ܺ���ļ���
	 */
	private File fileMkdir ;
	
	/**
	 * �����ļ�����
	 */
	private FileManager fileManager ;
	
	/**
	 * ���췽��
	 * @param filePath �ļ���ַ
	 * @param encryptKey ������Կ
	 */
	public FileBlockEncrypt(String filePath ,String encryptKey ,int tempfileid) {
		this.filePath = filePath ;
		this.encryptKey = encryptKey ;
		this.tempfileid = tempfileid ;
		this.fileManager = new FileManager() ;
		init() ;
		getKey(encryptKey) ;
		initCipher() ;
	}
	/**
	 * ������ʼ��
	 */
	public void init() {
		// �����ļ���ַ����File����
		file = new File(this.filePath) ;
		// ��ȡfilePath������ļ��б�
		fileBlockPath = file.list() ;
		// ���ܺ���ļ����ŵ�ַ
		outPath = new String[fileBlockPath.length] ;
		// �������ܺ���ļ�����Ŀ¼���ڵ�ǰ�ļ����½���encrypt�ļ���
		fileMkdir = new File(filePath + "\\encrypt") ;
		// ����Ŀ¼
		fileMkdir.mkdirs() ;
	}
	/**
	 * ����������Կ
	 * @param encryptKey ������Կ
	 * @return
	 */
	public Key getKey(String encryptKey) {
		byte[] keyByte = encryptKey.getBytes() ;
		byte[] tempByte = new byte[8] ;
        for (int i = 0; i < tempByte.length && i < keyByte.length; i++) {  
        	tempByte[i] = keyByte[i];  
        }  
        
        /**
         * ����������Կ
         */
        this.key = new SecretKeySpec(tempByte, "DES");  
        return this.key;  
	}
	/**
	 * Cipher�����ʼ��
	 */
	public void initCipher() {
		try {
			fileBlockEncrypt = Cipher.getInstance("DES") ;
			fileBlockEncrypt.init(Cipher.ENCRYPT_MODE, this.key) ;
		} catch (InvalidKeyException e) {  
            e.printStackTrace();  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
        }  
		
	}
	
    /** 
     * �����ļ����Ҽ�¼���ܺ���ļ�·�� 
     * */  
    public boolean crateEncryptFile() {  
    	//�������ݿ���ʱ�ļ���״̬Ϊ�������С�
    	this.fileManager.updateTempUploadFileState(tempfileid,"������") ;
    	// ���ܺ���ļ����㱣��·��
        String path = null;  
        /**
         * ������filePath���ƶ����ļ��������м������
         */     
        covernamelist = new String[fileBlockPath.length];
        randomnamelist = new String[fileBlockPath.length];        
        for (int i = 0; i < fileBlockPath.length; i++) {  
            try {
            	String covername = this.fileManager.generateSecretString() ;
            	System.out.println(fileBlockPath[i] + ".bin" + i);
            	covernamelist[i] = fileBlockPath[i] + ".bin" + i;
            	randomnamelist[i] = covername;
            	path = filePath + "\\encrypt\\" + covername;
                encrypt(filePath + "\\" + fileBlockPath[i], path);  
                // ��ż��ܺ���ļ����·��
                outPath[i] = path;  
                System.out.println(fileBlockPath[i]+"������ɣ����ܺ���ļ���:"+outPath[i]);  
            } catch (Exception e) {  
                e.printStackTrace();  
                return false ;
            }  
        }  
        System.out.println("=========================�������=======================");  
        return true ;
    } 
    /** 
     * �����ļ��ĺ��� 
     *  
     * @param file 
     *            Ҫ���ܵ��ļ� 
     * @param destFile 
     *            ���ܺ��ŵ��ļ��� 
     *            ���ļ����ܺ�������ƶ����ļ�����
     */  
    public void encrypt(String file, String destFile) throws Exception {  
        InputStream is = new FileInputStream(file);  
        OutputStream out = new FileOutputStream(destFile);  
  
        CipherInputStream cis = new CipherInputStream(is, fileBlockEncrypt);  
        byte[] buffer = new byte[1024];  
        int r;  
        while ((r = cis.read(buffer)) > 0) {  
            out.write(buffer, 0, r);  
        }  
        cis.close();  
        is.close();  
        out.close();  
    } 
    
    public String[] getCovernamelist(){
    	return this.covernamelist;
    }
    
    public String[] getRondomnamelist(){
    	return this.randomnamelist;
    }
}
