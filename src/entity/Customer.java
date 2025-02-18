package entity;

import java.util.Date;

public class Customer extends Person {
    private String deliveryAddress;
    private Date dateOfFirstContact;
	
    //Constructors
    public Customer(int iD, String name, int phoneNumber, String email, String deliveryAddress,
			Date dateOfFirstContact) {
		super(iD, name, phoneNumber, email);
		this.deliveryAddress = deliveryAddress;
		this.dateOfFirstContact = dateOfFirstContact;
	}

	public Customer(int iD, String name, int phoneNumber, String email) {
		super(iD, name, phoneNumber, email);
	}

	//Getters & Setters
	
	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public Date getDateOfFirstContact() {
		return dateOfFirstContact;
	}

	public void setDateOfFirstContact(Date dateOfFirstContact) {
		this.dateOfFirstContact = dateOfFirstContact;
	}
    
    
	
    
    

}
