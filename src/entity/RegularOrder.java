package entity;

import java.util.ArrayList;
import java.util.Date;

public class RegularOrder extends Order {
	private int mainCustomerID;
	private ArrayList<CustomerRegularOrder> customers;
	
	//Constructor
	public RegularOrder(int orderNumber, Date orderDate, OrderStatus status, Date shipmentDate, int mainCustomerID,
			ArrayList<CustomerRegularOrder> customers) {
		super(orderNumber, orderDate, status, shipmentDate);
		this.mainCustomerID = mainCustomerID;
		this.customers = customers;
	}

	public RegularOrder(int orderNumber, Date orderDate, OrderStatus status, Date shipmentDate) {
		super(orderNumber, orderDate, status, shipmentDate);
	}

	//Getters & Setters
	public int getMainCustomerID() {
		return mainCustomerID;
	}

	public void setMainCustomerID(int mainCustomerID) {
		this.mainCustomerID = mainCustomerID;
	}

	public ArrayList<CustomerRegularOrder> getCustomers() {
		return customers;
	}

	public void setCustomers(ArrayList<CustomerRegularOrder> customers) {
		this.customers = customers;
	}
	 
	 public void addCustomer(Customer customer) {
	        CustomerRegularOrder cro = new CustomerRegularOrder(customer, this);
	        customers.add(cro);
	        customer.getOrders().add(cro);
	    }
	 
	 
	

}
