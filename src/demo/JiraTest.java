package demo;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

import org.testng.Assert;

import Files.ReusableMethods;

public class JiraTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RestAssured.baseURI = "http://localhost:8080";
		
	//Jira login Authentication
		
		SessionFilter session = new SessionFilter(); //This class directly captures the session id from the response generated
		
//		String response = given().header("Content-Type","application/json").body("{ \r\n" + 
//				"\r\n" + 
//				"\"username\": \"thanmaitummalapalli\", \r\n" + 
//				"\"password\": \"aacharya@98\"\r\n" + 
//				"\r\n" + 
//				"}").when().post("/rest/auth/1/session").then().log().all().extract().response().asString();
//		JsonPath js = ReusableMethods.convertRawDataToJsonFormat(response);
//		String jsessionid = js.getString("value");
//		System.out.println("JSESSION ID = "+jsessionid);
//		
		String response = given().relaxedHTTPSValidation().header("Content-Type","application/json").body("{ \r\n" + 
				"\r\n" + 
				"\"username\": \"thanmaitummalapalli\", \r\n" + 
				"\"password\": \"aacharya@98\"\r\n" + 
				"\r\n" + 
				"}").log().all().filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();
		
		//Create new bug
		
		String response1 = given().header("Content-Type","application/json").body("{\r\n" + 
				"      \"fields\": {\r\n" + 
				"        \"project\": {\r\n" + 
				"            \"key\": \"RES\"\r\n" + 
				"        },\r\n" + 
				"       \"summary\": \"Creating a issue for adding comment\",\r\n" + 
				"       \"description\": \"Creating a first bug\",\r\n" + 
				"       \"issuetype\": {\r\n" + 
				"            \"name\": \"Bug\"\r\n" + 
				"        }\r\n" + 
				"      }\r\n" + 
				"}").filter(session).when().post("/rest/api/2/issue").then().log().all().extract().response().asString();
		JsonPath js1 = ReusableMethods.convertRawDataToJsonFormat(response1);
		String id = js1.getString("id");
		System.out.println("ID = "+id);
		
		//Adding new comment to the bug created
		
		String comment = "Adding a new comment from REST API";
		String response2 = given().pathParam("id",id).log().all().header("Content-Type","application/json")
		.body("{\r\n" + 
				"    \"body\": \""+comment+"\",\r\n" + 
				"    \"visibility\": {\r\n" + 
				"        \"type\": \"role\",\r\n" + 
				"        \"value\": \"Administrators\"\r\n" + 
				"    }\r\n" + 
				"}").filter(session).when().post("/rest/api/2/issue/{id}/comment").then().log().all().assertThat().statusCode(201)
		.extract().response().asString();
		JsonPath js2 = ReusableMethods.convertRawDataToJsonFormat(response2);
		String comment_id = js2.getString("id");
		System.out.println("ID = "+comment_id);
		
		//Updating the existing comment for the created bug
		
		given().pathParam("creationid",id).pathParam("commentid",comment_id).log().all().header("Content-Type","application/json")
		.body("{\r\n" + 
				"    \"body\": \"Updating the existing comment from API\",\r\n" + 
				"    \"visibility\": {\r\n" + 
				"        \"type\": \"role\",\r\n" + 
				"        \"value\": \"Administrators\"\r\n" + 
				"    }\r\n" + 
				"}").filter(session).when().put("/rest/api/2/issue/{creationid}/comment/{commentid}").then().log().all().assertThat().statusCode(200);
		
		//Adding Attachment to the bug created
		
		given().pathParam("creationid",id).header("X-Atlassian-Token","no-check").filter(session).header("Content-Type","multipart/form-data")
		.multiPart("file",new File("JiraAttachment.txt")).when().post("/rest/api/2/issue/{creationid}/attachments").then().log().all()
		.assertThat().statusCode(200);
		
		//Get Issue
		
		String issueDetails = given().filter(session).pathParam("creationid",id).queryParam("fields", "comment").log().all().when().get("/rest/api/2/issue/{creationid}").then()
		.log().all().extract().response().asString();
		System.out.println("Issue Details = "+issueDetails);
		JsonPath js3 = ReusableMethods.convertRawDataToJsonFormat(issueDetails);
		int commentCount = js3.getInt("fields.comment.comments.size()");
		for(int i=0;i<commentCount;i++){
			int bugId = js3.getInt("fields.comment.comments["+i+"].id");
			System.out.println(bugId);
			System.out.println(id);
			if(Integer.parseInt(id)==bugId){
				String extractedComment = js3.get("fields.comment.comments["+i+"].body");
				System.out.println(extractedComment);
				if(comment.equalsIgnoreCase(extractedComment)){
					System.out.println("Retrieved Comment from the response matched with the actual comment added");
					Assert.assertEquals(comment, extractedComment);
				}
			}
		}
		
		//Deleting the bug created

//	    given().pathParam("id",id).log().all().filter(session).when().delete("/rest/api/2/issue/{id}")
//	    .then().log().all().assertThat().statusCode(204);
    }
}
