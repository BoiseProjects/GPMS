package gpms.selenium;

import java.awt.AWTException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/*
 * Created by: Liliana Acevedo
 * last modified: 7/13/16
 */

public class TestGPMS {

	public static void main(String[] args) {

		// objects and vars instantiation
		String currentUser = new String();

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
//<<<<<<< HEAD
		currentUser = ul.lilyLogin(driver);

		// Wait for page to load
		bub.waitForPageLoad(driver);
//=======
		// currentUser = ul.lilyLogin(driver);

		// Wait for page to load
		// bub.waitForPageLoad(driver);
//>>>>>>> 791b98db077e70b77e1b53f7f15b16c55c04cde9

		// verify title of web page
		// Home page: Home - GPMS
		expectedTitle = "Home - GPMS";

		// String actualTitle = driver.getTitle();

		// Call method to compare to the actual title of the web page
		bub.compareTitle(expectedTitle, driver);

		// Verify user matches expected
		// declare and initialize the variable to store
		// expected name associated with user
		String expectedUser = currentUser;
		us.compareUser(expectedUser, driver);

		// Hooray! We are logged in
		us.getAllPositionTypes(driver);

		// Wait for the desired link to load
		bub.waitForPageLoad(driver);

		// Get full name of user
		String userName = us.getUserFullName(driver);

		// Get position type of current user
		String positionType = us.getPositionType(driver);
		System.out.println("POSITION " + positionType);
//<<<<<<< HEAD
		
		//testing session method ---- I have not figured out how to get data from session yet
/*		HttpServletRequest req;
		bub.getSessionID(driver, req);
*/		System.out.println("DO I SEE A SESSION ID?");
//	Above not working

//=======

		// testing session method ---- I have not figured out how to get data
		// from session yet
		/*
		 * HttpServletRequest req; bub.getSessionID(driver, req);
		 */System.out.println("DO I SEE A SESSION ID?");
		// Above not working

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
//>>>>>>> 791b98db077e70b77e1b53f7f15b16c55c04cde9
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

//<<<<<<< HEAD
		bub.waitForPageLoad(driver);
		
		WebElement addNewProp = driver.findElement(By.id("btnAddNew"));
		addNewProp.click();
		us.addCoPI(driver);
		WebElement element = driver.findElement(By.cssSelector("#dataTable > tbody > tr.trStatic > td:nth-child(9)"));
		bub.clickHiddenElement(driver, element);
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
//=======
		// Attempt to edit a proposal
		pup.editProposal(driver);
		// Click Add New Proposal button
		/*
		 * try { us.addNewProposal(driver, userName, positionType); } catch
		 * (AWTException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
/*		bub.waitForPageLoad(driver);

		// us.addCoPI(driver);

		// Fill out project information tab of proposal sheet
		// pup.addProposal(driver, userName, positionType);

		// click on sign out button
		// us.userLogout(driver);
//>>>>>>> 791b98db077e70b77e1b53f7f15b16c55c04cde9

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
//<<<<<<< HEAD
//		driver.close();
	*/	System.out.println("Test script executed successfully");
//=======

//>>>>>>> 791b98db077e70b77e1b53f7f15b16c55c04cde9

		// terminate program
		System.exit(0);

	}

}
