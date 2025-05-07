package edu.usfca.cs112.project2.idea_database;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;

public class App extends JFrame {
    private JButton addEntryButton;
    private JButton deleteEntryButton;
    private JButton sortByRatingButton;
    private JButton sortByParadoxAlphabeticallyButton;
    private JButton giveRatingButton;
    private JButton saveToParadoxDatabase;

    public App() {
        super("Paradox Database");
        ParadoxList paradoxes = new ParadoxList();
        paradoxes.loadFromDatabase();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 300); // Adjust frame size to be more compact
        setLayout(new BorderLayout());

        // Initialize the table
        DefaultTableModel model = initializeTable(paradoxes);
        JTable table = new JTable(model);


        table.getColumnModel().getColumn(3).setCellRenderer(new MultiLineTableCellRenderer());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        add(new JScrollPane(table), BorderLayout.CENTER); 

        // Panel for buttons with GridBagLayout
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); 

        addEntryButton = new JButton("Add paradox");
        addEntryButton.addActionListener(new AddEntryHandler(model, paradoxes));
        gbc.gridx = 0;
        gbc.gridy = 1;
        buttonPanel.add(addEntryButton, gbc);


        deleteEntryButton = new JButton("Delete paradox");
        deleteEntryButton.addActionListener(new DeleteEntryHandler(model, paradoxes, table));
        gbc.gridx = 0;
        gbc.gridy = 2;
        buttonPanel.add(deleteEntryButton, gbc);


        sortByRatingButton = new JButton("Sort rank numerically");
        sortByRatingButton.addActionListener(new SortNumerically(model, paradoxes));
        gbc.gridx = 1;
        gbc.gridy = 1;
        buttonPanel.add(sortByRatingButton, gbc);

        sortByParadoxAlphabeticallyButton = new JButton("Sort paradox alphabetically");
        sortByParadoxAlphabeticallyButton.addActionListener(new SortAlphabetically(model, paradoxes));
        gbc.gridx = 1;
        gbc.gridy = 2;
        buttonPanel.add(sortByParadoxAlphabeticallyButton, gbc);

        giveRatingButton = new JButton("Give rating");
        giveRatingButton.addActionListener(new GiveRating(model, paradoxes, table));
        gbc.gridx = 2;
        gbc.gridy = 1;
        buttonPanel.add(giveRatingButton, gbc);

        saveToParadoxDatabase = new JButton("Save to database");
        saveToParadoxDatabase.addActionListener(new SaveDatabase(paradoxes));
        gbc.gridx = 2;
        gbc.gridy = 2;
        buttonPanel.add(saveToParadoxDatabase, gbc);

        // Add the button panel to the frame
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private DefaultTableModel initializeTable(ParadoxList paradoxes) {
        String[] columnNames = {"FirstName", "LastName", "Last Added", "View Paradox", "Rating"};
        int columns = columnNames.length;
        int rows = paradoxes.length();
        String[][] data = new String[rows][columns];

        for (int i = 0; i < data.length; i++) {
            data[i][0] = paradoxes.getParadox(i).getPerson().getFirstName();
            data[i][1] = paradoxes.getParadox(i).getPerson().getLastName();
            data[i][2] = paradoxes.getParadox(i).getDate().getMonth() + "/" +
                         paradoxes.getParadox(i).getDate().getDay() + "/" +
                         paradoxes.getParadox(i).getDate().getYear();
            data[i][3] = paradoxes.getParadox(i).getParadox();
            data[i][4] = String.valueOf(paradoxes.getParadox(i).getRating());
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only allow editing in the "View Paradox" column
            }
        };
    }

    public static void main(String[] args) {
        App frame = new App();
        frame.setVisible(true);
    }


private class MultiLineTableCellRenderer extends JTextArea implements TableCellRenderer {
    public MultiLineTableCellRenderer() {
        setLineWrap(true);
        setWrapStyleWord(true);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        setText(value != null ? value.toString() : "");
        setFont(table.getFont());
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding
        setSize(table.getColumnModel().getColumn(column).getWidth(), Integer.MAX_VALUE);

        int height = getPreferredSize().height;
        if (table.getRowHeight(row) < height) {
            table.setRowHeight(row, height);
        }

        return this;
    }
}

private class AddEntryHandler implements ActionListener {
    private DefaultTableModel model;
    private ParadoxList paradoxes;

    public AddEntryHandler(DefaultTableModel model, ParadoxList paradoxes) {
        this.model = model;
        this.paradoxes = paradoxes;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean validInput = false;

        while (!validInput) {
            LocalDateTime now = LocalDateTime.now();
            JTextField firstNameField = new JTextField();
            JTextField lastNameField = new JTextField();
            JTextField paradoxField = new JTextField();

            Object[] message = {
                "First Name:", firstNameField,
                "Last Name:", lastNameField,
                "Paradox:", paradoxField,
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Add New Entry", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String paradox = paradoxField.getText();

                if (firstName.length() > 200 || lastName.length() > 200 || paradox.length() > 200) {
                    JOptionPane.showMessageDialog(null, 
                        "Each field must be 200 characters or fewer. Please re-enter your data.", 
                        "Input Too Long", 
                        JOptionPane.ERROR_MESSAGE);
                } else {
                    validInput = true; 

                    String date = now.getMonthValue() + "/" + now.getDayOfMonth() + "/" + now.getYear();
                    int rating = 0;

                    model.addRow(new Object[]{firstName, lastName, date, paradox, rating});

                    Paradox newParadox = new Paradox(paradox, new Person(firstName, lastName),
                        new Date(String.valueOf(now.getMonthValue()), String.valueOf(now.getDayOfMonth()), String.valueOf(now.getYear())));
                    paradoxes.addParadox(newParadox);
                }
            } else {
                break; // User canceled; exit the loop.
            }
        }
    }
}

private class DeleteEntryHandler implements ActionListener {
    private DefaultTableModel model;
    private ParadoxList paradoxes;
    private JTable table;

    public DeleteEntryHandler(DefaultTableModel model, ParadoxList paradoxes, JTable table) {
        this.model = model;
        this.paradoxes = paradoxes;
        this.table = table;
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        table.requestFocus();
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete this entry?",
                "Delete Confirmation",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                model.removeRow(selectedRow);
                paradoxes.removeParadox(selectedRow);

                JOptionPane.showMessageDialog(
                    null,
                    "Entry deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                null,
                "No entry selected. Please select an entry to delete.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

private class SortNumerically implements ActionListener{
    private DefaultTableModel model;
    private ParadoxList paradoxes;
	
	public SortNumerically(DefaultTableModel model, ParadoxList paradoxes) {
		this.model = model;
		this.paradoxes = paradoxes;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
	    paradoxes.sortRatings(); 

	    model.setRowCount(0);

	    // Repopulate the table model with the sorted data
	    for (int i = 0; i < paradoxes.length(); i++) {
	        Paradox p = paradoxes.getParadox(i);
	        String date = p.getDate().getMonth() + "/" + p.getDate().getDay() + "/" + p.getDate().getYear();
	        model.addRow(new Object[]{
	            p.getPerson().getFirstName(),
	            p.getPerson().getLastName(),
	            date,
	            p.getParadox(),
	            p.getRating()
	        });
	    }
	}
	
}

private class SortAlphabetically implements ActionListener{
    private DefaultTableModel model;
    private ParadoxList paradoxes;
	
	public SortAlphabetically(DefaultTableModel model, ParadoxList paradoxes) {
		this.model = model;
		this.paradoxes = paradoxes;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
	    paradoxes.sortParadoxesInAlphabeticalOrder();


	    model.setRowCount(0);

	    // Repopulate the table model with the sorted data
	    for (int i = 0; i < paradoxes.length(); i++) {
	        Paradox p = paradoxes.getParadox(i);
	        String date = p.getDate().getMonth() + "/" + p.getDate().getDay() + "/" + p.getDate().getYear();
	        model.addRow(new Object[]{
	            p.getPerson().getFirstName(),
	            p.getPerson().getLastName(),
	            date,
	            p.getParadox(),
	            p.getRating()
	        });
	    }
	}
	
}

private class GiveRating implements ActionListener{
    private DefaultTableModel model;
    private ParadoxList paradoxes;
    private JTable table;
	
	public GiveRating(DefaultTableModel model, ParadoxList paradoxes, JTable table) {
		this.model = model;
		this.paradoxes = paradoxes;
		this.table = table;
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
	    table.requestFocus();
	    int selectedRow = table.getSelectedRow();
	    if (selectedRow >= 0) {
	        String newRating = JOptionPane.showInputDialog(
	            null,
	            "Enter a rating (1 to 5) for the selected paradox:",
	            "Rate Paradox",
	            JOptionPane.QUESTION_MESSAGE
	        );

	        try {
	            int rating = Integer.parseInt(newRating);
	            if (rating >= 1 && rating <= 5) {
	                Paradox selectedParadox = paradoxes.getParadox(selectedRow);
	                int addRating = selectedParadox.getRating();
	                addRating += rating;
	                model.setValueAt(addRating, selectedRow, 4);
	                selectedParadox.setRating(addRating);

	                JOptionPane.showMessageDialog(
	                    null,
	                    "Rating updated successfully!",
	                    "Success",
	                    JOptionPane.INFORMATION_MESSAGE
	                );
	            } else {
	                JOptionPane.showMessageDialog(
	                    null,
	                    "Please enter a valid rating between 1 and 5.",
	                    "Invalid Input",
	                    JOptionPane.ERROR_MESSAGE
	                );
	            }
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(
	                null,
	                "Please enter a valid number for the rating.",
	                "Invalid Input",
	                JOptionPane.ERROR_MESSAGE
	            );
	        }
	    } else {
	        JOptionPane.showMessageDialog(
	            null,
	            "No entry selected. Please select a paradox to rate.",
	            "Error",
	            JOptionPane.ERROR_MESSAGE
	        );
	    }
	}
	
}

private class SaveDatabase implements ActionListener{
    private ParadoxList paradoxes;
	
	public SaveDatabase(ParadoxList paradoxes) {
		this.paradoxes = paradoxes;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		paradoxes.saveToDatabase();
        JOptionPane.showMessageDialog(
                null,
                "Saved to database!",
                "Saved",
                JOptionPane.INFORMATION_MESSAGE
            );
	}
	
}
}
    
// public class App{
//    public static void main(String[] args) {
//		Scanner scan = new Scanner(System.in);
//		ParadoxList paradoxes = new ParadoxList();
//		paradoxes.loadFromDatabase();
//		paradoxes.printParadoxes();
//		while(true) {
//	    	System.out.println("Enter number 1 to make your own paradox. ");  
//	    	System.out.println("Enter number 2 rate a user's paradox. "); 
//	    	System.out.println("Enter number 3 to compare paradoxes. ");  
//	    	System.out.println("Enter number 4 to remove a paradox. ");  
//	    	String choice = scan.nextLine();
//		    	if(choice.contains("1")) {
//		        System.out.println("Please enter your first and last name. ");
//		        String first = scan.nextLine();
//		        String last = scan.nextLine();
//		        LocalDateTime now = LocalDateTime.now();
//		        int year = now.getYear();
//		        int month = now.getMonthValue();
//		        int day = now.getDayOfMonth();
//		        System.out.println("Please type in a paradox that you think that is more confusing than all. ");
//		        String userParadox = scan.nextLine();
//		        Person person = new Person(first, last);
//		        Date date = new Date(String.valueOf(month) , String.valueOf(day), String.valueOf(year));
//		        Paradox paradox = new Paradox(userParadox, person, date);
//		        paradoxes.addParadox(paradox);
//	    	}else if(choice.contains("2")) {
//	    		System.out.println("Please choose a paradox to rate. ");
//	    		paradoxes.printParadoxes();
//	    		int user_input = scan.nextInt();
//	    		System.out.println("Please give the paradox a rating out of 5. ");
//	    		int user_rating = scan.nextInt();
//	    		paradoxes.addRatingList(user_input, user_rating);
//	    	}else if(choice.contains("3")) {
//	    		paradoxes.sortParadoxesInAlphabeticalOrder();	    		
//	    		paradoxes.printParadoxes();
//	    	}else if(choice.contains("4")) {
//	    		int what = scan.nextInt();
//	    		paradoxes.removeParadox(what);
//	    	}else if(choice.contains("5")) {
//	    		paradoxes.saveToDatabase();
//	    		break;
//	    	}
//		}   
//		scan.close();
//    }
//}

