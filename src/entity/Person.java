package entity;

import java.util.Objects;

public class Person {
	private int ID; //PK
	private String name;
	private int phoneNumber;
	private String email;
	
	//Constructors
	public Person(int iD, String name, int phoneNumber, String email) {
		super();
		ID = iD;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	public Person() {
		super();
	}

	

	//Getters & Setters
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	//equal & hashcode
	
	@Override
	public int hashCode() {
		return Objects.hash(ID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		return ID == other.ID;
	}
	
	
	
	

}
