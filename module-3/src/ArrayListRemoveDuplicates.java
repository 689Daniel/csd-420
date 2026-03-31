/* Daniel Preller, 31 March 2026, Assignment 3
 * Program to remove duplicate items from a list using generics, including a demonstration */

import java.util.ArrayList;
import java.util.Random;

public class ArrayListRemoveDuplicates {

	public static void main(String[] args) {
		Random randomGenerator = new Random();
		ArrayList<Integer> testList = new ArrayList<>();
		
		for (int i = 1; i <= 50; i ++) {//Populates list with 50 random values from 1 to 20
			testList.add(randomGenerator.nextInt(1, 21));
		}
		
		//Prints list
		System.out.println("Original List:");
		for (int i : testList) {
			System.out.println(i);
		}
		
		testList = removeDuplicates(testList);//Removes duplicates
		
		//Prints updated list
		System.out.println("\nList after removing duplicates:");
		for (int i : testList) {
			System.out.println(i);
		}

	}
	
	public static <E> ArrayList<E> removeDuplicates(ArrayList<E> list) {
		ArrayList<E> newList = new ArrayList<>();
		
		//For each element in the original list, adds the element to the new list if it is not already there
		for (E element : list) {
			if (!newList.contains(element)) {
				newList.add(element);
			}
		}
		
		return newList;
	}
}