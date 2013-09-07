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
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
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
import com.rmi.api.impl.Register;

import java.awt.Button;

import javax.swing.JTextField;

public class ClientWindow {

	private JFrame frame;
	private JTextArea textArea;
	private static ClientWindow instance;
	/**
	 * @wbp.nonvisual location=43,629
	 */
	private final JOptionPane optionPane = new JOptionPane();
	/**
	 * @wbp.nonvisual location=114,629
	 */
	private final JFileChooser fileChooser = new JFileChooser();

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
		
		try {
			IRegister register = (IRegister)Naming.lookup("rmi://192.168.1.61:1099/register");
			register.register("1111", "haha.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 


	}
	
	

	/**
	 * Create the application.
	 */
	 private ClientWindow() {
		initialize();
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

		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// String aa = JOptionPane.showInputDialog(frame, "flsdjf");
				// System.out.println(aa);

				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
					// this.downloadPath =
					// fc.getSelectedFile().getAbsolutePath();
					// this.pathLabel.setText("默认路径：" + this.downloadPath);
					String path = fileChooser.getSelectedFile().getAbsolutePath();
					System.out.println(path);
				}

			}
		});
		btnNewButton.setBounds(579, 318, 98, 26);
		panel.add(btnNewButton);
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
		if(instance == null) {
			instance = new ClientWindow();
		}
		return instance;
	}
}
