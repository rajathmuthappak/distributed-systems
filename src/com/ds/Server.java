/**
 * Name : Rajath Muthappa Kallichanda
 * Student ID : 1001724662
 */

package com.ds;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Font;

public class Server {

	private JFrame frame;
	// To maintain a list of connected clients
	public static List<String> listOfClients = new ArrayList<String>();
	// To maintain a map of client names and ther identifiers.
	public static Map<String, String> clientIdentifiers = new HashMap<String, String>();

	// Map to maintain the list of synchronized paths for each server directory.
	public static Map<String, Set<String>> clientDirectories = new HashMap<String, Set<String>>();
	/**
	 * Initializing the map with a key values. i.e. the Directories available for
	 * synchronization at SERVER side
	 */
	static {
		clientDirectories.put("home_Directory_1", new HashSet<String>());
		clientDirectories.put("home_Directory_2", new HashSet<String>());
		clientDirectories.put("home_Directory_3", new HashSet<String>());
	};

	private static ServerSocket serverSocket;
	private static TextArea listOfClientsTextField;
	public static TextArea serverLogsTextArea = new TextArea();

	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server window = new Server();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		serverSocket = new ServerSocket(Constants.SERVER_PORT);
		serverLogsTextArea.append(Constants.SERVER_STARTED);
		while (true) {
			Socket socket = null;
			try {
				serverLogsTextArea.append(Constants.WAITING_FOR_CONNECTION);
				// https://www.geeksforgeeks.org/introducing-threads-socket-programming-java/
				socket = serverSocket.accept();
				DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
				DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
				String clientName = dataInputStream.readUTF();
				// check to see if the client is already connected
				if (listOfClients.contains(clientName)) {
					dataOutputStream.writeUTF(Constants.CLIENT_ALREADY_CONNECTED);
					socket.close();
				} else {
					// If the client disconnects from the server then close the socket and remove
					// the client name from the list of clients
					if (clientName.contains(Constants.DISCONNECT)) {
						Iterator<String> iterator = listOfClients.iterator();
						while (iterator.hasNext()) {
							if (iterator.next()
									.equalsIgnoreCase(clientName.substring(clientName.indexOf(" ")).trim())) {
								iterator.remove();
							}
						}
						updateClientList();
					} else {
						// Create a root folder for the new client if its not present.
						// Else create a new folder and send the confirmation message to the client
						if (!new File(Constants.ROOT + clientName).exists()) {
							new File(Constants.ROOT + clientName).mkdirs();
						}
						dataOutputStream.writeUTF(Constants.ROOT + clientName);
						serverLogsTextArea.append("Client -> " + clientName + " CONNECTED. \n");
						listOfClients.add(clientName);
						updateClientList();
						// Start thread for a particular once a connection is established.
						Thread clientThread = new ClientServices(dataInputStream, dataOutputStream, socket, clientName);
						clientThread.start();
					}
				}
			} catch (Exception e) {
				serverLogsTextArea.append("ERROR : " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Create the application.
	 */
	public Server() {
		initialize();
	}

	/**
	 * Initialize the contents of the Server frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 585, 706);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel serverLabel = new JLabel(Constants.SERVER_LABEL);
		serverLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		serverLabel.setHorizontalAlignment(SwingConstants.CENTER);
		serverLabel.setBounds(10, 11, 414, 39);
		frame.getContentPane().add(serverLabel);

		JLabel listOfClientsLabel = new JLabel(Constants.LIST_OF_CLIENTS_LABEL);
		listOfClientsLabel.setBounds(40, 49, 216, 20);
		frame.getContentPane().add(listOfClientsLabel);

		listOfClientsTextField = new TextArea();
		listOfClientsTextField.setBounds(44, 75, 207, 103);
		listOfClientsTextField.setEditable(false);
		frame.getContentPane().add(listOfClientsTextField);

		JLabel logsLabel = new JLabel("Server Logs");
		logsLabel.setBounds(40, 184, 138, 27);
		frame.getContentPane().add(logsLabel);

		serverLogsTextArea.setBounds(40, 217, 503, 440);
		frame.getContentPane().add(serverLogsTextArea);

		// Terminate Server
		JButton serverStopButton = new JButton("TERMINATE");
		serverStopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		serverStopButton.setForeground(Color.RED);
		serverStopButton.setBounds(410, 11, 133, 39);
		frame.getContentPane().add(serverStopButton);

		JButton directoryBtn = new JButton("EDIT DIRECTORY");
		directoryBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// frame.dispose();
				// https://stackoverflow.com/questions/17018857/how-to-call-jframe-from-another-java-class
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						new ServerWindow("SERVER");
					}
				});
			}
		});
		directoryBtn.setBounds(410, 61, 133, 39);
		frame.getContentPane().add(directoryBtn);
	}

	/**
	 * Method to update the list of clients when one connects or disconnects
	 */
	public static void updateClientList() {
		listOfClientsTextField.setText("");
		for (String client : listOfClients) {
			listOfClientsTextField.append(client);
			listOfClientsTextField.append("\n");
		}
	}
}
