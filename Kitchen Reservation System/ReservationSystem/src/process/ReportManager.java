package process;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import model.*;

public class ReportManager { 
	//Generate monthly Report method
	public void generateMonthlyReport(int year, int month) {
		ReportInfo monthlyReport = new ReportInfo(year,month);
		generateReport(monthlyReport);
	}
	
	//Generate yearly report method
	public void generateYearlyReport(int year) {
		ReportInfo yearlyReport = new ReportInfo(year,0);
		generateReport(yearlyReport);
	}
	
	//Generate report method
	private void generateReport(ReportInfo report) {
		int totalBooking = 0; //Define default booking is 0
		double totalRevenue = 0.0; //Define default revenue is 0
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		try{
			if(report.getMonth() == 0) {
				System.out.printf("================ REPORT FOR %s ===============\n",report.getYear());
			}
			else {
				System.out.printf("============ REPORT FOR %s %s ============\n",Month.of(report.getMonth()),report.getYear());
			}
			
			ArrayList<KitchenInfo> kitchenlst = readKitchen();
			ArrayList<String[]> reservelst = readReservation();
			
			for(int i=0; i<kitchenlst.size(); i++) {
				int booking = 0;
				double revenue = 0.0;
				String kitchenID = kitchenlst.get(i).getKitchenID();
				
				//Check Kitchen ID to get total revenue for different kitchen
				for(int z=0; z<reservelst.size();z++) {
					String[] detail = reservelst.get(z);
					String resKitchenID = detail[0];
					String datestr = detail[1];
					String status = detail[2];
					double amount = Double.parseDouble(detail[3]);
					LocalDate date = LocalDate.parse(datestr,dtf);
					boolean match = false;

							
					if(report.getMonth() == 0) {
						if(date.getYear() == report.getYear()) {
							match = true;
						}
					}
					else {
						if(date.getYear() == report.getYear() && date.getMonthValue() == report.getMonth()) {
							match = true;
						}
					}
							
					if(match && (status.equalsIgnoreCase("Confirmed") || status.equalsIgnoreCase("Expired")) && resKitchenID.equalsIgnoreCase(kitchenID)) {
						booking ++;
						revenue += amount;
					}
				}
				
				System.out.printf("Kitchen %s --> Bookings: %d, Revenue: RM %.2f\n",kitchenID,booking,revenue);
				totalBooking += booking;
				totalRevenue += revenue;
			}
			
			//User Interface
			System.out.println("------------------------------------------------");
			System.out.println("Total Bookings: " + totalBooking);
            System.out.println("Total Revenue : RM " + totalRevenue);
            System.out.println();
		}catch(Exception e) {
			System.out.print("Error: " + e.getMessage());
			
		}
	}
	
	//read Kicthen info from text file
	public ArrayList<KitchenInfo> readKitchen() {
		ArrayList<KitchenInfo> kitchenlst = new ArrayList<KitchenInfo>();
		try {
			File file = new File("kitchen.txt");
			Scanner sc = new Scanner(file);
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				if(line.isEmpty()) {
					continue;
				}
				else {
					String[] parts = line.split(",");
					kitchenlst.add(new KitchenInfo(parts[0],parts[1],Integer.parseInt(parts[2]),Double.parseDouble(parts[3])));
				}
			}
			sc.close();
		}catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return kitchenlst;
	}
	
	//Read Reservation info from text file
	public ArrayList<String[]> readReservation(){
		ArrayList<String[]> reservelst = new ArrayList<>();
		try {
			File file = new File("reservation.txt");
			Scanner sc = new Scanner(file);
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				if(line.isEmpty()) {
					continue;
				}
				else {
					//Append Reservation Info into an ArrayList
					String[] parts = line.split(",");
					String[] newline = {parts[1],parts[3],parts[8],parts[7]};
					reservelst.add(newline);
				}
			}
			sc.close();
		}catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return reservelst;
	}
}