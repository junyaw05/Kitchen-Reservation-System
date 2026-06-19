package main;

import java.util.*;

import model.User;
import process.*;
import viewMenu.*;

public class Main {
	//Main System Menu
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserManager userManager = new UserManager();
        KitchenManager kitchenManager = new KitchenManager();
        CustomerManager cm = new CustomerManager();
        ReservationManager rm = new ReservationManager();
        ReportManager rpm = new ReportManager();
        
        while (true) {
        	//Login Interface
            System.out.println("============ Login Register Page ============");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.print("Choose a number: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": // User Register
                    System.out.print("\nEnter the role (admin/customer) (Q - Quit): ");
                    String role = scanner.nextLine();
                    if(role.equalsIgnoreCase("Q")) {
                    	break;
                    }
                    else {
                        System.out.print("Enter your username: ");
                        String uname = scanner.nextLine();
                        System.out.print("Enter your password: ");
                        String pass = scanner.nextLine();
                        System.out.print("Enter your real name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter your contact number: ");
                        String contactNo = scanner.nextLine();
                        userManager.register(role, uname, pass, name, contactNo);
                        break;
                    }

                case "2": // User Login
                    System.out.print("Enter your username (Q - Quit): ");
                    String uuname = scanner.nextLine();
                    if(uuname.equalsIgnoreCase("Q")) {
                    	break;
                    }
                    else {
                        System.out.print("Enter your password: ");
                        String upass = scanner.nextLine();
                        System.out.println();
                        User completeLog = userManager.login(uuname, upass); //call user manager login
                        if (completeLog != null) {
                            System.out.println("Login Successful, Welcome " + completeLog.getUsername() + "(" + completeLog.getRole() + ").");
                            if(completeLog.getRole().equalsIgnoreCase("admin")) {
                            	AdminMenu.adminMenu(scanner,kitchenManager,cm,rm,rpm);
                            }else {
                            	CustomerMenu.customerMenu(scanner,kitchenManager,cm,rm);
                            }
                        } else {
                            System.out.println("Invalid input.\n");
                        }
                        break;
                    }

                case "0": //Exit System
                	if(UserManager.getCurrentUsername() != null) {
                		userManager.logout();
                	}
                    System.out.println("Goodbye, have a good day!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid input.\n");
            }
        }
    }   
}
