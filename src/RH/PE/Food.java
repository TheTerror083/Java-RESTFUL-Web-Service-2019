package RH.PE;

/** 
* This Class is used to store parameters of food received from the XML.
*/

public class Food
{
	private String name;
	private String price;
	private String Descr;
	private String Calories;
	
	public Food() {
		price = "";
		name = "";
		Descr = "";
		Calories = "";
	}
	
	public Food(String name,String Descr, String price, String Calories) {
		this.price = "$"+price;
		this.name = name;
		this.Descr = Descr;
		this.Calories = Calories;
	}
	
	/** 
	* These are the class' Setters and Getters:
	*/
	// setters
	public void setPrice(String Price) {this.price = Price;}
	public void setDescr(String Descr) {this.Descr = Descr;}
	public void setName(String name) {this.name = name;}
	public void setCalories(String Calories) {this.Calories = Calories;}
	
	// getters
	public String getPrice() {return this.price;}
	public String getDescr() {return this.Descr;}
	public String getName() {return this.name;}
	public String getCalories() {return this.Calories;}	
}
