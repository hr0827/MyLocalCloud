package com.test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.test.tools.Tools;
/**
 * �������ݿ���
 * @author asus
 *
 */
public class DBManage {
	/**
	 * �������ݿ����
	 */
	private Connection conn = null ;
	private PreparedStatement preparedStatement = null ;
	

	/**
	 * ��ȡConnection����
	 * @return
	 * @throws Exception 
	 */
	public Connection getConnection() throws Exception {
		try {
			Class.forName(Tools.DRIVER) ;
			this.conn = DriverManager.getConnection(Tools.URL , Tools.USERNAME , Tools.PASSWORD) ;
		}catch(SQLException e) {
			e.printStackTrace() ;
		}finally {
			
		}
		return this.conn ;
	}
	
	
	/**
	 * ��ȡStatement����
	 * @return
	 */
	public PreparedStatement getPreparedStatement(String sql) throws Exception {
		try {
			this.preparedStatement = this.conn.prepareStatement(sql) ;
		}catch(SQLException e ) {
			e.printStackTrace() ;
		}finally{
			
		}
		return this.preparedStatement ;
	}
	
	/**
	 * �ر�����
	 */
	public void closeLink() {
		try {
			if(this.preparedStatement != null) {
				this.preparedStatement.close() ;
			}
			if(this.conn != null) {
				this.conn.close();
			}
		}catch(SQLException e) {
			e.printStackTrace() ;
		}
	}
	
}
