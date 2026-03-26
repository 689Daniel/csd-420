/* Daniel Preller, 26 March 2026, Assignment 2
 * Program to read arrays of integers and doubles from a binary file
 */

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class BinaryRead {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		String fileName = "Daniel datafile.dat";
		
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName));) {
			while (true) {//Executes until end of file causes an exception
				//Reads and displays an array of integers
				int[] integers = (int[]) input.readObject();
				for (int i = 0; i < integers.length; i++) {
					System.out.println(integers[i]);
				}
				System.out.println();
				
				//Reads and disiplays an array of doubles
				double[] doubles = (double[]) input.readObject();
				for (int i = 0; i < doubles.length; i++) {
					System.out.println(doubles[i]);
				}
				System.out.println();
			}
		} catch (EOFException exception) {}//On EOF, catch statement triggers and ends loop
	}
}
