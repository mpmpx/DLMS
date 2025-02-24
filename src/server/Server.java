package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ImplementRemoteInterface.Library;
import resource.ID;
import resource.LibraryName;
import resource.Log;

public class Server implements Runnable {
	
	public static final int CON_PORT = 4000;
	public static final int MCG_PORT = 4010;
	public static final int MON_PORT = 4020;
	public static final int RMI_OFFSET = 1;
	

	public Log log;
	private int port;
	private Registry registry;
	protected Library library;
	protected LibraryName libraryName;


	public Server(int port) {
		this.port = port;
		switch (port) {
			case CON_PORT : 
				libraryName = LibraryName.CON; break;
			case MCG_PORT : 
				libraryName = LibraryName.MCG; break;
			case MON_PORT : 
				libraryName = LibraryName.MON; break;
		}
	}
	
	private void createLog() {
		String rootDir = (Paths.get("").toAbsolutePath().toString());
		String[] path = {rootDir, "src", "server", "logs", libraryName.toString() + "_server_log.txt"};
		log = new Log(path);
	}
	
	public void run() {
		try {
			createLog();
			library = new Library(this, libraryName);
			
			registry = LocateRegistry.createRegistry(port + RMI_OFFSET);
			registry.bind("Library", library);
			System.out.println("Server(" + libraryName + ") is Started, RMI port: " + (port + RMI_OFFSET) + ".");
			log.write("Server(" + libraryName + ") is Started.");
			
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Server(" + libraryName + ") is Started, TCP port: " + port + ".");
						
			while (true) {
				Socket clientSocket = serverSocket.accept();
				new Thread(new ClientHandler(this, clientSocket)).start();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			log.write("Error: " + e.getMessage());
			log.close();
		}
	}
	
	public static void main(String[] args) {
		new Thread(new Server(CON_PORT)).start();
		new Thread(new Server(MCG_PORT)).start();
		new Thread(new Server(MON_PORT)).start();
	}
}

class ClientHandler implements Runnable {
	
	private Server server;
	private Socket socket;
	
	public ClientHandler(Server server, Socket clientSocket) {
		this.server = server;
		socket = clientSocket;
	}
	
	@Override
	public void run() {
		try {
			BufferedReader inFromClient = 
						new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String clientMsg = inFromClient.readLine();
			String replyMsg = "";
			String[] splitMsg = clientMsg.split(";");
			DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
				
			switch (splitMsg[0]) {
				case "Borrow" :{
					replyMsg = server.library.borrowItem(new ID(splitMsg[1]), new ID(splitMsg[2]), Integer.parseInt(splitMsg[3]));
					outToClient.writeBytes(replyMsg + "\n");
					break;
				}
				case "Find" : {
					replyMsg = server.library.findItem(new ID(splitMsg[1]), splitMsg[2], false);
					outToClient.writeBytes(replyMsg + "\n");
					break;
				}
				case "Return" : {
					replyMsg = server.library.returnItem(new ID(splitMsg[1]), new ID(splitMsg[2]));
					outToClient.writeBytes(replyMsg + "\n");
					break;
				}
				case "AddList" : {
					replyMsg = server.library.addWaitList(new ID(splitMsg[1]), new ID(splitMsg[2]));
					outToClient.writeBytes(replyMsg + "\n");
					break;
				}
				default : break;
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
 	}
}
