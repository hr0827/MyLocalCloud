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
public class FileBlockDecrypt {
	/**
	 * ���������ļ����ļ����ŵ�λ��
	 */
	private String filePath ;
	/**
	 * ���ܺ���ļ����ŵ�λ��
	 */
	private String outPath[] ;
	/**
	 * ����·�����ļ����а������ļ�
	 */
	private String fileBlockPath[] ;
	
	/**
	 * ���ܶ���
	 */
	private Cipher fileBlockDecrypt ;
	/**
	 * ������Կ�ַ���
	 */
	@SuppressWarnings("unused")
	private String decryptKey ;
	/**
	 * ���ݽ�����Կ�ַ����õ��Ľ�����Կ
	 */
	private Key key ;
	
	
	/**
	 * ���ݸ������ļ�·������һ��File����
	 */
	private File file ;
	/**
	 * ���ڴ���һ��Ŀ¼��Ž��ܺ���ļ���
	 */
	private File fileMkdir ;
	
	/**
	 * ���췽��
	 * @param filePath �ļ�·��
	 * @param decryptKey ������Կ
	 */
	public FileBlockDecrypt(String filePath ,String decryptKey ) {
		this.filePath = filePath ;
		this.decryptKey = decryptKey ;
		init() ;
		getKey(decryptKey) ;
		initCipher() ;
		crateDecryptFile() ;
	}
	/**
	 * ������ʼ��
	 */
	public void init() {
		file = new File(this.filePath) ;
		fileBlockPath = file.list() ;
		outPath = new String[fileBlockPath.length] ;
		fileMkdir = new File(filePath + "\\decrypt") ;
		fileMkdir.mkdir() ;
	}
    /**
     * ��ʼ������ģʽ
     */
	public void initCipher() {
		try {
			fileBlockDecrypt = Cipher.getInstance("DES") ;
			fileBlockDecrypt.init(Cipher.DECRYPT_MODE, this.key) ;
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ����������Կ
	 * @param decryptKey ������Կ
	 * @return
	 */
	public Key getKey(String decryptKey) {
		byte[] keyByte = decryptKey.getBytes() ;
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
     * �����ļ����Ҽ�¼���ܺ���ļ�·�� 
     * */ 
    private void crateDecryptFile() {  
    	// ���ܺ���ļ��ı���·��
        String path = null;  
        /**
         * ����filePath��ָ�����ļ��������н��ܲ���
         */
        for (int i = 0; i < fileBlockPath.length; i++) {  
            try {
            	path = filePath + "\\decrypt\\" + fileBlockPath[i].substring(0 ,fileBlockPath[i].lastIndexOf(".")) ;  
            	System.out.println(path);
                decrypt(filePath + "\\" + fileBlockPath[i], path);  
                // ��ż��ܺ���ļ����·��
                outPath[i] = path;  
                System.out.println(fileBlockPath[i]+"������ɣ����ܺ���ļ���:"+outPath[i]);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        System.out.println("==========================�������=======================");  
          
    }
    /**
     * �����ļ�
     * @param file �ļ���ַ
     * @param destFile ���ܺ��ļ��Ĵ�ŵ�ַ
     * @throws Exception
     */
    public void decrypt(String file ,String destFile) throws Exception {  
        InputStream is = new FileInputStream(file);  
        OutputStream out = new FileOutputStream(destFile) ;
        
        CipherInputStream cis = new CipherInputStream(is, fileBlockDecrypt);  
        byte[] buffer = new byte[1024];  
        int r;  
        System.out.println("file " + file + " testFile " + destFile);
        while ((r = cis.read(buffer)) > 0) {  
            out.write(buffer, 0, r);  
        }  
        cis.close();  
        is.close();  
        out.close();
    }  

}
