package com.test.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import com.test.db.DBManage;
import com.test.tools.Tools;
import com.test.ui.ChangePassword;
import com.test.ui.FindPassword;
import com.test.ui.LoginFrame;
import com.test.ui.MD5;
import com.test.ui.Register;

public class User {
	@SuppressWarnings("unused")
	private String username ; 
	
	// ���ݿ����
	private DBManage dbmanage ;
	@SuppressWarnings("unused")
	private Connection connection ;
	private PreparedStatement preparedStatement ;
	private ResultSet resultSet ;
	
	public User() {
		this.dbmanage = new DBManage() ;
	}
	
	public User(String username) {
		this.username =  username ;
		this.dbmanage = new DBManage() ;
	}

	
	/**
	 * ��¼��֤
	 * @param mf
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public boolean login(LoginFrame loginFrame) throws Exception{
		String username = loginFrame.getUsername() ;
		String password = loginFrame.getPassword() ;
		String password_MD5 = new MD5().GetMD5Code(password)  ;
		String str ;
		try {
			String userLogin = "select u_password from User where u_name =?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.LOGIN) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(userLogin) ;
			this.preparedStatement.setString(1 , username) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			if(this.resultSet.next()) {
				str = this.resultSet.getString("u_password");
				@SuppressWarnings("unused")
				int str_length = str.length() ;
				if(str.substring(0 , password_MD5.length()).equals(password_MD5)) {
					return true ;
				}else {
					loginFrame.paint("�û��������벻��ȷ") ;
					return false ;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			loginFrame.paint("�û��������벻��ȷ") ;
			e.printStackTrace();
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
		loginFrame.paint("�û��������벻��ȷ") ;
		return false ;
	}
	
	/**
	 * �û�ע��
	 * @param register
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public boolean register(Register register) throws Exception {
		String username = register.getUsername() ;
		String password = register.getPassword() ;
		String password_MD5 = new MD5().GetMD5Code(password) + this.generateSecretString() ;
		String confirmPassword = register.getConfirmPassword() ;
		String problem = register.getProblem() ;
		String answer = register.getAnswer() ;
		System.out.println(password_MD5);
		
		if(!(username.length() >=6 && username.length() <= 12)) {
			register.paint("�û�������Ϊ6~12���ַ�") ;
			return false ;
		}else if (username.indexOf(" ") != -1) {
			register.paint("�û������ܰ����ո�") ;
			return false ;
		}
		else if(!(password.length() >= 6 && password.length() <= 12)) {
			register.paint("���볤��Ϊ6~12���ַ�") ;
			return false ;
		} else if(!password.equals(confirmPassword)) {
			register.paint("���벻һ��") ;
			return false ;
		}else if(answer.equals("")) {
			register.paint("�𰸲��ܿ�") ;
			return false ;
		}
		
		
		try {
			String validateUsername = "select * from User where u_name = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.VALIDATE_USERNAME) ;;
			this.preparedStatement = this.dbmanage.getPreparedStatement(validateUsername) ;
			this.preparedStatement.setString(1, username) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			if(this.resultSet != null && this.resultSet.next()) {
				register.paint("�û����Ѵ���") ;
				// �ر����ݿ�����
				if(this.resultSet != null) {
					this.resultSet.close() ;
				}
				return false ;
			}
			String userRegister = "insert into User (u_name , u_password , u_problem , u_answer) values(? , ? , ? , ?)" ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.REGISTER) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(userRegister) ;
			this.preparedStatement.setString(1, username) ;
			this.preparedStatement.setString(2, password_MD5) ;
			this.preparedStatement.setString(3, problem) ;
			this.preparedStatement.setString(4, answer) ;
			this.preparedStatement.execute() ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	/**
	 * �һ�����
	 * @param findPassword
	 * @return
	 */
	@SuppressWarnings("static-access")
	public boolean findPassword(FindPassword findPassword) {
		String username = findPassword.getUsername() ;
		String problem = findPassword.getProblem() ;
		String answer = findPassword.getAnswer() ;
		String password = findPassword.getPassword() ;
		String confirmPassword = findPassword.getConfirmPassword() ;
		String password_MD5 = new MD5().GetMD5Code(password) + this.generateSecretString() ;
		System.out.println(password);
		if(username.equals("")) {
			findPassword.paint("�û������ܿ�") ;
			return false ;
		} else if(answer.equals("")) {
			findPassword.paint("�𰸲��ܿ�") ;
			return false ;
		} else if(!(password.length() >=6 && password.length() <=12)) {
			findPassword.paint("���볤��Ϊ6~12") ;
			return false ;
		} else if(!password.equals(confirmPassword)) {
			findPassword.paint("���벻һ��") ;
			return false ;
		}
		System.out.println("�һ�����1");
		try {
			String findPasswordSql = "update user set u_password = ? where u_name = ? and u_problem = ? and u_answer = ?" ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.FIND_PASSWORD) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(findPasswordSql) ;
			this.preparedStatement.setString(1, password_MD5) ;
			this.preparedStatement.setString(2, username) ;
			this.preparedStatement.setString(3, problem) ;
			this.preparedStatement.setString(4, answer) ;
			if(this.preparedStatement.executeUpdate() == 1) {
//				this.dbmanage.closeLink() ;
			} else {
				findPassword.paint("�һ�����ʧ��") ;
				return false ;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		System.out.println("�һ�����2");
		return true ;
	}
	
	/**
	 * ͨ���û�����ѯ�û�ID��
	 */
	public int queryIdByName(String username) {
		int id = -1 ; 
		try {
			String queryIdByName = "select u_id from User where u_name = ? " ;
			this.connection = this.dbmanage.getConnection() ;
//			this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_ID_BY_NAME) ;
			this.preparedStatement = this.dbmanage.getPreparedStatement(queryIdByName) ;
			this.preparedStatement.setString(1, username) ;
			this.resultSet = this.preparedStatement.executeQuery() ;
			if(this.resultSet.next()) {
				id = this.resultSet.getInt("u_id") ;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1 ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1 ;
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
		return id ;
	}
	
	/**
	 * ������������õ��ַ���
	 */
	public String generateSecretString(){
		String str = "" ;
		String base = "abcdefghijklmnopqrstuvwxyzABCDEGHIJKLMNOPQRSTUVWSYZ0123456789"; 
		Random random = new Random(); 
		StringBuffer sb = new StringBuffer(); 
	    for (int i = 0; i < 20; i++) {
	        int number = random.nextInt(base.length());
	        sb.append(base.charAt(number));
	        }
	    str = sb.toString(); 
	    return str ;
	}
	
	/**
	 * �޸�����
	 */
	@SuppressWarnings("static-access")
	public boolean changePassword(ChangePassword changePassword) {
		String password = null ;
		
		String oldPassword = changePassword.getOldPassword() ;
		String oldPassowrd_MD5 = new MD5().GetMD5Code(oldPassword) ;
		String newPassword = changePassword.getNewPassword() ;
		String confirmPassword = changePassword.getConfirmPassword() ;
		String newPassword_MD5 = new MD5().GetMD5Code(newPassword) + this.generateSecretString() ;
		
		if(newPassword.equals(confirmPassword)) {
			if(newPassword.length() >= 6 && newPassword.length() <= 12) {
				try {
					String queryPasswordByUserId = "select u_password from user where u_id = ?" ;
					this.connection = this.dbmanage.getConnection() ;
//					this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.QUERY_PAASWORD_BY_USER_ID) ;
					this.preparedStatement = this.dbmanage.getPreparedStatement(queryPasswordByUserId) ;
					this.preparedStatement.setInt(1, Tools.userId) ;
					this.resultSet = this.preparedStatement.executeQuery() ;
					if(this.resultSet.next()) {
						password = this.resultSet.getString("u_password") ;
					}
					if(password != null && oldPassowrd_MD5.equals(password.substring(0 , oldPassowrd_MD5.length()))) {
						String changePasswordSql = "update user set u_password = ? where u_id = ?" ;
//						this.preparedStatement = this.dbmanage.getPreparedStatement(Tools.CHANGE_PAASWORD) ;
						this.preparedStatement = this.dbmanage.getPreparedStatement(changePasswordSql) ;
						this.preparedStatement.setString(1, newPassword_MD5) ;
						this.preparedStatement.setInt(2, Tools.userId) ;
						this.preparedStatement.execute() ;
					}else {
						changePassword.paint("�����벻��ȷ") ;
						return false ;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false ;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
			}else {
				changePassword.paint("���볤�ȱ���λ6~12") ;
				return false ;
			}
		}else {
			changePassword.paint("�¾����벻һ��") ;
			return false ;
		}
		return true ;
	}
}
