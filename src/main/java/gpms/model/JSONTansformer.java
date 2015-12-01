package gpms.model;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JSONTansformer {
	public static String ConvertToJSON(ArrayList<?> sendData)
			throws JsonGenerationException, JsonMappingException, IOException {
		String response = new String();

		// // Using Gson without pretty formatting
		// Gson gson = new Gson();
		// response = gson.toJson(sendData);

		// // To use pretty JSON response formatting
		// Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// response = gson.toJson(sendData);

		// using Jackson with pretty JSON response formatting
		ObjectMapper mapper = new ObjectMapper();

		final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		// 2014/09/1 for item detail bind to jQuery Date picker
		// final DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		mapper.setDateFormat(df);

		// response = mapper.writeValueAsString(sendData);
		response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				sendData);

		return response;
	}

	// public static Object ConvertFromJSON(String sendData)
	// throws JsonParseException, JsonMappingException, IOException {
	// UserInfo response = new UserInfo();
	// // Using Gson
	// Gson gson = new Gson();
	// response = gson.fromJson(sendData, UserInfo.class);
	//
	// // Using Jackson
	// ObjectMapper mapper = new ObjectMapper();
	// response = mapper.readValue(sendData, UserInfo.class);
	//
	// return response;
	// }

	public static String ConvertToJSON(String value)
			throws JsonGenerationException, JsonMappingException, IOException {
		String response = new String();
		ObjectMapper mapper = new ObjectMapper();
		response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				value);

		return response;
	}

	// public static String toJSON(UserInfo messageData) {
	// String response = new String();
	// Gson gson = new Gson();
	// response = gson.toJson(messageData);
	// return response;
	// }
}
