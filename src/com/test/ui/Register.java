package com.test.ui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import com.test.event.RegisterEvent;
import com.test.model.User;
import com.test.override.JTextField2;

/**
 * �û�ע����
 * @author asus
 *
 */
@SuppressWarnings("serial")
public class Register extends JFrame {
	//	���
	private JPanel panel = new JPanel();
	
	private JLabel lusername = new JLabel("�����˺ţ�");
	private JLabel lpassword = new JLabel("�������룺");
	private JLabel lconfirmpassword = new JLabel("ȷ�����룺");
	private JLabel lquestion = new JLabel("�ܱ����⣺");
	private JLabel lanswer = new JLabel("�ܱ��𰸣�");
	
	private JTextField2 tusername = new JTextField2("");
	private JTextField2 tanswer = new JTextField2("");
	private JPasswordField tpassword = new JPasswordField();
	private JPasswordField tconfirmPassword = new JPasswordField();	
	private String[] items = {"����˭��"} ;
	private JComboBox<String> jcproblem = new JComboBox<String>(items);
	public JButton confirm = new JButton("ȷ��");
	public JButton cancle = new JButton("ȡ��");
	
	private RegisterEvent registerEvent ;	
	public User user = null ;
	
	public Register() {
		init() ;
	}
	
	public void init() {
		registerEvent = new RegisterEvent(this) ;
		user = new User() ;
		
		this.setTitle("Safe Cloud ע��");
		this.setBounds(525, 150, 450, 500);
		this.setResizable(false);
		this.add(panel);
		//���λ��
		lusername.setBounds(new Rectangle(50 , 50 , 80 , 30)) ;
		tusername.setBounds(new Rectangle(110 , 50 , 200 , 30)) ;
		tusername.setFont(new Font("����" ,Font.PLAIN , 16)) ;
		lpassword.setBounds(new Rectangle(50 , 100 , 80 , 30)) ;
		tpassword.setBounds(new Rectangle(110 , 100 , 200 , 30)) ;
		tpassword.setFont(new Font("����" ,Font.PLAIN , 16)) ;
		lconfirmpassword.setBounds(new Rectangle(50 , 150 , 80 , 30)) ;
		tconfirmPassword.setBounds(new Rectangle(110 , 150 , 200 , 30)) ;
		tconfirmPassword.setFont(new Font("����" ,Font.PLAIN , 16)) ;
		
		lquestion.setBounds(new Rectangle(50 , 200 , 80 , 30)) ;
		jcproblem.setBounds(new Rectangle(110 , 200 , 200 , 30)) ;
		lanswer.setBounds(new Rectangle(50 , 250 , 80 , 30)) ;
		tanswer.setBounds(new Rectangle(110 , 250 , 200 , 30)) ;
		tanswer.setFont(new Font("����" ,Font.PLAIN , 16)) ;
		
		confirm.setBounds(new Rectangle(115 , 300 , 90 , 30)) ;
		confirm.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		cancle.setBounds(new Rectangle(215 , 300 , 90 , 30)) ;
		//�������¼�
		confirm.addMouseListener(registerEvent) ;
		cancle.addMouseListener(registerEvent) ;
		//������
		panel.setLayout(null);
		panel.add(lusername) ;
		panel.add(tusername) ;
		panel.add(lpassword) ;
		panel.add(lanswer) ;
		panel.add(tpassword) ;
		panel.add(lconfirmpassword) ;
		panel.add(tconfirmPassword) ;
		panel.add(lquestion) ;
		panel.add(jcproblem) ;
		panel.add(tanswer) ;
		panel.add(confirm) ;
		panel.add(cancle) ;
		
		this.setVisible(true);
		
	}
	/**
	 * ��ȡ�û���
	 */
	public String getUsername() {
		return this.tusername.getText() ;
	}
	
	/**
	 * ��ȡ����
	 */
	@SuppressWarnings("deprecation")
	public String getPassword() {
		return this.tpassword.getText() ;
	}
	/**
	 * ��ȡȷ������
	 */
	@SuppressWarnings("deprecation")
	public String getConfirmPassword() {
		return this.tconfirmPassword.getText() ;
	}
	/**
	 * ��ȡ����
	 */
	public String getProblem() {
		return this.jcproblem.getSelectedItem().toString() ;
	}
	/**
	 * ��ȡ�����
	 */
	public String getAnswer(){
		return this.tanswer.getText() ;
	}

	/**
	 * ��ʾ�쳣��Ϣ
	 * @param str
	 */
	public void paint(String str) {
		Graphics g = this.getGraphics();
		super.paint(g) ;
		g.setFont(new Font("΢���ź�", Font.BOLD, 15));
		g.drawString(str, 200, 60);
	}

}
