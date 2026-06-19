package viewMenu;

import java.util.Scanner;
import process.*;
import utility.ConsoleUtil;

public class AdminMenu {
	//Display Admin Menu
	public static void adminMenu(Scanner scan,KitchenManager kitchenManager, CustomerManager cm,ReservationManager rm, ReportManager rpm) {
		String choice;
		boolean loopChoice = true;
		while(loopChoice) {
			//User Interface
			System.out.println("============ Admin Menu ============");
			System.out.println("1. Customer Management");
			System.out.println("2. Kitchen Management");
			System.out.println("3. Reservation Management");
			System.out.println("4. Generate Report");
			System.out.println("0. Logout");
			System.out.print("Choose a number: ");
			choice = scan.nextLine(); //input option
			System.out.println();
			
			switch(choice) {
			case"1":
				adminCustomerMenu(scan,cm);
				break;
			case"2":
				adminKitchenMenu(scan,kitchenManager);
				break;
			case"3":
				adminReservationMenu(scan,rm);
				break;
			case"4":
				adminReportMenu(scan,rpm);
				break;
			case"0": 
            	System.out.println("Return back to login page.\n");
            	loopChoice = false;
            	break;
            default:
            	System.out.println("Invalid input.\n");
			}
		}
		
	}
	
	//Customer Management Menu
	private static void adminCustomerMenu(Scanner scan,CustomerManager cm) {
		String chooseCust;
    	boolean loopCust = true;
    	while(loopCust) {
    		System.out.println("============ Admin Customer Menu ============");
            System.out.println("1. View All Customer");
            System.out.println("2. Remove Customer");
            System.out.println("0. Return to menu");
            System.out.print("Choose a number: ");
            chooseCust = scan.nextLine(); //input option
            System.out.println();
            
            switch(chooseCust) {
            case"1": // View all customer
            	cm.viewAllCustomer();
            	break;
            case"2": // Remove customer
            	boolean loop = true;
            	while(loop) {
        			System.out.print("Enter the username to delete: ");
        			String username = scan.nextLine(); //Input UserName to delete
        			boolean loop2 = true;
        			if(cm.removeCustomer("customer.txt", username)) {
        				System.out.println("Customer '" + username + "' removed successfully."); //Show removed successful
        			}
        			else {
        				System.out.println("Customer '" + username + "' not found."); //Show uer not found if user not exist
        			}
        			
        			while(loop2) { // Delete other customer
        				System.out.print("Do you want to delete another customer>(y/n): ");
        				String choice = scan.nextLine().toUpperCase();
        				if(choice.equals("N")) { // No delete 
        					loop = false;
        					loop2 = false;
        				}
        				else if(choice.equals("Y")) { // continue delete other user
        					loop2 = false;
        					continue;
        				}
        				else { //invalid input
        					System.out.println("Invalid input. Please try again.");
        				}
        			}
            	}
            	break;
            case"0": // back to admin menu
            	System.out.println("Return back to admin menu page.\n");
            	loopCust = false;
            	break;
            default:
            	System.out.println("Invalid input.\n");
    	}
	
	}
}
	//Admin Kitchen Management 
	private static void adminKitchenMenu(Scanner scan,KitchenManager kitchenManager) { //acmin page
    	String choose;
    	boolean loop = true;
    	while(loop) {
    		System.out.println("============ Admin Kitchen Menu ============");
            System.out.println("1. Add Kitchen");
            System.out.println("2. Edit Kitchen");
            System.out.println("3. View Kitchens");
            System.out.println("0. Return to menu");
            System.out.print("Choose a number: ");
            choose = scan.nextLine();
            System.out.println();
            
            switch(choose) {
            case "1":
            	System.out.print("Enter the kitchen ID(eg KC###): ");
            	String kitchenID = scan.nextLine();
            	System.out.print("Enter the kitchen name: ");
            	String kitchenName = scan.nextLine();
            	
            	int kitchenCapy = 0;
        		while(true) { //start loop
        			System.out.print("Enter the kitchen capacity(mÂ²): "); //enter capacity in String form first
        			String kitchenInput = scan.nextLine();
        			try {
        				kitchenCapy = Integer.parseInt(kitchenInput); //start try catch, swap to integer
        				if(kitchenCapy < 0) { //validate if lower than 0
        					System.out.println("The capacity of kitchen must greater than 0 !");
        					continue; // if lower than 0 repeat
        				}
        				break; // if greater then stop
        			}catch(NumberFormatException IMex) { //for validation not number
        				System.out.println("Invalid input, please enter valid number !");
        			}
        		}
        		
        		double kitchenHouRate = 0.0;
        		while(true) {
        			System.out.print("Enter the hourly rate (RM per hour): ");
        			String kitchenInput = scan.nextLine();
        			try {
        				kitchenHouRate = Double.parseDouble(kitchenInput);
        				if (kitchenHouRate < 0.0) {
        					System.out.println("The price must higer than 0");
        					continue;
        				}
        				break;
        			}catch(NumberFormatException IMex) {
        				System.out.println("Invalid input, please enter valid number !");
        			}
        		}
            	
            	kitchenManager.addKitchen(kitchenID,kitchenName,kitchenCapy,kitchenHouRate);
            	break;
            	
            case "2":
            	kitchenManager.viewKitchen();
            	kitchenManager.editKitchen(scan);
            	break;
            case"3":
            	kitchenManager.viewKitchen();
            	break;
            case"0":
            	System.out.println("Return back to admin menu page.\n");
            	loop = false;
            	break;
            default:
            	System.out.println("Invalid input.\n");
            }
    	}
    }
	
	//Admin Reservation Management
	private static void adminReservationMenu(Scanner scan,ReservationManager rm) {
		String chooseRes;
    	boolean loopRes = true;
    	while(loopRes) {
    		System.out.println("============ Admin Reservation Menu ============");
            System.out.println("1. View All Reservation");
            System.out.println("2. Cancell Reservation");
            System.out.println("0. Return to menu");
            System.out.print("Choose a number: ");
            chooseRes = scan.nextLine();
            System.out.println();
            
            switch(chooseRes) {
            case"1":
            	rm.viewAllReservation("reservation.txt");
            	break;
            case"2":
            	System.out.println("Enter the Reservation ID to cancel: ");
            	String cancelId = scan.nextLine(); //Input Reservation Id to cancel.
            	System.out.println("Enter the username to cancel: ");
            	String cancelname = scan.nextLine(); //Input the username to delete his/her reservation
                if(rm.cancelReservation(cancelId,cancelname)) {
                    System.out.println("Reservation canceled successfully.");
                } else {
                    System.out.println("Reservation not found or already canceled.");
                }
                break;
            case"0": // Back to admin menu
            	System.out.println("Return back to admin menu page.\n");
            	loopRes = false;
            	break;
            default:
            	System.out.println("Invalid input.\n");
            }
    	}
	}
	
	//Admin Report Management
	private static void adminReportMenu(Scanner scan, ReportManager rpm) {
		String choice;
		boolean reportchoice;
		reportchoice = true;
		while(reportchoice) {
			System.out.println("===== Admin Menu =====");
	        System.out.println("1. Generate Monthly Report");
	        System.out.println("2. Generate Yearly Report");
	        System.out.println("0. Exit");
	        System.out.print("Choose option: ");
	        choice = scan.nextLine();
	        System.out.println();
	        
	        switch(choice) {
	        case"1":
	        	// Monthly Report
	        	System.out.print("Enter year (e.g., 2025): ");
	        	int year = scan.nextInt();
	        	System.out.print("Enter month (1-12): ");
	        	int month = scan.nextInt();
	        	scan.nextLine();
	        	rpm.generateMonthlyReport(year, month);
	        	ConsoleUtil.pause();
	        	break;

	        case"2":
	        	// Yearly Report
	        	System.out.print("Enter year (e.g., 2025): ");
	        	int years = scan.nextInt();
	        	scan.nextLine();
	        	rpm.generateYearlyReport(years);
	        	ConsoleUtil.pause();
	        	break;
	        	
	        case"0": //Back to admin menu
	        	System.out.println("Exiting...\n");
	        	reportchoice = false;
	        	break;
	        
	        default:
	        	System.out.println("Invalid Input.\n");
	        }
		}
    }
}