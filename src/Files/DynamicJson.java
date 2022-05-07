package Files;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DynamicJson {
	static RandomStringUtils r = new RandomStringUtils();
//	String isbn = r.randomAlphabetic(3);
//	String aisle = r.randomNumeric(3);
	@Test(dataProvider="BooksData")	
	public void addBook(String isbn,String aisle){
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().header("content-type","application/json").body(Payload.addBook(isbn,aisle))
		.when().post("Library/Addbook.php")
		.then().assertThat().statusCode(200).extract().response().asString();
		JsonPath js = ReusableMethods.convertRawDataToJsonFormat(response);
		String id = js.get("ID");
		System.out.println("Book id = "+id);
	}
	
	@DataProvider(name="BooksData")
	public Object[][] getData(){
		return new Object[][] {{"djs","687"},{"ryt","789"},{"gfs","245"},{"lkj","936"}};
		}
	}
