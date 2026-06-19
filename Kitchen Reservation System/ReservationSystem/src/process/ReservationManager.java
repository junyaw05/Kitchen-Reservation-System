package process;

import java.util.*;
import java.io.*;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

import model.ReservationInfo;
import model.User;

public class ReservationManager {
	private static DateTimeFormatter timeformatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern("hh:mm a").toFormatter();
	private static DateTimeFormatter Format12Hr = DateTimeFormatter.ofPattern("hh:mm a");
	private static final LocalTime Bussiness_OpenTime = LocalTime.of(9, 0);
	private static final LocalTime Bussiness_CloseTime = LocalTime.of(22, 0);
	private static DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private static String Date;
	
	//Display all reservation for admin view
	public void viewAllReservation(String filename) {
		try {
			File file = new File(filename);
			Scanner sc = new Scanner(file);
			
			System.out.println("===== All Reservation =====");
			
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] detail = line.split(",");
				
				if(detail.length == 9) {
					
					ReservationInfo r = new ReservationInfo(
	                        detail[0], // reservationId
	                        detail[1], // customerUsername
	                        detail[2], // kitchenId or date (depends on your file format)
	                        detail[3], // date
	                        detail[4], // Start time
	                        detail[5], // End time
	                        detail[6], // Contact Number
	                        detail[8],  // status
	                        detail[7]
	                    );
					System.out.println("Reservation ID: " + r.getReservationId());
                    System.out.println("Customer Name : " + r.getCustomerUsername());
                    System.out.println("Kitchen ID    : " + r.getKitchenId());
                    System.out.println("Date          : " + r.getDate());
                    System.out.println("Time          : " + r.getStartTime() + " - " + r.getEndTime());
                    System.out.println("Phone No.     : " + r.getContactNo());
                    System.out.println("Status        : " + r.getStatus());
                    System.out.println("------------------------------");
				}
			}
			sc.close();
		}catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

	}
	
	//Cancel Reservation method 
	public boolean cancelReservation(String id,String username) {
		boolean canceled = false;
		try {
			File file = new File("reservation.txt");
			Scanner sc = new Scanner(file);
			ArrayList<ReservationInfo> reservations = new ArrayList<>();
			
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				if(line.isEmpty()) {
					continue;
				}
				else {
					String[] detail = line.split(",");
					if(detail.length == 9) {
	                    ReservationInfo r = new ReservationInfo(
	                            detail[0],
	                            detail[1],
	                            detail[2],
	                            detail[3],
	                            detail[4],
	                            detail[5],
	                            detail[6],
	                            detail[8],
	                            detail[7]
	                     );
						if(r.getReservationId().equalsIgnoreCase(id) && r.getCustomerUsername().equalsIgnoreCase(username)) {
							if(!r.getStatus().equalsIgnoreCase("Cancelled")) {
								r.setStatus("Cancelled");
								canceled = true;
							}
						}
						reservations.add(r);
					}
				}
			}
			sc.close();
			
			FileWriter write = new FileWriter("reservation.txt");
			for(ReservationInfo r : reservations) {
				write.write(r.toFileString()+"\n");
			}
			write.close();
			return canceled;
		}catch(IOException e) {
			System.out.println("Error occured.");
			return false;
		}
	}
	
	//Display specific customer booking detail
	public static void viewBooking(String FileName, String customerName) {
		List<ReservationInfo> customerReservations = getCustomerReseravtions(FileName,customerName);
		if (customerReservations.isEmpty()) {
			System.out.println("No bookings found for " + customerName);
		} else
			System.out.println("Booking History for " + customerName);
		for (ReservationInfo r : customerReservations) {
			System.out.println(r);
		}
	}
	
	//Show booked time slot
	public void checkAvailableSlots(String FileName, String kitchenID, String date, String status) {
		KitchenManager.showAvailableSlots(FileName,kitchenID,date,status);
	}
	
	//get booked time slot with customer input date. Avoid time slow conflict
	public static List<ReservationInfo> BookedSlot(String FileName,String kitchenID, String date, String status) {
		List<ReservationInfo> AllReservation = getAllReservations(FileName);
		List<ReservationInfo> bookedSlot = new ArrayList<>();
		
		for(ReservationInfo reservations : AllReservation) {
			if(reservations.getKitchenId().equalsIgnoreCase(kitchenID) && reservations.getDate().equalsIgnoreCase(date) && reservations.getStatus().equalsIgnoreCase(status)){
				bookedSlot.add(reservations);
			}
		}
		
		return bookedSlot;
	}
	
	//Display booked slot to customer (e.g. 5-05-2025 09:00 AM - 10:00 PM Booked [Confirm])
	public static void displayBookedSlot(String FileName,String kitchenID, String date, String status) {
		List<ReservationInfo> bookedSlot = BookedSlot(FileName,kitchenID,date,status);
		System.out.println("=============Availablity Booking Slot=============");
		if(bookedSlot.isEmpty()) {
			System.out.println("--------------No booking on this day--------------");
		}
		else {
			for(ReservationInfo reservations : bookedSlot) {
				System.out.println(reservations.getDate() + "     " + reservations.TimeFormat() + " Booked [Confirm]");
			}
		}
	}
	
	//Validate user input time slot 
	public static boolean TimeSlotValidate(String FileName,String kitchenID, String date, String status,LocalTime StartTime,LocalTime EndTime) {
		List<ReservationInfo> bookedSlot = BookedSlot(FileName,kitchenID,date,status);
		for(ReservationInfo reservations : bookedSlot) { // Checked time slot with all reservations time slot
			LocalTime BookedStartTime = LocalTime.parse(reservations.getStartTime(),timeformatter);
			LocalTime BookedEndTime = LocalTime.parse(reservations.getEndTime(),timeformatter);
			if(StartTime.isBefore(BookedEndTime) && EndTime.isAfter(BookedStartTime)) {
	            return true; 
	        }
		}
		return false;
	}
	
	//Generate customer reservation id
	public static String generateReservationID() {
		try {
			int maxid = FindMaxReservationID();
			return FormatReservationID(maxid + 1);
		}
		catch(IOException e) {
			throw new RuntimeException("Failed to generate Reservation ID: " + e.getMessage());
		}	
	}
	
	//get lastest reservation id of customer.
	public static int FindMaxReservationID() throws IOException {
		//Get Customer Name
		User user = UserManager.getCurrentUsername(); 
		String customerUserName = user.getUsername();
		String customerName = null;
		File cfile = new File("customer.txt");
		try(Scanner sc = new Scanner(cfile)){
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
		File file = new File("reservation.txt");
		if(!file.exists()) {
			CreateNewFile();
			return 0;
		}
		
		//return max id of the customer
		int MaxID = getCustomerReseravtions("reservation.txt", customerName).size();
		return MaxID;
		
	}
	
	//format reservation id in a constant format
	private static String FormatReservationID(int ID) {
		String ReservationID = String.format("%03d", ID);
		//return formatted reservation id
		return ("R" + ReservationID);
	}
	
	//Create File method if file not exist.
	public static void CreateNewFile() throws IOException {
		File file = new File("reservation.txt");
		if(!file.exists()) {
			file.createNewFile();
		}
	}
	
	//Get all reservation and save into an arraylist
	public static List<ReservationInfo> getAllReservations(String FileName) {
		List<ReservationInfo> reservations = new ArrayList<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(FileName))){
			String line;
			while((line = reader.readLine()) != null ) {
				ReservationInfo reservation = parseReservation(line);
				if(reservation != null) {
					reservations.add(reservation);
				}
			}
		}catch(IOException e) {
			System.out.println("Reservation Error: " + e.getMessage());
		}
		
		return reservations;
	}
	
	//Parse All Reservation Info into an object.
	public static ReservationInfo parseReservation(String line) {
		try {
			String[] parts = line.split(",");
			if (parts.length < 9) {
	            return null;
	        }
			
			String reservationID = parts[0];
			String kitchenID = parts[1];
			String customerName = parts[2];
			String date =parts[3];
			String startTime = parts[4];
			String endTime = parts[5];
			String phoneNum = parts[6];
			String status = parts[8];
			String TotalPay = parts[7];
			
			//Return object
			return new ReservationInfo(reservationID, kitchenID, customerName, date, startTime, endTime, phoneNum,status,TotalPay);
		}
		catch(Exception e) {
			//Display error message.
			System.out.println("Object Error: " + e.getMessage());
			return null;
		}		
	}
	
	//Get a specific customer reservation 
	public static List<ReservationInfo> getCustomerReseravtions (String FileName,String CustomerName){
		List<ReservationInfo>  AllReservations = getAllReservations(FileName);
		List<ReservationInfo> CustomerReservations = new ArrayList<>();
		for(ReservationInfo reservations : AllReservations) {
			if(reservations.getCustomerUsername().equalsIgnoreCase(CustomerName)) {
				CustomerReservations.add(reservations);
			}
		}
		//return a arrraylist include the specific customer reservation. 
		return CustomerReservations;
	}

	//Format Date to a constant pattern
	public static String FormatDate(String inputDate) {
		try {
			LocalDate.parse(inputDate, dateformatter);
			return inputDate;
		} catch (DateTimeParseException e) {
			showerror(e);
			return null;
		}

	}

	//Display error message about date
	private static void showerror(DateTimeParseException e) {
		if ((e.getMessage()).contains("DayOfMonth")) {
			System.out.println("Invalid DAY it must be between (1-31)");
		} else if ((e.getMessage()).contains("MonthOfYear")) {
			System.out.println("Invalid MONTH it must be between (1-12)");
		}
		else {
			System.out.println("Invalid Input.");
		}
	}
	
	//Validate user input time is valid or invalid
	public static void checkTime(String StartTime, String EndTime, String KitchenID, String date, String Status) {
		LocalTime ParsedStartTime = ParseTime(StartTime);
		LocalTime ParsedEndTime = ParseTime(EndTime);

		//User input time slot not valid
		if(TimeSlotValidate("reservation.txt",KitchenID,date,Status,ParsedStartTime,ParsedEndTime)) {
			throw new IllegalArgumentException("Invalid Time Slot. The Time Slot is already Booked By other Customer.");
		}
		
		//Customer does not input the start time or end time
		if (ParsedStartTime == null || ParsedEndTime == null) {
			throw new IllegalArgumentException("Invalid Time Format.");
		}

		//Invalid time format
		if (!ReservationTimeValidate(ParsedStartTime, ParsedEndTime)) {
			throw new IllegalArgumentException("Invalid reservation time. Must be between business hours, and end after start.");
		}

		//Duration Hour of customer input is not valid.
		if (isValidDurationHour(ParsedStartTime, ParsedEndTime) == false) {
			throw new IllegalArgumentException("Invalid reservation time. Must be at least 1 Hr.");
		}
	}
	
	//Format time to a constant pattern
	public static String FormatTime(String Time) {
		String InputTime = Time.trim().toUpperCase();
		String inputTime = InputTime.replaceAll("(?i)(\\d{1,2}:\\d{2})(AM|PM)", "$1 $2");
		return inputTime;
	}
	
	//parse string time to localtime variable type
	private static LocalTime ParseTime(String Time) {
		try {
			
			return LocalTime.parse(FormatTime(Time), timeformatter);
		} catch (DateTimeException e) {
			showerror(e);
		}
		return null;
	}

	//Display error message of time validation.
	private static void showerror(DateTimeException e) {
		if ((e.getMessage()).contains("ClockHourOfAmPm")) { //Invalid Hour
			System.out.println("Invalid HOUR it must be between (1-12)");
		} else if ((e.getMessage()).contains("MinuteOfHour")) { //Invalid Minute
			System.out.println("Invalid MINUTE it must be between (0-59)");
		} else { //Invalid Time Format
			System.out.println(e.getMessage());
			System.out.println("Invalid Time Format. It must be HH:MM AM/PM");
		}
	}

	//Validate the reservation time
	private static boolean ReservationTimeValidate(LocalTime StartTime, LocalTime EndTime) {
		if (StartTime.isBefore(Bussiness_OpenTime) || EndTime.isAfter(Bussiness_CloseTime)) { //if the customer input time slot is between the bussiness hour
			System.out.println(
					"Invalid Start Time. Our Bussiness hour is " + Bussiness_OpenTime + " - " + Bussiness_CloseTime);
			return false;
		} else {
			return !EndTime.isBefore(StartTime);
		}
	}

	//Validate duration hour of user input time slot
	public static boolean isValidDurationHour(LocalTime StartTime, LocalTime EndTime) {
		if (StartTime != null && EndTime != null) {
			Duration duration = Duration.between(StartTime, EndTime);
			double DurationTime = duration.toMinutes() / 60.0;
			if (DurationTime < 1.0) { //duration hour less than 1 hr
				return false;
			} else {
				return true;
			}
		} else {
			throw new IllegalStateException("Start Time or End Time are not set.");
		}

	}

	//Calculate the duration hour of user input time slot
	public static double getDurationHour(String StartTime, String EndTime) {
		if (StartTime != null && EndTime != null) {
			Duration duration = Duration.between(ParseTime(StartTime), ParseTime(EndTime));
			return duration.toMinutes() / 60.0;
		} else {
			throw new IllegalStateException("Start Time or End Time are not set.");
		}
	}
	
	//Update customer reservation status to expired
	public static void UpdateStatus(String FileName) {
		List<ReservationInfo>  AllReservations = getAllReservations(FileName);
		for(ReservationInfo reservations : AllReservations) {
			if(isDateBeforeToday(reservations.getDate())) { //Check reservation date is before today date or not
				reservations.setStatus("Expired");
			}
		}
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(FileName))){
			for(ReservationInfo reservations : AllReservations) {
				writer.write(reservations.toFileString());
				writer.newLine();
			}
			writer.close();
		}catch(IOException e) {
			System.out.println("Update Status Error: " + e.getMessage());
		}
	}
	
	//Validate the user input date is before the today date
	public static boolean isDateBeforeToday(String InputDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDate inputDate = LocalDate.parse(InputDate,formatter);
		LocalDate Today = LocalDate.now();
		
		return inputDate.isBefore(Today);
		
	}
	
	//Cancell booking method (Change the reservation status to "Cancell")
	public static void cancelBooking(String fileName, String reservationID,String customerName) {
		List<ReservationInfo>  AllReservations = getAllReservations(fileName);
		boolean found = false;
		
		for(ReservationInfo r : AllReservations)
		{
			if(r.getReservationId().equalsIgnoreCase(reservationID) && r.getCustomerUsername().equalsIgnoreCase(customerName))
			{
				found = true;
				if(r.getStatus().equalsIgnoreCase("Confirmed"))
				{
					r.setStatus("Cancelled");
					System.out.println("Booking " + r.getReservationId() + " for " + r.getCustomerUsername()+ " is cancelled.");
					
				}
				else {
						System.out.println("Booking " + r.getReservationId() +" for " +r.getCustomerUsername() + " cannot be cancelled.");
				}
				break;
				
			}
		}
		if(!found)
		{
			System.out.println("Reservation ID " + reservationID + " is not found.");
		}
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)))
		{
			for(ReservationInfo r : AllReservations)
			{
				writer.write(r.toFileString());
				writer.newLine();
			}
		}catch(IOException e)
		{
			System.out.println("Cancel Booking Error: "+ e.getMessage());
		}

	}
}