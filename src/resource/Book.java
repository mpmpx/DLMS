package resource;

import java.io.Serializable;

public class Book implements Serializable {
	private ID itemID;
	private String name;
	private int totalQuantity;
	private int availableQuantity;
	
	public Book(ID itemID, String name, int quantity) {
		this.itemID = itemID;
		this.name = name;
		totalQuantity = quantity;
		availableQuantity = totalQuantity;
	}
	
	public void add(int quantity) {
		totalQuantity += quantity;
		availableQuantity += quantity;
	}
	
	public void remove(int quantity) {
		if (availableQuantity >= quantity) {
			totalQuantity -= quantity;
			availableQuantity -= quantity;
		}
	}
	
	public ID getID() {
		return itemID;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAvailableQuantity() {
		return availableQuantity;
	}
	
	public int getTotalQuantity() {
		return totalQuantity;
	}
	
	public String toString() {
		return itemID + " " + name + " " + availableQuantity;
	}
}
