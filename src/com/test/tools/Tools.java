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
	 * HDFS�ڵ�������ݿ�������
	 */
	// ���HDFS�ڵ�
//	public static String ADD_HDFS_NODE = "insert into hdfs (u_id ,hdfs_ip) values(?,?)" ;
//	// ��ʾHDFS�ڵ��б�
//	public static String SHOW_HDFS_NODES = "select * from hdfs where u_id = ?" ;
//	//����HDFS�ڵ����
//	public static String HDFS_NODES = "select * from hdfs where u_id = ?" ;
//	// ��ѯ�ļ�����Կ
//	public static String SHOW_FILE_BLOCK_KEY = "select * from fileblock where f_id = ?" ;
//	// ��ѯ�ļ����С
//	public static String SHOW_FILE_SIZE = "select * from file where f_id = ?" ;
	/**
	 * �ļ��������ݿ�������
	 */
	//ע��
//	public static String REGISTER = "insert into User (u_name , u_password , u_problem , u_answer) values(? , ? , ? , ?)" ;
//	//��½
//	public static String LOGIN = "select u_password from User where u_name =?" ;
//	// ע����֤�û���
//	public static String VALIDATE_USERNAME = "select * from User where u_name = ?";
//	//�����û�ID�Ų�ѯ����
//	public static String QUERY_PAASWORD_BY_USER_ID = "select u_password from user where u_id = ?" ;
//	//�޸�����
//	public static String CHANGE_PAASWORD = "update user set u_password = ? where u_id = ?" ;
//	// �һ�����
//	public static String FIND_PASSWORD = "update user set u_password = ? where u_name = ? and u_problem = ? and u_answer = ?" ;
//	// ��ȡ�ڵ�ǰperant_id�µ������ļ�����ʾ�ڽ�����
//	public static String SHOW_FILE = "select * from file where parent_id = ? and u_id = ? order by f_type DESC" ;
//	// �����û�����ѯ�û�id ��
//	public static String QUERY_ID_BY_NAME = "select u_id from User where u_name = ? " ;
//	// �½��ļ���
//	public static String CREATE_DIR = "insert into File (u_id , f_name , f_date , f_type , f_size , parent_id) values(? , ? , ? , ? , ? , ?)" ;
//	// ����ļ��Ƿ�����
//	public static String CHECK_FILE_NAME = "select * from File where u_id = ? and f_name = ? and parent_id = ?" ;
//	// �����û�ID���ļ��� �� ��Ŀ¼ID���Ҷ�Ӧ�ļ���ID
//	public static String QUERY_FILE_ID = "select * from File where u_id = ? and f_name = ? and parent_id = ?" ;
//	//ͨ���ļ�����ѯ�ļ�ID��
//	public static String QUERY_FILE_ID_BY_FILE_NAME = "select f_id from File where u_id = ? and f_name = ? and parent_id = ?" ;
//	//ͨ����ʱ�ļ�����ѯ�ϴ��ļ�ID��
//	public static String QUERY_UPLOAD_FILE_ID_BY_TEMP_FILE_NAME = "select tf_id from temp_upload_file where u_id = ? and tf_name = ? and parent_id = ?" ;
//	//ͨ����ʱ�ļ�����ѯ�����ļ�ID��
//	public static String QUERY_DOWNLOAD_FILE_ID_BY_TEMP_FILE_NAME = "select tf_id from temp_download_file where u_id = ? and tf_name = ? and parent_id = ?" ;
//	
//	// ���ļ��У���ʾ���������
//	public static String OPEN_DIR = "select f_name from File where parent_id = (select f_id from File where u_id = ? and f_name = ? and parent_id = ?)" ;
//	// ճ���ļ���
//	public static String PASTE_FILE = "update File set parent_id = ? where f_id = ?" ;
//	// ͨ���ļ���ID�Ų�ѯ�ļ���
//	public static String CHECK_PASTE_FILE_NAME = "select * from File where f_name = (select f_name from File where f_id = ?) and u_id = ? and parent_id = ?" ;
//	// �ļ�������
//	public static String RENAME_FILE = "update file set f_name = ? where u_id = ? and parent_id = ? and f_name = ?" ;
//	// �鿴�ļ�ID�ź��ļ�����
//	public static String QUERY_FILE_TYPE = "select f_id,f_type,f_size from file where u_id = ? and f_name = ? and parent_id = ?" ;
//	// �ļ�ɾ��
//	public static String DELETE_FILE = "delete from file where u_id = ? and f_name = ? and parent_id = ?" ;
//	// ����ļ�ɾ��
//	public static String CHECK_DELETE_FILE = "select * from file where parent_id = ?" ;
//	// ͨ���ļ�idɾ���ļ�
//	public static String DELETE_FILE_BY_ID = "delete from file where f_id = ?" ;
//	// ��ѯ���ļ����ļ���
//	public static String QUERY_SUB_FILE = "select f_id,f_type,f_name from file where u_id = ? and parent_id = ?" ;
//	// �鿴�ļ�����
//	public static String GET_FILE_PROPERTY = "select * from file where u_id = ? and f_name = ? and parent_id = ?" ;
//	// �����ļ��������ļ�
//	public static String SEARCH_FILE = "select f_name,f_type,parent_id from file where u_id = ? and f_name = ?" ;
//	// �����ļ��еĴ�С
//	public static String UPDATE_FILE_SIZE = "update file set f_size = ? where f_id = ?" ;
//	// �����ļ�ID�Ų�ѯ��ID�ź��ļ���С
//	public static String QUERY_PARENT_ID_BY_FILE_ID = "select f_size,parent_id from file where f_id = ?" ;
//	// �����ļ�����ѯ�ļ�Id���ļ���С
//	public static String QUERY_FILE_ID_AND_SIZE_BY_NAME = "select f_id,f_size,parent_id from file where u_id = ? and f_name = ? and parent_id = ?" ;
//	// �ļ������ز���
//	public static String DOWNLOAD_STRATEGY = "select * from fileblock where f_id = ?" ;
	
	/**
	 * �ļ��ϴ��ɹ���ͬ������file���fileblock���е�����
	 */
	// �����ļ���¼
//	public static String SAVE_FILE = "insert into file (u_id,f_name,f_date,f_type,f_size,parent_id) values(?,?,?,?,?,?)" ; 
//	// �����ļ����¼
//	public static String SAVE_FILE_BLOCK = "insert into fileblock (f_id,b_name,b_covername,netdisk_id,f_key) values(?,?,?,?,?)" ;
//	
	/**
	 * ��ʱ�ļ���/�ļ�������
	 */
	// ��ѯ��ʱ�ļ���С
//	public static String QUERY_TEMP_FILE_SIZE = "select tf_size from temp_download_file where tf_id = ? "  ;
//	// ��ѯ��ʱ�ļ���
//	public static String QUERY_TEMP_FILE_NAME = "select tf_name from temp_download_file where tf_id = ? "  ;
//	public static String QUERY_TEMP_UPLOADFILE_NAME = "select tf_name from temp_upload_file where tf_id = ? "  ;	
//	// ��ѯ��ʱ�ļ�����Ϣ
//	public static String QUERY_TEMP_FILE_UPLOAD_FILE_INFO =  "select * from temp_upload_file where tf_id = ?" ;
//	// �����ļ��ϴ����񣬲����������ݿ�
//	public static String SAVE_TEMP_UPLOAD_FILE = "insert into temp_upload_file (u_id,tf_name,tf_date,tf_tasktype,tf_type,parent_id,tf_state) values(?,?,?,?,?,?,?)" ; 
//	// �����ļ��������񣬲����������ݿ�
//	public static String SAVE_TEMP_DOWNLOAD_FILE = "insert into temp_download_file (u_id,tf_name,tf_date,tf_tasktype,tf_type,parent_id,tf_state) values(?,?,?,?,?,?,?)" ; 
//	
//	// �����ϴ���ʱ�ļ�״̬Ϊ���ļ��ֿ��С�
//	public static String UPDATE_UPLOAD_TEMP_FILE_STATE = "update temp_upload_file set tf_state = ? where tf_id = ?" ;
//	// ����������ʱ�ļ�״̬Ϊ���ļ��ֿ��С�
//	public static String UPDATE_DOWNLOAD_TEMP_FILE_STATE = "update temp_download_file set tf_state = ? where tf_id = ?" ;
//	
//	// �����ϴ���ʱ�ļ��ϴ��ٷֱ�
//	public static String UPDATE_UPLOAD_TEMP_FILE_PERCENT = "update temp_upload_file set tf_percent = ? where tf_id = ?" ;
//	// ����������ʱ�ļ��ϴ��ٷֱ�
//	public static String UPDATE_DOWNLOAD_TEMP_FILE_PERCENT = "update temp_download_file set tf_percent = ? where tf_id = ?" ;
//	
//	// ��ѯ�����ϴ����ļ����б�����
//	public static String UPLOAD_TABLE_ROWS = "select * from temp_upload_file where u_id = ?" ;
//	// ��ѯ�����б���ļ�������
//	public static String DOWNLOAD_TABLE_ROWS = "select * from temp_download_file where u_id = ?" ;
//	// ��ѯ�������ص��ļ����б�����
//	public static String DOWNLOADING_FILE_NUMBER = "select * from temp_download_file where u_id = ? and tf_state = ?" ;
//	// ��ѯ�����ϴ����ļ����б�����
//	public static String UPLOADING_FILE_NUMBER = "select * from temp_upload_file where u_id = ? and tf_state = ?" ;
//	// ��ѯ�ļ���
//	public static String QUERY_DOWNLOAD_TEMP_FILE_NAME = "select * from temp_download_file where u_id = ? and tf_id = ?" ;
//	public static String QUERY_UPLOAD_TEMP_FILE_NAME = "select * from temp_upload_file where u_id = ? and tf_id = ?" ;	
//	// ��ѯ�ļ���Ϣ
//	public static String QUERY_FILE = "select f_name,f_size,parent_id from file where f_id = ? " ;

	/**
	 * BlockDeleteManager sql
	 */
//	public static String QUERY_BLOCK_INFO = "select netdisk_id,b_covername from fileblock where f_id = ?" ;
	/**
	 * ɾ���ϴ������б�
	 */
//	public static String DELETE_UPLOAD_ASSIGNMENT = "delete from temp_upload_file where u_id = ? and tf_name = ?" ;
	/**
	 * ɾ�����������б�
	 */
//	public static String DELETE_DOWNLOAD_ASSIGNMENT = "delete from temp_download_file where u_id = ? and tf_name = ?" ;
	/**
	 * ʧ���ش�
	 */
	// ��ѯʧ�ܵĿ��ID�ź��ϴ���״̬��ȷ���Ƿ��ϲ�ʧ�ܣ�
//	public static String QUERY_REUPLOAD_FILE_ID = "select tf_id,tf_state from temp_upload_file where u_id = ? and tf_name = ? " ;
	
	/**
	 * �ش���ɺ�����ļ���
	 */
	// ��ȡ��ʱ�ļ����е���Ϣ
//	public static String QUERY_TEMP_FILE_INFO = "select * from temp_upload_file where tf_id = ?" ;
	
	/**
	 * ʧ������
	 */
	// ��ѯʧ�ܵĿ��ID�ź����ص�״̬��ȷ���Ƿ�����ʧ�ܣ�
//	public static String QUERY_REDOWNLOAD_FILE_ID = "select tf_id,tf_state from temp_download_file where u_id = ? and tf_name = ? " ;
	/**
	 * ����ϴ������б����Ƿ����ظ�������
	 */
	// ����ϴ��б�
//	public static String 	CKECK_UPLOAD_TABLE = "select tf_name from temp_upload_file where u_id = ?" ;
//	// ��������б�
//	public static String 	CKECK_DOWNLOAD_TABLE = "select tf_name from temp_download_file where u_id = ?" ;
//	
	/**
	 * �رմ��ڵĺ�������
	 */
	// �ر������ϴ�����ʱ�ļ�
//	public static String CLOSE_UPLOAD_FILE = "select tf_id from temp_upload_file where u_id = ? and tf_state = ?" ;
//	// ���������ϴ�����ʱ�ļ���״̬
//	public static String UPDATE_UPLOAD_FILE_STATE = "update temp_upload_file set tf_state = ? where tf_id = ?" ;
//	// ���������ϴ�����ʱ�ļ����״̬
//	public static String UPDATE_UPLOAD_TEMP_FILE_BLOCK_STATE = "update temp_upload_BLOCK set tb_state = ? where tf_id = ? and tb_state = ?" ;
//		
//	// �ر��������ص���ʱ�ļ�
//	public static String CLOSE_DOWNLOAD_FILE = "select tf_id from temp_download_file where u_id = ? and tf_state = ?" ;
//	// �����������ص���ʱ�ļ���״̬
//	public static String UPDATE_DOWNLOAD_FILE_STATE = "update temp_download_file set tf_state = ? where tf_id = ?" ;
//	
	
	// ��ȡ���ص����ļ���С
//	public static String GET_DOWNLOAD_FILE_SIZE = "select tf_size from temp_download_file where tf_id = ?" ;
		
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
	
	
	// 2018-04-30
//	public static String GET_FILE_NAME_BY_ID = "select f_name from file where f_id = ?" ;
	// ��ѯ�ļ��ϴ�״̬
//	public static String QUERY_UPLOAD_TEMP_FILE_STATE = "select tf_state from temp_upload_file where tf_id = ?" ;
	// �ļ�����Ϣ���浽���ݿ�
//	public static String SAVE_FILE_BLICK_INFO = "insert into fileblock(f_id ,b_name , b_covername , f_key) values(?,?,?,?)" ;
	
}
