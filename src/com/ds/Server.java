/**
 * Name : Rajath Muthappa Kallichanda
 * Student ID : 1001724662
 */

package com.ds;

import java.awt.EventQueue;
import java.awt.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Server {

	private JFrame frame;
	public static List<String> listOfClients = new ArrayList<String>();

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
				System.out.println("clint name " + clientName);
				// check to see if the client is already connected
				if (listOfClients.contains(clientName)) {
					dataOutputStream.writeUTF(Constants.CLIENT_ALREADY_CONNECTED);
					socket.close();
				} else {
					// If the client disconnects from the server then close the socket and remove
					// the client name from the list of clients
					if (clientName.contains(Constants.DISCONNECT)) {
						System.out.println(clientName.substring(clientName.indexOf(" ")));
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
						System.out.printf(Constants.CLIENT_CONNECTED, clientName);
						serverLogsTextArea.append("Client -> "+clientName + " CONNECTED. \n");
						listOfClients.add(clientName);
						updateClientList();
						// Start thread for a particular once a connection is estalished.
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
		serverLabel.setHorizontalAlignment(SwingConstants.CENTER);
		serverLabel.setBounds(10, 11, 414, 39);
		frame.getContentPane().add(serverLabel);

		JLabel listOfClientsLabel = new JLabel(Constants.LIST_OF_CLIENTS_LABEL);
		listOfClientsLabel.setBounds(40, 49, 216, 20);
		frame.getContentPane().add(listOfClientsLabel);

		listOfClientsTextField = new TextArea();
		listOfClientsTextField.setBounds(44, 75, 207, 80);
		listOfClientsTextField.setEditable(false);
		frame.getContentPane().add(listOfClientsTextField);

		JLabel logsLabel = new JLabel("Server Logs");
		logsLabel.setBounds(41, 173, 138, 27);
		frame.getContentPane().add(logsLabel);

		serverLogsTextArea.setBounds(40, 206, 503, 451);
		frame.getContentPane().add(serverLogsTextArea);

		// Terminate Server
		JButton serverStopButton = new JButton("TERMINATE");
		serverStopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		serverStopButton.setForeground(Color.RED);
		serverStopButton.setBounds(434, 11, 109, 39);
		frame.getContentPane().add(serverStopButton);
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
