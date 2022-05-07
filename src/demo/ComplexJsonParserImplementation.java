package demo;

import org.testng.Assert;

import Files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParserImplementation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JsonPath js = new JsonPath(Payload.getCourseMockResponse());
		
		//No.of courses returned by API
		
		int count = js.getInt("courses.size()");
		System.out.println("Count of courses = "+count);
		
		//Print purchase Amount
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println("Purchase Amount = "+purchaseAmount);
		
		//Print Title of first course
		String firstCourseTitle = js.get("courses[0].title");
		System.out.println("First Course Title = "+firstCourseTitle);
		
		//Print All Course Titles and their respective prices
		for(int i=0;i<count;i++){
			String courseTitle = js.get("courses["+i+"].title");
			int coursePrice = js.getInt("courses["+i+"].price");
			System.out.println("Course Title = "+courseTitle+" and Course Price = "+coursePrice);
		}
		
		//Print No.of copies sold by RPA course
		for(int i=0;i<count;i++){
			if(js.get("courses["+i+"].title").toString().equalsIgnoreCase("RPA")){
				System.out.println("RPA Course Copies = "+js.getInt("courses["+i+"].copies"));
				break;
			}
		} 
		
//		//Verify if sum of all course prices matches with purchase amount
//		int coursePrice = 0;
//		for(int i=0;i<count;i++){
//			coursePrice+=(js.getInt("courses["+i+"].price")*js.getInt("courses["+i+"].copies"));
//		}
//		Assert.assertEquals(purchaseAmount, coursePrice);
	}
}
