package gpms.dao;

import gpms.DAL.DepartmentsPositionsCollection;
import gpms.DAL.MongoDBConnector;
import gpms.model.Address;
import gpms.model.PositionDetails;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public class Create10UsersTest {

	MongoClient mongoClient;
	Morphia morphia;
	String dbName = "db_gpms";
	UserAccountDAO newUserAccountDAO;
	UserProfileDAO newUserProfileDAO;
	ProposalDAO newProposalDAO;
	final int MAXIMUM_PROFILES = 10; // Adjust this to make more or less
										// profiles
										// with the generator.

	@Before
	public void initiate() {
		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		newUserAccountDAO = new UserAccountDAO(mongoClient, morphia, dbName);
		newUserProfileDAO = new UserProfileDAO(mongoClient, morphia, dbName);
		newProposalDAO = new ProposalDAO(mongoClient, morphia, dbName);
	}

	@Test
	public void create100() throws ParseException {
		int creationCounter = 0;

		while (creationCounter < MAXIMUM_PROFILES) {
			// String userProfile = "userName" + creationCounter;
			String userAccount = "userAccount" + creationCounter;
			String firstName = "firstName" + creationCounter;
			String middleName = "middleName" + creationCounter;
			String lastName = "lastName" + creationCounter;

			UserAccount newAccount = new UserAccount();
			UserProfile newProfile = new UserProfile();

			newAccount.setUserName(userAccount);
			// newUserAccountDAO.setAccountName(newProfile, newAccount,
			// userAccount);
			newAccount.setPassword(userAccount);
			// newUserAccountDAO.setPassword(newProfile, newAccount,
			// userAccount);
			newAccount.setActive(true);
			newAccount.setAddedOn(new Date());

			newProfile.setFirstName(firstName);

			Random rand1 = new Random();
			int haveMiddle = rand1.nextInt(3);
			if (haveMiddle < 1) {
				newProfile.setMiddleName(middleName);
			}

			newProfile.setLastName(lastName);
			newProfile.setUserAccount(newAccount);
			newProfile.getHomeNumbers().add("2084661200");
			newProfile.getOfficeNumbers().add("2084947492");
			newProfile.getMobileNumbers().add("2087024522");
			// newProfile.getWorkEmails().add("workman@worksite.org");
			newProfile.getPersonalEmails().add(userAccount + "@yahoo.com");

			// Add three Position Detail with second one get default role
			setPositionDetails(newProfile);
			setPositionDetails(newProfile);
			setPositionDetails(newProfile);

			Address newAddress = new Address();

			// We need his living address too.
			String street, apt, city, state, zipcode, country;
			street = "12019 Torrey Pine Court";
			apt = "13B";
			city = "Hoodbridge";
			state = "Idaho";
			zipcode = "83686";
			country = "United States";

			newAddress = new Address();
			newAddress.setStreet(street);
			newAddress.setApt(apt);
			newAddress.setCity(city);
			newAddress.setState(state);
			newAddress.setZipcode(zipcode);
			newAddress.setCountry(country);

			newProfile.getAddresses().add(newAddress);

			// Address newAddress2 = new Address();

			// street = "466 West Floridian Road";
			// city = "Langley";
			// state = "Virginia";
			// zipcode = "22192";
			// country = "United States";
			//
			// newAddress2.setStreet(street);
			// newAddress2.setCity(city);
			// newAddress2.setState(state);
			// newAddress2.setZipcode(zipcode);
			// newAddress2.setCountry(country);
			//
			// newProfile.getAddresses().add(newAddress2);

			String strDOB = "1984-12-01";
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date dateDOB = formatter.parse(strDOB);

			String newPassword = "password";
			newProfile.getUserAccount().setPassword(newPassword);

			newProfile.setDateOfBirth(dateDOB);
			newProfile.setGender("Male");

			newProfile.getOtherNumbers().add("2089389302");

			newProfile.getWorkEmails().add(userAccount + "@officialplace.com");

			// Save the informations
			newUserAccountDAO.save(newAccount);
			newUserProfileDAO.save(newProfile);

			// Increment Count
			creationCounter++;
		}

	}

	/**
	 * This method does some random assignment of position details to the
	 * profile
	 * 
	 * @param theProfile
	 */
	public void setPositionDetails(UserProfile theProfile) {
		DepartmentsPositionsCollection newThing = new DepartmentsPositionsCollection();
		Set<String> firstList = newThing.getCollegeKeys();
		PositionDetails newDetails = new PositionDetails();
		Random rand = new Random();

		int choice1 = rand.nextInt(firstList.size());
		newDetails.setCollege(CollectionUtils.get(firstList, choice1)
				.toString());

		Set<String> secondList = newThing.getDepartmentKeys(CollectionUtils
				.get(firstList, choice1).toString());
		int choice2 = rand.nextInt(secondList.size());
		newDetails.setDepartment(CollectionUtils.get(secondList, choice2)
				.toString());

		Set<String> thirdList = newThing.getPositionType(
				CollectionUtils.get(firstList, choice1).toString(),
				CollectionUtils.get(secondList, choice2).toString());
		int choice3 = rand.nextInt(thirdList.size());
		newDetails.setPositionType(CollectionUtils.get(thirdList, choice3)
				.toString());

		List<String> fourthList = newThing.getPositionTitle(CollectionUtils
				.get(firstList, choice1).toString(),
				CollectionUtils.get(secondList, choice2).toString(),
				CollectionUtils.get(thirdList, choice3).toString());
		int choice4 = rand.nextInt(fourthList.size());
		newDetails.setPositionTitle(fourthList.get(choice4));

		if (theProfile.getDetails().size() == 1) {
			newDetails.setAsDefault(true);
		}
		theProfile.getDetails().add(newDetails);
	}
}
