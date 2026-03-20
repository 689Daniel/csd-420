/* Daniel Preller, 19 March 2026, Assignment 1
 * JavaFX program to display four random cards from a set of images and refresh them on a button click
 * Duplicate cards are allowed */

import java.io.File;
import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class RandomCards extends Application {
	Random randomGenerator = new Random();
	String[] cardImages = new File("cards").list();//Creates an array of all the card image files
	/* Jokers and card backs have been removed from the cards folder because the instructions implied
	 * they should not be there, but the array is dynamic and would still work with extra cards */
	
	@Override
	public void start(Stage primaryStage) {
		//Creates an array of four ImageViews using random cards and formats them
		ImageView[] cardViews = new ImageView[4];
		for (int card = 0; card < cardViews.length; card++) {
			cardViews[card] = new ImageView(generateCard());
			cardViews[card].setFitWidth(150);
			cardViews[card].setPreserveRatio(true);
		}
		
		//Creates an HBOX and adds the image views to it
		HBox cardBox = new HBox(10, cardViews);
		
		//Creates a refresh button that randomizes the cards
		Button refreshButton = new Button("Refresh");
		refreshButton.setOnAction(event -> {//Generates new cards for each imageView
			for (ImageView cardView: cardViews) {
				cardView.setImage(generateCard());
			}
		});
		
		//Border pane for the entire UI
		BorderPane mainPane = new BorderPane();
		mainPane.setCenter(cardBox);
		mainPane.setBottom(refreshButton);
		//Uses the static method and and the mainPane's get method to avoid warnings
		BorderPane.setAlignment(mainPane.getBottom(), Pos.CENTER);
		BorderPane.setMargin(mainPane.getCenter(), new Insets(0, 10, 0, 10));
		BorderPane.setMargin(mainPane.getBottom(), new Insets(10, 0, 10, 0));
		
		//Displays UI
		Scene scene = new Scene(mainPane);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Random Cards");
		primaryStage.show();
	}
	
	private Image generateCard() {//Returns an Image object with a random image from the cards folder
		String cardURL = "file:cards/" + cardImages[(randomGenerator.nextInt(cardImages.length - 1) + 1)];//Selects a random card image and prefixes with directory
		return new Image(cardURL);
	}

	public static void main(String[] args) {//Included for IDE compatibility
		launch();
	}
}
