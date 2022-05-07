package demo;

import static io.restassured.RestAssured.*;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class OAuthAutomation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//get the authorisation code - Wecommented below lines of code as google stopped accepting automation requests for gmail sign .So,inorder to get the code we need to manually login and then capture the code from the url generated
		//actually thought of constructing the url and login to gmail using front end automation and capture the url after logging in
		
//		String code = given().queryParams("scope","https://www.googleapis.com/auth/userinfo.email")
//		.queryParams("auth_url","https://accounts.google.com/o/oauth2/v2/auth")
//		.queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
//		.queryParams("response_type","code")
//		.queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
//		.queryParams("state","verifyfjdss")
//		.when().get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=verifyfjdss")
//		.asString();
//		System.out.println(code);
		
        String url = "https://rahulshettyacademy.com/getCourse.php?state=verifyfjdss&code=4%2F0AX4XfWhScV8Zd39N0woDK2wQ1mSZhP2Qfx3OwResT7L__C4LuX8c68kejxLoSUu4iSGCrg&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none#";
        String codeValue = url.split("code=")[1].split("&scope=")[0];
		System.out.println(codeValue);
		
		//get Access Token
		//urlEncodingEnabled(false) ---> This param is used inorder to indicate restassured to remain the special characters in the code as it is.
		//If we don't use the bove param,all the special char's will be conerted to some numerical entity
		
		String response = given().urlEncodingEnabled(false).queryParams("code",codeValue).queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W").queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type","authorization_code").when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();
		System.out.println(response);
		JsonPath js = new JsonPath(response);
		String accessToken = js.getString("access_token");
		System.out.println(accessToken);
		
		//get Course Details
		
		String courseDetails = given().queryParam("access_token",accessToken)
				.when().log().all().get("https://rahulshettyacademy.com/getCourse.php").asString();
		System.out.println(courseDetails);
	}

}
