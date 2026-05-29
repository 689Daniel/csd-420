/* Daniel Preller, 28 May 2026, Assignment 11
 * Example of converting a JSON string to a Java object using Jackson
 * Code sections have been copied from https://www.baeldung.com/jackson-object-mapper-tutorial
 * and combined alongside required Java components (such as main method header).
 * Copied portions of code are commented as such
 */

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONExample {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";// Copied from source
		Car car = objectMapper.readValue(json, Car.class);// Copied from source
		
		car.printCar();
	}

}


// Car class has been designed to correspond with requirements of copied code
class Car {
	public String color;
	public String type;
	
	public void printCar() {
		System.out.println("Color: " + color);
		System.out.println("Type: " + type);
	}
}