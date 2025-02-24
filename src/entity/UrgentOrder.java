package entity;

import java.util.Date;
import java.util.HashMap;

public class UrgentOrder extends Order {
	private PriorityLevel priorityLevel;
	private int expectedDeliveryTime;
	private int customerID;
	private HashMap<Wine, Integer> wineQuantities;
	
	//Constructors
	public UrgentOrder(int orderNumber, Date orderDate, OrderStatus status, Date shipmentDate,
			int assignedSaleEmployeeID, PriorityLevel priorityLevel, int expectedDeliveryTime, int customerID,
			HashMap<Wine, Integer> winesInUrgentOrder) {
		super(orderNumber, orderDate, status, shipmentDate, assignedSaleEmployeeID);
		this.priorityLevel = priorityLevel;
		this.expectedDeliveryTime = expectedDeliveryTime;
		this.customerID = customerID;
		this.wineQuantities = winesInUrgentOrder;
	}
	
	
	
	public UrgentOrder(int orderNumber, Date orderDate, OrderStatus status, Date shipmentDate,
			int assignedSaleEmployeeID, PriorityLevel priorityLevel, int expectedDeliveryTime, int customerID) {
		super(orderNumber, orderDate, status, shipmentDate, assignedSaleEmployeeID);
		this.priorityLevel = priorityLevel;
		this.expectedDeliveryTime = expectedDeliveryTime;
		this.customerID = customerID;
	}



	public UrgentOrder(int orderNumber, Date orderDate, OrderStatus status, Date shipmentDate, int assignedSaleEmployeeID) {
		super(orderNumber, orderDate, status, shipmentDate, assignedSaleEmployeeID);
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



	public HashMap<Wine, Integer> getWinesQuantities() {
		return wineQuantities;
	}



	public void setWinesQuantities(HashMap<Wine, Integer> winesInUrgentOrder) {
		this.wineQuantities = winesInUrgentOrder;
	}
	
	
	
	
	

}
