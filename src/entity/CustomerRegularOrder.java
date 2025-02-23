package entity;

import java.util.HashMap;

public class CustomerRegularOrder {
	  private Customer customer;
	  private RegularOrder order;
	  private HashMap<Wine, Integer> wineQuantities;
	
	  //Constructor
	  public CustomerRegularOrder(Customer customer, RegularOrder order) {
		super();
		this.customer = customer;
		this.order = order;
		this.wineQuantities = new HashMap<Wine, Integer>();
	}

	public CustomerRegularOrder() {
		super();
	}

	//Getters & Setters
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public RegularOrder getOrder() {
		return order;
	}

	public void setOrder(RegularOrder order) {
		this.order = order;
	}

	public HashMap<Wine, Integer> getWineQuantities() {
		return wineQuantities;
	}

	public void setWineQuantities(HashMap<Wine, Integer> wineQuantities) {
		this.wineQuantities = wineQuantities;
	}
	  
	
	public void addWine(Wine wine, int quantity) {
		 if (wineQuantities == null) {
		        wineQuantities = new HashMap<>();
		    }
		    wineQuantities.put(wine, wineQuantities.getOrDefault(wine, 0) + quantity);
    }
	
	public void printOrderDetails() {
		System.out.println("Customer: " + customer.getName() + ", Address: " + customer.getDeliveryAddress());
	    System.out.println("Order ID: " + order.getOrderNumber());
	    if (wineQuantities != null && !wineQuantities.isEmpty()) {
	        for (HashMap.Entry<Wine, Integer> entry : wineQuantities.entrySet()) {
	            System.out.println("  - " + entry.getKey().getName() + ": " + entry.getValue() + " bottles");
	        }
	    } else {
	        System.out.println("  No wines in this order.");
	    }
	}
	  
	  
	  
}
