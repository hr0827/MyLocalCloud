package com.test.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.json.JSONException;
import org.json.JSONObject;
import com.test.model.ApiTest;
import com.test.tools.Tools;
import com.test.ui.AddHdfsNode;
/**
 * ��������¼���
 * @author asus
 *
 */
public class AddHdfsEvent extends MouseAdapter {
	//��ȡ��������������
	private AddHdfsNode addHdfsNode ;
	public AddHdfsEvent(AddHdfsNode addHdfsNode) {
		this.addHdfsNode = addHdfsNode ;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == this.addHdfsNode.testHdfs) {
			// ������԰�ť
			JOptionPane.showMessageDialog(null, "����һ��", "��Ϣ��ʾ", 1);
		}
		if(e.getSource() == this.addHdfsNode.confirm) {
			// ����Ⱥ�ڵ���Ϣд�����ݿ�
			String ip = this.addHdfsNode.thdfsIP.getText() ;
			if(Tools.loginSuccess.hdfs.addHdfsNode(ip)) {
				JOptionPane.showMessageDialog(null, "HDFS�ڵ���ӳɹ�", "��Ϣ��ʾ", 1);
			}
		}
		if(e.getSource() == this.addHdfsNode.cancle) {
			this.addHdfsNode.setVisible(false) ;
		}
	}

}
