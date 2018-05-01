package com.test.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import com.test.event.LoginEvent;
import com.test.model.User;
import com.test.override.JTextField2;
/**
 * ��¼������
 * @author asus
 *
 */
public class LoginFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private JPanel pane = new JPanel();
	private JPanel basepane=new JPanel(); 
	public JButton login = new JButton("��      ¼");
	public JButton register = new JButton("ע���˺�");
	public JButton forgotPassword = new JButton("�һ�����");
	public JTextField2 tusername = new JTextField2("");
	private JPasswordField tpassword = new JPasswordField(20);
	private JLabel lusername = new JLabel("�˺ţ�");
	private JLabel lpassword = new JLabel("���룺");
	private JLabel image = new JLabel();
	private ImageIcon img = new ImageIcon("image/1.png");
	private JCheckBox rememberusername = new JCheckBox("��ס����",false);
	private JCheckBox autologin = new JCheckBox("�Զ���¼",false);
	private GridBagLayout gb = new GridBagLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	
	public Register registerFrame ; 
	public LoginSuccess loginSuccess ;
	public FindPassword findPassword ;
	
	public User user ;
	private LoginEvent loginEvent ;
	
	public LoginFrame() {
		init() ;
	}
	
	public void init() {
		loginEvent = new LoginEvent(this) ;
		user = new User() ;
		
		this.register.addMouseListener(loginEvent) ;
		this.login.addMouseListener(loginEvent) ;
		this.forgotPassword.addMouseListener(loginEvent) ;
		
		this.setTitle("Safe Cloud ��¼");
		this.setBounds(500, 250, 500, 375);
		this.setResizable(false);
		this.add(basepane);
		basepane.setLayout(new BorderLayout());
		image.setIcon(img);
		basepane.add(image,BorderLayout.NORTH);
		basepane.add(pane);
		pane.setLayout(gb);
		gbc.insets = new Insets(3,3,3,3);
		lusername.setFont(new Font("����" ,Font.PLAIN , 16)) ;
		gb.setConstraints(lusername, gbc);
		pane.add(lusername);
		gbc.gridwidth = 3;
		tusername.setColumns(20);
		tusername.setFont(new Font("����" ,Font.PLAIN , 16)) ;
		gb.setConstraints(tusername, gbc);
		pane.add(tusername);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gb.setConstraints(register, gbc);
		pane.add(register);
		gbc.gridwidth = 1;
		lpassword.setFont(new Font("����" ,Font.PLAIN , 16)) ;
		gb.setConstraints(lpassword, gbc);
		pane.add(lpassword);
		gbc.gridwidth = 3;
		tpassword.setFont(new Font("����" ,Font.PLAIN , 16)) ;
		gb.setConstraints(tpassword, gbc);
		pane.add(tpassword);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gb.setConstraints(forgotPassword, gbc);
		pane.add(forgotPassword);
		gbc.gridx = 1;
		gbc.gridwidth = 1;
		gb.setConstraints(rememberusername, gbc);
		pane.add(rememberusername);
		gbc.gridx = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gb.setConstraints(autologin, gbc);
		pane.add(autologin);
		gbc.gridx = 1;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		login.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
		gb.setConstraints(login, gbc);
		pane.add(login);
		
		this.setVisible(true);
	}
	
	/**
	 * ��ȡ�û���
	 * @return
	 */
	public String getUsername() {
		return this.tusername.getText() ;
	}
	/**
	 * ��ȡ����
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String getPassword() {
		return this.tpassword.getText() ;
	}
	
	
	/**
	 * ��ʾ�쳣��Ϣ
	 * @param str
	 */
	public void paint(String str) {
		Graphics g = this.getGraphics();
		super.paint(g) ;
		g.setFont(new Font("΢���ź�", Font.BOLD, 15));
		g.drawString(str, 180, 120);
	}
	
}
