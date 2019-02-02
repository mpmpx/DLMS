package resource;

import java.io.Serializable;

public class ID implements Serializable{
	private String libraryName;
	private String IDType;
	private String IDNum;
	int length;
	
	public ID (String newID) {
		length = newID.length();
		if (length >= 7) {
			libraryName = newID.substring(0, 3);
			IDType = newID.substring(3, 4);
			IDNum = newID.substring(4, newID.length());
		}
	}
	
	public String getLibraryName() {
		return libraryName;
	}
	
	public boolean isUser() {
		return isValidUser() && IDType.equals("U") ; 
	}
	
	public boolean isManager() {
		return isValidUser() && IDType.equals("M") ; 
	}
	
	private boolean isValidUser() {
		return length == 8 && isValidLibrary(libraryName) && isNumber(IDNum);
	}
	
	public boolean isItem() {
		return length == 7 && isValidLibrary(libraryName) && isNumber(IDNum);
	}
	
	private boolean isValidLibrary(String libraryName) {
		return libraryName.equals("CON") 
			|| libraryName.equals("MCG")
			|| libraryName.equals("MON");
	}
	
	private boolean isNumber(String number) {
		for (int i = 0; i < number.length(); i++) {
			if (!Character.isDigit(number.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	public String toString() {
		return libraryName + IDType + IDNum;
	}
	
}
