package entity;

import java.util.Objects;

public class StorageLocation {
	private int locationNumber; //PK
	private String storageName;
	
	//Constructor
	public StorageLocation(int locationNumber, String storageName) {
		super();
		this.locationNumber = locationNumber;
		this.storageName = storageName;
	}
	public StorageLocation() {
		super();
	}
	
	//Getters & Setters
	public int getLocationNumber() {
		return locationNumber;
	}
	public void setLocationNumber(int locationNumber) {
		this.locationNumber = locationNumber;
	}
	public String getStorageName() {
		return storageName;
	}
	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}
	
	//hashCode & equals
	@Override
	public int hashCode() {
		return Objects.hash(locationNumber);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StorageLocation other = (StorageLocation) obj;
		return locationNumber == other.locationNumber;
	}
	
	@Override
	public String toString() {
	    return "Storage Number " + getLocationNumber() + " - " + getStorageName();
	}

	
	
	

}
