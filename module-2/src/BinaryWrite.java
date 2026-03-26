/* Daniel Preller, 26 March 2026, Assignment 2
 * Program to write arrays of random integers and doubles to a binary file
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Random;

public class BinaryWrite {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		File outputFile = new File("Daniel datafile.dat");
		
		int[] integers = new int[5];
		double[] doubles = new double[5];
		
		Random randomGenerator = new Random();
		
		//Populates arrays with random numbers from 1 - 99
		for (int i = 0; i < integers.length; i++) {
			integers[i] = randomGenerator.nextInt(1, 100);
		}
		for (int i = 0; i < doubles.length; i++) {
			doubles[i] = randomGenerator.nextInt(100, 10000) / 100.0;
			//Integers are generated and divided by 100.0 to create doubles with two decimals
		}
		
		if (!outputFile.exists()) {//If the file does not exist, creates it, adds the necessary stream header, and closes it
			ObjectOutputStream tempStream = new ObjectOutputStream(new FileOutputStream(outputFile));
			tempStream.close();
		}
		
		//Creates a new headerless object output stream and writes arrays to file
		try (HeaderlessObjectOutputStream output = new HeaderlessObjectOutputStream(new FileOutputStream(outputFile, true));) {
			output.writeObject(integers);
			output.writeObject(doubles);
		}
	}

}

/* HeaderlessObjectOutputStream class is an ObjectOutputStream that does not write stream headers
 * Using multiple ObjectOutputStream objects on the same file (which happens when the program is run multiple times)
 * creates a new stream header, which disrupts reading, so the headerless version is required to read the file
 * after the write program has run multiple times */
class HeaderlessObjectOutputStream extends ObjectOutputStream {
	public HeaderlessObjectOutputStream(OutputStream out) throws IOException {
		super(out);
	}
	
	@Override
	protected void writeStreamHeader() throws IOException {}//Prevents writing stream headers
}
