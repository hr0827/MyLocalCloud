package com.test.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import com.test.event.FilePanelEvent;
import com.test.override.JTextField2;
/**
 * ��ʾ�ļ���
 * @author asus
 *
 */
@SuppressWarnings("serial")
public class FilePanel extends JPanel{
	private FilePanelEvent filePanelEvent ;
	public JLabel filePic ;
	public JTextField2 fileName ;
	public JPopupMenu popMenu ;
	public JMenuItem paste ;
	public JMenuItem cut ;
	public JMenuItem rename ;
	public JMenuItem delete ;
	public JMenuItem property ;
	
	public FilePanel(Icon icon , String name) {
		init(icon , name) ;
	}
	/**
	 * �����ļ�ͼ����ļ���
	 * @param icon
	 * @param name
	 */
	public void init(Icon icon , String name) {
		this.setBackground(new Color(252,252,252)) ;
		filePanelEvent = new FilePanelEvent(this) ;
		this.filePic = new JLabel(icon) ;
		this.fileName = new JTextField2("") ;
		this.fileName.setBorder(null) ;
		this.fileName.setFont(new Font("����" ,Font.PLAIN , 13)) ;
		this.fileName.setBackground(new Color(252,252,252)) ;
		this.fileName.setHorizontalAlignment(JTextField2.CENTER);
		this.fileName.setColumns(10) ;
		this.fileName.setText(name) ;
		this.setLayout(new BorderLayout()) ;
		this.add(filePic , BorderLayout.CENTER) ;
		this.add(fileName , BorderLayout.SOUTH) ;
		this.addMouseListener(filePanelEvent) ;
		this.fileName.setEditable(false) ;
		this.setSize(80, 80) ;
	}
	
	/**
	 * ���ļ������һ������ʾ�Ĳ˵�ѡ��
	 * @param x
	 * @param y
	 */
	public void mouseRightClick(int x , int y) {
		popMenu = new JPopupMenu() ;
		paste = new JMenuItem("ճ��") ;
		cut = new JMenuItem("����") ;
		rename = new JMenuItem("������") ;
		delete = new JMenuItem("ɾ��") ;
		property = new JMenuItem("�ļ�����") ;

		popMenu.add(cut) ;
		popMenu.add(paste) ;
		popMenu.add(rename) ;
		popMenu.add(delete) ;
		popMenu.add(property) ;
		popMenu.setLocation(x, y) ;
		popMenu.show(this , x ,y) ;
		
		paste.addActionListener(filePanelEvent) ;
		cut.addActionListener(filePanelEvent) ;
		rename.addActionListener(filePanelEvent) ;
		delete.addActionListener(filePanelEvent) ;
		property.addActionListener(filePanelEvent) ;
	}

}
