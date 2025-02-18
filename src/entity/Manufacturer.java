package entity;

import java.util.Objects;

public class Manufacturer {

	private int uniqueIdentifier; //PK - not editable
	private String name;
	private String phoneNumber;
	private String address;
	private String email;
	
	public Manufacturer(int uniqueIdentifier, String name, String phoneNumber, String address, String email) {
		super();
		this.uniqueIdentifier = uniqueIdentifier;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.email = email;
	}
	public Manufacturer() {
		super();
	}
	
	public void setUniqueIdentifier(int uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}
	
	public int getUniqueIdentifier() {
		return uniqueIdentifier;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public int hashCode() {
		return Objects.hash(uniqueIdentifier);
	}
	@Override
	public boolean equals(Object obj) { //only compare according to the PK
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Manufacturer other = (Manufacturer) obj;
		return uniqueIdentifier == other.uniqueIdentifier;
	}

	
	
	
	
}