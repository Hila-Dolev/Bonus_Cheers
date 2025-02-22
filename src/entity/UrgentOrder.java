package entity;

import java.util.Date;

public class UrgentOrder extends Order {
	private PriorityLevel priorityLevel;
	private int expectedDeliveryTime;
	private int customerID;
	
	//Constructors
	public UrgentOrder(int orderNumber, Date orderDate, OrderStatus status, Date shipmentDate,
			PriorityLevel priorityLevel, int expectedDeliveryTime, int customerID) {
		super(orderNumber, orderDate, status, shipmentDate);
		this.priorityLevel = priorityLevel;
		this.expectedDeliveryTime = expectedDeliveryTime;
		this.customerID = customerID;
	}
	public UrgentOrder(int orderNumber, Date orderDate, OrderStatus status, Date shipmentDate) {
		super(orderNumber, orderDate, status, shipmentDate);
	}
	
	//Getters & Setters
	public PriorityLevel getPriorityLevel() {
		return priorityLevel;
	}
	public void setPriorityLevel(PriorityLevel priorityLevel) {
		this.priorityLevel = priorityLevel;
	}
	public int getExpectedDeliveryTime() {
		return expectedDeliveryTime;
	}
	public void setExpectedDeliveryTime(int expectedDeliveryTime) {
		this.expectedDeliveryTime = expectedDeliveryTime;
	}
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	
	
	

}
