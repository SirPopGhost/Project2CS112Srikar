package edu.usfca.cs112.project2.idea_database;

public class Date {
	private String month;
	private String day;
	private String year;
	
	public Date(String month, String day, String year){
		this.month = month;
		this.day = day;
		this.year = year;
	}
	
	public String getYear() {
		return year;
	}
	
	public String getMonth() {
		return month;
	}
	
	public String getDay() {
		return day;
	}
	
	public String toString() {
		return "The date this user submitted the paradox is " + month + "/" + day + "/" + year + ". ";
	}
}
