/**
 * Name : Rajath Muthappa Kallichanda
 * Student ID : 1001724662
 */

package com.ds;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class ClientWindow {

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

	/**
	 * Create the application.
	 */
	public ClientWindow(String clientName) {
		this.clientName = clientName;
		try {
			// https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/
			socket = new Socket(Constants.SERVER_IP_ADDRESS, Constants.SERVER_PORT);
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataOutputStream.writeUTF(clientName);
			dataInputStream = new DataInputStream(socket.getInputStream());
			String message = dataInputStream.readUTF();
			if (message.contains("Client Already Connected..!!")) {
				JOptionPane.showMessageDialog(null, message);
				// https://stackoverflow.com/questions/17018857/how-to-call-jframe-from-another-java-class
				// Invoke the client connection window for client to reconnect to the server
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						new ClientConnection();
					}
				});
			} else {
				// invoke the client window for it to send and receive instructions from/to
				// server
				initialize();
				frame.setVisible(true);
				System.out.println("connected");

			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 895, 562);
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
					if (!isValidFolderOrPath(createDirTextField.getText().toString().trim())
							|| createDirTextField.getText().toString().trim().length() == 0) {
						JOptionPane.showMessageDialog(null, "Invalid Folder Name or Directory structure");
					} else {
						dataOutputStream.writeUTF("CREATE_DIRECTORY " + createDirTextField.getText().toString().trim());
						createDirTextField.setText("");
						JOptionPane.showMessageDialog(null, dataInputStream.readUTF());
					}
					updateDirectoryView(Constants.ROOT + clientName);
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
					if (!isValidFolderOrPath(deleteDirTextField.getText().toString().trim())
							|| deleteDirTextField.getText().toString().trim().length() == 0) {
						JOptionPane.showMessageDialog(null, "Invalid Folder Name or Directory structure");
					} else {
						dataOutputStream.writeUTF("DELETE_DIRECTORY " + deleteDirTextField.getText().toString().trim());
						deleteDirTextField.setText("");
						JOptionPane.showMessageDialog(null, dataInputStream.readUTF());
					}
					updateDirectoryView(Constants.ROOT + clientName);
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
					if (!isValidFolderOrPath(renameDirFromTextField.getText().toString().trim())
							&& !isValidFolderOrPath(renameDirToTextField.getText().toString().trim())) {
						JOptionPane.showMessageDialog(null, "Invalid Folder Name or Directory structure");
					} else {
						dataOutputStream
								.writeUTF("RENAME_DIRECTORY " + renameDirFromTextField.getText().toString().trim() + ":"
										+ renameDirToTextField.getText().toString().trim());
						JOptionPane.showMessageDialog(null, dataInputStream.readUTF());

					}
					renameDirFromTextField.setText("");
					renameDirToTextField.setText("");
					updateDirectoryView(Constants.ROOT + clientName);
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
					if (!isValidFolderOrPath(moveDirFromTextField.getText().toString().trim())
							&& !isValidFolderOrPath(moveDirToTextField.getText().toString().trim())) {
						JOptionPane.showMessageDialog(null, "Invalid Folder Name or Directory structure");
					} else {
						dataOutputStream.writeUTF("MOVE_DIRECTORY " + moveDirFromTextField.getText().toString().trim()
								+ ":" + moveDirToTextField.getText().toString().trim());
						JOptionPane.showMessageDialog(null, dataInputStream.readUTF());
					}
					moveDirFromTextField.setText("");
					moveDirToTextField.setText("");
					updateDirectoryView(Constants.ROOT + clientName);
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
		JButton browseBtn = new JButton("BROWSE ");
		browseBtn.setBounds(107, 468, 123, 36);
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
		disconnectBtn.setBounds(306, 468, 123, 36);
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
		directoryListViewTextArea.setBounds(566, 99, 303, 405);
		directoryListViewTextArea.setEditable(false);
		frame.getContentPane().add(directoryListViewTextArea);

		JLabel directoryListViewLabel = new JLabel("Directory View");
		directoryListViewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		directoryListViewLabel.setBounds(587, 68, 103, 14);
		frame.getContentPane().add(directoryListViewLabel);

		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDirectoryView(Constants.ROOT + clientName);
			}
		});
		refreshButton.setBounds(751, 69, 89, 23);
		frame.getContentPane().add(refreshButton);
		updateDirectoryView(Constants.ROOT + clientName);
	}

	/**
	 * Used to dynamically update the Directory view for the client when ever there
	 * is a change like create, delete, rename or move.
	 * 
	 * @param rootPath
	 */
	// https://www.geeksforgeeks.org/java-program-list-files-directory-nested-sub-directories-recursive-approach/
	private void updateDirectoryView(String rootPath) {
		directoryListViewTextArea.setText("");
		// TODO Auto-generated method stub
		File rootDir = new File(rootPath);

		if (rootDir.exists() && rootDir.isDirectory()) {
			// array for files and sub-directories
			// of directory pointed by rootPath
			File arr[] = rootDir.listFiles();

			directoryListViewTextArea.append("**********************************************\n");
			directoryListViewTextArea.append("Files from main directory : " + rootDir + "\n");
			directoryListViewTextArea.append("**********************************************\n");

			// Calling recursive method
			RecursivePrint(arr, 0);
		}

	}

	/**
	 * Recursive method to list the folder structure.
	 * 
	 * @param arrayOfFiles
	 * @param level
	 */
	public void RecursivePrint(File[] arrayOfFiles, int level) {
		// for-each loop for main directory files
		for (File f : arrayOfFiles) {
			// tabs for internal levels
			for (int i = 0; i < level; i++)
				directoryListViewTextArea.append("-----+");

			if (f.isFile())
				directoryListViewTextArea.append(f.getName() + "\n");

			else if (f.isDirectory()) {
				directoryListViewTextArea.append("[" + f.getName() + "]\n");

				// recursion for sub-directories
				RecursivePrint(f.listFiles(), level + 1);
			}
		}
	}

	/**
	 * Method to validate the input directory path for special characters apart from
	 * the specified characters
	 * 
	 * @param folder
	 * @return
	 */
	private boolean isValidFolderOrPath(String folder) {
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9_/]*$");
		Matcher matcher = pattern.matcher(folder);
		if (!matcher.matches() || folder.length() == 0) {
			return false;
		}
		return true;
	}

}
