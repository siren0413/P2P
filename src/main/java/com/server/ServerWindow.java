package com.server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import com.db.ServerDB.ServerHSQLDB;
import com.rmi.api.impl.Register;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.SystemColor;

public class ServerWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerWindow window = new ServerWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		ServerHSQLDB.initDB();
		//*************************************//
		
		
	}

	/**
	 * Create the application.
	 */
	public ServerWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.control);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		final JTextArea textArea = new JTextArea();
		textArea.setBackground(SystemColor.control);
		textArea.setEditable(false);
		textArea.setBounds(89, 180, 254, 44);
		frame.getContentPane().add(textArea);
		
		final JButton btnNewButton = new JButton("Start Index Server");
		btnNewButton.setEnabled(true);
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNewButton.setEnabled(false);
				
				// RMI registry
				try {
					LocateRegistry.createRegistry(1099);
					Register register = new Register();
					Naming.rebind("register",register);
					
					textArea.append("Index server is running...");
					
					Thread.sleep(10000);
					Connection conn = ServerHSQLDB.getConnection();
					Statement stmt = conn.createStatement();
					ResultSet result = stmt.executeQuery("select * from RegistryInfo");
					while(result.next()) {
						String id = result.getString(1);
						String ip = result.getString(2);
						String port = result.getString(3);
						String file = result.getString(4);
						System.out.println("id["+id+"] ip["+ip+"] port["+port+"] file["+file+"]");
					}
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				
				
			}
		});
		btnNewButton.setBounds(79, 55, 269, 113);
		frame.getContentPane().add(btnNewButton);
		
		
	}
}
