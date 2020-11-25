/**
 * Name : Rajath Muthappa Kallichanda
 * Student ID : 1001724662
 */

package com.ds;

import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.io.FileUtils;

public class ServerWindow {

	private JFrame frame;
	private JTextField createDirTextField;
	private JTextField deleteDirTextField;
	private JTextField renameDirToTextField;
	private JTextField renameDirFromTextField;
	private JTextField moveDirFromTextField;
	private JTextField moveDirToTextField;
	private TextArea directoryListViewTextArea;
	private JLabel clientLabel;
	private String server;
	private DataOutputStream dataOutputStream;
	private JTextField undoCreateTextField;

	/**
	 * Create the application.
	 */
	public ServerWindow(String server) {
		this.server = server;
		try {
			// invoke the server window to modify server directory
			initialize();
			frame.setVisible(true);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 893, 578);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		clientLabel = new JLabel("SERVER");
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
						Server.serverLogsTextArea.append(
								"SERVER : CREATE_DIRECTORY ->" + createDirTextField.getText().toString().trim() + "\n");
						createDirectory(createDirTextField.getText().toString().trim());
						createDirTextField.setText("");
					}
					updateDirectoryView(Constants.ROOT + server);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		createDirBtn.setBounds(369, 94, 146, 23);
		frame.getContentPane().add(createDirBtn);

		JSeparator separator = new JSeparator();
		separator.setBounds(20, 129, 505, 2);
		frame.getContentPane().add(separator);

		// 2. DELETE DIRECTORY CODE
		deleteDirTextField = new JTextField();
		deleteDirTextField.setColumns(10);
		deleteDirTextField.setBounds(29, 142, 305, 26);
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
		deleteDirBtn.setBounds(369, 144, 146, 23);
		deleteDirBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Delete directory instructions sent to server on click of delete button
				try {
					if (!isValidFolderOrPath(deleteDirTextField.getText().toString().trim())
							|| deleteDirTextField.getText().toString().trim().length() == 0) {
						JOptionPane.showMessageDialog(null, "Invalid Folder Name or Directory structure");
					} else {
						Server.serverLogsTextArea.append(
								"SERVER : DELETE_DIRECTORY ->" + deleteDirTextField.getText().toString().trim() + "\n");
						deleteDirectory(deleteDirTextField.getText().toString().trim());
						deleteDirTextField.setText("");
					}
					updateDirectoryView(Constants.ROOT + server);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(deleteDirBtn);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(20, 179, 505, 2);
		frame.getContentPane().add(separator_1);

		// 3. RENAME DIRECRTORY CODE
		renameDirFromTextField = new JTextField();
		renameDirFromTextField.setColumns(10);
		renameDirFromTextField.setBounds(29, 192, 305, 26);
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
		renameDirToTextField.setBounds(29, 229, 305, 26);
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
		renameDirBtn.setBounds(369, 211, 146, 23);
		renameDirBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Rename directory instructions sent to server on click of Rename button
				try {
					if (!isValidFolderOrPath(renameDirFromTextField.getText().toString().trim())
							&& !isValidFolderOrPath(renameDirToTextField.getText().toString().trim())) {
						JOptionPane.showMessageDialog(null, "Invalid Folder Name or Directory structure");
					} else {
						Server.serverLogsTextArea.append(
								"SERVER : RENAME_DIRECTORY " + renameDirFromTextField.getText().toString().trim() + ":"
										+ renameDirToTextField.getText().toString().trim() + "\n");
						renameDirectory(renameDirFromTextField.getText().toString().trim(),
								renameDirToTextField.getText().toString().trim());
					}
					renameDirFromTextField.setText("");
					renameDirToTextField.setText("");
					updateDirectoryView(Constants.ROOT + server);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(renameDirBtn);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(20, 266, 505, 2);
		frame.getContentPane().add(separator_2);

		// 4 . MOVE DIRECTORY CODE
		moveDirFromTextField = new JTextField();
		moveDirFromTextField.setColumns(10);
		moveDirFromTextField.setBounds(29, 279, 305, 26);
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
		moveDirToTextField.setBounds(29, 316, 305, 26);
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
		moveDirBtn.setBounds(369, 299, 146, 23);
		moveDirBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Move directory instructions sent to server on click of Move button
				try {
					if (!isValidFolderOrPath(moveDirFromTextField.getText().toString().trim())
							&& !isValidFolderOrPath(moveDirToTextField.getText().toString().trim())) {
						JOptionPane.showMessageDialog(null, "Invalid Folder Name or Directory structure");
					} else {
						Server.serverLogsTextArea
								.append("SERVER : MOVE_DIRECTORY ->" + moveDirFromTextField.getText().toString().trim()
										+ ":" + moveDirToTextField.getText().toString().trim() + "\n");
						moveDirectory(moveDirFromTextField.getText().toString().trim(),
								moveDirToTextField.getText().toString().trim());
					}
					moveDirFromTextField.setText("");
					moveDirToTextField.setText("");
					updateDirectoryView(Constants.ROOT + server);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(moveDirBtn);

		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(20, 353, 505, 2);
		frame.getContentPane().add(separator_3);

		// DISCONNECT FROM SERVER CODE
		JButton closeBtn = new JButton("CLOSE");
		closeBtn.setBounds(288, 478, 146, 26);
		closeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		frame.getContentPane().add(closeBtn);

		JSeparator separator_4 = new JSeparator();
		separator_4.setBounds(20, 79, 505, 2);
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
				updateDirectoryView(Constants.ROOT + server);
			}
		});
		refreshButton.setBounds(751, 69, 89, 23);
		frame.getContentPane().add(refreshButton);

		undoCreateTextField = new JTextField();
		undoCreateTextField.setForeground(Color.GRAY);
		undoCreateTextField.setColumns(10);
		undoCreateTextField.setText("Enter Logged operation to Undo");
		undoCreateTextField.setBounds(29, 366, 305, 26);
		undoCreateTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (undoCreateTextField.getText().isEmpty()) {
					undoCreateTextField.setText("Enter Logged operation to Undo");
					undoCreateTextField.setForeground(Color.GRAY);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				if (undoCreateTextField.getText().equals("Enter Logged operation to Undo")) {
					undoCreateTextField.setText("");
					undoCreateTextField.setForeground(Color.BLACK);

				}
			}
		});
		frame.getContentPane().add(undoCreateTextField);

		JButton undoCreateBtn = new JButton("UNDO");
		undoCreateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undoOPeration(undoCreateTextField.getText());
			}
		});
		undoCreateBtn.setBounds(369, 368, 146, 23);
		frame.getContentPane().add(undoCreateBtn);

		JSeparator separator_5 = new JSeparator();
		separator_5.setBounds(20, 403, 505, 2);
		frame.getContentPane().add(separator_5);

		JSeparator separator_7 = new JSeparator();
		separator_7.setBounds(10, 638, 505, 2);
		frame.getContentPane().add(separator_7);
		updateDirectoryView(Constants.ROOT + server);
	}

	/**
	 * Used to dynamically update the Directory view for the server when ever there
	 * is a change like create, delete, rename or move.
	 * 
	 * @param rootPath
	 */
	// https://www.geeksforgeeks.org/java-program-list-files-directory-nested-sub-directories-recursive-approach/
	private void updateDirectoryView(String rootPath) {
		directoryListViewTextArea.setText("");
		File rootDir = new File(rootPath);

		if (rootDir.exists() && rootDir.isDirectory()) {
			// array for files and sub-directories
			// of directory pointed by rootPath
			File arr[] = rootDir.listFiles();

			directoryListViewTextArea.append("**********************************************\n");
			directoryListViewTextArea.append("Files from server directory : \n");
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

	/**
	 * Method to create the directory specified by the server and also to sync the
	 * changes across all local directories synchronized by the clients
	 * 
	 * @param directory
	 * @throws IOException
	 */
	private void createDirectory(String directory) throws IOException {
		try {
			String pathName = Constants.ROOT + server + "/" + directory;
			if (new File(pathName).exists()) {
				Server.serverLogsTextArea.append(server + " : Directory Creation Failed \n");
			} else {
				new File(Constants.ROOT + server + "/" + directory).mkdirs();
				Server.serverLogsTextArea.append(server + " : Directory Creation Successful -> " + pathName + "\n");
				// Code to Sync the LD across all clients
				for (String path : Server.clientDirectories.get(directory.substring(0, directory.indexOf("/")))) {
					new File(path + directory.substring(directory.indexOf("/"), directory.length())).mkdirs();
				}
				Server.serverLogsTextArea.append(server + " : Directory Sync Successful \n");
			}
		} catch (Exception e) {
			Server.serverLogsTextArea.append("ERROR : " + e.getMessage() + "\n");
			dataOutputStream.writeUTF(e.getMessage());
		}
	}

	/**
	 * Method to delete the directory as specified by the server and also to sync
	 * the changes across all local directories synchronized by the clients
	 * 
	 * @param directory
	 * @throws IOException
	 */

	private void deleteDirectory(String directory) throws IOException {
		try {
			String pathName = Constants.ROOT + server + "/" + directory;
			if (new File(pathName).exists()) {
				deleteDir(new File(Constants.ROOT + server + "/" + directory));
				Server.serverLogsTextArea.append(server + " : Deletion Successful -> " + pathName + "\n");
				// Code to Sync the LD across all clients
				for (String path : Server.clientDirectories.get(directory.substring(0, directory.indexOf("/")))) {
					deleteDir(new File(
							path + "\\" + directory.substring(directory.indexOf("/") + 1, directory.length())));
				}
				Server.serverLogsTextArea.append(server + " : Directory Sync Successful \n");
			} else {
				Server.serverLogsTextArea.append(server + " : Deletion Failed \n");
			}
		} catch (Exception e) {
			Server.serverLogsTextArea.append("ERROR : " + e.getMessage() + "\n");
			dataOutputStream.writeUTF(e.getMessage().toString());
		}

	}

	/**
	 * Method to delete any nested files and folders within the specified directory
	 * 
	 * @param file
	 */
	// https://stackoverflow.com/questions/20281835/how-to-delete-a-folder-with-files-using-java
	void deleteDir(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				if (!Files.isSymbolicLink(f.toPath())) {
					deleteDir(f);
				}
			}
		}
		file.delete();
	}

	/**
	 * Method to move the Folder from one directory to another as specified by the
	 * server and also to sync the changes across all local directories synchronized
	 * by the clients
	 * 
	 * @param oldPath
	 * @param newPath
	 * @throws IOException
	 */
	// https://www.studytrails.com/java-io/file-copying-and-moving-deleting/
	private void moveDirectory(String oldPath, String newPath) throws IOException {
		File oldFolder = new File(Constants.ROOT + server + "/" + oldPath);
		File newFolder = new File(Constants.ROOT + server + "/" + newPath);
		try {
			if (oldFolder.exists()) {
				FileUtils.moveDirectoryToDirectory(oldFolder, newFolder, true);
				Server.serverLogsTextArea
						.append(server + " : Move Successful -> from : " + oldPath + " to " + newPath + "\n");
				// call the below method to validate the file movement before synchronizing
				synchronizeDirectoryMove(oldPath, newPath);
			} else {
				Server.serverLogsTextArea.append(server + " : Move Failed. Directory does not exist \n");
			}
		} catch (Exception e) {
			Server.serverLogsTextArea.append("ERROR : " + e.getMessage() + "\n");
		}
	}

	/**
	 * Method to Synchronize server move operation across all client local
	 * directories
	 * 
	 * This method also checks if the movement of files is between different Home
	 * directories.
	 * 
	 * @param oldPath
	 * @param newPath
	 * @throws IOException
	 */
	private void synchronizeDirectoryMove(String oldPath, String newPath) throws IOException {
		String srcRootDirectory = oldPath.substring(0, oldPath.indexOf("/"));
		String destinationRootDirectory = newPath.substring(0, newPath.indexOf("/"));
		// check if the files are being moved within the same server directory
		if (srcRootDirectory.equals(destinationRootDirectory)) {
			for (String path : Server.clientDirectories.get(srcRootDirectory)) {
				File oldFolder = new File(
						path + "\\" + oldPath.substring(oldPath.indexOf("/") + 1, oldPath.length()).replace("/", "\\"));
				File newFolder = new File(
						path + "\\" + newPath.substring(newPath.indexOf("/") + 1, newPath.length()).replace("/", "\\"));
				FileUtils.moveDirectoryToDirectory(oldFolder, newFolder, true);
			}
		} else {
			for (String path : Server.clientDirectories.get(srcRootDirectory)) {
				File oldFolder = new File(
						path + "\\" + oldPath.substring(oldPath.indexOf("/") + 1, oldPath.length()).replace("/", "\\"));
				// Generate a new destination path since the folder is being moved to a
				// different destination directory
				String newDestPath = path.substring(0, path.lastIndexOf("\\")) + "\\copy_" + destinationRootDirectory;
				// Check to see if the new directory to which the file is being moved to
				// is already synchronized by the client. If the destination directory id
				// synchronized then go ahead with moving the files.
				if (new File(newDestPath).exists()) {
					File newFolder = new File(newDestPath + "\\"
							+ newPath.substring(newPath.indexOf("/") + 1, newPath.length()).replace("/", "\\"));
					FileUtils.moveDirectoryToDirectory(oldFolder, newFolder, true);
				} else {
					// If the Destination directory is not synchronized by the client then
					// delete the file being moved from the client's local directory to maintain
					// the folder structure of the synchronized directories.
					deleteDir(new File(path + "\\"
							+ oldPath.substring(oldPath.indexOf("/") + 1, oldPath.length()).replace("/", "\\")));
				}

			}

			for (String path : Server.clientDirectories.get(destinationRootDirectory)) {
				File serverFolder = new File(Constants.ROOT + "SERVER/" + destinationRootDirectory);
				File clientFolder = new File(path);
				FileUtils.copyDirectory(serverFolder, clientFolder);
			}
		}
		Server.serverLogsTextArea.append(server + " :  Directory Sync Successful \n");
	}

	/**
	 * Method to rename the directory to the name specified by the server and also
	 * to sync the changes across all local directories synchronized by the clients
	 * 
	 * @param oldFolderName
	 * @param newFolderName
	 * @throws IOException
	 */

	// https://stackoverflow.com/questions/48339967/how-to-rename-the-folder-in-java
	private void renameDirectory(String oldFolderName, String newFolderName) throws IOException {
		File oldFolder = new File(Constants.ROOT + server + "/" + oldFolderName);
		File newFolder = new File(Constants.ROOT + server + "/" + newFolderName);
		try {
			if (oldFolder.exists()) {
				oldFolder.renameTo(newFolder);
				Server.serverLogsTextArea.append(
						server + " : Rename Successful -> From " + oldFolderName + " to " + newFolderName + "\n");
				// Code to Sync the LD across all clients
				for (String path : Server.clientDirectories
						.get(oldFolderName.substring(0, oldFolderName.indexOf("/")))) {
					File oldLDFolder = new File(path + "\\" + oldFolderName
							.substring(oldFolderName.indexOf("/") + 1, oldFolderName.length()).replace("/", "\\"));
					File newLDFolder = new File(path + "\\" + newFolderName
							.substring(newFolderName.indexOf("/") + 1, newFolderName.length()).replace("/", "\\"));
					oldLDFolder.renameTo(newLDFolder);
				}
				Server.serverLogsTextArea.append(server + " : Directory Sync Successful \n");
			} else {
				Server.serverLogsTextArea.append(server + " : Rename Failed. Directory does not exist \n");
			}
		} catch (Exception e) {
			Server.serverLogsTextArea.append("ERROR : " + e.getMessage() + "\n");
			dataOutputStream.writeUTF(e.getMessage());
		}

	}

	/**
	 * This method is used for Undo operations Operations that could be performed
	 * are UNDO CREATE, UNDO RENAME and UNDO move. UNDO delete cannot be performed.
	 * This method accepts the log statement that the user wishes to undo.
	 * 
	 * @param text
	 */
	private void undoOPeration(String text) {
		// keep track of the existing log contents.
		String currentLog = Server.serverLogsTextArea.getText();
		String path = "";
		try {
			if (text.contains(Constants.CREATE_DIRECTORY)) {
				path = text.substring(text.indexOf(">") + 1, text.length()).trim();
				// calling delete method to UNDO create.
				deleteDirectory(path);
			} else if (text.contains(Constants.RENAME_DIRECTORY)) {
				String oldFolderName = text.substring(text.indexOf(":") + 1).trim();
				String newFolderName = text.substring(text.indexOf(" ") + 1, text.indexOf(":")).trim();
				renameDirectory(oldFolderName, newFolderName);
				path = text.substring(text.indexOf(" ") + 1, text.length()).trim();
			} else if (text.contains(Constants.MOVE_DIRECTORY)) {
				// MOVE_DIRECTORY ->home_Directory_1/new45:home_Directory_2/
				String firstName = text.substring(text.indexOf(">") + 1, text.indexOf(":")).trim();
				String secondName = text.substring(text.indexOf(":") + 1).trim();
				String oldFolderName = secondName.concat(firstName.substring(firstName.lastIndexOf("/") + 1));
				String newFolderName = firstName.substring(0, firstName.lastIndexOf("/") + 1);
				moveDirectory(oldFolderName, newFolderName);
				path = text.substring(text.indexOf(">") + 1, text.length()).trim();
			}
			// call clearLog to remove the logged contents from the server log file.
			clearLog(currentLog, text, path);
			updateDirectoryView(Constants.ROOT + server);
		} catch (IOException e) {
			Server.serverLogsTextArea.append("Error: " + e.getMessage());
		}
	}

	/**
	 * This method is used to clear the operations that were selected to UNDO. All
	 * the operation performed would be deleted from the server logs. Matches the
	 * log file with the UNDO operation mentioned by the user.
	 * 
	 * @param currentLog
	 * @param text
	 * @param path
	 */
	private void clearLog(String currentLog, String text, String path) {
		// TODO Auto-generated method stub
		String[] logArray = currentLog.split("\n");
		StringBuffer newLog = new StringBuffer();
		for (String str : logArray) {
			if (!((text.contains(Constants.CREATE_DIRECTORY)
					&& (str.contains(Constants.CREATE_DIRECTORY) || str.contains("Directory Creation Successful"))
					&& str.contains(path))
					|| (text.contains(Constants.RENAME_DIRECTORY)
							&& (str.contains(Constants.RENAME_DIRECTORY) || str.contains("Rename Successful -> From"))
							&& str.contains(path.substring(0, path.indexOf(":")))
							&& str.contains(path.substring(path.indexOf(":") + 1)))
					|| (text.contains(Constants.MOVE_DIRECTORY)
							&& (str.contains(Constants.MOVE_DIRECTORY) || str.contains("Move Successful -> from"))
							&& str.contains(path.substring(0, path.indexOf(":")))
							&& str.contains(path.substring(path.indexOf(":") + 1))))) {
				newLog.append(str + "\n");
			}
		}
		Server.serverLogsTextArea.setText(newLog.toString());
	}
}
