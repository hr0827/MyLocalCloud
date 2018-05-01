package com.test.event;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import com.sun.corba.se.impl.orbutil.closure.Future;
import com.test.dao.file_dir.FileDelete;
import com.test.dao.file_dir.FileManager;
import com.test.tools.Tools;
import com.test.ui.FilePanel;
import com.test.ui.ShowMessage;
/**
 * �ļ��С��ļ��¼���
 * @author asus
 *
 */
public class FilePanelEvent extends MouseAdapter implements ActionListener,KeyListener {
	String old_filename = "" ;
	String new_filename = "" ;
	private FilePanel filePanel ;
	// �����ļ�����
	private FileManager fileManager ;
	public FilePanelEvent(FilePanel filePanel) {
		this.filePanel = filePanel ;
		this.fileManager = new FileManager() ;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		//˫�������ļ�
		if(e.getSource() == this.filePanel) {
			if(e.getClickCount() == 2) {
				String filename = this.filePanel.fileName.getText() ;
				this.fileManager.openDir(filename) ;
			}
		}
	}
	/**
	 * �������ļ�����
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this.filePanel) {
			// �������ļ���������ʾ��Ӱ
			if(this.filePanel.filePic.getIcon().toString().equals("image/dir.png" )) {
				this.filePanel.filePic.setIcon(new ImageIcon("image/dir2.png")) ;
				this.filePanel.setBackground(new Color(208,228,252)) ;
				this.filePanel.fileName.setBackground(new Color(196,221,252)) ;
				String filename = this.filePanel.fileName.getText() ;
				String fileInfo = this.fileManager.getProperty(filename) ;
				Tools.loginSuccess.fileInfoPanel.removeAll() ;
				Tools.loginSuccess.fileInfoPanel.setBackground(new Color(241,245,251)) ;
				
				JTextArea showFileInfo = new JTextArea(fileInfo) ;
				showFileInfo.setBackground(new Color(241,245,251)) ;
				showFileInfo.setFont(new Font("����" ,Font.PLAIN , 12)) ;
				Tools.loginSuccess.fileInfoPanel.add(showFileInfo) ;
				Tools.loginSuccess.repaint() ;
			}
			// �������ļ�������ʾ��Ӱ
			if(this.filePanel.filePic.getIcon().toString().equals("image/file.png" )) {
				this.filePanel.filePic.setIcon(new ImageIcon("image/file2.png")) ;
				this.filePanel.setBackground(new Color(208,228,252)) ;
				this.filePanel.fileName.setBackground(new Color(196,221,252)) ;
				String filename = this.filePanel.fileName.getText() ;
				String fileInfo = this.fileManager.getProperty(filename) ;
				Tools.loginSuccess.fileInfoPanel.removeAll() ;
				Tools.loginSuccess.fileInfoPanel.setBackground(new Color(241,245,251)) ; ;
				
				JTextArea showFileInfo = new JTextArea(fileInfo) ;
				showFileInfo.setBackground(new Color(241,245,251)) ;
				showFileInfo.setFont(new Font("����" ,Font.PLAIN , 12)) ;
				Tools.loginSuccess.fileInfoPanel.add(showFileInfo) ;
				Tools.loginSuccess.repaint() ;
			}
		}
	}
	/**
	 * ����˳��ļ�����
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this.filePanel) {
			// ����˳��ļ�������ȥ����Ӱ
			if(this.filePanel.filePic.getIcon().toString().equals("image/dir2.png" )) {
				this.filePanel.filePic.setIcon(new ImageIcon("image/dir.png")) ;
				this.filePanel.setBackground(new Color(252,252,252)) ;
				this.filePanel.fileName.setBackground(new Color(252,252,252)) ;
				Tools.loginSuccess.fileInfoPanel.removeAll() ;
				Tools.loginSuccess.fileInfoPanel.setBackground(Color.white) ;
				Tools.loginSuccess.repaint() ;
			}
			// ����˳��ļ�����ȥ����Ӱ
			if(this.filePanel.filePic.getIcon().toString().equals("image/file2.png" )) {
				this.filePanel.filePic.setIcon(new ImageIcon("image/file.png")) ;
				this.filePanel.setBackground(new Color(252,252,252)) ;
				this.filePanel.fileName.setBackground(new Color(252,252,252)) ;
				Tools.loginSuccess.fileInfoPanel.removeAll() ;
				Tools.loginSuccess.fileInfoPanel.setBackground(Color.white) ;
				Tools.loginSuccess.repaint() ;
			}
		}
	}

	// �������һ��¼�
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.isPopupTrigger()) {
			this.filePanel.mouseRightClick(e.getX() , e.getY()) ;
		}
	}
	/**
	 * ����һ����ļ����еĲ���
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand() == "����") {
			// �ļ�����
			this.fileManager.cutFile(this.filePanel.fileName.getText()) ;
			// �����ļ���С
			this.fileManager.updateFileSize(Tools.cutFileId, -Tools.copyFileSize) ;
		}
		if(e.getActionCommand() == "ճ��") {
			// �ļ�ճ��
			this.fileManager.pasteFile() ;
			Tools.loginSuccess.fileBottomPanel.removeAll() ;
			this.fileManager.showFileList(Tools.parentIdStack.lastElement(), Tools.userId) ;
			Tools.loginSuccess.repaint() ;
			// �����ļ���С
			this.fileManager.updateFileSize(Tools.cutFileId, Tools.copyFileSize) ;
			Tools.cutFileId = -1 ;
			Tools.copyFileSize = 0 ;
		}

		if(e.getActionCommand() == "������") {
			old_filename = this.filePanel.fileName.getText() ;
			System.out.println("old  " + old_filename);
			this.filePanel.fileName.requestFocus() ;
			this.filePanel.fileName.setEditable(true) ;
			this.filePanel.fileName.addKeyListener(this) ;
			if(old_filename.contains(".")) {
				int index = old_filename.lastIndexOf(".") ;
				this.filePanel.fileName.setSelectionStart(0) ;
				this.filePanel.fileName.setSelectionEnd(index) ;
			}else {
				this.filePanel.fileName.setSelectionStart(0) ;
				this.filePanel.fileName.setSelectionEnd(old_filename.length()) ;
			}
		}
		if(e.getActionCommand() == "ɾ��") {
			int res = JOptionPane.showConfirmDialog(null, "ȷ��ɾ�����ļ���", "ɾ����ʾ", JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
			if(res == JOptionPane.OK_OPTION) {
				String filename = this.filePanel.fileName.getText() ;
				this.fileManager.queryFileIdAndSizeByName(filename, Tools.parentIdStack.lastElement()) ;
				this.fileManager.updateFileSize(Tools.deleteFileId, -Tools.deleteFileSize) ;
				Tools.deleteFileId = -1 ;
				Tools.deleteFileSize = 0 ;
				int parentId = Tools.parentIdStack.lastElement() ;
				
				/**
				 * �ļ�ɾ����������ٵ����⣩
				 */
				FileDelete fileDeleteManager = new FileDelete(filename , parentId) ;
				FutureTask<Boolean> task = new FutureTask<Boolean>(fileDeleteManager) ;
				new Thread(task).start() ;
				
				try {
					if(task.get()) {
						Tools.loginSuccess.fileBottomPanel.removeAll() ;
						this.fileManager.showFileList(Tools.parentIdStack.lastElement(), Tools.userId) ;
						Tools.loginSuccess.repaint() ;
					}
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else if(res == JOptionPane.CANCEL_OPTION) {
				
			}
		}
		if(e.getActionCommand() == "�ļ�����") {
			String filename = this.filePanel.fileName.getText() ;
			new ShowMessage(this.fileManager.getProperty(filename)) ;
		}
	}
	/**
	 * ��������Enter��ȷ���ύ
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode() ;
		switch(key) {
		case KeyEvent.VK_ENTER:
			new_filename = this.filePanel.fileName.getText() ;
			// �ļ���δ�Ķ���������
			if(old_filename.equals(new_filename)) {
				System.out.println("����û��");
				this.filePanel.fileName.setEditable(false) ;
			}else {
				// �����ļ���
				System.out.println("new : " + new_filename);
				if(new_filename.contains(".") 
						|| new_filename.contains("\\")
						|| new_filename.contains("|")
						|| new_filename.contains("\"")
						|| new_filename.contains("'") 
						|| new_filename.contains("��")
						|| new_filename.contains("��")
						|| new_filename.contains("*")
						|| new_filename.contains(":") 
						|| new_filename.contains("/")
						|| new_filename.contains("?")
				) {
					this.filePanel.fileName.setText(old_filename) ;
					this.filePanel.fileName.setEditable(false) ;
					Tools.loginSuccess.repaint() ;
					JOptionPane.showMessageDialog(null, "�ļ������ܰ����Ƿ��ַ�(\\,|,\",',��,��,*,:,/,?)", "��Ϣ��ʾ", 1);
				}else if(!(new_filename.length() > 0 && new_filename.length() <= 20)) {
					this.filePanel.fileName.setText(old_filename) ;
					this.filePanel.fileName.setEditable(false) ;
					Tools.loginSuccess.repaint() ;
					JOptionPane.showMessageDialog(null, "�ļ������Ƚ���0~20���ַ�����", "��Ϣ��ʾ", 1);
				}else {
					// �ļ������ĳɹ�
					if(this.fileManager.rename(new_filename, old_filename)) {
						System.out.println("���ָ��ĳɹ�");
						this.filePanel.fileName.setText(new_filename) ;
						this.filePanel.fileName.setEditable(false) ;
						Tools.loginSuccess.repaint() ;
					}else {
						// �ļ�������ʧ��
						System.out.println("���ָ���ʧ��");
						this.filePanel.fileName.setText(old_filename) ;
						this.filePanel.fileName.setEditable(false) ;
						Tools.loginSuccess.repaint() ;
					}
				}

			}
			this.filePanel.fileName.removeKeyListener(this) ;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
