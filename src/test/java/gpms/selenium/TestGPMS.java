package gpms.selenium;

import java.awt.AWTException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/*
 * Created by: Liliana Acevedo
 * last modified: 7/7/16
 */

public class TestGPMS {

	public static void main(String[] args) {

		// objects and vars instantiation
		String currentUser; 


		Browser bub = new Browser();
		User us = new User();
		Proposal pup = new Proposal();
		UserLogin ul = new UserLogin();

		// Run method to open browser
		//openWebApp("firefox") will run firefox
		//openWebApp("") will run in chrome
		WebDriver driver = bub.openWebApp("");
		System.out.println("Open browser");

		// declare and initialize the variable to store
		// expected title of web page
		// The first page called should be the login page
		// Expected title: Log in - GPMS
		String expectedTitle = "Log in - GPMS";

		// Call method to compare page title to expected
		bub.compareTitle(expectedTitle, driver);

		// Run method to login desired user
		currentUser = ul.lilyLogin(driver);

		// Wait for page to load
		bub.waitForPageLoad(driver);

		// verify title of web page
		// Home page: Home - GPMS
		expectedTitle = "Home - GPMS";

	//	String actualTitle = driver.getTitle();

		// Call method to compare to the actual title of the web page
		bub.compareTitle(expectedTitle, driver);

		// Verify user matches expected
		// declare and initialize the variable to store
		// expected name associated with user
		String expectedUser = currentUser;
		us.compareUser(expectedUser, driver);

		//Hooray! We are logged in
		us.getAllPositionTypes(driver);
		
		// Wait for the desired link to load
		bub.waitForPageLoad(driver);

		//Get full name of user 
		String userName = us.getUserFullName(driver);
			

		
		// Get position type of current user
		String positionType = us.getPositionType(driver);
		System.out.println("POSITION " + positionType);
		
		//testing session method ---- I have not figured out how to get data from session yet
/*		HttpServletRequest req;
		bub.getSessionID(driver, req);
*/		System.out.println("DO I SEE A SESSION ID?");
//	Above not working

		// View proposals
		bub.viewMyProposals(driver);

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

		bub.waitForPageLoad(driver);
		
		WebElement addNewProp = driver.findElement(By.id("btnAddNew"));
		addNewProp.click();
		WebElement element = driver.findElement(By.cssSelector("select[class='sfListmenu']"));
		bub.enableDisabledElement(driver, element);
/*
		//Fill out project information tab of proposal sheet
		try {
			us.addNewProposal(driver, userName, positionType);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		
		//Actually save the proposal
		pup.saveProposal(driver);

		// click on sign out button
		us.userLogout(driver);

		//Start the process of signing with the department chair
		ul.chairCSLogin(driver);

		bub.viewMyProposals(driver);

		pup.editProposal(driver);
		
		String currentUserFullName = us.getUserFullName(driver);
		String currentUserPositionType = us.getPositionType(driver);

		bub.viewMyProposals(driver);

		pup.editProposal(driver);
		
		pup.certificationSignatures(driver, currentUserFullName, currentUserPositionType);
		
		pup.saveProposal(driver);
		
		us.userLogout(driver);
		
		//Start the process of signing with the business manager
		ul.bmCSLogin(driver);

		
		currentUserFullName = us.getUserFullName(driver);
		currentUserPositionType = us.getPositionType(driver);

		bub.viewMyProposals(driver);

		pup.editProposal(driver);
			
		pup.certificationSignatures(driver, currentUserFullName, currentUserPositionType);
		
		pup.saveProposal(driver);
		
		us.userLogout(driver);
		
		//Start the process of signing with the dean
		ul.deanCSLogin(driver);
		
		currentUserFullName = us.getUserFullName(driver);
		currentUserPositionType = us.getPositionType(driver);

		bub.viewMyProposals(driver);

		pup.editProposal(driver);
			
		pup.certificationSignatures(driver, currentUserFullName, currentUserPositionType);
		
		pup.saveProposal(driver);
		
		us.userLogout(driver);
		
		//Start the process of signing with the research admin
		ul.raCSLogin(driver);
		
		currentUserFullName = us.getUserFullName(driver);
		currentUserPositionType = us.getPositionType(driver);
	
		bub.viewMyProposals(driver);

		pup.editProposal(driver);
			
		pup.certificationSignatures(driver, currentUserFullName, currentUserPositionType);
		
		pup.saveProposal(driver);
		
		us.userLogout(driver);
		
		//Start the process of signing with the research director
		ul.directorCSLogin(driver);
		
		currentUserFullName = us.getUserFullName(driver);
		currentUserPositionType = us.getPositionType(driver);
		
		bub.viewMyProposals(driver);

		pup.editProposal(driver);
			
		pup.certificationSignatures(driver, currentUserFullName, currentUserPositionType);
		
		pup.saveProposal(driver);
		
		us.userLogout(driver);
		
		//Start the submission process with the research admin
		ul.raCSLogin(driver);
		
		currentUserFullName = us.getUserFullName(driver);
		currentUserPositionType = us.getPositionType(driver);
	
		bub.viewMyProposals(driver);

		pup.editProposal(driver);
			
		pup.certificationSignatures(driver, currentUserFullName, currentUserPositionType);
		
		bub.clickProposalSubmitBtn(driver);
		
		us.userLogout(driver);
		// close the web browser
//		driver.close();
	*/	System.out.println("Test script executed successfully");

		// terminate program
		System.exit(0);

	}

}
