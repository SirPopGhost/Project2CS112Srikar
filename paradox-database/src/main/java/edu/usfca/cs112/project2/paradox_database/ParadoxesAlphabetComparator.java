package edu.usfca.cs112.project2.idea_database;
import java.util.*;

public class ParadoxesAlphabetComparator implements Comparator<Paradox>{

	@Override
	public int compare(Paradox o1, Paradox o2) {
		return o1.getParadox().compareTo(o2.getParadox());
	}

}
