package demo;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;

import Files.Payload;
import Files.ReusableMethods;

import static io.restassured.RestAssured.*;

public class Basics {

	public static void main(String[] args) throws IOException {
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		//Add Place to an API using POST operation
		
		//Type-1:
		 //Reading payload data from the project by placing the payload in a different file
	String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(Payload.getBody())
		.when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.header("Server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();
		
		//Other way of reading payload data i.e., from external file
//		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
//				.body(new String(Files.readAllBytes(Paths.get("C:\\Users\\Admin\\Desktop\\API Automation Materials\\AddPlace.json"))))
//				.when().post("maps/api/place/add/json")
//				.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
//				.header("Server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();
				
		System.out.println("Response = "+response);
		
		//Add Place ---> Update Place with New Address----> Get Place to validate if new address is present in response

//		JsonPath js = new JsonPath(response);
		JsonPath js = ReusableMethods.convertRawDataToJsonFormat(response);
		String placeid = js.getString("place_id");
		System.out.println("Place_id = "+placeid);
		
		String newAddress = "Sarjapur,Bangalore";
		
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n" + 
				"\"place_id\":\""+placeid+"\",\r\n" + 
				"\"address\":\""+newAddress+"\",\r\n" + 
				"\"key\":\"qaclick123\"\r\n" + 
				"}")
		.when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get Place
		
		String r = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeid)
		.when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).body("address", equalTo("Sarjapur,Bangalore")).extract().response().asString();
		
		System.out.println("Response = "+r);
		
//		JsonPath js1 = new JsonPath(r);
		JsonPath js1 = ReusableMethods.convertRawDataToJsonFormat(r);
		String updatedAddress = js1.getString("address");
		System.out.println("Address = "+updatedAddress);
		
		Assert.assertEquals(updatedAddress,newAddress);
		
		//Type-2:
		
//		Response resp = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
//				.body(Payload.getBody())
//				.when().post("maps/api/place/add/json");
//		System.out.println("Status code - " +resp.getStatusCode());
//		resp.then().body("scope", equalTo("APP")).header("Server", "Apache/2.4.18 (Ubuntu)");
//		System.out.println("Response = "+resp.then().extract().response().asString());
		
	}

}
