package com.test.tools;

import java.util.Stack;
import com.test.ui.LoginSuccess;
/**
 * ������
 * @author asus
 *
 */
public class Tools {
	/**
	 * ���ݿ����Ӳ���
	 */
	public static String DRIVER = "com.mysql.jdbc.Driver" ;
	public static String URL = "jdbc:mysql://127.0.0.1:3306/cloud?characterEncoding=UTF8" ;
	public static String USERNAME = "root" ; 
	public static String PASSWORD = "root" ;
	
	/**
	 * ���ڴ�Ÿ�Ŀ¼ID�ŵ�ջ
	 */
	public static Stack<Integer> parentIdStack = new Stack<Integer>() ;
	/**
	 * �洢�û�ID��
	 */
	public static int userId ;
	/**
	 * ���Ƶ��ļ��е�ID��
	 */
	public static int cutFileId = -1 ;
	/**
	 * ���Ƶ��ļ��Ĵ�С
	 */
	public static long copyFileSize = 0 ;
	/**
	 * ɾ���ļ�ID��
	 */
	public static int deleteFileId = -1 ;
	/**
	 * ɾ���ļ��Ĵ�С
	 */
	public  static long deleteFileSize = 0 ;
	/**
	 * ͬʱ���е���������������
	 */
	public static int fileDownloadLimitNumber = 2;
	/**
	 * ͬʱ���е��ϴ�����������
	 */
	public static int fileUploadLimitNumber = 2;
	
	public static LoginSuccess loginSuccess ;
	
}
