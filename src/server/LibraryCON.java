package server;

public class LibraryCON {
	public static void main(String[] args) {
		new Thread(new Server(Server.CON_PORT)).start();
	}
}
