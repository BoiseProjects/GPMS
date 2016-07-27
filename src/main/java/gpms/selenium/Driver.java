package gpms.selenium;

import java.awt.AWTException;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/*
 * Created by: Liliana Acevedo
 * last modified: 7/13/16
 * 
 * Purpose: handle the different kinds of workflows to be tested
 */

public class Driver {
	
	/*
	 * Purpose: test entire workflow from start to finish
	 * 	Start with PI, test all functionalities
	 * 	Then Department Chair, test all
	 * 	Business Manager, test all
	 * 	Dean, test all
	 * 	IRB, test all
	 * 	Research admin, test all
	 * 	Research director, test all
	 * 	Research admin, test all
	 */
	public void wholeFlowInChrome(WebDriver driver)
	{
		String currentUser = new String();

		Browser bb = new Browser();
		User us = new User();
		Proposal pp = new Proposal();
		UserLogin ul = new UserLogin();
		
		// Run method to open browser
		//openWebApp("firefox") will run firefox
		//openWebApp("") will run in chrome
		driver = bb.openWebApp("");
		
		// declare and initialize the variable to store
		// expected title of web page
		// The first page called should be the login page
		// Expected title: Log in - GPMS
		String expectedTitle = "Log in - GPMS";
		
		// Call method to compare page title to expected
		bb.compareTitle(expectedTitle, driver);
		
		// Run method to login desired user
		currentUser = ul.lilyLogin(driver);

		// Wait for page to load
		bb.waitForPageLoad(driver);
		
		// verify title of web page
		// Home page: Home - GPMS
		expectedTitle = "Home - GPMS";

		// Call method to compare to the actual title of the web page
		bb.compareTitle(expectedTitle, driver);
		
		// Verify user matches expected
		// declare and initialize the variable to store
		// expected name associated with user
		String expectedUser = currentUser;
		us.compareUser(expectedUser, driver);
		
		// Hooray! We are logged in
		us.getAllPositionTypes(driver);

		// Wait for the desired link to load
		bb.waitForPageLoad(driver);

		// Get full name of user
		String userName = us.getUserFullName(driver);

		// Get position type of current user
		String positionType = us.getPositionType(driver);
		
		// View proposals
		bb.viewMyProposals(driver);

		// Wait for the next page to load
		bb.waitForPageLoad(driver);

		// Verify Title of page
		// expected title of web page
		expectedTitle = "My Proposals";

		// fetch actual title of web page and compare
		bb.compareTitle(expectedTitle, driver);

		// verify Heading of page is "Manage Your Proposals"
		String expectedHeader = "Manage Your Proposals";
		bb.verifyPageHeader(expectedHeader, driver);
		
		bb.waitForPageLoad(driver);
/*		
 * 
 * 		Testing activation of elements... 
		WebElement addNewProp = driver.findElement(By.id("btnAddNew"));
		addNewProp.click();
		us.addCoPI(driver);
		WebElement element = driver.findElement(By.cssSelector("#dataTable > tbody > tr.trStatic > td:nth-child(9)"));
		bb.clickHiddenElement(driver, element);
*/		
		//Fill out project information tab of proposal sheet
		try {
			us.addNewProposal(driver, userName, positionType);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		
		//Actually save the proposal
		pp.saveProposal(driver);

		// click on sign out button
		us.userLogout(driver);

		//Start the process of signing with the department chair
		ul.chairCSLogin(driver);

		bb.viewMyProposals(driver);

		pp.editProposal(driver);
		
		String currentUserFullName = us.getUserFullName(driver);
		String currentUserPositionType = us.getPositionType(driver);

		bb.viewMyProposals(driver);

		pp.editProposal(driver);
		
		pp.certificationSignatures(driver, currentUserFullName, currentUserPositionType);
		
		pp.saveProposal(driver);
		
		us.userLogout(driver);
		
		//Start the process of signing with the business manager
		ul.bmCSLogin(driver);

		
		currentUserFullName = us.getUserFullName(driver);
		currentUserPositionType = us.getPositionType(driver);

		bb.viewMyProposals(driver);

		pp.editProposal(driver);
			
		pp.certificationSignatures(driver, currentUserFullName, currentUserPositionType);
		
		pp.saveProposal(driver);
		
		us.userLogout(driver);
		
		//Start the process of signing with the dean
		ul.deanCSLogin(driver);
		
		currentUserFullName = us.getUserFullName(driver);
		currentUserPositionType = us.getPositionType(driver);

		bb.viewMyProposals(driver);

		pp.editProposal(driver);
			
		pp.certificationSignatures(driver, currentUserFullName, currentUserPositionType);
		
		pp.saveProposal(driver);
		
		us.userLogout(driver);
		
		//Start the process of signing with the research admin
		ul.raCSLogin(driver);
		
		currentUserFullName = us.getUserFullName(driver);
		currentUserPositionType = us.getPositionType(driver);
	
		bb.viewMyProposals(driver);
		
		// Attempt to edit a proposal
		pp.editProposal(driver);
		
		// Click Add New Proposal button
		/*
		 * try { us.addNewProposal(driver, userName, positionType); } catch
		 * (AWTException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		
		bb.waitForPageLoad(driver);

		// us.addCoPI(driver);

		// Fill out project information tab of proposal sheet
		// pp.addProposal(driver, userName, positionType);

		// click on sign out button
		// us.userLogout(driver);
		
		pp.editProposal(driver);
		
		pp.certificationSignatures(driver, currentUserFullName, currentUserPositionType);
		
		pp.saveProposal(driver);
		
		us.userLogout(driver);
		
		//Start the process of signing with the research director
		ul.directorCSLogin(driver);
		
		currentUserFullName = us.getUserFullName(driver);
		currentUserPositionType = us.getPositionType(driver);
		
		bb.viewMyProposals(driver);

		pp.editProposal(driver);
			
		pp.certificationSignatures(driver, currentUserFullName, currentUserPositionType);
		
		pp.saveProposal(driver);
		
		us.userLogout(driver);
		
		//Start the submission process with the research admin
		ul.raCSLogin(driver);
		
		currentUserFullName = us.getUserFullName(driver);
		currentUserPositionType = us.getPositionType(driver);
	
		bb.viewMyProposals(driver);

		pp.editProposal(driver);
			
		pp.certificationSignatures(driver, currentUserFullName, currentUserPositionType);
		
		bb.clickProposalSubmitBtn(driver);
		
		us.userLogout(driver);
		// close the web browser
		driver.close();
		
		// terminate program
		System.exit(0);
	}
	
	public void csChairFlow(WebDriver driver)
	{
		Browser bb = new Browser();
		User us = new User();
		Proposal pp = new Proposal();
		UserLogin ul = new UserLogin();
		Combinations cc = new Combinations();
		
		driver = bb.openWebApp("");

		
		//Start the process of signing with the department chair
		ul.chairCSLogin(driver);
		
		// Hooray! We are logged in
		us.getAllPositionTypes(driver);
		
		String currentUserFullName = us.getUserFullName(driver);
		String currentUserPositionType = us.getPositionType(driver);
		
		bb.viewMyProposals(driver);

		pp.editProposal(driver);
				
		this.buttonMucker(driver);
		
//		pp.certificationSignatures(driver, currentUserFullName, currentUserPositionType);
		
//		pp.saveProposal(driver);
		
//		us.userLogout(driver);
		
		// terminate program
		System.exit(0);
	}
	
	/*
	 * Purpose: control individual workflow
	 */
	public void doThing(WebDriver driver, String user)
	{
/*		User us = new User();

		//login the given user; if fail, let me know
		login(user);
		if homepage not visible, 
			spew user login failed
			
		//gather user data
		String currentUserFullName = us.getUserFullName(driver);
		String currentUserPositionType = us.getPositionType(driver);		
		us.getAllPositionTypes(driver);
		
		//Start the process
		//For every position this user hold, try e'rything
		for(every position in position type)
			first, attempt to add a proposal
			if successful
				attempt saving with every comination of buttons that shouldnt go through
				plus all the mucking
				else
					tell me I couldnt do it
			for random proposal on the list
				edit proposal - muck with all the buttons
					
*/
		
	}
	
	/*
	 * The sequence of mucking with buttons
	 * Same process whether we are editing or saving
	 * don't forget to try every permutation of buttons
	 * and data types
	 */
	public void buttonMucker(WebDriver driver)
	{
		Browser bb = new Browser();
//		go to first section, fill out all permutations of buttons, trying to save after each
		
		//Expand all sections
		WebElement element = driver.findElement(By.id("accordion-expand-holder"));
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].removeAttribute('style')", element);
		driver.findElement(By.cssSelector("button[class='expandAll sfBtn']")).click();
		
		//All table data within accordion
		WebElement formElement = driver.findElement(By.id("accordion"));
		List<WebElement> allFormInput = formElement.findElements(By.tagName("input"));
		List<WebElement> allFormSelect = formElement.findElements(By.tagName("select"));		

		int ii = 0, size;
		size = allFormInput.size() + allFormSelect.size();
		String name;
		String[] nameList = new String[size];
		
		//Store names of all table data within string array
		for (WebElement item : allFormInput)
		{
			name = item.getAttribute("name");
			nameList[ii] = name;
			ii++;
		}
		
		for (WebElement item : allFormSelect)
		{
			name = item.getAttribute("name");
			nameList[ii] = name;
			ii++;
		}		
	
		Arrays.sort(nameList);
		
		//Some webelements have identical names... I can't have that!!
		//Use JS to change the name
		
		for(int jj=0; jj < size-1; jj++)
		{
			int kk = jj + 1;
			if(nameList[jj].equals(nameList[kk]))
			{
				String nameOriginal;
				nameOriginal = nameList[kk];
				nameList[kk] = nameOriginal + kk;
				String nameNew = nameList[kk];
				bb.changeElementName(driver, nameOriginal, nameNew);
			}
			System.out.println(nameList[jj]);
		}

		//String[] result = new String[size];
		//Combinations cc = new Combinations();
/*        for (int jj = 0; jj <= nameList.length; jj++)
        {
    		this.combinations2(nameList, jj, 0, new String[jj]);
        }
*/
		
		//Runs the combination of all buttons... which is all buttons
		int jj = nameList.length;
   		this.combinations2(nameList, jj, 0, new String[jj]);
        
	}
	
	 public void combinations2(String[] arr, int len, 
			 int startPosition, String[] result)
	 {
		 if (len == 0)
	     {
	        System.out.println(Arrays.toString(result));
	        return;
	     }       
	     for (int ii = startPosition; ii <= arr.length-len; ii++)
	     {
	        result[result.length - len] = arr[ii];
	        combinations2(arr, len-1, ii+1, result);
	     }
	}

	 /*
	  * A brief function that takes all the names, changes the name of the webelement
	  * if needed, 
	  * then clicks and mucks with them
	  */
	 public void doodad(WebDriver driver)
	 {
		 //Find the 
	 }
}
