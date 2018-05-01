package com.test.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.test.event.AddHdfsEvent;
import com.test.override.JTextField2;
/**
 * ���Hadoop�ڵ㣬ֻ��Ҫ��Ⱥ�е�һ���ڵ㼴��
 * @author asus
 *
 */
@SuppressWarnings("serial")
public class AddHdfsNode extends JFrame {
	private JLabel jhdfsIP ;
	public JTextField2 thdfsIP ;
	
	public JButton testHdfs ;
	public JButton confirm ;
	public JButton cancle ;
	
	private AddHdfsEvent addHdfsEvent ;
	
	private JPanel addHdfsPanel ;
	
	public AddHdfsNode() {
		init() ;
	}
	
	public void init() {
		addHdfsEvent = new AddHdfsEvent(this) ;
		
		jhdfsIP = new JLabel("HDFS�ڵ�IP��");
		
		thdfsIP = new JTextField2("") ;
		thdfsIP.setFont(new Font("����" ,Font.PLAIN , 16)) ;
		
		testHdfs = new JButton("����") ;
		confirm = new JButton("���") ;
		cancle = new JButton("ȡ��") ;
		
		
		addHdfsPanel = new JPanel() ;
		
		addHdfsPanel.setLayout(null) ;
		
		jhdfsIP.setBounds(new Rectangle(50 , 50 , 80 , 30)) ;
		
		thdfsIP.setBounds(new Rectangle(150 , 50 , 100 , 30)) ;

		testHdfs.setBounds(new Rectangle(30 , 300 , 80 , 30));
		confirm.setBounds(new Rectangle(150 , 300 , 80 , 30)) ;
		cancle.setBounds(new Rectangle(200 , 300 , 80 , 30)) ;
		
		addHdfsPanel.add(jhdfsIP) ;
		addHdfsPanel.add(thdfsIP) ;
		
		addHdfsPanel.add(testHdfs) ;
		addHdfsPanel.add(confirm) ;
		addHdfsPanel.add(cancle) ;
		
		testHdfs.addMouseListener(addHdfsEvent) ;
		confirm.addMouseListener(addHdfsEvent) ;
		cancle.addMouseListener(addHdfsEvent) ;
		
		this.setLayout(new BorderLayout()) ;
		this.add(addHdfsPanel) ;
		this.setSize(400, 500) ;
		this.setVisible(true) ;
	}
}
