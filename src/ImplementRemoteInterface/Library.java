package ImplementRemoteInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Queue;

import ServerInterface.LibraryInterface;
import resource.Book;
import resource.ID;
import resource.LibraryName;
import server.Server;

public class Library extends UnicastRemoteObject implements LibraryInterface{

	private LibraryName name;
	private Server server;
	private HashMap<String, Book> repository;
	private HashMap<String, Queue<String>> waitingList;
	private HashMap<String, Integer> borrowRecord;
	
	public Library(Server server, LibraryName name) throws RemoteException {
		super();
		
		this.name = name;
		this.server = server;
		repository = new HashMap<String, Book>();
		waitingList = new HashMap<String, Queue<String>>();
		borrowRecord = new HashMap<String, Integer>();
	}
	
	@Override
	public boolean addItem(ID managerID, ID itemID, String itemName, int quantity) {
		if (!itemID.isItem() || !itemID.getLibraryName().equals(name.toString())) {

			server.log.write(managerID + " failed to add " + quantity + " book(s) (ID: " + itemID + ", name: "
					+ itemName + ") because of invalid itemID.");
			return false;
		}

		if (repository.containsKey(itemID.toString())) {
			if (!repository.get(itemID.toString()).getName().equals(itemName)) {
				server.log.write(managerID + " failed to add " + quantity + " book(s) (ID: " + itemID + ", name: "
						+ itemName + ") because of invalid itemID.");
				return false;
			} else {

				repository.get(itemID.toString()).add(quantity);
				server.log.write(managerID + " added " + quantity + " book(s) (ID: " + itemID + ", name: " + itemName
						+ ") in library " + name + ".");
			}
		} else {
			repository.put(itemID.toString(), new Book(itemID, itemName, quantity));
			server.log.write(managerID + " created and added " + quantity + " book(s) (ID: " + itemID + ", name: "
					+ itemName + ") in library " + name + ".");
		}

		return true;
	}

	@Override
	public boolean deleteItem(ID managerID, ID itemID, int quantity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Book[] listItemAvailability(ID managerID) {
		Book[] books = new Book[repository.size()];
		repository.values().toArray(books);
		server.log.write(managerID + " listed all items in the library.");
		return books;
	}

	@Override
	public boolean borrowItem(ID userID, ID itemID, int numberOfDay) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean findItem(ID userID, String itemName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean returnItem(ID userID, ID itemID) {
		// TODO Auto-generated method stub
		return false;
	}

}
