package Files;

import io.restassured.path.json.JsonPath;

public class ReusableMethods {

	public static JsonPath convertRawDataToJsonFormat(String response){
		JsonPath js = new JsonPath(response);
		return js;
	}
}
