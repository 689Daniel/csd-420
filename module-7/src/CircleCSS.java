/* Daniel Preller, 28 April 2026, Assignment 7
 * JavaFX program to display four circles with specific CSS applied
*/

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class CircleCSS extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		
		double circleSize = 100;// Used to set size of all circles simultaneously
		
		//Creates a plain circle
		Circle plainCircle = new Circle(circleSize);
		plainCircle.getStyleClass().add("plaincircle");
		
		//Creates a red circle
		Circle redCircle = new Circle(circleSize);
		redCircle.setId("redcircle");
		
		//Creates a green circle
		Circle greenCircle = new Circle(circleSize);
		greenCircle.setId("greencircle");
		
		//Creates a plain circle with borders
		Circle borderCircle = new Circle(circleSize);
		borderCircle.getStyleClass().addAll("plaincircle", "circleborder");
		
		//Creates an HBox to show all circles, and applies borders to it
		HBox circleWindow = new HBox();
		circleWindow.getChildren().addAll(plainCircle, redCircle, greenCircle, borderCircle);
		circleWindow.setSpacing(10);
		circleWindow.getStyleClass().add("border");
		circleWindow.setAlignment(Pos.CENTER);
		
		//Sets title and CSS and displays the scene
		Scene scene = new Scene(circleWindow);
		scene.getStylesheets().add("mystyle.css");
		primaryStage.setScene(scene);
		primaryStage.setTitle("Circle CSS");
		primaryStage.show();
	}

	public static void main(String[] args) {// Required for compatibility with certain IDEs
		launch();
	}
}