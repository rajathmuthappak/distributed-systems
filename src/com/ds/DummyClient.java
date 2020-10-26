package com.ds;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class DummyClient {

	private JFrame frame;
	private JTextField createDirTextField;
	private JTextField deleteDirTextField;
	private JTextField renameDirToTextField;
	private JTextField renameDirFromTextField;
	private JTextField moveDirFromTextField;
	private JTextField moveDirToTextField;
	private TextArea directoryListViewTextArea;
	private JLabel clientLabel;
	private String clientName;
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	private Socket socket;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DummyClient window = new DummyClient();
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
	public DummyClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 895, 605);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		clientLabel = new JLabel("Client : " + clientName);
		clientLabel.setForeground(Color.BLACK);
		clientLabel.setBackground(Color.LIGHT_GRAY);
		clientLabel.setHorizontalAlignment(SwingConstants.CENTER);
		clientLabel.setBounds(10, 11, 505, 47);
		frame.getContentPane().add(clientLabel);

		// 1 . CREATE DIRECTORY CODE
		createDirTextField = new JTextField();
		createDirTextField.setBounds(29, 92, 305, 26);
		createDirTextField.setText("Enter the Directory name to be created");
		createDirTextField.setForeground(Color.GRAY);
		// https://stackoverflow.com/questions/16213836/java-swing-jtextfield-set-placeholder
		// TO add placeholder for the text fields
		createDirTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (createDirTextField.getText().isEmpty()) {
					createDirTextField.setText("Enter the Directory name to be created");
					createDirTextField.setForeground(Color.GRAY);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (createDirTextField.getText().equals("Enter the Directory name to be created")) {
					createDirTextField.setText("");
					createDirTextField.setForeground(Color.BLACK);

				}
			}
		});
		frame.getContentPane().add(createDirTextField);
		createDirTextField.setColumns(10);

		JButton createDirBtn = new JButton("CREATE DIRECTORY");
		createDirBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Create directory instructions sent to server on click of create button
				try {
					if (true) {
						JOptionPane.showMessageDialog(null, "Invalid Folder Name or Directory structure");
					} else {
						dataOutputStream.writeUTF("CREATE_DIRECTORY " + createDirTextField.getText().toString().trim());
						createDirTextField.setText("");
						JOptionPane.showMessageDialog(null, dataInputStream.readUTF());
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		createDirBtn.setBounds(369, 94, 146, 23);
		frame.getContentPane().add(createDirBtn);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 147, 505, 2);
		frame.getContentPane().add(separator);

		// 2. DELETE DIRECTORY CODE
		deleteDirTextField = new JTextField();
		deleteDirTextField.setColumns(10);
		deleteDirTextField.setBounds(29, 172, 305, 26);
		deleteDirTextField.setText("Enter the Directory name/path to be deleted");
		deleteDirTextField.setForeground(Color.GRAY);
		// https://stackoverflow.com/questions/16213836/java-swing-jtextfield-set-placeholder
		// TO add placeholder for the text fields
		deleteDirTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (deleteDirTextField.getText().isEmpty()) {
					deleteDirTextField.setText("Enter the Directory name/path to be deleted");
					deleteDirTextField.setForeground(Color.GRAY);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (deleteDirTextField.getText().equals("Enter the Directory name/path to be deleted")) {
					deleteDirTextField.setText("");
					deleteDirTextField.setForeground(Color.BLACK);

				}
			}
		});
		frame.getContentPane().add(deleteDirTextField);

		JButton deleteDirBtn = new JButton("DELETE DIRECTORY");
		deleteDirBtn.setBounds(369, 174, 146, 23);
		deleteDirBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Delete directory instructions sent to server on click of delete button
				try {
					if (true) {
						JOptionPane.showMessageDialog(null, "Invalid Folder Name or Directory structure");
					} else {
						dataOutputStream.writeUTF("DELETE_DIRECTORY " + deleteDirTextField.getText().toString().trim());
						deleteDirTextField.setText("");
						JOptionPane.showMessageDialog(null, dataInputStream.readUTF());
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(deleteDirBtn);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 227, 505, 2);
		frame.getContentPane().add(separator_1);

		// 3. RENAME DIRECRTORY CODE
		renameDirFromTextField = new JTextField();
		renameDirFromTextField.setColumns(10);
		renameDirFromTextField.setBounds(29, 240, 305, 26);
		renameDirFromTextField.setText("Enter the Directory name to rename");
		renameDirFromTextField.setForeground(Color.GRAY);
		// https://stackoverflow.com/questions/16213836/java-swing-jtextfield-set-placeholder
		// TO add placeholder for the text fields
		renameDirFromTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (renameDirFromTextField.getText().isEmpty()) {
					renameDirFromTextField.setText("Enter the Directory name to rename");
					renameDirFromTextField.setForeground(Color.GRAY);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (renameDirFromTextField.getText().equals("Enter the Directory name to rename")) {
					renameDirFromTextField.setText("");
					renameDirFromTextField.setForeground(Color.BLACK);

				}
			}
		});
		frame.getContentPane().add(renameDirFromTextField);

		renameDirToTextField = new JTextField();
		renameDirToTextField.setColumns(10);
		renameDirToTextField.setBounds(29, 295, 305, 26);
		renameDirToTextField.setText("Enter the new Directory name.");
		renameDirToTextField.setForeground(Color.GRAY);
		// https://stackoverflow.com/questions/16213836/java-swing-jtextfield-set-placeholder
		// TO add placeholder for the text fields
		renameDirToTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (renameDirToTextField.getText().isEmpty()) {
					renameDirToTextField.setText("Enter the new Directory name.");
					renameDirToTextField.setForeground(Color.GRAY);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (renameDirToTextField.getText().equals("Enter the new Directory name.")) {
					renameDirToTextField.setText("");
					renameDirToTextField.setForeground(Color.BLACK);

				}
			}
		});
		frame.getContentPane().add(renameDirToTextField);

		JButton renameDirBtn = new JButton("RENAME DIRECTORY");
		renameDirBtn.setBounds(369, 279, 146, 23);
		renameDirBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Rename directory instructions sent to server on click of Rename button
				try {
					if (true) {
						JOptionPane.showMessageDialog(null, "Invalid Folder Name or Directory structure");
					} else {
						dataOutputStream
								.writeUTF("RENAME_DIRECTORY " + renameDirFromTextField.getText().toString().trim() + ":"
										+ renameDirToTextField.getText().toString().trim());
						JOptionPane.showMessageDialog(null, dataInputStream.readUTF());

					}
					renameDirFromTextField.setText("");
					renameDirToTextField.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(renameDirBtn);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, 332, 505, 2);
		frame.getContentPane().add(separator_2);

		// 4 . MOVE DIRECTORY CODE
		moveDirFromTextField = new JTextField();
		moveDirFromTextField.setColumns(10);
		moveDirFromTextField.setBounds(29, 346, 305, 26);
		moveDirFromTextField.setText("Enter the Directory name / path to be moved.");
		moveDirFromTextField.setForeground(Color.GRAY);
		// https://stackoverflow.com/questions/16213836/java-swing-jtextfield-set-placeholder
		// TO add placeholder for the text fields
		moveDirFromTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (moveDirFromTextField.getText().isEmpty()) {
					moveDirFromTextField.setText("Enter the Directory name / path to be moved.");
					moveDirFromTextField.setForeground(Color.GRAY);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (moveDirFromTextField.getText().equals("Enter the Directory name / path to be moved.")) {
					moveDirFromTextField.setText("");
					moveDirFromTextField.setForeground(Color.BLACK);

				}
			}
		});
		frame.getContentPane().add(moveDirFromTextField);

		moveDirToTextField = new JTextField();
		moveDirToTextField.setColumns(10);
		moveDirToTextField.setBounds(29, 401, 305, 26);
		moveDirToTextField.setText("Enter the new Directory path.");
		moveDirToTextField.setForeground(Color.GRAY);
		// https://stackoverflow.com/questions/16213836/java-swing-jtextfield-set-placeholder
		// TO add placeholder for the text fields
		moveDirToTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (moveDirToTextField.getText().isEmpty()) {
					moveDirToTextField.setText("Enter the new Directory path.");
					moveDirToTextField.setForeground(Color.GRAY);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (moveDirToTextField.getText().equals("Enter the new Directory path.")) {
					moveDirToTextField.setText("");
					moveDirToTextField.setForeground(Color.BLACK);

				}
			}
		});
		frame.getContentPane().add(moveDirToTextField);

		JButton moveDirBtn = new JButton("MOVE DIRECTORY");
		moveDirBtn.setBounds(369, 385, 146, 23);
		moveDirBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Move directory instructions sent to server on click of Move button
				try {
					if (true) {
						JOptionPane.showMessageDialog(null, "Invalid Folder Name or Directory structure");
					} else {
						dataOutputStream.writeUTF("MOVE_DIRECTORY " + moveDirFromTextField.getText().toString().trim()
								+ ":" + moveDirToTextField.getText().toString().trim());
						JOptionPane.showMessageDialog(null, dataInputStream.readUTF());
					}
					moveDirFromTextField.setText("");
					moveDirToTextField.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(moveDirBtn);

		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(10, 438, 505, 2);
		frame.getContentPane().add(separator_3);

		// BROWSE USER DIRECTORY CODE
		JButton syncServerFilesBtn = new JButton("SYNC SERVER FILES");
		syncServerFilesBtn.setBounds(52, 519, 129, 36);
		syncServerFilesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked");

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
//						new SyncServerDirectory(clientName ,"");
					}
				});
			}
		});
		frame.getContentPane().add(syncServerFilesBtn);

		// BROWSE USER DIRECTORY CODE
				JButton browseBtn = new JButton("BROWSE ");
				browseBtn.setBounds(245, 519, 123, 36);
				browseBtn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// Browse directory instructions sent to server on click of Browse button
						try {
							dataOutputStream.writeUTF("BROWSE_DIRECTORY ");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						JFileChooser jFileChooser = new JFileChooser(Constants.ROOT + clientName);
						jFileChooser.getFileView();
						disableUpFolderButton(jFileChooser);
						jFileChooser.showSaveDialog(null);

					}

					// https://www.coderanch.com/t/468663/java/Disabling-Enabling-level-button-folder
					// Method to disable buttons in the jFileChooser explorer window
					public void disableUpFolderButton(Container c) {
						int len = c.getComponentCount();
						for (int i = 0; i < len; i++) {
							Component comp = c.getComponent(i);
							if (comp instanceof JButton) {
								JButton b = (JButton) comp;
								Icon icon = b.getIcon();
								if (icon != null && (icon == UIManager.getIcon("FileChooser.detailsViewIcon")
										|| icon == UIManager.getIcon("FileChooser.homeFolderIcon")
										|| icon == UIManager.getIcon("FileChooser.listViewIcon")
										|| icon == UIManager.getIcon("FileChooser.newFolderIcon")
										|| icon == UIManager.getIcon("FileChooser.upFolderIcon"))) {
									b.setEnabled(false);
								}
							} else if (comp instanceof Container) {
								disableUpFolderButton((Container) comp);
							}
						}

					}
				});
				frame.getContentPane().add(browseBtn);
				
		// DISCONNECT FROM SERVER CODE
		JButton disconnectBtn = new JButton("DISCONNECT");
		disconnectBtn.setBounds(392, 515, 123, 36);
		disconnectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Disconnect instructions sent to server on click of disconnect button
				try {
					dataOutputStream.writeUTF("DISCONNECT ");
					frame.dispose();
					System.exit(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(disconnectBtn);

		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(10, 69, 505, 2);
		frame.getContentPane().add(separator_4);

		directoryListViewTextArea = new TextArea();
		directoryListViewTextArea.setBackground(Color.WHITE);
		directoryListViewTextArea.setBounds(566, 99, 303, 443);
		directoryListViewTextArea.setEditable(false);
		frame.getContentPane().add(directoryListViewTextArea);

		JLabel directoryListViewLabel = new JLabel("Directory View");
		directoryListViewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		directoryListViewLabel.setBounds(587, 68, 103, 14);
		frame.getContentPane().add(directoryListViewLabel);

		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		refreshButton.setBounds(751, 69, 89, 23);
		frame.getContentPane().add(refreshButton);
		
		textField = new JTextField();
		textField.setText("Enter the Directory name to be created");
		textField.setForeground(Color.GRAY);
		textField.setColumns(10);
		textField.setBounds(29, 451, 305, 26);
		frame.getContentPane().add(textField);
		
		JButton button = new JButton("CREATE DIRECTORY");
		button.setBounds(369, 449, 146, 23);
		frame.getContentPane().add(button);
		
		JSeparator separator_5 = new JSeparator();
		separator_5.setBounds(10, 502, 505, 2);
		frame.getContentPane().add(separator_5);
	}
}
