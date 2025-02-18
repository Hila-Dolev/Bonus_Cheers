package entity;

import java.util.Date;
import java.util.Objects;

public class Wine {

	private int catalogNumber; //PK - not editable
	private int uniqueIdentifier; //FK Manufacturer
	private String name;
	private String description;
	private Date productionYear;
	private double pricePerBottle;
	private SweetnessLevel sweetnessLevel;
	private String productImagePath;
	private int wineTypeID; //FK WineTye
	
	public Wine(int catalogNumber, int uniqueIdentifier, String name, String description, Date productionYear,
			double pricePerBottle, SweetnessLevel sweetnessLevel, String productImagePath, int wineTypeID) {
		super();
		this.catalogNumber = catalogNumber;
		this.uniqueIdentifier = uniqueIdentifier;
		this.name = name;
		this.description = description;
		this.productionYear = productionYear;
		this.pricePerBottle = pricePerBottle;
		this.sweetnessLevel = sweetnessLevel;
		this.productImagePath = productImagePath;
		this.wineTypeID = wineTypeID;
		
		
	}

	public Wine() {
		super();
	}

	public void setCatalogNumber(int catalogNumber) {
		this.catalogNumber = catalogNumber;
	}
	
	public int getCatalogNumber() {
		return catalogNumber;
	}


	public int getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(int uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getProductionYear() {
		return productionYear;
	}

	public void setProductionYear(Date productionYear) {
		this.productionYear = productionYear;
	}

	public double getPricePerBottle() {
		return pricePerBottle;
	}

	public void setPricePerBottle(double pricePerBottle) {
		this.pricePerBottle = pricePerBottle;
	}

	public SweetnessLevel getSweetnessLevel() {
		return sweetnessLevel;
	}

	public void setSweetnessLevel(SweetnessLevel sweetnessLevel) {
		this.sweetnessLevel = sweetnessLevel;
	}

	public String getProductImagePath() {
		return productImagePath;
	}

	public void setProductImagePath(String productImagePath) {
		this.productImagePath = productImagePath;
	}
	
	
	public int getWineTypeID() {
		return wineTypeID;
	}

	public void setWineTypeID(int wineTypeID) {
		this.wineTypeID = wineTypeID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(catalogNumber);
	}

	@Override
	public boolean equals(Object obj) { ////only compare according to the PK
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Wine other = (Wine) obj;
		return catalogNumber == other.catalogNumber;
	}

	@Override
	public String toString() {
		return "Wine [catalogNumber=" + catalogNumber + ", uniqueIdentifier=" + uniqueIdentifier + ", name=" + name
				+ ", description=" + description + ", productionYear=" + productionYear + ", pricePerBottle="
				+ pricePerBottle + ", sweetnessLevel=" + sweetnessLevel + ", productImagePath=" + productImagePath
				+ "]";
	}

	
	
	
}