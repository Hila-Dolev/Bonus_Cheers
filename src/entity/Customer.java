package entity;

import java.util.ArrayList;
import java.util.Date;

public class Customer extends Person {
    private String deliveryAddress;
    private Date dateOfFirstContact;
    private ArrayList<CustomerRegularOrder> orders;
	
    //Constructors
    public Customer(int iD, String name, int phoneNumber, String email, String deliveryAddress, Date dateOfFirstContact,
			ArrayList<CustomerRegularOrder> orders) {
		super(iD, name, phoneNumber, email);
		this.deliveryAddress = deliveryAddress;
		this.dateOfFirstContact = dateOfFirstContact;
		this.orders = orders;
	}
    
    public Customer(int iD, String name, int phoneNumber, String email, String deliveryAddress, Date dateOfFirstContact) {
		super(iD, name, phoneNumber, email);
		this.deliveryAddress = deliveryAddress;
		this.dateOfFirstContact = dateOfFirstContact;
	}
    
    public Customer() {
		super();

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

	public ArrayList<CustomerRegularOrder> getOrders() {
		return orders;
	}

	public void setOrders(ArrayList<CustomerRegularOrder> orders) {
		this.orders = orders;
	}
    
	public void addOrder(RegularOrder order) {
        CustomerRegularOrder cro = new CustomerRegularOrder(this, order);
        orders.add(cro);
        order.getCustomers().add(cro);
    }
    
	
    
    

}
