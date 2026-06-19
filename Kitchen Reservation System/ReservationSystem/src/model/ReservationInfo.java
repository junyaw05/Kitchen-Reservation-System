package model;

public class ReservationInfo {
	private String reservationId;
    private String customerUsername;
    private String kitchenId;
    private String date;
    private String Starttime;
    private String Endtime;
    private String ContactNo;
    private String status;
    private String slot;
    private String payment;
    
    //Reservation Info Constructor
    public ReservationInfo(String reservationId, String kitchenId, String customerUsername, String date, String Starttime, String Endtime, String ContactNo, String status, String payment) {
    	this.reservationId = reservationId;
    	this.customerUsername = customerUsername;
    	this.kitchenId = kitchenId;
    	this.date = date;
    	this.Starttime = Starttime;
    	this.Endtime = Endtime;
    	this.ContactNo = ContactNo;
    	this.status = status;
    	this.payment = payment;
    }
    
    //Reservation Info Constructor (Polymorphism)
	public ReservationInfo(String customerName,String slot) {
		this.slot = slot;
		this.customerUsername = customerName;
	}
    
	//Accessor
    public String getReservationId() { 
    	return reservationId; 
    }
    
    public String getCustomerUsername() { 
    	return customerUsername; 
    }
    
    public String getKitchenId() { 
    	return kitchenId; 
    }
    
    public String getDate() { 
    	return date; 
    }
    
    public String getStartTime() { 
    	return Starttime; 
    }
    
    public String getEndTime() { 
    	return Endtime; 
    }
    
    public String getContactNo() {
    	return ContactNo;
    }
    
	public String getSlot()
	{
		return slot;
	}
	
	public String getPayment()
	{
		return payment;
	}
    
    public String getStatus() { 
    	return status; 
    }
    
    //Mutator
    public void setKitchenID(String kitchenId)
	{
		this.kitchenId = kitchenId;
	}
	
	public void setSlot(String slot)
	{
		this.slot = slot;
	}

	
	public void setDate(String date)
	{
		this.date = date;
	}
	
	public void setStatus(String status)
	{
		this.status = status;
	}

	public void setContactNo(String ContactNo) {
		this.ContactNo = ContactNo;
	}
	
	public void setStartTime(String Starttime) {
		this.Starttime = Starttime;
	}

	public void setEndTime(String Endtime) {
		this.Endtime = Endtime;
	}
    
	@Override
	//Return String representation of the Reservation Object and Display it
	public String toString()
	{
		return  "Booking ID   : " + reservationId + 
				"\nCustomer     : " + customerUsername +
				"\nKitchen ID   : " + kitchenId +
				"\nDate         : " + date + 
				"\nStart Time   : " + Starttime + 
				"\nEnd Time     : " + Endtime + 
				"\nPhone        : " + ContactNo + 
				"\nStatus       : " + status + 
				"\nTotal Payment: " + payment +
				"\n------------------------------------";
		
	}

	//Save Reservation Info into a text file
	public String toFileString() {
		return reservationId + "," + kitchenId + "," + customerUsername + "," + date + "," + Starttime + "," + Endtime + "," + ContactNo + "," + payment + "," + status;
	}
	
	//Display Reservation Time
	public String TimeFormat() {
		return Starttime + " - " + Endtime;
	}
}