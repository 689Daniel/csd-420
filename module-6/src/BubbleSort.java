/* Daniel Preller, 20 April 2026, Assignment 6
 * Java program to implement bubble sort algorithm using both the Comparable and Comparator interface,
 * including test code */

import java.util.Comparator;

public class BubbleSort {

	public static void main(String[] args) {
		//Test code to demonstrate sorting with both methods of bubble sort
		Integer[] integerTestArray = {5, 9, 27, 4, 3, 15, 2, 7, 8, 1, 14, 92, 36, 11, 47, 8, 79, 6};
		Double[] doubleTestArray = {1.7, 8.6, 9.3, 4.2, 8.8, 1.4, 5.3, 9.7, 8.1, 2.4, 5.3, 6.1, 1.1, 9.2};
		
		System.out.println("Integer array before sorting:");
		printArray(integerTestArray);
		bubbleSort(integerTestArray);
		System.out.println("\nInteger array after sorting:");
		printArray(integerTestArray);
		
		System.out.println("\n\nDouble array before sorting:");
		printArray(doubleTestArray);
		bubbleSort(doubleTestArray, new DoubleComparator());//Uses the comparator overloaded version of the bubbleSort() method
		System.out.println("\nDouble array after sorting:");
		printArray(doubleTestArray);
	}
	
	public static <T extends Comparable<T>> T[] bubbleSort(T[] array) {//Performs bubble sort on comparable objects
		T temp;
		boolean swapped = true;
		
		for (int i = 0; i < array.length && swapped; i++) {//Iterates through the array
			swapped = false;//If no swaps occur, then the array is already sorted, and execution ends prematurely
			for (int j = 0; j < array.length - (i + 1); j++) {//Iterates through every item in the array except the last item and all items that have been sorted already
				if (array[j].compareTo(array[j + 1]) > 0) {//Swaps the next value with the current value if the current value is larger
					temp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = temp;
					swapped = true;
				}
			}
		}
		return array;
	}
	
	public static <T> T[] bubbleSort(T[] array, Comparator<T> comparator) {//Performs bubble sort using comparable objects
		T temp;
		boolean swapped = true;
		
			for (int i = 0; i < array.length && swapped; i++) {//Iterates through the array
				swapped = false;//If no swaps occur, then the array is already sorted, and execution ends prematurely
				for (int j = 0; j < array.length - (i + 1); j++) {//Iterates through every item in the array except the last item and all items that have been sorted already
					if (comparator.compare(array[j], array[j + 1]) > 0) {//Swaps the next value with the current value if the current value is larger
						temp = array[j];
						array[j] = array[j + 1];
						array[j + 1] = temp;
						swapped = true;
					}
				}
			}
		return array;
	}
	
	public static <T> void printArray(T[] array) {//Prints the contents of an array with each item on a new line
		for (T i : array) {
			System.out.println(i);
		}
	}

}

//A Comparator for Double objects
//Directly uses the Double.compareTo() method, making it pointless as a real comparator but simple for demonstration
class DoubleComparator implements Comparator<Double> {
	@Override
	public int compare(Double value1, Double value2) {
		return value1.compareTo(value2);
	}
}
