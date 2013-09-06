package com.frame;

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

import com.db.HSQLDB;

import java.awt.Button;

public class Window {

	private JFrame frame;
	private JTextArea textArea;
	private static Window instance;
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
					Window window = Window.getInstance();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		HSQLDB.initDB();

		// try {
		// Class.forName("org.hsqldb.jdbcDriver");
		// Connection conn =
		// DriverManager.getConnection("jdbc:hsqldb:file:db/temp","sa","");
		// try {
		// Statement statement = conn.createStatement();
		// statement.executeUpdate("create table customer(id integer not null primary key,firstname varchar,lastname varchar)");
		// for (int i = 10; i < 20; i++) {
		// statement.executeUpdate("insert into customer values(" + i +
		// ",'liu','zhaoyang')");
		// }
		// statement.close();
		// statement = conn.createStatement();
		// ResultSet set = statement.executeQuery("select * from customer");
		// while(set.next()) {
		// System.out.println(set.getString(2));
		// }
		// statement.close();
		// } catch (SQLException ex) {
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}
	
	

	/**
	 * Create the application.
	 */
	 private Window() {
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
		btnNewButton.setBounds(41, 294, 98, 26);
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

	public static Window getInstance() {
		if(instance == null) {
			instance = new Window();
		}
		return instance;
	}
}
