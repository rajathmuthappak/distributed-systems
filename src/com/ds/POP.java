package com.ds;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class POP {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					POP window = new POP();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public POP() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 275, 380);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JCheckBox hd1CheckBox = new JCheckBox("home_Directory_1");
		hd1CheckBox.setBounds(30, 88, 140, 23);
		frame.getContentPane().add(hd1CheckBox);
		
		JCheckBox hd2CheckBox = new JCheckBox("home_Directory_2");
		hd2CheckBox.setBounds(30, 141, 140, 23);
		frame.getContentPane().add(hd2CheckBox);
		
		JCheckBox hd3CheckBox = new JCheckBox("home_Directory_3");
		hd3CheckBox.setBounds(30, 189, 140, 23);
		frame.getContentPane().add(hd3CheckBox);
		
		JButton syncBtn = new JButton("SYNC");
		syncBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(hd1CheckBox.isSelected()) {
					System.out.println("hd1 selected");
				}
				if(hd2CheckBox.isSelected()) {
					System.out.println("hd2 selected");
				}
				if(hd3CheckBox.isSelected()) {
					System.out.println("hd3 selected");
				}
			}
		});
		syncBtn.setBounds(38, 244, 89, 23);
		frame.getContentPane().add(syncBtn);
		
		JButton cancelBtn = new JButton("CANCEL");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		cancelBtn.setBounds(139, 244, 89, 23);
		frame.getContentPane().add(cancelBtn);
		
		JLabel syncLabel = new JLabel("SYNC SERVER DIRECTORIES");
		syncLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		syncLabel.setHorizontalAlignment(SwingConstants.CENTER);
		syncLabel.setBounds(10, 27, 218, 41);
		frame.getContentPane().add(syncLabel);
	}
}
