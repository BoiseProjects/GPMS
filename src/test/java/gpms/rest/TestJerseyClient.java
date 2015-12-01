package gpms.rest;

import gpms.model.UserProfile;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class TestJerseyClient {

	public static void main(String[] args) {
		try {
			String dateOfBirth = "Nov 4, 1984 8:14 PM";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
			Date dob = df.parse(dateOfBirth);

			UserProfile user = new UserProfile();
			user.setFirstName("Adriana");
			user.setMiddleName("Herald");
			user.setLastName("Barrer");
			user.setDateOfBirth(dob);
			user.setGender("Male");

			ClientConfig clientConfig = new DefaultClientConfig();

			clientConfig.getFeatures().put(
					JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

			Client client = Client.create(clientConfig);

			WebResource webResource = client
					.resource("http://localhost:8181/GPMS/REST/jsonServices/send");

			ClientResponse response = webResource.accept("application/json")
					.type("application/json").post(ClientResponse.class, user);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			String output = response.getEntity(String.class);

			System.out.println("Server response .... \n");
			System.out.println(output);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
