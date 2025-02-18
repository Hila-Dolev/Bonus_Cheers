package entity;

import java.util.Objects;

public class WineType {
	
	//Attributes
	private int serialNumber;
	private String name;
	private String occasion;
	private OccasionLocation location;
	private Season season;
	
	//Constructor
	public WineType(int serialNumber, String name, String occasion, OccasionLocation location, Season season) {
		super();
		this.serialNumber = serialNumber;
		this.name = name;
		this.occasion = occasion;
		this.location = location;
		this.season = season;
	}

	
	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getOccasion() {
		return occasion;
	}


	public void setOccasion(String occasion) {
		this.occasion = occasion;
	}


	public OccasionLocation getLocation() {
		return location;
	}


	public void setLocation(OccasionLocation location) {
		this.location = location;
	}


	public Season getSeason() {
		return season;
	}


	public void setSeason(Season season) {
		this.season = season;
	}


	@Override
	public int hashCode() {
		return Objects.hash(location, name, season, serialNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WineType other = (WineType) obj;
		return Objects.equals(location, other.location) && Objects.equals(name, other.name)
				&& Objects.equals(season, other.season) && serialNumber == other.serialNumber;
	}
	
	

}
