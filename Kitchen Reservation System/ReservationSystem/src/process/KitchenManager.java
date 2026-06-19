package process;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import model.KitchenInfo;

public class KitchenManager {
	//Define a arraylist to save kitchen info
	private static ArrayList<KitchenInfo> kitchen;

	//Constructor
	public KitchenManager() {
		 kitchen = new ArrayList<>();
	     readFile();
	 }
	 
	//Add new kitchen
	public void addKitchen(String kitchenID,String kitchenName,int kitchenCapy,double kitchenHouRate) { //add new detail
		 if(findID(kitchenID) != null) {
			 System.out.println("Kitchen ID(" + kitchenID + ") already exists.");
			 return;
		 }
		 if(kitchenCapy < 0) { // Check Kitchen Capacity
			 System.out.println("The capacity must higher than 0!");
			 return;
		 }
		 if(kitchenHouRate < 0.0) { // Check Kitchen Hour Rate
			 System.out.println("The hourly rate must higher than 0!");
			 return;
		 }
		 kitchen.add(new KitchenInfo(kitchenID,kitchenName,kitchenCapy,kitchenHouRate));
		 saveFile();
		 System.out.println("Kitchen Added Successful!");
	 }
	 
	 public void editKitchen(Scanner scan) { //edit detail, can choose which one to edit
		 System.out.print("Enter the kitchen ID to edit: ");
		 String edit = scan.nextLine();
		 
		 KitchenInfo editKitchen = findID(edit);
		 if(editKitchen == null) {  //if is empty not found
			 System.out.println("Kitchen ID(" + edit + ") not found.");
			 return;
		 }
		 
		 while(true) {
			 	// Choose kitchen info to edit
				System.out.println("Edit " + editKitchen.getKitchenName() + "(" + editKitchen.getKitchenID() + "):");
				System.out.println("1. Name");
				System.out.println("2. Capacity");
				System.out.println("3. Hourly Rate");
				System.out.println("0. Return Back to Kitchen Menu");
				System.out.print("Choose a number to edit the kitchen detail: ");
				String editChoose = scan.nextLine();
				
				switch (editChoose) {
				case "1": // Edit Kitchen Name
					System.out.println("Enter new name(type cancel to return): ");
					String newName = scan.nextLine();
					if (newName.equalsIgnoreCase("CANCEL")) {
			            System.out.println("edit cancelled.");
			            continue;
			        }
			        editKitchen.setKitchenName(newName);
			        System.out.println("Name has been renew.");
			        break;
					
				case "2": // Edit Kitchen Capacity
					while(true) { //start loop
						System.out.print("Enter new capacity(type cancel to return) : "); //enter capacity in String form first
						String newCapy = scan.nextLine();
						if (newCapy.equalsIgnoreCase("CANCEL")) {
				            System.out.println("edit cancelled.");
				            break;
				        }
						try {
							int newKitchenCapy = Integer.parseInt(newCapy); //start try catch, swap to integer
							if(newKitchenCapy > 0) { //validate if higher than 0
								editKitchen.setKitchenCapy(newKitchenCapy);
								System.out.println("Capacity has been renew.");
								break; // if greater then stop
							}else {
								System.out.println("The capacity of kitchen must greater than 0 !");
							}
						}catch(NumberFormatException IMex) { //for validation not number
							System.out.println("Invalid input, please enter valid number !");
						}
					}
					break;
					
				case "3": // Edit Kitchen Hour Rate
					while(true) {
						System.out.print("Enter new hourly rate[RM per hour](type cancel to return): ");
						String newHour = scan.nextLine();
						if (newHour.equalsIgnoreCase("CANCEL")) {
				            System.out.println("edit cancelled.");
				            break;
				        }
						try {
							double newKitchenHouRate = Double.parseDouble(newHour);
							if (newKitchenHouRate > 0.0) {
								editKitchen.setKitchenHouRate(newKitchenHouRate);
								System.out.println("Hourly rate has been renew.");
								break;
							}else {
								System.out.println("The price must higer than 0");
							}
						}catch(NumberFormatException IMex) {
							System.out.println("Invalid input, please enter valid number !");
						}
					}
					break;
				
				case"0": // Return Menu
					System.out.println("Return Back To Menu...");
					return;
					
				default:
					System.out.println("Invalid input");
				}
				saveFile();
			}
		 
	 }
	 
	 //Display all kitchen info 
	 public static void viewKitchen() {
		 System.out.println("=========================Kitchen View List=========================");
		 System.out.printf("%-10s| %-20s| %-15s| %-15s%n",  "ID", "Name", "Capacity(mÂ²)", "Hourly Rate(RM)");
		 System.out.println("-------------------------------------------------------------------");
		 for (KitchenInfo kInfo : kitchen) {
			 System.out.println(kInfo.showMenu()); // call kitchen info show menu 
		 }
	 }
	 
	 public static void showAvailableSlots(String FileName,String kitchenID, String date, String status)
		{
			
			ReservationManager.displayBookedSlot("reservation.txt",kitchenID,date,status);
		}
		
	 public static void freeSlot(String FileName,String reservationID, String customerName)
		{
			ReservationManager.cancelBooking("reservation.txt",reservationID,customerName);
		}
	
	 
	 private KitchenInfo findID(String kitchenID) { //find is this kitchen id exists
		 for (KitchenInfo kInfo : kitchen) {
	         if (kInfo.getKitchenID().equalsIgnoreCase(kitchenID)) {
	             return kInfo;
	         }
	      }
	        return null;
	    }
	 private void saveFile() { //Save editted kitchen info into a file
	        try (PrintWriter writer = new PrintWriter(new FileWriter("kitchen.txt"))) {
	            for (KitchenInfo kInfo : kitchen) {
	                writer.println(kInfo.toString());
	            }
	        } catch (IOException e) {
	            System.out.println("Error saving users: " + e.getMessage());
	        }
	  }
	 private void readFile() { //Read all kitchen info from kitchen file
	     File file = new File("kitchen.txt");
	     if (!file.exists()) {
	     return;
	     }
	     try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	         String readLine;
	         while ((readLine = reader.readLine()) != null) {
	        	 String[] info = readLine.split(",");
	             if (info.length == 4) {
	            	 String kitchenID = info[0];
	                 String kitchenName = info[1];
	                 int kitchenCapy = Integer.parseInt(info[2]);
	                 double kitchenHouRate = Double.parseDouble(info[3]);
	                 kitchen.add(new KitchenInfo(kitchenID,kitchenName,kitchenCapy,kitchenHouRate));
	             }
	         }
	      } catch (IOException e) {
	    	  System.out.println("Error loading users: " + e.getMessage());
	      }
	 }
}