package com.test.event;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.test.dao.file_dir.DownloadTableManager;
import com.test.dao.file_dir.FileManager;
import com.test.dao.file_dir.FileUpload;
import com.test.dao.file_dir.UploadTableManager;
import com.test.thread.HdfsNodesListThread;
import com.test.tools.Tools;
import com.test.ui.AddHdfsNode;
import com.test.ui.ChangePassword;
import com.test.ui.CloseWindowOperation;
import com.test.ui.LoginFrame;
/**
 * ��½�ɹ��¼���
 * @author asus
 *
 */
public class LoginSuccessEvent extends MouseAdapter implements ActionListener , WindowListener {
	// �ļ�����
	private FileManager fileManager ;
	// ����ѡ�е��б������������
	private int row ;
	@SuppressWarnings("unused")
	private int column ;
	/**
	 * �ļ��ϴ��������б����
	 */
	private UploadTableManager uploadTableManager ;
	private DownloadTableManager downloadTableManager ;
	
	public LoginSuccessEvent() {
		this.fileManager = new FileManager() ;
		this.uploadTableManager = new UploadTableManager() ;
		this.downloadTableManager = new DownloadTableManager() ;
	}
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		/**
		 * HDFS�ڵ���Ϣ����
		 * ���HDFS�ڵ�
		 */
		if(e.getSource() == Tools.loginSuccess.addHdfsNode) {
			//���ѡ���ļ��ؼ�
			new AddHdfsNode() ;
		}
		/**
		 * ��ʾ�û���HDFS�ڵ��б�
		 */
		if(e.getSource() == Tools.loginSuccess.myHdfsNode) {
			new Thread(new HdfsNodesListThread()).start() ;
		}
		
		/**
		 * �ļ�������
		 * ������һ��Ŀ¼��ͨ��ջ����
		 */
		if(e.getSource() == Tools.loginSuccess.goBack) {
			if(Tools.parentIdStack.size() > 1) {
				Tools.parentIdStack.pop() ;
				Tools.loginSuccess.fileBottomPanel.removeAll() ;
				this.fileManager.showFileList(Tools.parentIdStack.lastElement(), Tools.userId) ;
				Tools.loginSuccess.repaint() ;
				// ����ʱ�����ļ���ַ��
				int index = Tools.loginSuccess.search.getText().lastIndexOf("\\") ;
				Tools.loginSuccess.search.setText(Tools.loginSuccess.search.getText().substring(0 , index)) ;
			}
		}
		/**
		 * �ڵ�ǰĿ¼�´������ļ���
		 */
		if(e.getSource() == Tools.loginSuccess.createDir) {
			this.fileManager.createDir() ;
		}
		/**
		 * ѡ��Ҫ�ϴ����ļ�
		 */
		if(e.getSource() == Tools.loginSuccess.upload) {
			Tools.loginSuccess.fileChooserUpload.showDialog(Tools.loginSuccess , "�ϴ�������");
		}

		/**
		 * �Ӹ�Ŀ¼��ʾ�ļ��б������ļ��С��ļ�
		 */
		if(e.getSource() == Tools.loginSuccess.myFiles) {
			// ��ո�Ŀ¼ID��ջ
			Tools.parentIdStack.removeAllElements() ;
			// �Ƴ�������ԭ�е��ļ�
			Tools.loginSuccess.fileBottomPanel.removeAll() ;
			// ��ʼ����Ŀ¼ջ����ʼֵΪ0����ʾ�ڸ�Ŀ¼
			Tools.parentIdStack.add(0) ;
			//��һ����������Ϊ0��ʾ��ʼʱ��ʾ���Ǹ�Ŀ¼�µ��ļ����ڶ��������Ǹ����û�����ѯ�����û�ID��
			this.fileManager.showFileList(0 , Tools.userId) ;
			// ��ַ������Ϊ��
			Tools.loginSuccess.search.setText("") ;
			// ˢ�½���
			Tools.loginSuccess.repaint() ;
		}
	}
	// �������һ��¼�
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton()==MouseEvent.BUTTON3) {
			/**
			 *  �����ļ�ճ��
			 */
			if(e.getSource() == Tools.loginSuccess.fileBottomPanel ) {
				Tools.loginSuccess.mouseRightCilck(e.getX() , e.getY()) ;
			}
			/**
			 * �ϴ������б�����Ҽ����ܲ���
			 */
			if(e.getSource() == Tools.loginSuccess.uploadTable) {
				Point p = e.getPoint(); 
				row = Tools.loginSuccess.uploadTable.rowAtPoint(p); 
				column = Tools.loginSuccess.uploadModel.getColumnCount() ; 
				System.out.println("row -> " + row + " column -> " + column);
				int selectRow = Tools.loginSuccess.uploadTable.getSelectedRow() ;
				System.out.println("selectRow ->" + selectRow);
				if (selectRow != -1) {
					Tools.loginSuccess.mouseRightClickUploadTable(e.getX() , e.getY()) ;
				}
			}
			if(e.getSource() == Tools.loginSuccess.downloadTable) {
				Point p = e.getPoint(); 
				row = Tools.loginSuccess.downloadTable.rowAtPoint(p); 
				column = Tools.loginSuccess.downloadModel.getColumnCount() ; 
				
				int selectRow = Tools.loginSuccess.downloadTable.getSelectedRow() ;
				if (selectRow != -1) {
					Tools.loginSuccess.mouseRightClickDownloadTable(e.getX() , e.getY()) ;
				}
			}
		}
	}
	
	/**
	 * �����ļ��ָ���ܡ��ϴ�����
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		// ͨ��Ŀ¼ѡ���ϴ��ļ��Ĳ���
		if(e.getSource() == Tools.loginSuccess.fileChooserUpload) {
			if(e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
				// ��ȡ��Ҫ�ϴ����ļ���ַ
				String filePath = Tools.loginSuccess.fileChooserUpload.getSelectedFile().toString() ;
				if(this.uploadTableManager.checkUploadTable(filePath.substring(filePath.lastIndexOf("\\") + 1))) {
    				FileUpload fileUpload = new FileUpload(filePath , "E:\\SafeCloudFiles\\uploadtemp") ;
    				new Thread(fileUpload).start();
				}else {
					JOptionPane.showMessageDialog(null , "���ļ������ϴ��б��У�����","�ϴ���ʾ" , 1);
				}
			}
		}
		
		// �ļ�����ճ������
		if(e.getActionCommand() == "ճ��") {
			// �ļ�ճ��
			this.fileManager.pasteFile() ;
			Tools.loginSuccess.fileBottomPanel.removeAll() ;
			this.fileManager.showFileList(Tools.parentIdStack.lastElement(), Tools.userId) ;
			Tools.loginSuccess.repaint() ;
			// �����ļ��д�С
			this.fileManager.updateFileSize(Tools.cutFileId, Tools.copyFileSize) ;
			Tools.cutFileId = -1 ;
			Tools.copyFileSize = 0 ;
		}
		
		/**
		 * �ϴ��б����
		 */
//		if(e.getSource() == Tools.loginSuccess.reUpload) {
//			String filename = (String)Tools.loginSuccess.uploadModel.getValueAt(row, 0) ;
//			String state = (String)Tools.loginSuccess.uploadModel.getValueAt(row, 2) ;
//			if(state.equals("�ϴ�ʧ��")) {
//				new Thread(new ReUpload(filename)).start() ;
//			}else {
//				JOptionPane.showMessageDialog(null, "�ļ����ϴ��ɹ������������ϴ���", "��Ϣ��ʾ", 1);
//			}
//		}
		if(e.getSource() == Tools.loginSuccess.deleteUploadAssignment) {
			System.out.println("ɾ���ϴ�����");
			String filename = (String)Tools.loginSuccess.uploadModel.getValueAt(row, 0) ;
			String fileParentName = filename.substring(0, filename.indexOf(".")) ;
			System.out.println("��Ŀ¼ " + fileParentName);
			if(this.uploadTableManager.deleteUploadAssignment(filename)) {
				this.uploadTableManager.fileManager.deleteDirectory("E:\\SafeCloudFiles\\uploadtemp\\" +fileParentName) ;
			}
		}
		/**
		 * �����б����
		 */
//		if(e.getSource() == Tools.loginSuccess.reDownload) {
//			String filename = (String)Tools.loginSuccess.downloadModel.getValueAt(row, 0) ;
//			String state = (String)Tools.loginSuccess.downloadModel.getValueAt(row, 2) ;
//			if(state.equals("����ʧ��")) {
//				new Thread(new ReDownload(filename)).start() ;
//			}else {
//				JOptionPane.showMessageDialog(null, "�ļ������سɹ��������������أ�", "��Ϣ��ʾ", 1);
//			}
//		} 
		if(e.getSource() == Tools.loginSuccess.deleteDownloadAssignment) {
			System.out.println("ɾ����������");
			String filename = (String)Tools.loginSuccess.downloadModel.getValueAt(row, 0) ;
			
			String fileParentName = filename.substring(0, filename.indexOf(".")) ;
			System.out.println("��Ŀ¼ " + fileParentName);
			if(this.downloadTableManager.deleteDownloadAssignment(filename)) {
				this.downloadTableManager.fileManager.deleteDirectory("E:\\SafeCloudFiles\\downloadtemp\\" +fileParentName) ;
			}
		}
		
		/**
		 * �˵����¼�����
		 */
		if(e.getSource() == Tools.loginSuccess.useritem) {
			System.out.println("�û���Ϣ");
		}
		if(e.getSource() == Tools.loginSuccess.pswditem) {
			System.out.println("�޸�����");
			@SuppressWarnings("unused")
			ChangePassword changePassword = new ChangePassword() ;
		}
		if(e.getSource() == Tools.loginSuccess.logoutitem) {
			System.out.println("ע��");
			Tools.loginSuccess.setVisible(false) ;
			@SuppressWarnings("unused")
			LoginFrame loginFrame = new LoginFrame() ;
		}
	}
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == Tools.loginSuccess) {
			new CloseWindowOperation() ;
		}
		System.exit(0) ;
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
	}
}
