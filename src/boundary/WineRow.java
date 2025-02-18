package boundary;

import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.toedter.calendar.JYearChooser;

import entity.SweetnessLevel;
public class WineRow {
    private int catalogNumber;
    private int uniqueIdentifier;
    private String name;
    private String description;
    private Date productionYear;
    private double pricePerBottle;
    private SweetnessLevel sweetnessLevel;
    private String productImagePath;
    private int wineTypeID;

    // Constructor
    public WineRow(int catalogNumber, int uniqueIdentifier, String name, String description,
                   Date productionYear, double pricePerBottle, SweetnessLevel sweetnessLevel, String productImagePath, int wineTypeID) {
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

    // Getters and Setters for all fields
    public int getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(int catalogNumber) {
        this.catalogNumber = catalogNumber;
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
}
