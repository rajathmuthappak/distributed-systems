package com.ds;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class SyncServerDirectory {

	private JFrame frame;
	private static String clientName;
	private static DataOutputStream dataOutputStream;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					SyncServerDirectory window = new SyncServerDirectory("","");
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public SyncServerDirectory(String clientName, DataOutputStream dataOutputStream) {
		this.clientName = clientName;
		this.dataOutputStream = dataOutputStream;
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 324, 326);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JCheckBox homeDirectory1CheckBox = new JCheckBox("Home_Directory_1");
		homeDirectory1CheckBox.setBounds(88, 67, 144, 23);
		frame.getContentPane().add(homeDirectory1CheckBox);
		
		JCheckBox homeDirectory2CheckBox = new JCheckBox("Home_Directory_2");
		homeDirectory2CheckBox.setBounds(88, 93, 144, 23);
		frame.getContentPane().add(homeDirectory2CheckBox);
		
		JCheckBox homeDirectory3CheckBox = new JCheckBox("Home_Directory_3");
		homeDirectory3CheckBox.setBounds(88, 119, 144, 23);
		frame.getContentPane().add(homeDirectory3CheckBox);
		
		JButton createCopyBtn = new JButton("SYNC");
		createCopyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				StringBuffer stringBuffer = new StringBuffer("SYNC_DIRECTORY ");
				if(homeDirectory1CheckBox.isSelected()) {
					stringBuffer.append("/home_Directory_1");
				}
				if(homeDirectory2CheckBox.isSelected()) {
					stringBuffer.append("/home_Directory_2");
				}
				if(homeDirectory3CheckBox.isSelected()) {
					stringBuffer.append("/home_Directory_3");
				}
				try {
					dataOutputStream.writeUTF(stringBuffer.toString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		createCopyBtn.setBounds(32, 178, 89, 23);
		frame.getContentPane().add(createCopyBtn);
		
		JButton cancelBtn = new JButton("CANCEL");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		cancelBtn.setBounds(163, 178, 89, 23);
		frame.getContentPane().add(cancelBtn);
	}
}
