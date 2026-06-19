package viewMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.time.LocalDate;

import main.Main;
import model.ReservationInfo;
import model.User;
import process.*;

public class CustomerMenu {
	//Customer Menu 
	public static void customerMenu(Scanner input, KitchenManager kitchenManager, CustomerManager cm, ReservationManager rm) {
		rm.UpdateStatus("reservation.txt");
		while(true) {
			UserMenu();
			String Choice = input.nextLine();

			switch (Choice) {
			case "1": //Create reservation
				BookingMenu(input);
				break;
				
			case "2": //Manage Reservation
				User user = UserManager.getCurrentUsername(); //get username from login
				String customerUserName = user.getUsername();
				String customerName = null;
				File file = new File("customer.txt");
				try(Scanner sc = new Scanner(file)){
					while(sc.hasNextLine()) {
						String line = sc.nextLine();
						String [] partList = line.split(",");
						if(customerUserName.equalsIgnoreCase(partList[1])) {
							customerName = partList[3];
							break;
						}
					}
				}catch(Exception e){
					System.out.println("Unexpected Error: " + e.getMessage());
				}
				
				System.out.println("Name: " + customerName);
				
				//View Customer Booking
				ReservationManager.viewBooking("reservation.txt",customerName);
				
				System.out.println("Enter Reservation ID to cancel (Q - quit):");
				String rID = input.nextLine().trim();
				if(rID.equalsIgnoreCase("Q")) {
					break;
				}
				else {
					ReservationManager.cancelBooking("reservation.txt",rID,customerName);
					break;
				}
			
			case"3":
				manageProfile(input,cm);
				break;
			case "0":
				return;

			default:
				System.out.println("Invalid Choice.");
			}
		}
	}
	
	public static void UserMenu() { //Dsiplay user menu
		System.out.println("\n┌──────────────────────────────────┐");
	    System.out.println("│            USER MENU             │");
	    System.out.println("├──────────────────────────────────┤");
	    System.out.println("│ 1. Book Kitchen                  │");
	    System.out.println("│ 2. Manage Reservations           │");
	    System.out.println("│ 3. Manage Profile                │");
	    System.out.println("│ 0. Logout                        │");
	    System.out.println("└──────────────────────────────────┘");
	    System.out.print("Select an option → ");
	}
	
	public static void manageProfile(Scanner input, CustomerManager cm) { //Manage Profile Menu
		User user = UserManager.getCurrentUsername();
		String username = user.getUsername();
		
		while (true) {
	        System.out.println("\n┌──────────────────────────────┐");
	        System.out.println("│        MANAGE PROFILE        │");
	        System.out.println("├──────────────────────────────┤");
	        System.out.println("│ 1. Update Password           │");
	        System.out.println("│ 2. Update Contact Number     │");
	        System.out.println("│ 0. Return to User Menu       │");
	        System.out.println("└──────────────────────────────┘");
	        System.out.print("Select an option → ");
	        String choice = input.nextLine();

	        switch (choice) {
	            case "1": //Edit Password
	                System.out.print("Enter new password: ");
	                String newPassword = input.nextLine();
	                if(newPassword.equalsIgnoreCase("Q")) {
	                	break;
	                }
	                else {
	                	cm.updatePassword(username, newPassword);
	                	break;
	                }
	                
	            case "2": //Edit Contact Number
	                System.out.print("Enter new contact number: ");
	                String newContact = input.nextLine();
	                cm.updateContact(username, newContact);
	                break;

	            case "0":
	                return; //Back to main menu

	            default:
	                System.out.println("Invalid choice. Try again.");
	        }
	    }
	}
	
	public static void BookingMenu(Scanner input){ //Create Reservation
		String Status = "Confirmed";
		
		String[] userdetail;
		userdetail = readUser();
		String CustomerName = userdetail[0];
		String PhoneNum = userdetail[1];
		System.out.println("\nCustomer Name : " + CustomerName);
		
		String date = null;
		String startTime = null;
		String endTime = null;
		String KitchenID = null;
		double rate = 0.0;
		int step = 0;
		
		do {
			try {
				KitchenManager.viewKitchen(); //Show kitchen list
				List<String> kitchenlst = new ArrayList<>();
				List<Double> kitchenRatelst = new ArrayList<>();
				int count = 0;
				File file = new File("kitchen.txt");
				Scanner sc = new Scanner(file);
				
				while(sc.hasNextLine()) {
					String line = sc.nextLine();
					String[] parts = line.split(",");
					kitchenlst.add(parts[0]);
					kitchenRatelst.add(Double.parseDouble(parts[3]));
					count ++;
				}
				
				System.out.print("Enter Your Booking Choice of Kitchen (Q - Quit): ");
				KitchenID = input.nextLine().toUpperCase(); //Input Kitchen ID to book
				
				if(KitchenID.equalsIgnoreCase("Q")) {
					return;
				}
				int index = kitchenlst.indexOf(KitchenID);
				if (index != -1) {
					rate = kitchenRatelst.get(index); //Match rate with ID
					break;
				} else {
					System.out.println("Kitchen ID not found.");
				}
				
			}catch(Exception e) {
				System.out.println("Error.");
			}
		}while(true);
		
		
		do { //User Input Date and Check Input Date is valid or not
			try {
				System.out.print("Enter Date (DD-MM-YYYY) (Q - Quit): ");
				
				String inputdate = input.nextLine();
				if(inputdate.equalsIgnoreCase("Q")){
					return;
				}
				else if(ReservationManager.isDateBeforeToday(inputdate)) {
					System.out.println("Invalid Date. The Date is Expired.");
					continue;
				}
				else {
					date = ReservationManager.FormatDate(inputdate);
					if(date == null) {
						continue;
					}
					KitchenManager.showAvailableSlots("reservation.txt",KitchenID,date,Status);
					step++;
					break;
				}
			} catch (Exception e) {
				System.out.println("Invalid Date.");
			}
		} while (true);

		do { // User input time to check time is valid or not
			try {
				System.out.print("Enter Start Time (HH:MM AM/PM) (Q - Quit): ");
				startTime = input.nextLine();
				if(startTime.equalsIgnoreCase("Q")) {
					return;
				}
				else {
					System.out.print("Enter End Time (HH:MM AM/PM): ");
					endTime = input.nextLine();
					ReservationManager.checkTime(startTime, endTime,KitchenID,date,Status);
					startTime = ReservationManager.FormatTime(startTime);
					endTime = ReservationManager.FormatTime(endTime);
					break;
				}
			} catch (Exception e) {
				System.out.println("An Unexpected Error: " + e.getMessage());
				System.out.println("Please try again....");
			}
		} while (true);

		while (true) {
			double TotalPay = getTotalPay(ReservationManager.getDurationHour(startTime,endTime),rate);
			BookingSummary(CustomerName,date,startTime,endTime,TotalPay,PhoneNum);
			String choice2 = input.nextLine();

			switch (choice2) {
			case "1": //Confirm Booking
				//Create reservation object
				ReservationInfo RInfo = new ReservationInfo(ReservationManager.generateReservationID(), KitchenID, CustomerName,date,startTime,endTime, PhoneNum, Status, String.valueOf(TotalPay));
				System.out.println("Booking confirmed!");
				ProceedPayment(ReservationManager.getDurationHour(startTime,endTime), rate, RInfo);

				return; // Exit after booking

			case "2": //Edit Booking Date or Time
				boolean EditOption = true;
				while (EditOption) {
					try {
						EditOption();
						String Choice3 = input.nextLine();
						switch (Choice3) {
						case "1": // Edit Booking Date
							while (true)
								try {
									System.out.println("Enter New Booking Date: ");
									String NewDate = ReservationManager.FormatDate(input.nextLine());
									if (NewDate != null) {
									    date = NewDate;
									    String[] timelist = EditTime(input,KitchenID,date,Status,startTime,endTime);
										startTime = timelist[0];
										endTime = timelist[1];
									    break;
									} else {
									    System.out.println("Invalid Date, please try again.");
									}
									break;
								} catch (Exception e) {
									System.out.println("Invalid Date");
								}
							break;

						case "2": // Edit Booking Time Slot
							while (true)
								try {
									String[] timelst = EditTime(input,KitchenID,date,Status,startTime,endTime);
									startTime = timelst[0];
									endTime = timelst[1];
									break;
								} catch (Exception e) {
									System.out.println("Invalid Time.");
								}
							break;
							
						case "3": // Cancell Edit
							EditOption = false;
							
						default:
							System.out.println("Invalid Option.");
						}

						break;
					} catch (Exception e) {
						System.out.println("Invalid Time Format.");
					}
				}
				break;

			case "3":
				System.out.println("Booking canceled.");
				return; // Exit Booking

			default:
				System.out.println("Invalid choice. Please select 1, 2 or 3.");
			}
		}
	}
	
	//Display Booking Summary Menu
	public static void BookingSummary(String CustomerName,String date,String StartTime,String EndTime,double TotalPay,String PhoneNum) {
		System.out.println("\n┌────────────────────────────────────────────┐");
		System.out.println("│              BOOKING CONFIRMATION          │");
		System.out.println("├────────────────────────────────────────────┤");
		System.out.printf ("│ %-15s: %-25s │\n", "Customer", CustomerName);
		System.out.printf ("│ %-15s: %-25s │\n", "Date", date);
		System.out.printf ("│ %-15s: %-25s │\n", "Time", StartTime + " - " + EndTime);
		System.out.printf ("│ %-15s: %-25s │\n", "Duration", ReservationManager.getDurationHour(StartTime,EndTime) + " hours");
		System.out.printf ("│ %-15s: RM%-23.2f │\n", "Total", TotalPay);
		System.out.printf ("│ %-15s: %-25s │\n", "Contact", PhoneNum);
		System.out.println("├────────────────────────────────────────────┤");
		System.out.println("│  1. CONFIRM   2. EDIT   3. CANCEL          │");
		System.out.println("└────────────────────────────────────────────┘");
		System.out.print("Your choice: ");
	}
	
	//Display Edit Options
	public static void EditOption() {
		System.out.println("\n┌──────────────────────────────┐");
		System.out.println("│         EDIT OPTIONS         │");
		System.out.println("├──────────────────────────────┤");
		System.out.println("│ 1. Edit Date                 │");
		System.out.println("│ 2. Edit Time Slot            │");
		System.out.println("│ 3. Return to Summary         │");
		System.out.println("└──────────────────────────────┘");
		System.out.print("Choose an option: ");
	}
	
	//Edit Time method 
	public static String[] EditTime(Scanner input, String KitchenID, String date, String Status, String startTime, String endTime) {
		while(true) {
			try {
				System.out.println("\n┌──────────────────────────────┐");
				System.out.println("│        ENTER NEW TIME        │");
				System.out.println("├──────────────────────────────┤");
				System.out.print("  Start Time (HH:mm): ");
				String NewStartTime = input.nextLine();
				System.out.print("  End Time (HH:mm):   ");
				String NewEndTime = input.nextLine();
				ReservationManager.checkTime(NewStartTime,NewEndTime,KitchenID,date,Status);
				startTime = NewStartTime;
				endTime = NewEndTime;
				return new String[] {startTime,endTime};
			}catch(Exception e) {
				System.out.println("Invalid time: " + e.getMessage());
			}
		}

	}
	
	//Payment menu
	public static void ProceedPayment(double DurationHour, double rate, ReservationInfo RInfo) {
		Scanner input = new Scanner(System.in);
		System.out.println("Payment");
		double totalPayment = DurationHour * rate;
		System.out.println("Your Total Payment is : RM " + totalPayment);
		System.out.println("1. Confirm Payment");
		System.out.println("2. Cancel Payment");
		System.out.print("Choice: ");
		String PaymentChoise = input.nextLine();
		switch (PaymentChoise) {
		case "1": // Confirm Pay and Add Booking
			System.out.println("Payment Successfully");
			PrinterWriter("reservation.txt",RInfo); //Add Customer Reservation to reservation file
			return;

		case "2": //Cancel Payment
			System.out.println("Payment Failed.");
			return;
			
		default:
			System.out.println("Invalid Choice");
			return;
			}
		}
	
	//Write File Method
	public static void PrinterWriter(String FileName, ReservationInfo RInfo ) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(FileName,true));
			writer.println(RInfo.toFileString());
			writer.close();	
		}
		catch(IOException e) {
			System.out.println("Error :" + e.getMessage());
		}
	}
	
	//Calculate Total Fee to pay and return it
	public static double getTotalPay(double DurationHour,double rate) {
		return DurationHour * rate;
	}
	
	public static String[] readUser(){
		try {
			User user= UserManager.getCurrentUsername();
			File file = new File("customer.txt");
			Scanner sc = new Scanner(file);
			String username = user.getUsername();
			
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] details = line.split(",");
				if(username.equalsIgnoreCase(details[1])) {
					return new String[] {details[3],details[4]};
				}
			}
			return null;
		}catch(Exception e) {
			return null;
		}
	}
}