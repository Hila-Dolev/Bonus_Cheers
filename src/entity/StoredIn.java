package entity;

import java.util.Objects;

public class StoredIn {
	private Wine wine;
	private StorageLocation storageLocation;
	private int quantity;
	
	//Constructor
	public StoredIn(Wine wine, StorageLocation storageLocation, int quantity) {
		super();
		this.wine = wine;
		this.storageLocation = storageLocation;
		this.quantity = quantity;
	}
	public StoredIn() {
		super();
	}
	
	//Getters & Setters
	public Wine getWine() {
		return wine;
	}
	public void setWine(Wine wine) {
		this.wine = wine;
	}
	public StorageLocation getStorageLocation() {
		return storageLocation;
	}
	public void setStorageLocation(StorageLocation storageLocation) {
		this.storageLocation = storageLocation;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	//hashCode & equals
	@Override
	public int hashCode() {
		return Objects.hash(storageLocation, wine);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StoredIn other = (StoredIn) obj;
		return Objects.equals(storageLocation, other.storageLocation) && Objects.equals(wine, other.wine);
	}
	
	
	

}
