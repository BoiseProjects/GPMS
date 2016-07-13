package gpms.rest;

public class TestJerseyClient {

	public static void main(String[] args) {
		// Using API Tutorial in pluralsight
		// https://app.pluralsight.com/player?course=five-essential-tools-building-rest-api&author=elton-stoneman&name=five-essential-tools-building-rest-api-m1&clip=13&mode=live

		// Client client = ClientBuilder.newClient();
		// Response response = client
		// .target("http://private-cede7-spiderlog216.apiary-mock.com/spiders?page=1&size=10")
		// .request(MediaType.TEXT_PLAIN_TYPE)
		// .header("Authorization", "Basic dXNlcjpwYXNzd29yZA==")
		// .header("x-api-version", "1.0").get();
		//
		// System.out.println("status: " + response.getStatus());
		// System.out.println("headers: " + response.getHeaders());
		// System.out.println("body:" + response.readEntity(String.class));

		// END pluralsight demo

		// try {
		// String dateOfBirth = "Nov 4, 1984 8:14 PM";
		// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
		// Date dob = df.parse(dateOfBirth);
		//
		// UserProfile user = new UserProfile();
		// user.setFirstName("Adriana");
		// user.setMiddleName("Herald");
		// user.setLastName("Barrer");
		// user.setDateOfBirth(dob);
		// user.setGender("Male");
		//
		// ClientConfig clientConfig = new DefaultClientConfig();
		//
		// clientConfig.getFeatures().put(
		// JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		//
		// Client client = Client.create(clientConfig);
		//
		// WebResource webResource = client
		// .resource("http://localhost:8181/GPMS/REST/jsonServices/send");
		//
		// ClientResponse response = webResource.accept("application/json")
		// .type("application/json").post(ClientResponse.class, user);
		//
		// if (response.getStatus() != 200) {
		// throw new RuntimeException("Failed : HTTP error code : "
		// + response.getStatus());
		// }
		//
		// String output = response.getEntity(String.class);
		//
		// System.out.println("Server response .... \n");
		// System.out.println(output);
		//
		// } catch (Exception e) {
		//
		// e.printStackTrace();
		//
		// }

	}

}
