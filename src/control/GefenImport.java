package control;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import entity.Manufacturer;
import entity.SweetnessLevel;
import entity.Wine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GefenImport {

	public static void ImportWinesFromXML() {
		ArrayList<Wine> wines = new ArrayList<Wine>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            
            // טעינת הקובץ מתוך ה-JAR או resources
            InputStream inputStream = GefenImport.class.getResourceAsStream("/wines.xml");
            if (inputStream == null) {
                System.out.println("קובץ ה-XML לא נמצא.");
                return;
            }    
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize(); // איתור כל צמתי היצרנים
            NodeList manufacturerNodes = document.getElementsByTagName("manufacturer");
            for (int i = 0; i < manufacturerNodes.getLength(); i++) {
                Node manufacturerNode = manufacturerNodes.item(i);
                if (manufacturerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element manufacturerElement = (Element) manufacturerNode;

                    // איתור רשימת היינות של היצרן
                    NodeList wineNodes = manufacturerElement.getElementsByTagName("wine");
                    for (int j = 0; j < wineNodes.getLength(); j++) {
                        Node wineNode = wineNodes.item(j);

                        if (wineNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element wineElement = (Element) wineNode;

                            // קריאת הנתונים מתוך צומת היין
                            int catalogNumber = Integer.parseInt(wineElement.getElementsByTagName("catalogNumber").item(0).getTextContent());
                            int manufacturerNumber = Integer.parseInt(wineElement.getElementsByTagName("manufacturerNumber").item(0).getTextContent());
                            String name = wineElement.getElementsByTagName("name").item(0).getTextContent();
                            String description = wineElement.getElementsByTagName("description").item(0).getTextContent();
                            Date productionYear = new SimpleDateFormat("yyyy-MM-dd").parse(wineElement.getElementsByTagName("productionYear").item(0).getTextContent() + "-01-01");
                            double pricePerBottle = Double.parseDouble(wineElement.getElementsByTagName("pricePerBottle").item(0).getTextContent());
                            SweetnessLevel sweetnessLevel = SweetnessLevel.valueOf(wineElement.getElementsByTagName("sweetnessLevel").item(0).getTextContent());
                            String productImagePath = wineElement.getElementsByTagName("productImagePath").item(0).getTextContent();
                            int wineTypeID = Integer.parseInt(wineElement.getElementsByTagName("WineTypeID").item(0).getTextContent());

                            
                            // יצירת אובייקט של יין והוספתו לרשימה
                            Wine wine = new Wine(catalogNumber, manufacturerNumber, name, description, productionYear, pricePerBottle, sweetnessLevel, productImagePath, wineTypeID);
                            System.out.println(wine);
                            wines.add(wine);
                        }
                    }
                }
            }
        WineManagment.getInstance().setWinesList(wines);
    		
		} catch (Exception e) {
            e.printStackTrace();
        }
		
	}

	public static void ImportManufacturerFromXML() {
		ArrayList<Manufacturer> manufacturers = new ArrayList<Manufacturer>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            // טעינת הקובץ מתוך ה-JAR או resources
            InputStream inputStream = GefenImport.class.getResourceAsStream("/wines.xml");
            if (inputStream == null) {
                System.out.println("קובץ ה-XML לא נמצא.");
                return;
            }
            
            Document document = builder.parse(inputStream);
            document.getDocumentElement().normalize();
            // איתור כל צמתי היצרנים
            NodeList manufacturerNodes = document.getElementsByTagName("manufacturer");
            for (int i = 0; i < manufacturerNodes.getLength(); i++) {
                Node manufacturerNode = manufacturerNodes.item(i);
                if (manufacturerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element manufacturerElement = (Element) manufacturerNode;
                            int uniqueIdentifier = Integer.parseInt(manufacturerElement.getElementsByTagName("UniqueIdentifier").item(0).getTextContent());
                            String name = manufacturerElement.getElementsByTagName("name").item(0).getTextContent();
                            String phoneNumber = manufacturerElement.getElementsByTagName("phoneNumber").item(0).getTextContent();
                            String address = manufacturerElement.getElementsByTagName("address").item(0).getTextContent();
                            String email = manufacturerElement.getElementsByTagName("email").item(0).getTextContent();
                            
                            
                            // יצירת אובייקט של יין והוספתו לרשימה
                            Manufacturer manufacturer = new Manufacturer(uniqueIdentifier, name, phoneNumber, address, email);
                            System.out.println(manufacturer);
                            manufacturers.add(manufacturer);
                        }
                    }
               
            ManufacturerManagment.getInstance().setManufacturersList(manufacturers);
    		    
        } catch (Exception e) {
            e.printStackTrace();
        }

	}



	/**
	 * *all other methods all together - to maintain an organized environment
	 * @throws SQLException 
	 */
	public static void ImportAllXML() throws SQLException {
		ImportWinesFromXML();
		ImportManufacturerFromXML();
		
		ManufacturerManagment.getInstance().createNewManufacturer();
		WineManagment.getInstance().createNewWine();
	}
	

}