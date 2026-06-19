package model;

public class KitchenInfo {
	private String kitchenID;
	private String kitchenName;
	private int kitchenCapy;
	private double kitchenHouRate;
	
	//Kitchen Info Constructor
	public KitchenInfo(String kitchenID,String kitchenName,int kitchenCapy,double kitchenHouRate) {
		this.kitchenID = kitchenID;
		this.kitchenName = kitchenName;
		this.kitchenCapy = kitchenCapy;
		this.kitchenHouRate = kitchenHouRate;
	}
	
	//Kitchen Info accessor
	public String getKitchenID() { //
		return kitchenID;
	}
	public String getKitchenName() {
		return kitchenName;
	}
	public int getKitchenCapy() {
		return kitchenCapy;
	}
	public double getKitchenHouRate() {
		return kitchenHouRate;
	}
	
	//Kitchen Info mutator
	public void setKitchenID(String kitchenID) {
		this.kitchenID = kitchenID;
	}
	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
	public void setKitchenCapy(int kitchenCapy) {
		this.kitchenCapy = kitchenCapy;
	}
	public void setKitchenHouRate(double kitchenHouRate) {
		this.kitchenHouRate = kitchenHouRate;
	}
	
	@Override
	//Save Kitchen Info into a text File.
	public String toString() {
		return kitchenID + "," + kitchenName + "," + kitchenCapy + "," + kitchenHouRate;
	}
	
	//Show Kitchen Info in system.
	public String showMenu() {
		return String.format("%-10s| %-20s| %-15d| %-15.2f",kitchenID,kitchenName,kitchenCapy,kitchenHouRate);
	}
}