package com.test.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.test.dao.file_dir.FileManager;
import com.test.dao.file_dir.FileSeperate;
import com.test.event.LoginSuccessEvent;
import com.test.model.HadoopHDFS;
import com.test.override.JTextField2;

/**
 * ��¼�ɹ�������
 * @author asus
 *
 */
@SuppressWarnings("serial")
public class LoginSuccess extends JFrame {
	ImageIcon img = null;
	
	//�����	
	private JTabbedPane tabbedpane = new JTabbedPane(JTabbedPane.LEFT,JTabbedPane.WRAP_TAB_LAYOUT); 
	
	//�˵���
	private JMenuBar menubar ;
	private JMenu user ;
	private JMenu option ;
	public JMenuItem useritem ;
	public JMenuItem pswditem ;
	public JMenuItem logoutitem ;
	
	//����ͼƬ
	private ImageIcon backpic = new ImageIcon("image/background.png");
	
	//�����б�ؼ�
	public JPanel transmitPanel ;
	private JTabbedPane transmittabbedpane ;  
	//�ϴ��б�
	public JScrollPane uploadScrollpane; 
	private JPanel uploadPanel ;
	public DefaultTableModel uploadModel ;
	public JTable uploadTable ;
	public String[] uploadcolnames={"�ļ���","״̬"};
	public JMenuItem reUpload ;
	public JMenuItem deleteUploadAssignment ;
	//�����б�
	private JScrollPane downloadScrollpane; 
	private JPanel downloadPanel;
	public DefaultTableModel downloadModel ;
	public JTable downloadTable ;
	String[] downloadcolnames={"�ļ���","״̬"};
	public JMenuItem reDownload ;
	public JMenuItem deleteDownloadAssignment ;
	
	//HDFS�ڵ�����ֿؼ�
	public JPanel hdfsPanel ;
	@SuppressWarnings("unused")
	private ImageIcon hdfsIcon ;
	public JLabel addHdfsNode ;
	public JLabel myHdfsNode ;
	private JPanel hdfsTopPanel ;
	public JPanel hdfsBottomPanel ;
	public DefaultTableModel hdfsModel; 
	public JTable hdfsNodesTable; 
	private JScrollPane hdfsScrollpane; 
	String[] colnames={"IP��ַ","������","���ÿռ�","ȫ���ռ�","ʹ����","�ڵ�����"}; 
	
	// HDFS�ļ�ϵͳ��
	public HadoopHDFS hdfs ;
	
	//�ҵ��ļ������ֿؼ�
	public JPanel file ;
	@SuppressWarnings("unused")
	private ImageIcon fileIcon ;
	private JPanel fileTopPanel ;
	private JPanel navicatPanel ;
	@SuppressWarnings("unused")
	private JPanel searchPanel ;
	public JPanel fileBottomPanel ;
	public JPanel fileInfoPanel ;
	@SuppressWarnings("unused")
	private JScrollPane scrollPane ;
	
	//�ļ���������
	public JLabel goBack ; // ������һ��
	public JLabel createDir ; //�����ļ���
	public JLabel upload ; // �ϴ��ļ�
	public JLabel  myFiles ; // ���ϴ����ļ�
	public JFileChooser fileChooserUpload ; //ѡ��Ҫ�ϴ����ļ�
	public JTextField2 search ;
	public FilePanel filePanel ;
	public FileManager fileManager ;
	
	// �ļ����С�ճ������
	public JPopupMenu popMenu ;
	public JMenuItem paste ;
	
	// ��¼�ɹ�������¼�����
	public LoginSuccessEvent loginSuccessEvent ;
	
	public LoginSuccess() {
		init() ;
		setMenuBar() ;
		setUploadList(0) ;
		setDownloadList(0) ;
		this.addWindowListener(loginSuccessEvent) ;
	}
	
	public void init() {
		// HDFS�ļ�ϵͳ���ʼ��
		hdfs = new HadoopHDFS() ; 
		
		// �ļ������ʼ��
		fileManager = new FileManager() ;
		
		loginSuccessEvent = new LoginSuccessEvent() ;
		
		this.setBounds(220,10,900,675);
		this.setTitle("Safe Cloud");

		//���ñ���
		JPanel panel = new JPanel(){
			 public void paintComponent(Graphics g){
				 g.drawImage(backpic.getImage(), 0, 0, this.getSize().width,this.getSize().height,this);
			 }
		};
		this.add(panel);
		panel.setLayout(new BorderLayout());
		//���tabҳ

		panel.add(tabbedpane);
		
		/**
		 * �����б���
		 */
		transmitPanel = new JPanel();
		transmittabbedpane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.WRAP_TAB_LAYOUT);

		transmitPanel.setLayout(new BorderLayout());
		transmitPanel.add(transmittabbedpane);
		
		/**
		 * HDFS�ڵ������
		 */
		hdfsPanel = new JPanel() ;
//		netDiskIcon = new ImageIcon("image/plate.png") ;
		hdfsTopPanel = new JPanel() ;
		hdfsBottomPanel = new JPanel() ;
		addHdfsNode = new JLabel("���HDFS��Ⱥ") ;
		myHdfsNode = new JLabel("HDFS�ڵ��б�") ;
		this.fileManager.fileSeperate = new FileSeperate() ;
		hdfsModel = new DefaultTableModel(colnames,1); 
//		System.out.println("HDFS�ڵ������" + this.fileManager.fileSeperate.getFileBlockNum());
		hdfsNodesTable = new JTable(hdfsModel){
			public boolean isCellEditable(int row , int column) {
				return false ;
			}
		};
		hdfsScrollpane = new JScrollPane(hdfsNodesTable) ;
		hdfsPanel.setLayout(new BorderLayout() ) ;
		hdfsTopPanel.setLayout(new FlowLayout(FlowLayout.LEFT)) ;
		hdfsBottomPanel.setLayout(new BorderLayout()) ;
		hdfsTopPanel.add(myHdfsNode) ;
		hdfsTopPanel.add(addHdfsNode) ;		
		hdfsBottomPanel.add(hdfsScrollpane) ;
		hdfsPanel.add(hdfsTopPanel , BorderLayout.NORTH) ;
		hdfsPanel.add(hdfsBottomPanel , BorderLayout.CENTER) ;
		
		hdfsNodesTable.addMouseListener(loginSuccessEvent) ;
		addHdfsNode.addMouseListener(loginSuccessEvent) ;
		myHdfsNode.addMouseListener(loginSuccessEvent) ;
		
		/**
		 * �ļ�������
		 */
		goBack = new JLabel() ; //������һ��
		goBack.setIcon(new ImageIcon("image/goback.png")) ;
		goBack.addMouseListener(loginSuccessEvent) ;
		
		createDir = new JLabel(new ImageIcon("image/createDir.png")) ; //�½��ļ���
		createDir.addMouseListener(loginSuccessEvent) ;
		
		upload = new JLabel(new ImageIcon("image/upload.png")) ; // �ϴ��ļ�
		upload.addMouseListener(loginSuccessEvent) ;
		
		myFiles = new JLabel("�ҵ��ļ�") ;
		myFiles.addMouseListener(loginSuccessEvent) ;
				
		search = new JTextField2("") ;
		search.setEditable(false);
		//���ò�ѯ��ĳ���
		search.setColumns(60) ;
		
		//ѡ��Ҫ�ϴ����ļ�
		fileChooserUpload = new JFileChooser() ;																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																						
		fileChooserUpload.setDialogType(JFileChooser.OPEN_DIALOG) ;
		fileChooserUpload.addActionListener(loginSuccessEvent) ;
		
		file = new JPanel() ;
		file.setLayout(new BorderLayout()) ;
		
		fileTopPanel = new JPanel() ;
		fileTopPanel.setLayout(new BorderLayout()) ;
		
		//���õ�����
		navicatPanel = new JPanel() ;
		navicatPanel.setLayout(new FlowLayout(FlowLayout.LEFT , 5 , 0)) ;
		navicatPanel.add(goBack) ;
		navicatPanel.add(myFiles) ;
		navicatPanel.add(createDir) ;
		navicatPanel.add(upload) ;
		navicatPanel.add(search) ;
		
		fileBottomPanel = new JPanel() ;
		this.fileBottomPanel.setBackground(new Color(252,252,252)) ;
		//����Ϊ�������ʾ
		fileBottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT , 10 , 0) ) ;
		fileBottomPanel.addMouseListener(loginSuccessEvent) ;
		fileIcon = new ImageIcon("image/plate.png") ;

		fileInfoPanel=  new JPanel() ;
		fileInfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT)) ;
		
		fileTopPanel.add(navicatPanel,BorderLayout.CENTER ) ;
		
		file.add(fileTopPanel , BorderLayout.NORTH) ;
		file.add(fileBottomPanel , BorderLayout.CENTER) ;		
		file.add(fileInfoPanel , BorderLayout.SOUTH) ;		
		
		//���Tabҳ
		tabbedpane.addTab("�ҵ��ļ�"  , file) ;
		tabbedpane.addTab("�����б�" , transmitPanel) ;
		tabbedpane.addTab("HDFS����" , hdfsPanel) ;
//		drag();
		this.setVisible(true) ;
	}
	/**
	 * ���ò˵���
	 */
	public void setMenuBar() {
		menubar = new JMenuBar();
		user = new JMenu("�û�");
		option = new JMenu("ѡ��");
		useritem = new JMenuItem("�û���Ϣ");
		pswditem = new JMenuItem("�޸�����");
		logoutitem = new JMenuItem("ע���˻�");
		// ���˵�������¼�
		useritem.addActionListener(loginSuccessEvent) ;
		pswditem.addActionListener(loginSuccessEvent) ;
		logoutitem.addActionListener(loginSuccessEvent) ;
		//��װ�˵���
		user.add(useritem);
		user.add(pswditem);
		user.add(logoutitem);
		menubar.add(user);
		menubar.add(option);
		this.setJMenuBar(menubar);
	}

    //�������ק����
//    public void drag() {
//    	new DropTarget( fileBottomPanel, DnDConstants.ACTION_COPY_OR_MOVE, new FileDragIntoEvent()) ;
//    }
    
    /**
     * �����ļ��ϴ��б�
     * @param rows
     */
    public void setUploadList(int rows) {
    	//�ϴ��б�
		uploadPanel = new JPanel();
		uploadPanel.setLayout(new BorderLayout()) ;
		
		uploadModel = new DefaultTableModel(uploadcolnames,rows);
		uploadTable = new JTable(uploadModel){
			public boolean isCellEditable(int row , int column) {
				return false ;
			}
		};
		uploadTable.addMouseListener(loginSuccessEvent) ;
		uploadScrollpane = new JScrollPane(uploadTable) ; 
		
		transmittabbedpane.add("�����ϴ�", uploadPanel);
		uploadPanel.add(uploadScrollpane);
    }
    
    /**
     * �����ļ������б�
     * @param rows
     */
    public void setDownloadList(int rows) {
		downloadPanel = new JPanel();	
		downloadPanel.setLayout(new BorderLayout()) ;
		downloadModel = new DefaultTableModel(downloadcolnames,rows);
		downloadTable = new JTable(downloadModel){
			public boolean isCellEditable(int row , int column) {
				return false ;
			}
		};
		downloadTable.addMouseListener(loginSuccessEvent) ;
		downloadScrollpane = new JScrollPane(downloadTable) ;
		
		transmittabbedpane.add("��������", downloadPanel);

		downloadPanel.add(downloadScrollpane);
    }
    
	/**
	 * ���ļ�������嵥���Ҽ�������ճ������
	 * @param x
	 * @param y
	 */
	public void mouseRightCilck(int x,int y) {
		System.out.println("������");
		// �ļ�ճ������
		popMenu = new JPopupMenu() ;
		paste = new JMenuItem("ճ��") ;
		popMenu.add(paste) ;
		popMenu.setLocation(x, y) ;
		popMenu.show(this.fileBottomPanel , x ,y) ;
		paste.addActionListener(loginSuccessEvent) ;
	}
    
    /**
     * �ϴ��б������Ҽ�����
     * @param x
     * @param y
     */
	public void mouseRightClickUploadTable(int x , int y) {
		popMenu = new JPopupMenu() ;
		reUpload = new JMenuItem("ʧ���ش�") ;
		deleteUploadAssignment = new JMenuItem("ɾ������") ;

		reUpload.addActionListener(loginSuccessEvent) ;
		deleteUploadAssignment.addActionListener(loginSuccessEvent) ;
		
		popMenu.add(reUpload) ;
		popMenu.add(deleteUploadAssignment) ;
		popMenu.setLocation(x, y) ;
		popMenu.show(this.uploadTable , x ,y) ;
	}
	
	/**
	 * �����б������Ҽ�����
	 * @param x
	 * @param y
	 */
	public void mouseRightClickDownloadTable(int x , int y) {
		popMenu = new JPopupMenu() ;
		reDownload = new JMenuItem("ʧ������") ;
		deleteDownloadAssignment = new JMenuItem("ɾ������") ;

		reDownload.addActionListener(loginSuccessEvent) ;
		deleteDownloadAssignment.addActionListener(loginSuccessEvent) ;
		
		popMenu.add(reDownload) ;
		popMenu.add(deleteDownloadAssignment) ;
		popMenu.setLocation(x, y) ;
		popMenu.show(this.downloadTable , x ,y) ;
	}
}
