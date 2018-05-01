package com.test.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.test.dao.file_dir.FileManager;
import com.test.thread.DownloadListTableThread;
import com.test.thread.UploadListTableThread;
import com.test.tools.Tools;
import com.test.ui.FindPassword;
import com.test.ui.LoginFrame;
import com.test.ui.LoginSuccess;
import com.test.ui.Register;
/**
 * ��¼��֤�¼���
 * @author asus
 *
 */
public class LoginEvent extends MouseAdapter {
	/**
	 * ��ȡ��¼���������
	 */
	private LoginFrame loginFrame ;
	public LoginEvent(LoginFrame loginFrame) {
		this.loginFrame = loginFrame ;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// ע��
		// TODO Auto-generated method stub
		if(e.getSource() == this.loginFrame.register) {
			this.loginFrame.registerFrame = new Register() ;
		}else if (e.getSource() == this.loginFrame.login) {
			//��¼��֤�ɹ�
			try {
				if(this.loginFrame.user.login(this.loginFrame)) {
					// �����û�������Ψһ ���û�ID��
					Tools.userId = this.loginFrame.user.queryIdByName(this.loginFrame.tusername.getText()) ;
					System.out.println("�û�ID�ţ� " + Tools.userId);
					if(Tools.userId != -1) {
						// ��ʼ����½�ɹ�����
						Tools.loginSuccess = new LoginSuccess() ;
						//���ص�¼����
						this.loginFrame.setVisible(false) ;
						// ��ʼ����Ŀ¼ջ����ʼֵΪ0����ʾ�ڸ�Ŀ¼
						Tools.parentIdStack.add(0) ;
						//��һ����������Ϊ0��ʾ��ʼʱ��ʾ���Ǹ�Ŀ¼�µ��ļ����ڶ��������Ǹ����û�����ѯ�����û�ID��
						new FileManager().showFileList(0 , Tools.userId) ;
						new Thread(new UploadListTableThread(Tools.loginSuccess)).start() ;
						new Thread(new DownloadListTableThread(Tools.loginSuccess)).start() ;
						// ��ʾHDFS�ڵ���Ϣ
						Tools.loginSuccess.hdfs.showHdfsNodes() ;
					}
				}
			}catch(Exception ee) {
				ee.printStackTrace() ;
			}
		}else  if(e.getSource() == this.loginFrame.forgotPassword){
			// ��ʾ�һ��������
			this.loginFrame.findPassword = new FindPassword() ;
		}
	}
}
