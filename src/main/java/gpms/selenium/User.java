package gpms.selenium;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/*
 * Created by: Liliana Acevedo
 * last modified: 6/22/16
 */

public class User {
	// instantiate

	public String compareUser(String expectedUser, WebDriver driver) {
		// fetch actual logged in user
		WebElement findActualUser = driver
				.findElement(By.cssSelector("strong"));
		String actualUser = findActualUser.getText();

		// Compare actual to expected user
		if (expectedUser.equals(actualUser)) {
			System.out
					.println("Verification Successful - Username matches expected: "
							+ actualUser);
		} else {
			System.out
					.println("Verification failed - Username does not match actual: "
							+ actualUser);
		}
		return actualUser;
	}

	public void userLogin(String userEmail, String pword, WebDriver driver) {
		// Enter a valid username in email textbox
		WebElement username = driver.findElement(By.id("user_email"));
		username.clear();
		username.sendKeys(userEmail);

		// Enter a valid password
		WebElement password = driver.findElement(By.id("user_password"));
		password.clear();
		password.sendKeys(pword);

		// click on sign in button
		WebElement logInButton = driver.findElement(By.name("commit"));
		logInButton.click();
	}

	/*
	 * Note: This method includes a call to clickProfileDropdown because the
	 * logout button is displayed in the drop down menu
	 */
	public void userLogout(WebDriver driver) {
		Browser bb = new Browser();
		bb.waitForPageLoad(driver);
		System.out.println("Allowing page to load");
		bb.clickProfileDropdown(driver);
		System.out.println("We clicked the dropdown");
		WebElement logOffButton = driver.findElement(By
				.cssSelector("a[href='./Logout.jsp']"));
		logOffButton.click();
	}

	public String getPositionType(WebDriver driver) {
		String positionType;
		Browser bb = new Browser();
		bb.clickProfileDropdown(driver);
		WebElement positionChecked = driver.findElement(By
				.cssSelector("a[class='icon-checked']"));
		positionType = positionChecked.getAttribute("data-positiontype");
		System.out.println("Position type of user is: " + positionType);
		bb.clickProfileDropdown(driver);
		return positionType;
	}

	/*
	 * The only people who should be able to select the button to Add a New
	 * Proposal must be those that are either tenure track or non-tenure track
	 * professors Otherwise this MUST generate an error message Thus, it is
	 * important to verify pre- and post-conditions and report whether it should
	 * have worked...
	 */
	public void addNewProposal(WebDriver driver) {
		Browser bb = new Browser();
		WebElement addNewProp = driver.findElement(By.id("btnAddNew"));
		String currentPosition, expectedHeader;
		boolean verificationResult;

		// Find the current selected user view and determine the assigned
		// attribute data-positiontype
		// If the position is not professor, then this must produce an error
		// message
		currentPosition = this.getPositionType(driver);
		addNewProp.click();
		bb.waitForPageLoad(driver);
		if (currentPosition.equals("Tenured/tenure-track faculty")
				^ currentPosition.equals("Non-tenure track research faculty")) {
			System.out
					.println("Faculty should have access. We should see the New Proposal Details header");
			// Verify page heading: New Proposal Details
			expectedHeader = "New Proposal Details";
			verificationResult = bb.verifyPageHeader(expectedHeader, driver);
			if (verificationResult == true) {
				System.out.println("Access granted, as required");
			} else
				System.out.println("Uh oh, why can't faculty add a proposal?");
		} else {
			System.out.println("faculty should be denied access");
			bb.errorConfirm(driver);
			expectedHeader = "Manage Your Proposals";
			bb.verifyPageHeader(expectedHeader, driver);
		}
	}

	/*
	 * Co-PI can be added by the PI, but NOT by another co-PI The PI is always
	 * on the first row, this cannot be changed Co-PIs do not have access to
	 * add-coPI button
	 */
	public void addCoPI(WebDriver driver) {
		WebElement addCoPIBtn = driver.findElement(By
				.className("AddCoPI cssClassButtonSubmit"));
		// String currentUser = this.compareUser(driver);
		// I need a method that collects information about the person currently
		// logged in
		// so that I can verify if that person is actually the PI
		addCoPIBtn.click();
		// if(current)

		// row of table currently being worked on
		// problem here is that every fucking row has the same fucking title
		driver.findElement(By.cssSelector("select[title='Choose Role']"));

		// Get number of rows In table.
		int rowCount = driver
				.findElements(
						By.xpath("//div[@id='ui-id-2']/table[@id='dataTable']/tbody/tr"))
				.size();
		System.out.println("The number of rows in Investigator Info table: "
				+ rowCount);
	}

	/*
	 * Purpose: Get the data table from the add proposal page with investigator
	 * information
	 */
	public void getInvestigatorInfo(WebDriver driver) {
		int index = 0;
		WebElement baseTable = driver.findElement(By.id("dataTable"));
		List<WebElement> tableRows = baseTable.findElements(By.tagName("tr"));
		tableRows.get(index).getText();
	}

	/*
	 * Purpose: click on Investigator info tab
	 */
	public void clickInvestigatorInfo(WebDriver driver) {

	}

}
