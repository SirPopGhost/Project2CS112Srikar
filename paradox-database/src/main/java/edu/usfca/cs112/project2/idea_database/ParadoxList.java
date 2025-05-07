package edu.usfca.cs112.project2.idea_database;


import java.util.Comparator;
import java.util.List;
import java.sql.*;
public class ParadoxList {
	
	private MyLinkedList<Paradox> paradoxes;
	
	public ParadoxList() {
		paradoxes = new MyLinkedList<>();
	}
	
	public void getMyList() {
	    paradoxes.printList();;
	}
	
	public void saveToDatabase() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/paradox.db");
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO paradox (FirstName, LastName, Month, Day, Year, Paradox, Rating) VALUES (?, ?, ?, ?, ?, ?, ?)");
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("DELETE FROM paradox");
			for(Paradox p : paradoxes) {
				pstmt.setString(1, p.getPerson().getFirstName());
				pstmt.setString(2, p.getPerson().getLastName());
				pstmt.setString(3, p.getDate().getMonth());
				pstmt.setString(4, p.getDate().getDay());
				pstmt.setString(5, p.getDate().getYear());
				pstmt.setString(6, p.getParadox());
				pstmt.setInt(7, p.getRating());
				pstmt.executeUpdate();
			}
			pstmt.close();
			conn.close();
		}catch (SQLException e) {
			System.out.println(e);		
			}
	}
	
	public void loadFromDatabase() {
		try {
			Connection conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/paradox.db");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM paradox");
			while(rs.next()) {
				String fname = rs.getString("FirstName");
				String lname = rs.getString("LastName");
				String month = rs.getString("Month");
				String day = rs.getString("Day");
				String year = rs.getString("Year");
				String paradox = rs.getString("Paradox");
				int rating = rs.getInt("Rating");
				Paradox p = new Paradox(paradox, new Person(fname, lname), new Date(month, day, year));
				p.setRating(rating);
				paradoxes.add(p);
				
			}
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void addParadox(Paradox paradox) {
	    paradoxes.add(paradox);
	}
	public int length() {
		return paradoxes.size;
	}
	
	public void removeParadox(int index) {
		paradoxes.remove(index);
	}
	
	public void addRatingList(int index, int rating) {
		Paradox e = (Paradox)paradoxes.get(index-1);
		e.setRating(rating);
	}
	
	public int getSize() {
		return paradoxes.size;
	}
	
	public Paradox getParadox(int i) {
		return paradoxes.get(i);
	}
	
	
    public void printParadoxes() {
    	int count = 1;
        for (Paradox paradox : paradoxes) {
            System.out.println(count + ". " + paradox.toString());
            count++;
            }
    }

    public void sortRatings() {
    	paradoxes.sort(paradoxes);
    }
    
    public void sortParadoxesInAlphabeticalOrder() {
    	paradoxes.sort(new ParadoxesAlphabetComparator(), paradoxes);
    }

    

}
