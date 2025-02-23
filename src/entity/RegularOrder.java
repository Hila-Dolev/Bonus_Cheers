package entity;

import java.util.ArrayList;
import java.util.Date;

public class RegularOrder extends Order {
	private int mainCustomerID;
	private ArrayList<CustomerRegularOrder> customers;
	
	//Constructor
	public RegularOrder(int orderNumber, Date orderDate, OrderStatus status, Date shipmentDate, int mainCustomerID,
			ArrayList<CustomerRegularOrder> customers, int assignedSaleEmployeeID) {
		super(orderNumber, orderDate, status, shipmentDate, assignedSaleEmployeeID);
		this.mainCustomerID = mainCustomerID;
		this.customers = customers;
	}
	
	
	public RegularOrder(int orderNumber, Date orderDate, OrderStatus status, Date shipmentDate, int mainCustomerID, int assignedSaleEmployeeID) {
		super(orderNumber, orderDate, status, shipmentDate, assignedSaleEmployeeID);
		this.mainCustomerID = mainCustomerID;
		this.customers = new ArrayList<CustomerRegularOrder>();
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
		 if (customers == null) {
		        customers = new ArrayList<>();
		    }
		 
	        CustomerRegularOrder cro = new CustomerRegularOrder(customer, this);
	        customers.add(cro);
	        customer.getOrders().add(cro);
	    }
	 
	 @Override
	    public String toString() {
	        return "Order Num: " + getOrderNumber();  // מחזירה את המספר בפורמט "Order Num: (number)"
	    }
	

}
