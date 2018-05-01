package com.test.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import com.test.ui.Register;
/**
 * �û�ע���¼���
 * @author asus
 *
 */
public class RegisterEvent extends MouseAdapter {
	/**
	 * ��ȡע��������
	 */
	private Register register ;
	public RegisterEvent(Register register) {
		this.register = register ;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this.register.confirm) {
			try {
				if(this.register.user.register(this.register)) {
					JOptionPane.showMessageDialog(null, "ע��ɹ�", "��Ϣ��ʾ", 1);
					this.register.setVisible(false) ;
				}else {
					System.out.println("ע��ʧ��");
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(e.getSource() == this.register.cancle) {
			this.register.setVisible(false) ;
		}
	}
}
