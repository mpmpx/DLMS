package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import ServerInterface.LibraryInterface;
import resource.Book;
import resource.ID;
import resource.Log;
import server.Server;


public class Client {
	private final String BAR = "-----------------------";
	
	private Registry registry;
	private LibraryInterface library;
	private ID userID;
	
	private Log log;
	private BufferedReader inFromUser;
	
	
	public Client() {
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void login() throws IOException {
		while (true) {
			System.out.println(BAR);
			System.out.println("LOGIN");
			System.out.print("Enter your ID: ");
			userID = new ID(inFromUser.readLine());
			
			if (userID.isManager() || userID.isUser()) {
				break;
			}
			else {
				System.out.println("Invalid ID. Please try again.");
			}
		}
	}
	
	private void createLog() {
		String rootDir = (Paths.get("").toAbsolutePath().toString());
		String[] path = {rootDir, "src", "client", "logs", userID.toString() + ".txt" };
		log = new Log(path);
		log.write(userID + " logged in.");
	}
	
	private void connectToServer() throws Exception {
		int port = 0;
		
		switch (userID.getLibraryName()) {
			case "CON":
				port = Server.CON_PORT;
				break;
			case "MCG":
				port = Server.MCG_PORT;
				break;
			case "MON":
				port = Server.MON_PORT;
				break;
			default:
				throw new Exception(userID.getLibraryName() + ": no such library.");
		}
		
		Registry registry = LocateRegistry.getRegistry(port + Server.RMI_OFFSET);
		library = (LibraryInterface) registry.lookup("Library");
		log.write("Successfully connected to library.");
	}
	
	private void addItem() throws Exception {
		System.out.println(BAR);
		System.out.println("ADD AN ITEM");
		System.out.print("Enter the ID of the item: ");
		ID itemID = new ID(inFromUser.readLine());
		System.out.print("Enter the name of the item: ");
		String itemName = inFromUser.readLine();
		System.out.print("Enter the quantity of the item you want to add: ");
		int quantity = Integer.parseInt(inFromUser.readLine());

		String msg = library.addItem(userID, itemID, itemName, quantity);
		System.out.println(msg);
		log.write(msg);
	}
	
	private void removeItem() throws Exception {
		System.out.println(BAR);
		System.out.println("REMOVE AN ITEM");
		System.out.print("Enter the ID of the item: ");
		ID itemID = new ID(inFromUser.readLine());
		System.out.print("Enter the quantity of the item you want to remove: ");
		int quantity = Integer.parseInt(inFromUser.readLine());
		
		String msg = library.deleteItem(userID, itemID, quantity);
		System.out.println(msg);
		log.write(msg);
	}
	
	private void listItem() throws Exception {
		System.out.println(BAR);
		System.out.println("LIST ALL ITEMS");
		Book[] books = library.listItemAvailability(userID);
		String msg = userID + " listed all items in the library.";
		
		System.out.println();
		for (Book book : books) {
			System.out.println(book);
			msg += ("\r\n" + book);
		}
		System.out.println();
		log.write(msg);
	}
	
	private void borrowItem() throws Exception {
		System.out.println(BAR);
		System.out.println("BORROW AN ITEM");
		System.out.print("Enter the ID of the item: ");
		ID itemID = new ID(inFromUser.readLine());
		System.out.print("Enter number of days you want to borrow this book: ");
		int numberOfDay = Integer.parseInt(inFromUser.readLine());
		String msg = library.borrowItem(userID, itemID, numberOfDay);
		
		if (msg.equals(itemID + " is not availbale now. Do you want to be added in the waiting list?")) {
			System.out.print(msg + "(y/n): ");
			String choice = inFromUser.readLine();
			if (choice.equals("y")) {
				msg = library.addWaitList(userID, itemID);
			}
			else {
				msg = userID + " failed to borrow the item (ID: " + itemID + ") from the library.";
			}
		}
		System.out.println(msg);
		log.write(msg);
	}
	
	private void findItem() throws Exception {
		System.out.println(BAR);
		System.out.println("FIND AN ITEM");
		System.out.print("Enter the name of the item: ");
		String itemName = inFromUser.readLine();
		String msg = userID + " tried to find the item(Name: " + itemName + ")\r\n\r\n";
		msg += library.findItem(userID, itemName, true);
		System.out.println(msg);
		log.write(msg);
	}
	
	private void returnItem() throws Exception {
		System.out.println(BAR);
		System.out.println("RETURN AN ITEM");
		System.out.print("Enter the ID of the item: ");
		ID itemID = new ID(inFromUser.readLine());
		String msg = library.returnItem(userID, itemID);
		System.out.println(msg);
		log.write(msg);
	}
	
	private void menu() throws Exception {
		String userChoice;
		if (userID.isManager()) {
			while (true) {
				System.out.println(BAR);
				System.out.println("MANAGER MENU");
				System.out.println("1. Add an item");
				System.out.println("2. Remove an item");
				System.out.println("3. List all items");
				System.out.println("4. Exit");
				System.out.print("Select an operation (1-4): ");

				userChoice = inFromUser.readLine();
				switch (userChoice) {
					case "1" : addItem(); break;
					case "2" : removeItem(); break;
					case "3" : listItem(); break;
					case "4" : 
						log.write(userID + " logged out."); 
						System.out.println("You have logged out.");
						return;
					default  : System.out.println("Invalid choice. Please try again."); break;
				}
			}
		}
		else {
			while (true) {
				System.out.println(BAR);
				System.out.println("USER MENU");
				System.out.println("1. Borrow an item");
				System.out.println("2. Find an item");
				System.out.println("3. Return an item");
				System.out.println("4. Exit");
				System.out.print("Select an operation (1-4): ");

				userChoice = inFromUser.readLine();
				switch (userChoice) {
					case "1" : borrowItem(); break;
					case "2" : findItem(); break;
					case "3" : returnItem(); break;
					case "4" : 
						log.write(userID + " logged out."); 
						System.out.println("You have logged out.");
						return;
					default  : System.out.println("Invalid choice. Please try again."); break;
				}
			}
		}
	}
	
	private void run() {
		try {
			login();
			createLog();
			connectToServer();
			menu();
		}
		catch (Exception e) {
			e.printStackTrace();
			log.write("Error: " + e);
			log.close();
		}
	}
	
	public static void main(String args[]) throws Exception	{

		Client client = new Client();
		client.run();
	}
}
