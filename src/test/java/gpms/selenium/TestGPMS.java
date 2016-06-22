package gpms.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * Created by: Liliana Acevedo
 * last modified: 6/20/16
 * 
 * Test
 */

public class TestGPMS {

	public static void main(String[] args) {

		// objects and vars instantiation
		long timeoutInSeconds = 2;
		/*
		 * String currentUser = "liliana"; String userEmail =
		 * "lva34bsu@gmail.com"; String pword = "password";
		 */

		String currentUser = "bmcomputerengineering1";
		String userEmail = "bmcomputerengineering1@gmail.com";
		String pword = "gpmspassword";
		Browser bub = new Browser();
		User us = new User();
		Proposal pup = new Proposal();

		// Run method to open browser
		WebDriver driver = bub.openWebApp();
		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		System.out.println("Open browser");

		// declare and initialize the variable to store
		// expected title of web page
		// The first page called should be the login page
		// Expected title: Log in - GPMS
		String expectedTitle = "Log in - GPMS";

		// Call method to compare page title to expected
		bub.compareTitle(expectedTitle, driver);

		// Run method to login desired user
		us.userLogin(userEmail, pword, driver);

		// Wait for page to load
		bub.waitForPageLoad(driver);

		// verify title of web page
		// Home page: Home - GPMS
		expectedTitle = "Home - GPMS";

		String actualTitle = driver.getTitle();
		System.out.println(actualTitle);
		// Call method to compare to the actual title of the web page
		bub.compareTitle(expectedTitle, driver);

		// Verify user matches expected
		// declare and initialize the variable to store
		// expected name associated with user
		String expectedUser = currentUser;
		us.compareUser(expectedUser, driver);

		// click on myProfile Dropdown menu
		// bub.clickProfileDropdown(driver);

		// Wait for the desired link to load
		bub.waitForPageLoad(driver);

		// Get position type of current user
		String moo = us.getPositionType(driver);
		/*
		 * //Click on "View as Professor", if it is not already checked
		 * WebElement viewAsProfessor =
		 * driver.findElement(By.cssSelector("[data-positiontitle='Professor']"
		 * )); boolean selection = viewAsProfessor.isSelected();
		 * 
		 * if(selection = true) {
		 * System.out.println("View as Professor already selected"); } else {
		 * viewAsProfessor.click(); }
		 */
		// View proposals
		WebElement viewProposals = driver.findElement(By
				.cssSelector("a[href='./MyProposals.jsp']"));
		viewProposals.click();

		// Wait for the next page to load
		bub.waitForPageLoad(driver);

		// Verify Title of page
		// expected title of web page
		expectedTitle = "My Proposals";

		// fetch actual title of web page and compare
		bub.compareTitle(expectedTitle, driver);

		// verify Heading of page is "Manage Your Proposals"
		String expectedHeader = "Manage Your Proposals";
		bub.verifyPageHeader(expectedHeader, driver);

		// Attempt to edit a proposal

		// Click Add New Proposal button
		us.addNewProposal(driver);

		bub.waitForPageLoad(driver);

		// us.addCoPI(driver);

		pup.createProjectInformation(driver);
		// click on sign out button
		// us.userLogout(driver);

		// close the web browser
		driver.close();
		System.out.println("Test script executed successfully");

		// terminate program
		System.exit(0);

	}

}
