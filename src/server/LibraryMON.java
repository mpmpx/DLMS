package server;

public class LibraryMON {
	public static void main(String[] args) {
		new Thread(new Server(Server.MON_PORT)).start();
	}
}
