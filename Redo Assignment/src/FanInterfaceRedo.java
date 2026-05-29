/* Daniel Preller, 28 May 2026, Assignment 10 Redo
 * JavaFX program that displays fans from a database and allows their information to be updated 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.UnaryOperator;
import javafx.application.Application;
import javafx.util.converter.IntegerStringConverter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FanInterfaceRedo extends Application {	
	final String DATABASE_NAME = "databasedb";
	final String USERNAME = "student1";
	final String PASSWORD = "pass";
	
	// Declarations of members required in other methods
	Connection connection;
	
	TextField firstNameField;
	TextField lastNameField;
	TextField teamField;
	
	Label errorLabel;
	
	int currentID;
	
	TextField idInput;
	
	@Override
	public void start(Stage primaryStage) {

		try {// Connects to the database or closes the program with an error message
			connectToDatabase();
		} catch (Exception e) {
			System.out.print("Unable to connect to database");
			System.exit(1);
		}
		
		// The main containing window
		GridPane mainWindow = new GridPane();
		mainWindow.setAlignment(Pos.CENTER);
		
		int columnWidth = 200;// The size of each column in the grid
		
		mainWindow.setMaxWidth(columnWidth * 3);
		mainWindow.setPadding(new Insets(0, 20, 0, 20));
		
		// Instructions displayed at the top of the window
		String instructions1 = "To find information about a fan, enter the ID and click 'display' or press 'enter'\n";
		String instructions2 = "To change a fan's information, update the displayed information and click 'Update'";
		
		Label instructionsLabel1 = new Label(instructions1);
		Label instructionsLabel2 = new Label(instructions2);
		
		instructionsLabel1.setAlignment(Pos.CENTER_LEFT);
		instructionsLabel2.setAlignment(Pos.CENTER_LEFT);
		
		instructionsLabel1.setMaxWidth(columnWidth * 3);
		instructionsLabel2.setMaxWidth(columnWidth * 3);
		
		mainWindow.add(instructionsLabel1, 0, 0, 3, 1);
		mainWindow.add(instructionsLabel2, 0, 1, 3, 1);
		
		instructionsLabel2.setPadding(new Insets(0, 0, 20, 0));
		
		// Text Fields and Labels for showing and updating fan information
		firstNameField = new TextField();
		lastNameField = new TextField();
		teamField = new TextField();
		
		firstNameField.setMaxWidth(columnWidth);
		lastNameField.setMaxWidth(columnWidth);
		teamField.setMaxWidth(columnWidth);
		
		Label firstNameLabel = new Label("First Name");
		Label lastNameLabel = new Label("Last Name");
		Label teamLabel = new Label("Team");
		
		firstNameLabel.setLabelFor(firstNameField);
		lastNameLabel.setLabelFor(lastNameField);
		teamLabel.setLabelFor(teamField);
		
		firstNameLabel.setMaxWidth(columnWidth);
		lastNameLabel.setMaxWidth(columnWidth);
		teamLabel.setMaxWidth(columnWidth);
		
		firstNameLabel.setAlignment(Pos.CENTER);
		lastNameLabel.setAlignment(Pos.CENTER);
		teamLabel.setAlignment(Pos.CENTER);
		
		mainWindow.add(firstNameLabel, 0, 2);
		mainWindow.add(lastNameLabel, 1, 2);
		mainWindow.add(teamLabel, 2, 2);
		mainWindow.add(firstNameField, 0, 3);
		mainWindow.add(lastNameField, 1, 3);
		mainWindow.add(teamField, 2, 3);
		
		// Label for displaying error messages
		errorLabel = new Label();
		errorLabel.setMaxWidth(columnWidth * 3);
		errorLabel.setAlignment(Pos.CENTER);
		mainWindow.add(errorLabel,  0,  4, 3, 1);
		
		// HBox for storing primary controls
		HBox controlsBox = new HBox(10);
		controlsBox.setAlignment(Pos.CENTER);
		controlsBox.setPrefHeight(50);
		controlsBox.setMaxWidth(columnWidth * 3);
		
		// Label and text field used to enter an ID
		Label idLabel = new Label("ID");
		idLabel.setLabelFor(idInput);
		idInput = new TextField();
		idInput.setPrefColumnCount(2);
		
		// Only allows the text field to take integers
		idInput.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), null, new IntegerFilter()));
		
		// Allows the database to be queried by pressing 'enter' in addition to the button
		idInput.setOnKeyPressed(e -> {if (e.getCode() == KeyCode.ENTER) {queryFromInput();}});
		
		// Button to display results of selected ID query
		Button displayButton = new Button("Display");
		displayButton.setOnAction(e -> queryFromInput());
		
		// Button to update the database with the entered data
		Button updateButton = new Button("Update");
		updateButton.setOnAction(e -> updateFromInput());
		
		// Displays controls
		controlsBox.getChildren().addAll(idLabel, idInput, displayButton, updateButton);
		mainWindow.add(controlsBox, 0, 5, 3, 1);
		
		// Displays the scene
		Scene scene = new Scene(mainWindow);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Fans Database");
		idInput.requestFocus();
		primaryStage.show();
	}

	public static void main(String[] args) {// required for IDE compatibility
		launch();
	}
	
	// Connects to the database
	private void connectToDatabase() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost", USERNAME, PASSWORD);
		Statement initialStatement = connection.createStatement();
		initialStatement.executeUpdate("USE " + DATABASE_NAME);
	}
	
	// Finds results in the database matching the selected ID
	private void queryDatabase(int id) {
		try  {
			PreparedStatement statement = connection.prepareStatement("SELECT firstname, lastname, favoriteteam FROM fans WHERE ID = ?");
			statement.setInt(1, id);
			ResultSet results = statement.executeQuery();
			
			// Displays the results of the one column returned
			results.next();
			firstNameField.setText(results.getString(1));
			lastNameField.setText(results.getString(2));
			teamField.setText(results.getString(3));
			
			// Sets the ID of the last successful query
			// Used for updating the database
			// A specific variable is used instead of the ID text field in case field is changed without displaying results
			currentID = id;
			
			errorLabel.setText("");
			
		} catch (Exception e) {// Displays an error message if an invalid ID is entered
			emptyResults();
			errorLabel.setText("Invalid ID");
		}
	}
	
	// Queries the database using the value in the text field
	private void queryFromInput() {
		String idString = idInput.getText();
		if (!idString.isEmpty()) {
			queryDatabase(Integer.parseInt(idInput.getText()));
		}
	}
	
	// Updates the database entry for the selected fan
	private void updateFan(String firstName, String lastName, String team, int id) {
		String queryString = "UPDATE fans SET firstname = ?, lastname = ?, favoriteteam = ? WHERE id = ?";
		try (PreparedStatement statement = connection.prepareStatement(queryString)) {
			statement.setString(1, firstName);
			statement.setString(2, lastName);
			statement.setString(3, team);
			statement.setInt(4, id);
			
			statement.executeUpdate();
			
		} catch (Exception e) {// Displays an error message if an error occurs
			emptyResults();
			errorLabel.setText("");
			
		}
	}
	
	// Updates the database from the currently displayed values, even if they have been modified
	private void updateFromInput() {
		updateFan(firstNameField.getText(), lastNameField.getText(), teamField.getText(), currentID);
	}
	
	// Empties the results
	private void emptyResults() {
		firstNameField.setText("");
		lastNameField.setText("");
		teamField.setText("");
	}
}

// Filter used to only allow integers
class IntegerFilter implements UnaryOperator<Change> {

	@Override
	public Change apply(Change change) {
		if (change.getControlNewText().matches("[0-9]*")) {
			return change;
		} else {
			return null;
		}
	}
}