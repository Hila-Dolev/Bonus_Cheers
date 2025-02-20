package entity;

import java.util.Date;
import java.util.Objects;

public abstract class Order {
	private int orderNumber; //PK
	private Date orderDate;
	private OrderStatus status;
	private Date shipmentDate;
	
	//Constructors
	public Order(int orderNumber, Date orderDate, OrderStatus status, Date shipmentDate) {
		super();
		this.orderNumber = orderNumber;
		this.orderDate = orderDate;
		this.status = status;
		this.shipmentDate = shipmentDate;
	}
	public Order() {
		super();
	}
	
	//Getters & Setters
	public int getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public Date getShipmentDate() {
		return shipmentDate;
	}
	public void setShipmentDate(Date shipmentDate) {
		this.shipmentDate = shipmentDate;
	}
	
	//hashCode & equals
	@Override
	public int hashCode() {
		return Objects.hash(orderNumber);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return orderNumber == other.orderNumber;
	}
	
	

}
