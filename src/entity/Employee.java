package entity;

import java.util.Date;

public class Employee extends Person{
	private Date employmentStartDate;
	private String officeAddress;
	private EmployeeType type;
	
	//Constructors
	public Employee(int iD, String name, int phoneNumber, String email, Date employmentStartDate, String officeAddress,
			EmployeeType type) {
		super(iD, name, phoneNumber, email);
		this.employmentStartDate = employmentStartDate;
		this.officeAddress = officeAddress;
		this.type = type;
	}
	

	public Employee(int iD, String name, int phoneNumber, String email) {
		super(iD, name, phoneNumber, email);
	}


	//Getters & Setters
	
	public Date getEmploymentStartDate() {
		return employmentStartDate;
	}

	public void setEmploymentStartDate(Date employmentStartDate) {
		this.employmentStartDate = employmentStartDate;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
	

	public EmployeeType getType() {
		return type;
	}


	public void setType(EmployeeType type) {
		this.type = type;
	}

	
	

}
