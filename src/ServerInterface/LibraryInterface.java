package ServerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import resource.Book;
import resource.ID;

public interface LibraryInterface extends Remote{

	// manager operations
	public String addItem (ID managerID, ID itemID, String itemName, int quantity) throws RemoteException;
	public String deleteItem (ID managerID, ID itemID, int quantity) throws RemoteException;
	public Book[] listItemAvailability (ID managerID) throws RemoteException;
	
	// user operations
	
	public String borrowItem (ID userID, ID itemID, int numberOfDay) throws RemoteException;
	public String findItem (ID userID, String itemName, boolean recursive) throws RemoteException;
	public String returnItem (ID userID, ID itemID) throws RemoteException;
	public String addWaitList(ID userID, ID itemID) throws RemoteException;
	
}
