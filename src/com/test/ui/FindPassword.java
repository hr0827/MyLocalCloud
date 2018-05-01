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

import com.test.event.FindPasswordEvent;
import com.test.model.User;
import com.test.override.JTextField2;
/**
 * �һ�������
 * @author asus
 *
 */
public class FindPassword extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel = new JPanel();
	
	private JLabel lusername = new JLabel("�����˺ţ�");
	private JLabel lnewpassword = new JLabel("�����룺");
	private JLabel lconfirmPassword = new JLabel("ȷ�����룺");
	private JLabel lquestion = new JLabel("�ܱ����⣺");
	private JLabel lanswer = new JLabel("�ܱ��𰸣�");
	
	private JTextField2 tusername = new JTextField2("");
	private JTextField2 tanswer = new JTextField2("");
	private JPasswordField tpassword = new JPasswordField();
	private JPasswordField tconfirmPassword = new JPasswordField();
	
	private String[] items = {"����˭��" } ;
	private JComboBox<String> jcproblem = new JComboBox<String>(items);

	public JButton confirm = new JButton("ȷ��");
	public JButton cancle = new JButton("ȡ��");
	
	public User user ;	
	private FindPasswordEvent findPasswordEvent ;
	public FindPassword() {
		init() ;
	}
	
	
	public void init() {
		
		user = new User() ;
		findPasswordEvent = new FindPasswordEvent(this) ;
		
		this.setTitle("Safe Cloud �һ�����");
		this.setBounds(525, 150, 450, 500);
		this.setResizable(false);
		this.add(panel);
		
		lusername.setBounds(new Rectangle(50 , 50 , 60 , 30)) ;
		tusername.setBounds(new Rectangle(110 , 50 , 200 , 30)) ;
		tusername.setFont(new Font("����" ,Font.PLAIN , 16)) ;
		lquestion.setBounds(new Rectangle(50 , 100 , 60 , 30)) ;
		jcproblem.setBounds(new Rectangle(110,100,200,30)) ;
		lanswer.setBounds(new Rectangle(50 , 150 , 60 , 30)) ;
		tanswer.setBounds(new Rectangle(110 , 150 , 200 , 30)) ;
		tanswer.setFont(new Font("����" ,Font.PLAIN , 16)) ;
		lnewpassword.setBounds(new Rectangle(50 , 200 , 60 , 30)) ;
		tpassword.setBounds(new Rectangle(110 , 200 , 200 , 30)) ;
		tpassword.setFont(new Font("����" ,Font.PLAIN , 16)) ;
		lconfirmPassword.setBounds(new Rectangle(50 , 250 , 60 , 30)) ;
		tconfirmPassword.setBounds(new Rectangle(110 , 250 , 200 , 30)) ;
		tconfirmPassword.setFont(new Font("����" ,Font.PLAIN , 16)) ;
		
		confirm.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		confirm.setBounds(new Rectangle(115 , 300 , 90 , 30)) ;
		cancle.setBounds(new Rectangle(215 , 300 , 90 , 30)) ;
		
		confirm.addMouseListener(findPasswordEvent) ;
		cancle.addMouseListener(findPasswordEvent) ;
		
		panel.setLayout(null);
		panel.add(lusername) ;
		panel.add(tusername) ;
		panel.add(lquestion) ;
		panel.add(jcproblem) ;
		panel.add(lanswer) ;
		panel.add(tanswer) ;
		panel.add(lnewpassword) ;
		panel.add(tpassword) ;
		panel.add(lconfirmPassword) ;
		panel.add(tconfirmPassword) ;
		panel.add(confirm) ;
		panel.add(cancle) ;
		
		this.setVisible(true) ;
		

	}
	/**
	 * ��ȡ�û���
	 */
	public String getUsername() {
		return this.tusername.getText() ;
	}
	
	/**
	 * ��ȡ��������
	 * @return
	 */
	public String getProblem() {
		return this.jcproblem.getSelectedItem().toString() ;
	}
	
	/**
	 * ��ȡ�����
	 * @return
	 */
	public String getAnswer() {
		return this.tanswer.getText() ;
	}
	
	/**
	 * ��ȡ������
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getPassword() {
		return this.tpassword.getText() ;
	}
	
	/**
	 * ��ȡȷ������
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getConfirmPassword() {
		return this.tconfirmPassword.getText() ;
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
