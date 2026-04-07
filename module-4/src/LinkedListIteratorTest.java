/* Daniel Preller, 6 April 2026, Assignment 4
 * Program to demonstrate the time taken to iterate through a LinkedList with an Iterator vs the get() method
*/

import java.util.Iterator;
import java.util.LinkedList;

public class LinkedListIteratorTest {

	public static void main(String[] args) {
		testTimes(50000);
		testTimes(500000);

	}
	
	public static void testTimes(int numberOfItems) {
		//Creates a list and populates it with integers
		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int i = 1; i <= numberOfItems; i++) {
			list.addLast(numberOfItems);
		}
		
		//Iterates through the list using an Iterator
		Iterator<Integer> iterator = list.iterator();
		long startTime = System.currentTimeMillis();
		while (iterator.hasNext()) {
			iterator.next();//Does nothing with the value from the list because the purpose is to demonstrate execution time
		}
		long endTime = System.currentTimeMillis();
		System.out.println("To iterate over " + numberOfItems + " items with an Iterator, it takes " + (endTime - startTime) + " milliseconds.");
		
		//Iterates through the list using the get() method
		startTime = System.currentTimeMillis();
		for (int i = 0; i < numberOfItems; i++) {
			list.get(i);//Does nothing with the value from the list because the purpose is to demonstrate execution time
		}
		endTime = System.currentTimeMillis();
		System.out.println("To iterate over " + numberOfItems + " items with the get() method, it takes " + (endTime - startTime) + " milliseconds.");
	}
}
/* On my initial test, iterating over 50,000 items took 1 millisecond with an iterator and 685 milliseconds with the get() method.
 * Iterating over 500,000 items took 4 milliseconds with an iterator and 73420 milliseconds (over a minute) with the get() method.
 * This clearly shows that the get() method is not efficient for the LinkedList class. This is because the get() method has to go
 * through the whole list of items each time until it reaches the specified index. This causes significant slowdowns. This also
 * implies that the get() method takes longer the higher the specified index is, as it has to go further through the list. When
 * using the get() method to iterate through every item in the list, this means it scales exponentially with the size of the list.
 * Thankfully, the Iterator object, and the equivalent foreach loop, can make iterating through every item in a Linked list a fast
 * process.
 */