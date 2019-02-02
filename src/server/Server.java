package server;

import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ImplementRemoteInterface.Library;
import resource.LibraryName;
import resource.Log;

public class Server {
	
	public static final int CON_PORT = 4000;
	public static final int MCG_PORT = 4001;
	public static final int MON_PORT = 4002;

	public Log log;
	private int port;
	private Registry registry;
	private Library library;
	private LibraryName libraryName;


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
			log.write("Server(" + libraryName + ") is Started.");
			library = new Library(this, libraryName);
			
			registry = LocateRegistry.createRegistry(port);
			registry.bind("Library", library);
			System.out.println("Server(" + libraryName + ") is Started.");
		} 
		catch (Exception e) {
			e.printStackTrace();
			log.write(e.getMessage());
			log.close();
		}
	}
	
	public static void main(String[] args) {
		Server CONServer = new Server(CON_PORT);
		CONServer.run();
		Server MCGServer = new Server(MCG_PORT);
		MCGServer.run();
		Server MONServer = new Server(MON_PORT);
		MONServer.run();
	}
}
