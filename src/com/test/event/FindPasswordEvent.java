package com.test.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import com.test.ui.FindPassword;
/**
 * �һ������¼���
 * @author asus
 *
 */
public class FindPasswordEvent extends MouseAdapter {
	/**
	 * ��ȡ�һ������������
	 */
	private FindPassword findPassword ;
	public FindPasswordEvent(FindPassword findPassword) {
		this.findPassword = findPassword ;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this.findPassword.confirm) {
			if(this.findPassword.user.findPassword(this.findPassword)) {
				JOptionPane.showMessageDialog(null, "�һ�����ɹ�", "��Ϣ��ʾ", 1);
				this.findPassword.setVisible(false) ;
			}else {
//				JOptionPane.showMessageDialog(null, "�һ�����ʧ��", "��Ϣ��ʾ", 1);
			}
		}
		
		if(e.getSource() == this.findPassword.cancle) {
			this.findPassword.setVisible(false) ;
		}

	}
}
