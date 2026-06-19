package model;

public class ReportInfo {
	private int year;
	private int month;
	
	//ReportInfo Constructor.
	public ReportInfo(int year, int month) {
		this.year = year;
		this.month = month;
	}

	//Year Accessor
	public int getYear() {
		return year;
	}
	
	//Month Accessor
	public int getMonth() {
		return month;
	}
}