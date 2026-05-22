/* Daniel Preller, 21 May 2026, Assignment 10
 * JavaFX program that displays fans from a database and allows their information to be updated 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.UnaryOperator;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.converter.IntegerStringConverter;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FanInterface extends Application {	
	final String DATABASE_NAME = "databasedb";
	final String USERNAME = "student1";
	final String PASSWORD = "pass";
	
	// Declarations of members required in other methods
	Connection connection;
	
	TableView<Fan> resultsView = new TableView<>();
	
	ObservableList<Fan> observableFans;
	
	TableColumn<Fan, String> firstNameColumn;
	TableColumn<Fan, String> lastNameColumn;
	TableColumn<Fan, String> teamColumn;
	
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
		BorderPane mainWindow = new BorderPane();
		mainWindow.setPrefWidth(540);
		
		// Instructions displayed at the top of the window
		String instructions = "To find information about a fan, enter the ID and click 'dispaly' or press 'enter'\n"
				 + "To change a fan's information, update the displayed information, press 'enter', and click 'Update'";
		Label instructionLabel = new Label(instructions);
		instructionLabel.setAlignment(Pos.CENTER);
		instructionLabel.setPrefWidth(540);
		mainWindow.setTop(instructionLabel);
		
		// Columns of the table
		firstNameColumn = new TableColumn<>("First Name");
		lastNameColumn = new TableColumn<>("Last Name");
		teamColumn = new TableColumn<>("Team");
		
		// Sets the table columns to the appropriate fields (Assigned by the resultsView's observable
		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().getFirstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().getLastNameProperty());
		teamColumn.setCellValueFactory(cellData -> cellData.getValue().getTeamProperty());
		
		// Sets the table columns to editable text fields
		firstNameColumn.setCellFactory(TextFieldTableCell.<Fan>forTableColumn());
		lastNameColumn.setCellFactory(TextFieldTableCell.<Fan>forTableColumn());
		teamColumn.setCellFactory(TextFieldTableCell.<Fan>forTableColumn());
		
		// Sets table columns' width and makes them editable
		firstNameColumn.setPrefWidth(mainWindow.getPrefWidth() / 3 - 1);
		lastNameColumn.setPrefWidth(mainWindow.getPrefWidth() / 3 - 1);
		teamColumn.setPrefWidth(mainWindow.getPrefWidth() / 3 - 1);
		
		firstNameColumn.setEditable(true);
		lastNameColumn.setEditable(true);
		teamColumn.setEditable(true);
		
		// Sets the results table to empty
		emptyResultsList();
		
		// Adds the columns to the table
		resultsView.getColumns().setAll(firstNameColumn, lastNameColumn, teamColumn);
		
		
		// Puts the results in the center of the main window
		resultsView.setEditable(true);
		resultsView.setFixedCellSize(24);
		resultsView.setPrefHeight(50);
		mainWindow.setCenter(resultsView);
		
		// Default text to be displayed
		resultsView.setPlaceholder(new Label("Enter an ID to find information about a fan"));
		
		// HBox for storing primary controls
		HBox controlsBox = new HBox(10);
		controlsBox.setAlignment(Pos.CENTER);
		controlsBox.setPrefHeight(50);
		
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
		mainWindow.setBottom(controlsBox);
		
		// Displays the scene
		Scene scene = new Scene(mainWindow);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Fans Database");
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
			
			// Creates a new Fan object and sets the results display
			results.next();
			observableFans = FXCollections.observableArrayList(List.of(new Fan(results.getString(1), results.getString(2), results.getString(3))));
			resultsView.setItems(observableFans);
			
			// Sets the ID of the last successful query
			// Used for updating the database
			// A specific variable is used instead of the ID text field in case field is changed without displaying results
			currentID = id;
			
		} catch (Exception e) {// Displays an error message if an invalid ID is entered
			resultsView.setPlaceholder(new Label("Invalid ID"));
			emptyResultsList();
		}
	}
	
	// Queries the database using the value in the text field
	private void queryFromInput() {
		queryDatabase(Integer.parseInt(idInput.getText()));
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
			resultsView.setPlaceholder(new Label("An Error Occurred"));
			emptyResultsList();
		}
	}
	
	// Updates the database from the currently displayed values, even if they have been modified
	private void updateFromInput() {
		updateFan(firstNameColumn.getCellData(0), lastNameColumn.getCellData(0), teamColumn.getCellData(0), currentID);
	}
	
	// Sets the results to empty, which displays the current placeholder of the table
	private void emptyResultsList() {
		observableFans = FXCollections.observableList(List.of());
		resultsView.setItems(observableFans);
	}
}

// Fan class represents a fan using JavaFX properties for displaying in a table
class Fan {
	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleStringProperty team;
	
	public Fan(String firstName, String lastName, String team) {
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.team = new SimpleStringProperty(team);
	}
	
	// Get methods to return both strings and string properties
	public String getFirstName() {
		return firstName.get();
	}
	
	public String getLastName() {
		return lastName.get();
	}
	
	public String getTeam() {
		return team.get();
	}
	
	public SimpleStringProperty getFirstNameProperty() {
		return firstName;
	}
	
	public SimpleStringProperty getLastNameProperty() {
		return lastName;
	}
	
	public SimpleStringProperty getTeamProperty() {
		return team;
	}
}

// Filter used to only allow integers or blank strings
class IntegerFilter implements UnaryOperator<Change> {

	@Override
	public Change apply(Change change) {
		if (change.getControlNewText().matches("^$|[0-9]*")) {
			return change;
		} else {
			return null;
		}
	}
}