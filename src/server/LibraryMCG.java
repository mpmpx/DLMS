package server;

public class LibraryMCG {
	public static void main(String[] args) {
		new Thread(new Server(Server.MCG_PORT)).start();
	}
}
