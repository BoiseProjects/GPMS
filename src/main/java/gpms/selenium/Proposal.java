package gpms.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
/*
 *	Created by: Liliana Acevedo 
 *	last modified: 6/27/16
 *
 * I would like to go back through these and ensure that the tabs open properly; 
 * otherwise the user will not have any data visible, proving they cannot access that part
 * of the page through the visible elements;
 * being able to access hidden items will be tested using JS later
 */

public class Proposal {

	public void createProjectInformation(WebDriver driver) {
		//Fills out the project information tab of proposal 

		String currentElement;
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);

		// Locate and open Project information tab
		currentElement = "lblSection2";
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.id(currentElement)));
		WebElement projInfoDrop = driver.findElement(By.id(currentElement));
		projInfoDrop.click();

		// Insert a title
		currentElement = "txtProjectTitle";
		int projNum = (int) Math.floor(Math.random() * 10000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.id(currentElement)));
		WebElement projTitle = driver.findElement(By.id(currentElement));
		projTitle.clear();
		projTitle.sendKeys("Proposal " + projNum);

		// Select a project type
		currentElement = "ddlProjectType";
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.id(currentElement)));
		WebElement projType = driver.findElement(By.id(currentElement));
		// new Select(projType).selectByVisibleText("Research-Basic");
		projType.sendKeys(Keys.ARROW_DOWN);
		projType.sendKeys(Keys.ARROW_DOWN);
		projType.sendKeys(Keys.ARROW_DOWN);
		System.out.println("Selected project type");

		// Select a due date
		driver.findElement(By.id("txtDueDate")).click();
		driver.findElement(
				By.cssSelector("span.ui-icon.ui-icon-circle-triangle-e"))
				.click();
		driver.findElement(
				By.cssSelector("span.ui-icon.ui-icon-circle-triangle-e"))
				.click();
		driver.findElement(
				By.cssSelector("span.ui-icon.ui-icon-circle-triangle-e"))
				.click();
		driver.findElement(By.linkText("1")).click();

		// Select when the project period will start via Project From
		driver.findElement(By.id("txtProjectPeriodFrom")).click();
		driver.findElement(
				By.cssSelector("span.ui-icon.ui-icon-circle-triangle-e"))
				.click();
		driver.findElement(
				By.cssSelector("span.ui-icon.ui-icon-circle-triangle-e"))
				.click();
		driver.findElement(
				By.cssSelector("span.ui-icon.ui-icon-circle-triangle-e"))
				.click();
		driver.findElement(
				By.cssSelector("span.ui-icon.ui-icon-circle-triangle-e"))
				.click();
		driver.findElement(By.linkText("5")).click();

		// Select project period end date via Project To
		driver.findElement(By.id("txtProjectPeriodTo")).click();
		driver.findElement(
				By.cssSelector("span.ui-icon.ui-icon-circle-triangle-e"))
				.click();
		driver.findElement(
				By.cssSelector("span.ui-icon.ui-icon-circle-triangle-e"))
				.click();
		driver.findElement(
				By.cssSelector("span.ui-icon.ui-icon-circle-triangle-e"))
				.click();
		driver.findElement(
				By.cssSelector("span.ui-icon.ui-icon-circle-triangle-e"))
				.click();
		driver.findElement(
				By.cssSelector("span.ui-icon.ui-icon-circle-triangle-e"))
				.click();
		driver.findElement(By.linkText("1")).click();

		// Select Type of Request from dropdown
		// The Select method is not working for FireFox
		// new
		// Select(driver.findElement(By.id("ddlTypeOfRequest"))).selectByIndex(2);
		WebElement ddlTypeOfRequest = driver.findElement(By
				.id("ddlTypeOfRequest"));
		ddlTypeOfRequest.sendKeys(Keys.ARROW_DOWN);
		ddlTypeOfRequest.sendKeys(Keys.ARROW_DOWN);
		ddlTypeOfRequest.sendKeys(Keys.ARROW_DOWN);

		// Select Location of Project from drop down
		// new
		// Select(driver.findElement(By.id("ddlLocationOfProject"))).selectByVisibleText("On-campus");
		WebElement ddlLocationOfProject = driver.findElement(By
				.id("ddlLocationOfProject"));
		ddlLocationOfProject.sendKeys(Keys.ARROW_DOWN);

	}

	public void createSponsorAndBudgetInformation(WebDriver driver) {
		/*
		 * Fill out the Sponsor and Budget information tab of proposal
		 */
		
		WebElement sponsorDdl = driver.findElement(By.id("lblSection3"));
		sponsorDdl.click();
		
		WebElement grantingAgency = driver.findElement(By.id("txtNameOfGrantingAgency"));
		grantingAgency.clear();
	    grantingAgency.sendKeys("The Wayne Foundation");
	    
	    WebElement directCosts = driver.findElement(By.id("txtDirectCosts"));
	    directCosts.clear();
	    directCosts.sendKeys("500");
	    
	    WebElement totalCosts = driver.findElement(By.id("txtTotalCosts"));
	    totalCosts.clear();
	    totalCosts.sendKeys("700");
	    
	    WebElement faCosts = driver.findElement(By.id("txtFACosts"));
	    faCosts.clear();
	    faCosts.sendKeys("800");
	    
	    WebElement faRate = driver.findElement(By.id("txtFARate"));
	    faRate.clear();
	    faRate.sendKeys("20");
	}

	/*
	 * Fill out Cost Share Information Tab on proposal
	 */
	public void createCostShareInformation(WebDriver driver) {
		
		WebElement costShareDdl = driver.findElement(By.id("lblSection4"));
		costShareDdl.click();
		
		//Select "No" in dropdown "Institutional Commitment Cost"
		WebElement ddlInstitutionCost = driver.findElement(By.id("ddlInstitutionalCommitmentCost"));
		ddlInstitutionCost.sendKeys(Keys.ARROW_DOWN);
		ddlInstitutionCost.sendKeys(Keys.ARROW_DOWN);
		
		//Select "No" in dropdown "Third Party Commitment Cost"
		WebElement ddlThirdParty = driver.findElement(By.id("ddlThirdPartyCommitmentCost"));
		ddlThirdParty.sendKeys(Keys.ARROW_DOWN);
		ddlThirdParty.sendKeys(Keys.ARROW_DOWN);
	}

	/*
	 * Fill out University Commitments tab of proposal
	 */
	public void createUniversityCommitments(WebDriver driver) 
	{		
		//Open the University Commitments tab
		WebElement ddlUniCommitments = driver.findElement(By.id("lblSection5"));
		ddlUniCommitments.click();
		
		//Will new or renovated space/facilities be required? Select yes/no from ddl
		WebElement ddlNewSpace = driver.findElement(By.id("ddlNewSpaceRequired"));
		ddlNewSpace.sendKeys(Keys.ARROW_DOWN);//yes
		ddlNewSpace.sendKeys(Keys.ARROW_DOWN);//no
		
		//Will rental space be required? Select yes/no from ddl
		WebElement ddlRentalSpace = driver.findElement(By.id("ddlRentalSpaceRequired"));
		ddlRentalSpace.sendKeys(Keys.ARROW_DOWN);//yes
		ddlRentalSpace.sendKeys(Keys.ARROW_DOWN);//no
		
		//Does this project require institutional commitments beyond the end date of the project?
		//Select yes/no from ddl
		WebElement ddlInstCommitments = driver.findElement(By.id("ddlInstitutionalCommitmentsRequired"));
		ddlInstCommitments.sendKeys(Keys.ARROW_DOWN);//yes
		ddlInstCommitments.sendKeys(Keys.ARROW_DOWN);//no
	}

	/*
	 * Open and fill out Conflict of Interest and Commitment Information
	 */
	public void createConflictOfInterest(WebDriver driver) 
	{
		//Open conflict of interest tab
		WebElement ddlConflictInterest = driver.findElement(By.id("lblSection6"));
		ddlConflictInterest.click();
		
		//Is there a financial conflict of interest related to this proposal?
		//Select yes/no from ddl
		WebElement ddlFinancialCOI = driver.findElement(By.id("ddlFinancialCOI"));
		ddlFinancialCOI.sendKeys(Keys.ARROW_DOWN);//yes
		ddlFinancialCOI.sendKeys(Keys.ARROW_DOWN);//no
		
		//Has the financial conflict been disclosed?
		WebElement ddlDisclosedFinancialCOI = driver.findElement(By.id("ddlDisclosedFinancialCOI"));
		ddlDisclosedFinancialCOI.sendKeys(Keys.ARROW_DOWN);//yes
		ddlDisclosedFinancialCOI.sendKeys(Keys.ARROW_DOWN);//no
		
		//Has there been a material change to your annual disclosure form? 
		WebElement ddlMaterialChanged = driver.findElement(By.id("ddlMaterialChanged"));
		ddlMaterialChanged.sendKeys(Keys.ARROW_DOWN);//yes
		ddlMaterialChanged.sendKeys(Keys.ARROW_DOWN);//no
	}

	/*
	 * Open and fill out Compliance Information
	 */
	public void createComplianceInformation(WebDriver driver) 
	{	
		//Open Compliance Information tab
		WebElement ddlComplianceInformation = driver.findElement(By.id("lblSection7"));
		ddlComplianceInformation.click();
		
		//Does this project involve the use of Human Subjects?
		//Select yes/no
		WebElement ddlUseHumanSubjects = driver.findElement(By.id("ddlUseHumanSubjects"));
		ddlUseHumanSubjects.sendKeys(Keys.ARROW_DOWN);//yes
		ddlUseHumanSubjects.sendKeys(Keys.ARROW_DOWN);//no

		//Does this project involve the use of Vertebrate Animals? 
		//Select yes/no
		WebElement ddlUseVertebrateAnimals = driver.findElement(By.id("ddlUseVertebrateAnimals"));
		ddlUseVertebrateAnimals.sendKeys(Keys.ARROW_DOWN);//yes
		ddlUseVertebrateAnimals.sendKeys(Keys.ARROW_DOWN);//no
		
		//Does this project involve Biosafety concerns? 
		//Select yes/no
		WebElement ddlInvovleBioSafety = driver.findElement(By.id("ddlInvovleBioSafety"));
		ddlInvovleBioSafety.sendKeys(Keys.ARROW_DOWN);//yes
		ddlInvovleBioSafety.sendKeys(Keys.ARROW_DOWN);//no
		
		//Does this project have Environmental Health & Safety concerns? 
		//Select yes/no
		WebElement ddlEnvironmentalConcerns = driver.findElement(By.id("ddlEnvironmentalConcerns"));
		ddlEnvironmentalConcerns.sendKeys(Keys.ARROW_DOWN);//yes
		ddlEnvironmentalConcerns.sendKeys(Keys.ARROW_DOWN);//no
	}

	/*
	 * Open and fill out Additional Information
	 */
	public void createAdditionalInformation(WebDriver driver) 
	{
		//Open Additional Information Tab
		WebElement ddlAdditionalInformation = driver.findElement(By.id("lblSection8"));
		ddlAdditionalInformation.click();

		//Do you anticipate payment(s) to foreign nationals or on behalf of foreign nationals?
		//Select yes/no
		WebElement ddlAnticipateForeignNationals = driver.findElement(By.id("ddlAnticipateForeignNationals"));
		ddlAnticipateForeignNationals.sendKeys(Keys.ARROW_DOWN);//yes
		ddlAnticipateForeignNationals.sendKeys(Keys.ARROW_DOWN);//no
		
		//Do you anticipate course release time?
		//Select yes/no
		WebElement ddlAnticipateReleaseTime = driver.findElement(By.id("ddlAnticipateReleaseTime"));
		ddlAnticipateReleaseTime.sendKeys(Keys.ARROW_DOWN);//yes
		ddlAnticipateReleaseTime.sendKeys(Keys.ARROW_DOWN);//no
		
		//Are the proposed activities related to Center for Advanced Energy Studies? 
		//Select yes/no
		WebElement ddlRelatedToEnergyStudies = driver.findElement(By.id("ddlRelatedToEnergyStudies"));
		ddlRelatedToEnergyStudies.sendKeys(Keys.ARROW_DOWN);//yes
		ddlRelatedToEnergyStudies.sendKeys(Keys.ARROW_DOWN);//no
	}

	
	/*
	 * Open and fill out Collaboration Information
	 */
	public void createCollaborationInformation(WebDriver driver) 
	{
		//Open Collaboration Information tab
		WebElement ddlCollaborationInformation = driver.findElement(By.id("lblSection9"));
		ddlCollaborationInformation.click();
		
		//Does this project involve non-funded collaborations?
		//Select yes/no
		WebElement ddlInvolveNonFundedCollabs = driver.findElement(By.id("ddlInvolveNonFundedCollabs"));
		ddlInvolveNonFundedCollabs.sendKeys(Keys.ARROW_DOWN);//yes
		ddlInvolveNonFundedCollabs.sendKeys(Keys.ARROW_DOWN);//no
	}

	/*
	 * Open and fill out Proprietary/Confidential Information
	 */
	public void createProprietaryConfidentialInformation(WebDriver driver) {
		/*
		 * click id=lblSection10 select id=ddlProprietaryInformation no select
		 * id=ddlOwnIntellectualProperty no click
		 * css=#ddlOwnIntellectualProperty > option[value="2"]
		 */
		WebElement ddlProprietaryConfidentialInformation = driver.findElement(By.id("lblSection10"));
		ddlProprietaryConfidentialInformation.click();
		
		//Does this proposal contain any confidential information which is Proprietary that should not be publicly released?
		//Select yes, on pages/no
		WebElement ddlProprietaryInformation = driver.findElement(By.id("ddlProprietaryInformation"));
		ddlProprietaryInformation.sendKeys(Keys.ARROW_DOWN);//yes, on pages
		ddlProprietaryInformation.sendKeys(Keys.ARROW_DOWN);//no

		//Will this project involve intellectual property in which the University may own or have an interest?
		//Select yes/no
		WebElement ddlOwnIntellectualProperty = driver.findElement(By.id("ddlProprietaryInformation"));
		ddlOwnIntellectualProperty.sendKeys(Keys.ARROW_DOWN);//yes
		ddlOwnIntellectualProperty.sendKeys(Keys.ARROW_DOWN);//no
	}

	/*
	 * Open and fill out Certification/Signatures
	 * Requires first calling getUserFullName:String (User)
	 * and getPositionType:String (User)
	 * in order to call with correct parameters, unless they are to be entered manually
	 */
	public void certificationSignatures(WebDriver driver, String currentUserFullName, 
			String currentUserPositionType) 
	{
		WebElement sigbox;
		//Open Certification/Signatures tab
		WebElement ddlCertificationSignatures = driver.findElement(By.id("lblSection11"));
		ddlCertificationSignatures.click();
		
		
		//find that user's name in the table... 
		//I believe this is successful
		//*** Still need to include an if statement for when user not found
		WebElement nameLabel = driver.findElement(By.xpath("//span[contains(text(),'" + 
													currentUserFullName + "')]"));
		String nameText = nameLabel.getText();
		System.out.println(nameText);

		//find the position associated with that element
		//I believe this works as well
		String currRole = nameLabel.getAttribute("role");
		System.out.println(currRole);
		
		//determine whether role and position type actually makes sense
		
		/*
		 * Gather user position type and user full name
		 * What box, if any, is available to be signed?
		 * Are all other signatures present/not present as required?
		 * Seek the box associated with user, verify that is the ONLY modifiable box
		 */
		
		//Start with making sure a PI can sign their proposal
		//These signatures should only be allowed in the "trSignPICOPI" table
		//ONLY Tenured/tenure-track or Non-tenure-track research faculty can add a new
		//proposal and be a PI *** logic following is likely backwards
		
		//currentUserPositionType.equals("Tenured/tenure-track faculty") ^ 
		//currentUserPositionType.equals("Non-tenure-track research faculty")
		
		if(currRole.equals("PI"))
		{
			//This user role can only be a Tenured/tenure-track or 
			//Non-tenure-track research faculty
			if(currentUserPositionType.equals("Tenured/tenure-track faculty") ^
					currentUserPositionType.equals("Non-tenure-track research faculty"))
				{
					sigbox = driver.findElement(By.cssSelector("input[class='sfInputbox']"));

				}
			
	/*		if(sigbox.isEnabled())
			{
				sigbox.sendKeys("siggy");
			}
	*/	}
	
		
	}

	/*
	 * Note! Attempting to SAVE the proposal without SIGNING it first does not produce
	 * an alert box
	 * Instead it navigates to the same page with span class=error under the unsigned
	 * text boxes
	 * INCOMPLETE
	 */
	public void saveProposal(WebDriver driver) {
		/*
		 * click id=btnSaveProposal storeElementPresent id=BoxAlertBtnOk
		 * assertText id=alert-BoxContenedor Save confirmation click
		 * id=alert-BoxContenedor storeElementPresent id=BoxAlertBtnOk
		 * assertText id=alert-BoxContenedor Successful message
		 */
	}
	
	/*
	 * Single method to fill out entire proposal
	 */
	public void addProposal(WebDriver driver, String userName, String positionType)
	{
		this.createProjectInformation(driver);
		this.createSponsorAndBudgetInformation(driver);
		this.createCostShareInformation(driver);
		this.createUniversityCommitments(driver);
		this.createConflictOfInterest(driver);
		this.createComplianceInformation(driver);
		this.createAdditionalInformation(driver);
		this.createCollaborationInformation(driver);
		this.createProprietaryConfidentialInformation(driver);
		this.certificationSignatures(driver, userName, positionType);
	}
}
