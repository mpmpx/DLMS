package ServerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import resource.Book;
import resource.ID;

public interface LibraryInterface extends Remote{

	// manager operations
	public boolean addItem (ID managerID, ID itemID, String itemName, int quantity) throws RemoteException;
	public boolean deleteItem (ID managerID, ID itemID, int quantity) throws RemoteException;
	public Book[] listItemAvailability (ID managerID) throws RemoteException;
	
	// user operations
	
	public boolean borrowItem (ID userID, ID itemID, int numberOfDay) throws RemoteException;
	public boolean findItem (ID userID, String itemName) throws RemoteException;
	public boolean returnItem (ID userID, ID itemID) throws RemoteException;
	
}
