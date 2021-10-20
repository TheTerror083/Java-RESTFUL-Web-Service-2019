package RH.PE;

import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.awt.Color;
import java.io.*;
import java.net.*;

/** 
* This Class includes a dynamic list of Food class objects.
* It also includes the functionality of connecting to the web service,
* Printing information to the output file and filling the food menu table.
*/
public class Model
{	
		private ArrayList<Food> food; 
		private InputStream url;
		private JTable foodMenu;
		private TableRowSorter<TableModel> rowSorter;
		private FileOutputStream fos = null;
		private DataOutputStream whereTo = null;
	    private static final Logger logger = LogManager.getLogger(Model.class.getName());
		
		public DefaultTableModel createTmodel() {
			DefaultTableModel TModel = new DefaultTableModel();
			TModel.addColumn("Name");
			TModel.addColumn("Description");
			TModel.addColumn("Price");
			TModel.addColumn("Calories");
			createRows(TModel);
			return TModel;
		}
		
	Model()
	{
		try
		{
			fos = new FileOutputStream("foodOutput.txt");
			whereTo = new DataOutputStream(fos);
		} 
		catch (FileNotFoundException e2)
		{
			logger.error(e2.getMessage());
			e2.printStackTrace();
		}
		
		food = new ArrayList<Food>();
		
		//parsing the XML file
		try{
			// read it from the url
			url = new URL("https://www.w3schools.com/xml/simple.xml").openStream();
		}catch(Exception e){
			try
			{
				// should the url be unavailable, the program will read from the local xml instead
				logger.error(e.getMessage());
				url = new FileInputStream("foodMenu.xml");
			} catch (FileNotFoundException e1)
			{
				logger.error(e1.getMessage());
				e1.printStackTrace();
			}
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		int i, j;
		
		//constructing the Food Menu Table
		logger.info("Building the Menu.....");
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(url);
				
			//get food details by tags
			logger.info("Getting food details.....");
			NodeList foodList = doc.getElementsByTagName("food");
			for(i=0; i < foodList.getLength(); ++i)
			{
				/** 
				* reading the data from the XML by tags and adding it to the food list.
				* The received data is also printed to the Output file.
				*/
				food.add(i, new Food());
				Node tempFood = foodList.item(i);
				if(tempFood.getNodeType() == Node.ELEMENT_NODE)
				{
					Element tempFood_ = (Element)tempFood;
					NodeList infoList = tempFood_.getChildNodes();
					for(j=0; j < infoList.getLength(); ++j)
					{
						Node temp = infoList.item(j);
						if(temp.getNodeType() == Node.ELEMENT_NODE)
						{
							Element info = (Element) temp;
							if(info.getTagName() == "name"){
								food.get(i).setName(info.getTextContent());
								whereTo.writeBytes("Name: " + food.get(i).getName()+"\n");
							}
								
							else if(info.getTagName() == "price"){
								food.get(i).setPrice(info.getTextContent());
								whereTo.writeBytes("Price: " + food.get(i).getPrice()+"\n");
							}
							else if(info.getTagName() == "description"){
								food.get(i).setDescr(info.getTextContent());
								whereTo.writeBytes("Description: " + food.get(i).getDescr()+"\n");
							}
							else if(info.getTagName() == "calories"){
								food.get(i).setCalories(info.getTextContent());
								whereTo.writeBytes("Calories: " + food.get(i).getCalories()+"\n");
							}
						}
					}
					whereTo.writeChar('\n');
				}
			}
		} catch (ParserConfigurationException e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (SAXException e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		finally
		{
			try
			{
				if(fos != null)
					fos.close();
				if(whereTo != null)
					whereTo.close();
			} catch (IOException e)
			{
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		
		//set the food menu (table) values
		logger.info("Setting food menu values...");
		DefaultTableModel TModel = createTmodel();
		
	
		foodMenu = new JTable(TModel);
		rowSorter = new TableRowSorter<>(foodMenu.getModel());		//for text search in the food menu table
		foodMenu.setRowSorter(rowSorter);
		
		foodMenu.setBorder(BorderFactory.createLineBorder(Color.black));
	    foodMenu.setEnabled(false);
	    
		//center menu text
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		 for(int x = 1; x < 3 ; x++)
	         foodMenu.getColumnModel().getColumn(x).setCellRenderer( centerRenderer );
		 
	}
	
	public void createRows (DefaultTableModel x){
		for(int i = 0; i < food.size(); ++i)
			x.addRow(new Object[]{food.get(i).getName(), food.get(i).getDescr(), food.get(i).getPrice(), food.get(i).getCalories() });
	}
	
	public void removeFood(int foodCount) // removes and amount of food items from the list
	{
		int index = food.size()-1;
		while(foodCount > 0)
		{
			food.remove(index);
			index--;
			foodCount--;
		}
	}

	//setters and getters
	public ArrayList<Food> getFoodList() { return food; }
	public JTable getTable() { return foodMenu; }
	public TableRowSorter getRowSorter() { return rowSorter; }
	public void addValue(String Name, String Descr, String Price, String Calories){ 
		((DefaultTableModel) foodMenu.getModel()).addRow(new Object[]{Name, Descr, "$"+Price, Calories});
	}
	
	@Override
	public String toString() {
		
		String cosEmek = null;	
		for (int i = 0; i < food.size(); ++i){
			cosEmek += "Name: " + food.get(i).getName() + ", Calories: " + food.get(i).getCalories() + ", Description: "  + food.get(i).getDescr() + ", Price: " + food.get(i).getPrice() + "\n";                                                                 ;
		}
		return cosEmek;
	}
    
}
