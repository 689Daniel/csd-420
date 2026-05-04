/* Daniel Preller, 3 May 2026, Assignment 8
 * Java Program to generate random characters with multithreading and add them to a text area
 */

import java.util.Random;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class DanielThreeThreads extends Application{
	
	@Override
	public void start(Stage primaryStage) {
		
		StringBuffer outputText = new StringBuffer();// Stores generated characters
		
		// Displays output
		TextArea outputArea = new TextArea();
		outputArea.setWrapText(true);
		
		final int ITERATIONS = 10_000;
		
		//Creates and starts three threads, one for letters, one for numbers, and one for symbols
		Thread letterThread = new Thread(new RandomCharacterPrinter(ITERATIONS, 97, 123, outputText));
		Thread numberThread = new Thread(new RandomCharacterPrinter(ITERATIONS, 48, 58, outputText));
		Thread symbolThread = new Thread(new RandomCharacterPrinter(ITERATIONS, 33, 48, outputText));
		letterThread.start();
		numberThread.start();
		symbolThread.start();
		
		// Displays the scene
		Scene scene = new Scene(outputArea);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Randomly Generated Characters");
		primaryStage.show();
		
		/* Appends the output text to the text area
		The output text StringBuffer is necessary at a high number of iterations
		because JavaFX uses its own thread and will fail without using its own concurrency package.
		The JavaFX concurrency package is not used because the assignment instructions imply that the 
		Thread and Runnable class/interface should be used */
		outputArea.appendText(outputText.toString());
		
	}

	public static void main(String[] args) {// Required for IDE compatibility
		launch();
	}
}

class RandomCharacterPrinter implements Runnable {// A runnable class that generates characters in a given Unicode range
	int quantity;
	int minCharacter;
	int maxCharacter;// maxCharacter is not inclusive, which is based on the format of the Random class
	StringBuffer output;
	Random randomGenerator = new Random();
	
	// Basic constructor for all fields
	RandomCharacterPrinter(int quantity, int minCharacter, int maxCharacter, StringBuffer output) {
		this.quantity = quantity;
		this.minCharacter = minCharacter;
		this.maxCharacter = maxCharacter;
		this.output = output;
	}
	
	@Override
	public void run() {// Generates random characters in the provided range of Unicode characters according to the specified quantity
		for (int i = 1; i <= quantity; i++) {
			char newCharacter = (char) randomGenerator.nextInt(minCharacter, maxCharacter);
			output.append(newCharacter);
		}
	}
}