package com.client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPopupMenu;

import java.awt.Component;

import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;

import com.db.PeerDB.PeerHSQLDB;
import com.rmi.api.IRegister;
import com.rmi.api.impl.PeerTransfer;
import com.rmi.api.impl.Register;
import com.util.SystemUtil;

import java.awt.Button;

import javax.swing.JTextField;
import javax.swing.JLabel;

import org.apache.log4j.Logger;

public class ClientWindow {

	private final Logger LOGGER = Logger.getLogger(ClientWindow.class);
	
	private JFrame frame;
	private JTextArea textArea;
	private static ClientWindow instance;
	private final JOptionPane optionPane = new JOptionPane();
	private final JFileChooser fileChooser = new JFileChooser();
	private JTextField textField_serverIP;
	private JTextField textField_serverPort;
	private JTextField textField_downloadFileName;

	// regular expression
	private Pattern pattern;
	private Matcher matcher;
	private final String IP_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	private final String PORT_PATTERN = "^[\\d]{2,5}$";

	// default value
	private final String default_IP = "192.168.1.125";
	private final String default_port = "1099";

	// status
	private boolean connected = false;
	
	// Object
	private Peer peer;
	Registry peerRegistry;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientWindow window = ClientWindow.getInstance();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		PeerHSQLDB.initDB();

		
//		try {
//			IRegister register = (IRegister)Naming.lookup("rmi://192.168.1.61:1099/register");
//			register.registerPeer("1111");
////			Thread.sleep(10000);
//			register.registerFile("haha.txt");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
	

	}

	/**
	 * Create the application.
	 */
	private ClientWindow() {
		initialize();
		peer = new Peer(this);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnAbout = new JMenu("About...");
		menuBar.add(mnAbout);

		JMenuItem mntmAbout = new JMenuItem("About...");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "Vincent");
			}
		});
		mnAbout.add(mntmAbout);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setAutoscrolls(true);
		scrollPane.setBounds(0, 0, 794, 255);
		panel.add(scrollPane);

		textArea = new JTextArea();
		textArea.setMargin(new Insets(0, 5, 0, 0));
		scrollPane.setViewportView(textArea);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);

		JButton btnNewButton = new JButton("Share Files");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// String aa = JOptionPane.showInputDialog(frame, "flsdjf");
				// System.out.println(aa);

				//fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setMultiSelectionEnabled(true);
				fileChooser.showOpenDialog(frame);
				File[] files = fileChooser.getSelectedFiles();
				for(File file:files) {
					if(peer.shareFile(file)) {
						textArea.append(SystemUtil.getSimpleTime()+"share file ["+file.getName()+"]\n");
					}else {
						textArea.append(SystemUtil.getSimpleTime()+"Unable to register file [" + file.getName() + "]\n");
					}
				}
				
//				if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
//					// this.downloadPath =
//					// fc.getSelectedFile().getAbsolutePath();
//					// this.pathLabel.setText("默认路径：" + this.downloadPath);
//					String path = fileChooser.getSelectedFile().getAbsolutePath();
//					System.out.println(path);
//				}

			}
		});
		btnNewButton.setBounds(19, 301, 122, 26);
		panel.add(btnNewButton);

		textField_serverIP = new JTextField();
		textField_serverIP.setText(default_IP);
		textField_serverIP.setBounds(79, 261, 122, 28);
		panel.add(textField_serverIP);
		textField_serverIP.setColumns(10);

		final JButton btnConnect = new JButton("connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					String serverIP = textField_serverIP.getText();
					String serverPort = textField_serverPort.getText();

					pattern = Pattern.compile(IP_PATTERN);
					matcher = pattern.matcher(serverIP);
					if (!matcher.matches()) {
						JOptionPane.showMessageDialog(frame, "The IP address is not valid!", "ERROR", JOptionPane.ERROR_MESSAGE);
						return;
					}
					pattern = Pattern.compile(PORT_PATTERN);
					matcher = pattern.matcher(serverPort);
					if (!matcher.matches()) {
						JOptionPane.showMessageDialog(frame, "The port is not valid!", "ERROR", JOptionPane.ERROR_MESSAGE);
						return;
					}

					IRegister register = (IRegister) Naming.lookup("rmi://" + serverIP + ":" + serverPort + "/register");
					// register service port
					peerRegistry = LocateRegistry.createRegistry(2055);
					Naming.rebind("peerTransfer", new PeerTransfer());
					
					if(peerRegistry!=null && register.registerPeer("2055")) {
						LOGGER.info("Register service port [2055] successfully!");
					}else {
						textArea.append(SystemUtil.getSimpleTime()+"Unable to register service port [2055]!\n");
						LOGGER.error("Unable to register service port [2055]!");
						return;
					}
						
					textArea.append(SystemUtil.getSimpleTime() + "Connected to server [" + serverIP + ":" + serverPort
							+ "] successfully!\n");
					textField_serverIP.setEnabled(false);
					textField_serverPort.setEnabled(false);
					connected = true;
					btnConnect.setEnabled(false);
					
					//peer
					peer.setPeer_service_port("2055");
					peer.setServer_ip(serverIP);
					peer.setServer_port(serverPort);
					
					//btnConnect.setText("disconnect");
					
					// register.register("1111", "haha.txt");

				} catch (ConnectException e1) {
					JOptionPane.showMessageDialog(frame, "unable to connect to server!\nplease make sure the address is correct",
							"ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				} catch (MalformedURLException e1) {
					JOptionPane.showMessageDialog(frame, "unable to connect to server!\nplease make sure the address is correct",
							"ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				} catch (RemoteException e1) {
					JOptionPane.showMessageDialog(frame, "unknown server error! \nplease try again later", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					return;
				} catch (NotBoundException e1) {
					JOptionPane.showMessageDialog(frame, "unable to connect to server!\nplease make sure the address is correct",
							"ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		btnConnect.setBounds(326, 262, 117, 29);
		panel.add(btnConnect);

		textField_serverPort = new JTextField();
		textField_serverPort.setText(default_port);
		textField_serverPort.setBounds(245, 261, 66, 28);
		panel.add(textField_serverPort);
		textField_serverPort.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Server IP");
		lblNewLabel.setBounds(19, 267, 61, 16);
		panel.add(lblNewLabel);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(213, 267, 61, 16);
		panel.add(lblPort);
		
		JButton btnDownloadFiles = new JButton("Download Files");
		btnDownloadFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = textField_downloadFileName.getText();
				if("".equals(fileName)) {
					JOptionPane.showMessageDialog(frame, "The file name is not valid!", "ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				
				
			}
		});
		btnDownloadFiles.setBounds(19, 339, 122, 26);
		panel.add(btnDownloadFiles);
		
		textField_downloadFileName = new JTextField();
		textField_downloadFileName.setColumns(10);
		textField_downloadFileName.setBounds(152, 337, 122, 28);
		panel.add(textField_downloadFileName);
	}
	public JTextArea getTextArea() {
		return textArea;
	}

	public JOptionPane getOptionPane() {
		return optionPane;
	}
	public JFileChooser getFileChooser() {
		return fileChooser;
	}

	public static ClientWindow getInstance() {
		if (instance == null) {
			instance = new ClientWindow();
		}
		return instance;
	}

	public Registry getPeerRegistry() {
		return peerRegistry;
	}

	
}
