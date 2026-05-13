/* Daniel Preller, 13 M<ay 2026, Assignment 9
 * Test program to demonstrate connecting to a database with JDBC
*/

import java.sql.*;

public class MySQLTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		final String USERNAME = "student1";
		final String PASSWORD = "pass";
		
		// Loads driver, connects to database, and creates a statement
		Class.forName("com.mysql.cj.jdbc.Driver");		
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost", USERNAME, PASSWORD);
		Statement statement = connection.createStatement();
		
		// Shows the current user
		ResultSet results = statement.executeQuery("SELECT CURRENT_USER();");
		System.out.print("Current user: ");
		results.next();
		System.out.println(results.getString(1) + "\n");
		
		// Shows the databases
		results = statement.executeQuery("SHOW DATABASES;");
		System.out.println("Databases:");
		while (results.next()) {
			System.out.println(results.getString(1));
		}
		System.out.println();
		
		// Drops the database if it already exists
		statement.executeUpdate("DROP DATABASE IF EXISTS databasedb;");
		System.out.println("Removed existing database to prevent conflicts");
		
		// Creates or recreates database
		statement.executeUpdate("CREATE DATABASE databasedb;");
		System.out.println("Created database 'databasedb'");
		
		// Switches to the database
		statement.executeUpdate("USE databasedb;");
		System.out.println("Switched to databasedb\n");
		
		// Creates the table
		statement.executeUpdate("CREATE TABLE address33(ID int PRIMARY KEY,LASTNAME varchar(40), FIRSTNAME varchar(40), STREET varchar(40), CITY varchar(40), STATE varchar(40), ZIP varchar(40));");
		System.out.println("Created table 'address33'");
		
		// Inserts values into the table
		statement.executeUpdate("INSERT INTO address33 VALUES(24,'Lou','Woods','1919 Bluewing Circle','Bellevue','NE','68123'), "
				+ "(25,'Lou','Woods','1919 Bluewing Circle','Bellevue','NE','68123'), "
				+ "(26,'Lou','Woods','1919 Bluewing Circle','Bellevue','NE','68123');");
		System.out.println("Populated tables");
		System.out.println();
		
		// Shows all tables
		results = statement.executeQuery("SHOW TABLES;");
		System.out.println("Tables:");
		while (results.next()) {
			System.out.println(results.getString(1));
		}
		System.out.println();
		
		// Shows the contents of the new table
		results = statement.executeQuery("SELECT * FROM address33;");
		System.out.println("Contents of address33:");
		while (results.next()) {
			System.out.print(results.getInt(1) + " ");
			System.out.print(results.getString(2) + " ");
			System.out.print(results.getString(3) + " ");
			System.out.print(results.getString(4) + " ");
			System.out.print(results.getString(5) + " ");
			System.out.print(results.getString(6) + " ");
			System.out.println(results.getString(7));
		}
	}
}