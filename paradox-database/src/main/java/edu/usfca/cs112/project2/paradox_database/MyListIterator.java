package edu.usfca.cs112.project2.idea_database;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class MyListIterator<E extends Comparable<E>> implements Iterator<E>{

	MyLinkedList<E> ml;
	Object element;
	int index;
	int modCountCheck;
	
	public MyListIterator(MyLinkedList<E> list) {
		this.ml = list;
		this.element = this.ml.get(0);
		this.index = 0;
		this.modCountCheck = this.ml.getModCount();
	}
	
	
	@Override
	public boolean hasNext() {
		return this.element != null;	
	}

	@SuppressWarnings("unchecked")
	@Override
	public E next() {
		if(modCountCheck != this.ml.getModCount()) {
			throw new ConcurrentModificationException();
		}
		Object toReturn = this.element;
		index++;
		try {
			this.element = this.ml.get(index);
		}catch(IndexOutOfBoundsException e) {
			this.element = null;
		}
		return (E)toReturn;
	}
	
	public void remove() {
		ml.remove(index-1);
		this.modCountCheck++;
		index--;
	}

}