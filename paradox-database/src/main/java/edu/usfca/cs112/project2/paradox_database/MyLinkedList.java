package edu.usfca.cs112.project2.idea_database;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

public class MyLinkedList<E extends Comparable<E>> implements Iterable<E>{

	int modCount;
	
//	
	Node<E> head;
	Node<E> last;
	int size;
	
	public MyLinkedList() {
		this.head = null;
		this.last = null;
		this.size = 0;
		this.modCount = 0;
	}
	
	public void add(E data) {
		Node<E> n = new Node<>(data);
		if(size == 0) {
			this.head = n;
			this.last = n;
		}else {
			this.last.setNext(n);
			this.last = n;
		}
		this.size++;
		this.modCount++;
	}
	
	public void printList () {
		Node<E> mover = head;
		if(mover == null) {
			System.out.println("This list is empty");
			return;
		}
		while(mover != null) {
			System.out.print(mover.getData() + " - ");
			mover = mover.getNext();
		}
	}
	
	public void insert(E data, int index) {
		Node<E> node = new Node<>(data);
		if(index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}if(index == 0) {
			node.setNext(this.head);
			this.head = node;
		}else {
			Node<E> mover = this.head;
			int count = 0;
			while(count < index-1) {
				mover = mover.getNext();
				count++;
			}
			node.setNext(mover.getNext());
			mover.setNext(node);
		}
		modCount++;
		size++;
	}
	
	
	public void remove(int index){
		Node<E> mover = this.head;
		int count = 0;
		if(index < 0 || index >= size){
		throw new IndexOutOfBoundsException();
		}
		if(index == 0) {
			head = head.getNext();
		}else if(index < size-1) {
			while(count < index-1) {
				mover = mover.getNext();
				count++;
			}mover.setNext(mover.getNext().getNext());
		}else {
			while(count < index-1) {
				mover = mover.getNext();
				count++;	
			}
			mover.setNext(null);
			this.last = mover;
			}
		modCount++;
		size--;
	}
	
	public E get(int i) {
	    if (i < 0 || i >= size) { // Ensure proper bounds check
	        throw new IndexOutOfBoundsException();
	    }
	    if (i == 0) {
	        return head.getData(); // Return the head's data for index 0
	    }
	    Node<E> mover = head;
	    for (int index = 0; index < i; index++) {
	        mover = mover.getNext(); 
	    }
	    return mover.getData();
	}
	
	public int getModCount() {
		return this.modCount;
	}


	public boolean contains(E findMe) {
	    Node<E> mover = head;
	    while (mover != null) { 
	        if (mover.getData().equals(findMe)) { 
	            return true; 
	        }
	        mover = mover.getNext(); 
	    }
	    return false; 
	}
	
	public int size() {
		return this.size;
	}
	
	public void clear() {
	    head = null;
	    last = null;
	    size = 0;
	    modCount++;
	}
	
	@Override
	public Iterator<E> iterator() {
		return new MyListIterator<E>(this);
	}
	
	private class Compare<T extends Comparable<T>>{
		Comparator<T> comp;
		public Compare(Comparator<T> comp) {
			this.comp = comp;
		}
		public int compareT(T o1, T o2) {
			if(this.comp == null) {
				return o1.compareTo(o2);
			}else {
				return comp.compare(o1, o2);
			}
		}
	}
	

	
	public void sort(MyLinkedList<E> mainLinkedList) {
		Compare<E> compare = new Compare<>(null);
		mergeSort(mainLinkedList, compare);
	}
	
	public void sort(Comparator<E> comp, MyLinkedList<E> mainLinkedList) {
		Compare<E> compare = new Compare<>(comp);
		mergeSort(mainLinkedList, compare);
	}
	
	public void mergeSort(MyLinkedList<E> mainLinkedList, Compare<E> compare) {
	    if (mainLinkedList.size() <= 1) {
	        return;    
	    }
	    int middle = mainLinkedList.size() / 2;
	    MyLinkedList<E> leftLinkedList = new MyLinkedList<>();
	    MyLinkedList<E> rightLinkedList = new MyLinkedList<>();
	    Node<E> current = mainLinkedList.head;

	    for (int i = 0; current != null; i++, current = current.getNext()) {
	        if (i < middle) {
	            leftLinkedList.add(current.getData());
	        } else {
	            rightLinkedList.add(current.getData());
	        }
	    }

	    mergeSort(leftLinkedList, compare);
	    mergeSort(rightLinkedList, compare);
	    merge(leftLinkedList, rightLinkedList, mainLinkedList, compare);
	}
	
	public void merge(MyLinkedList<E> leftLinkedList, MyLinkedList<E> rightLinkedList, MyLinkedList<E> mainLinkedList, Compare<E> compare) {
	    mainLinkedList.clear();
	    Node<E> leftNode = leftLinkedList.head;
	    Node<E> rightNode = rightLinkedList.head;
	    while (leftNode != null && rightNode != null) {
	        if (compare.compareT(leftNode.getData(), rightNode.getData()) <= 0) {
	            mainLinkedList.add(leftNode.getData());
	            leftNode = leftNode.getNext();
	        } else {
	            mainLinkedList.add(rightNode.getData());
	            rightNode = rightNode.getNext();
	        }
	    }

	    while (leftNode != null) {
	        mainLinkedList.add(leftNode.getData());
	        leftNode = leftNode.getNext();
	    }

	    while (rightNode != null) {
	        mainLinkedList.add(rightNode.getData());
	        rightNode = rightNode.getNext();
	    }
	}

	


}
	
