package demo;

import org.testng.Assert;
import org.testng.annotations.Test;

import Files.Payload;
import io.restassured.path.json.JsonPath;

public class SumValidation {

	@Test
	
	public void sumOfCourses(){
		JsonPath js = new JsonPath(Payload.getCourseMockResponse());
		int count = js.getInt("courses.size()");
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		//Verify if sum of all course prices matches with purchase amount
				int coursePrice = 0;
				for(int i=0;i<count;i++){
					coursePrice+=(js.getInt("courses["+i+"].price")*js.getInt("courses["+i+"].copies"));
				}
				Assert.assertEquals(purchaseAmount, coursePrice);

	}
}
