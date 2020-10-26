/**
 * Name : Rajath Muthappa Kallichanda
 * Student ID : 1001724662
 */

package com.ds;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;

public class ClientConnection extends JFrame {

	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private static JTextField clientNameTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientConnection window = new ClientConnection();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientConnection() {
		initialize();
		this.frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel clientLabel = new JLabel(Constants.CLIENT_LABEL);
		clientLabel.setHorizontalAlignment(SwingConstants.CENTER);
		clientLabel.setBounds(10, 11, 414, 57);
		frame.getContentPane().add(clientLabel);

		JLabel clientNameLabel = new JLabel("Enter Client Name                :");
		clientNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		clientNameLabel.setBounds(20, 79, 156, 14);
		frame.getContentPane().add(clientNameLabel);

		clientNameTextField = new JTextField();
		clientNameTextField.setBounds(186, 79, 204, 20);
		frame.getContentPane().add(clientNameTextField);
		clientNameTextField.setColumns(10);

		JButton connectButton = new JButton(Constants.CONNECT_BTN);
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String clientName = clientNameTextField.getText().toString();
				if (!isValidClientName(clientName)) {
					JOptionPane.showMessageDialog(null, Constants.CLIENT_NAME_VALIDATION);
				} else {
					frame.dispose();
					// https://stackoverflow.com/questions/17018857/how-to-call-jframe-from-another-java-class
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							new ClientWindow(clientName);
						}
					});
				}
			}

			/**
			 * Method to validate the client name valid characters for client name are
			 * [a-zA-Z0-9_]
			 * 
			 * @param clientName
			 * @return
			 */
			private boolean isValidClientName(String clientName) {
				String clientNameFormat = "^[a-zA-Z0-9_]*$";
				Pattern pattern = Pattern.compile(clientNameFormat);
				Matcher matcher = pattern.matcher(clientName);
				if (matcher.matches()) {
					return true;
				}
				return false;
			}
		});
		connectButton.setBounds(124, 131, 89, 23);
		frame.getContentPane().add(connectButton);

		JButton clearButton = new JButton(Constants.CLEAR_BTN);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clientNameTextField.setText(null);
			}
		});
		clearButton.setBounds(223, 131, 89, 23);

		frame.getContentPane().add(clearButton);
	}
}
