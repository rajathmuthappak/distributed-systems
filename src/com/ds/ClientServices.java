/**
 * Name : Rajath Muthappa Kallichanda
 * Student ID : 1001724662
 */

package com.ds;

import java.awt.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

//https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/
public class ClientServices extends Thread {

	final DataInputStream dataInputStream;
	final DataOutputStream dataOutputStream;
	final Socket socket;
	final String clientName;

	public ClientServices(DataInputStream dataInputStream, DataOutputStream dataOutputStream, Socket socket,
			String clientName) {
		super();
		this.dataInputStream = dataInputStream;
		this.dataOutputStream = dataOutputStream;
		this.socket = socket;
		this.clientName = clientName;
	}

	@Override
	public void run() {

		String clientRequest;
		stopCurrentThread: while (true) {
			try {
				// accept the client instruction for further execution
				clientRequest = dataInputStream.readUTF();
				System.out.println("client request " + clientRequest);
				String command = clientRequest.substring(0, clientRequest.indexOf(" "));

				switch (command) {

				case "CREATE_DIRECTORY":
					String createDirectoryPath = clientRequest.substring(command.length(), clientRequest.length())
							.trim();
					Server.serverLogsTextArea
							.append(clientName + " : CREATE_DIRECTORY -> " + createDirectoryPath + "\n");
					createDirectory(createDirectoryPath);
					break;

				case "DELETE_DIRECTORY":
					String deleteDirectoryPath = clientRequest.substring(command.length(), clientRequest.length())
							.trim();
					Server.serverLogsTextArea
							.append(clientName + " : DELETE_DIRECTORY -> " + deleteDirectoryPath + "\n");
					deleteDirectory(deleteDirectoryPath);
					break;

				case "MOVE_DIRECTORY":
					String oldPath = clientRequest.substring(command.length(), clientRequest.indexOf(":")).trim();
					String newPath = clientRequest.substring(clientRequest.indexOf(":") + 1).trim();
					Server.serverLogsTextArea.append(clientName + " : MOVE_DIRECTORY " + oldPath + " -> " + newPath);
					moveDirectory(oldPath, newPath);
					break;

				case "RENAME_DIRECTORY":
					String oldFolder = clientRequest.substring(command.length(), clientRequest.indexOf(":")).trim();
					String newFolder = clientRequest.substring(clientRequest.indexOf(":") + 1).trim();
					Server.serverLogsTextArea
							.append(clientName + " : RENAME_DIRECTORY -> " + oldFolder + " -> " + newFolder + "\n");
					renameDirectory(oldFolder, newFolder);
					break;
				case "BROWSE_DIERCTORY":
					Server.serverLogsTextArea.append(clientName + " : BROWSE_DIRECTORY \n");
					break;
				case "SYNC_DIRECTORY":
					System.out.println(clientRequest.substring(command.length(), clientRequest.length()).trim());
					ArrayList<String> listOfLocalDirectories = new ArrayList<String>(Arrays
							.asList(clientRequest.substring(command.length(), clientRequest.length()).split("/")));
					Server.clientDirectories.put(clientName, listOfLocalDirectories);
					syncDirectory(listOfLocalDirectories);
					Server.serverLogsTextArea.append(clientName + " : BROWSE_DIRECTORY \n");
					break;
					
				case "DESYNC_DIRECTORY":
					System.out.println(clientRequest.substring(command.length(), clientRequest.length()).trim());
					deSyncDirectory();
					Server.serverLogsTextArea.append(clientName + " : BROWSE_DIRECTORY \n");
					break;

				case "DISCONNECT":
					Server.serverLogsTextArea.append(clientName + " : DISCONNECTED \n");
					Iterator<String> iterator = Server.listOfClients.iterator();
					while (iterator.hasNext()) {
						if (iterator.next().equalsIgnoreCase(clientName)) {
							iterator.remove();
						}
					}
					Server.updateClientList();
					this.dataInputStream.close();
					this.dataOutputStream.close();
					this.socket.close();
					System.out.println("DISCONNECT");
					break stopCurrentThread;
				}

			} catch (IOException e) {
				Server.serverLogsTextArea.append("ERROR : " + e.getMessage() + "\n");
				e.printStackTrace();
			}
		}

	}

	private void deSyncDirectory() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Method to rename the directory to the name specified by the client
	 * 
	 * @param oldFolderName
	 * @param newFolderName
	 * @throws IOException
	 */

	// https://stackoverflow.com/questions/48339967/how-to-rename-the-folder-in-java
	private void renameDirectory(String oldFolderName, String newFolderName) throws IOException {
		File oldFolder = new File(Constants.ROOT + clientName + "/" + oldFolderName);
		File newFolder = new File(Constants.ROOT + clientName + "/" + newFolderName);
		try {
			if (oldFolder.exists()) {
				oldFolder.renameTo(newFolder);
				Server.serverLogsTextArea.append(clientName + " : Rename Successful \n");
				dataOutputStream.writeUTF("Rename Successful");
			} else {
				Server.serverLogsTextArea.append(clientName + " : Rename Failed. Directory does not exist \n");
				dataOutputStream.writeUTF("Rename Failed. Directory does not exist");
			}
		} catch (Exception e) {
			Server.serverLogsTextArea.append("ERROR : " + e.getMessage() + "\n");
			dataOutputStream.writeUTF(e.getMessage());
		}

	}

	/**
	 * Method to move the Folder from one directory to another as specified by the
	 * client
	 * 
	 * @param oldPath
	 * @param newPath
	 * @throws IOException
	 */
	// https://www.studytrails.com/java-io/file-copying-and-moving-deleting/
	private void moveDirectory(String oldPath, String newPath) throws IOException {
		Server.serverLogsTextArea.append(clientName + " : MOVE_DIRECTORY " + Constants.ROOT + clientName + "/" + oldPath
				+ " -> " + Constants.ROOT + clientName + "/" + newPath + "/ \n");
		File oldFolder = new File(Constants.ROOT + clientName + "/" + oldPath);
		File newFolder = new File(Constants.ROOT + clientName + "/" + newPath);
		try {
			if (oldFolder.exists()) {
				FileUtils.moveDirectoryToDirectory(oldFolder, newFolder, true);
				Server.serverLogsTextArea.append(clientName + " : Move Successful \n");
				dataOutputStream.writeUTF("Move Successful");
			} else {
				Server.serverLogsTextArea.append(clientName + " : Move Failed. Directory does not exist \n");
				dataOutputStream.writeUTF("Move Failed. Directory does not exist");
			}
		} catch (Exception e) {
			Server.serverLogsTextArea.append("ERROR : " + e.getMessage() + "\n");
			dataOutputStream.writeUTF(e.getMessage());
		}
	}

	/**
	 * Method to copy the Directory Folder from server to clients local directory
	 * client
	 * 
	 * @param oldPath
	 * @param newPath
	 * @throws IOException
	 */
	private void syncDirectory(ArrayList<String> listOfLocalDirectories) throws IOException {
		
//		for(String directoryName : listOfLocalDirectories) {
//			
//		}
//		File serverFolder = new File(Constants.ROOT + directoryName + "/");
//		File clientFolder = new File(Constants.ROOT + clientName + "/copy_" + directoryName);
//		try {
//			if (serverFolder.exists()) {
//				FileUtils.copyDirectory(serverFolder, clientFolder, true);
//				Server.serverLogsTextArea.append(clientName + " : " + directoryName + "   Copy Successful \n");
//				dataOutputStream.writeUTF("Move Successful");
//			} else {
//				Server.serverLogsTextArea.append(clientName + " : Move Failed. Directory does not exist \n");
//				dataOutputStream.writeUTF("Move Failed. Directory does not exist");
//			}
//		} catch (Exception e) {
//			Server.serverLogsTextArea.append("ERROR : " + e.getMessage() + "\n");
//			dataOutputStream.writeUTF(e.getMessage());
//		}
	}

	/**
	 * Method to delete the directory as specified by the client
	 * 
	 * @param directory
	 * @throws IOException
	 */

	private void deleteDirectory(String directory) throws IOException {
		try {
			if (new File(Constants.ROOT + clientName + "/" + directory).exists()) {
				deleteDir(new File(Constants.ROOT + clientName + "/" + directory));
				dataOutputStream.writeUTF("Directory DELETED..!");
				Server.serverLogsTextArea.append(clientName + " : Deletion Successful \n");
			} else {
				dataOutputStream.writeUTF("Folder Does not exist");
				Server.serverLogsTextArea.append(clientName + " : Deletion Failed \n");
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
	 * Method to create the directory specified by the client
	 * 
	 * @param directory
	 * @throws IOException
	 */
	private void createDirectory(String directory) throws IOException {
		try {
			if (new File(Constants.ROOT + clientName + "/" + directory).exists()) {
				dataOutputStream.writeUTF("Folder already exists");
				Server.serverLogsTextArea.append(clientName + " : Directory Creation Failed \n");
			} else {
				new File(Constants.ROOT + clientName + "/" + directory).mkdirs();
				dataOutputStream.writeUTF("Folder created");
				Server.serverLogsTextArea.append(clientName + " : Directory Creation Successful \n");
			}
		} catch (Exception e) {
			Server.serverLogsTextArea.append("ERROR : " + e.getMessage() + "\n");
			dataOutputStream.writeUTF(e.getMessage());
		}

	}

}
