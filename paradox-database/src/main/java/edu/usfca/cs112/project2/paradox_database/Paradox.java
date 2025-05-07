package edu.usfca.cs112.project2.idea_database;

public class Paradox implements Comparable<Paradox>{
	private Date date;
	private Person person;
	private String paradox;
	private int rating;
	
	public Paradox(String paradox, Person person, Date date) {
		this.paradox = paradox;
		this.date = date;
		this.person = person;
		rating = 0;
	}
	
	public Person getPerson() {
		return person;
	}
	
	public Date getDate() {
		return date;
	}
	
	
	public String getParadox() {
		return paradox;
	}
	
	
	public int getRating() {
		return rating;
	}
	
	
	public void setRating(int user_rating) {
		rating = user_rating;
	}
	
	public String toString() {
		return person.toString() + date.toString() + "Their paradox is: " + paradox + ". Their total stars are: " + rating + ".";
	}

	@Override
	public int compareTo(Paradox o) {
		if(this.rating < o.getRating()) {
			return 1;
		}else if(this.rating > o.getRating()) {
			return -1;
		}else {
			return 0;
		}
	}

}
