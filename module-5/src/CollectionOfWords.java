/* Daniel Preller, 13 April, Assignment 5
 * Program to print each word of a file in ascending then descending order using a set.
 * All words in the example file are real words and can be found in the Merriam-Webster dictionary
 * https://www.merriam-webster.com */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class CollectionOfWords {

	public static void main(String[] args) throws FileNotFoundException {
		String fileName = "collection_of_words.txt";//File name declared as String to make it easy to change
		
		//Creates a file from the file name, closing if the file does not exist
		File inputFile = new File(fileName);
		if (!inputFile.exists()) {
			System.out.print("Error: File not found.");
			System.exit(1);
		}
		
		//Adds all words from the input file into a set
		TreeSet<String> wordSet = new TreeSet<>();
		try (Scanner input = new Scanner(inputFile);) {
			while (input.hasNext()) {
				wordSet.add(input.next());
			}
		}
		
		//Displays words from the set in ascending order
		System.out.println("Unique words in ascending order:");
		displayStringSet(wordSet);
		
		//Displays words from the set in descending order
		System.out.println("\nUnique words in descending order:");
		displayStringSet(wordSet.descendingSet());	
	}
	
	private static void displayStringSet(Set<String> set) {//Prints each String in a set on a new line
		for (String string : set) {
			System.out.println(string);
		}
	}
}
