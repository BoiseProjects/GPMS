package gpms.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

public class Proposal {

	public void createProjectInformation(WebDriver driver) {

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
		 * id=lblSection3 click type id=txtNameOfGrantingAgency Wayne Fdn type
		 * id=txtDirectCosts 500 type id=txtTotalCosts 700 type id=txtFACosts
		 * 800 type id=txtFARate 20
		 */
	}

	public void createCostShareInformation(WebDriver driver) {
		/*
		 * click id=lblSection4 select id=ddlInstitutionalCommitmentCost no
		 * select id=ddlThirdPartyCommitmentCost no click
		 * css=#ddlThirdPartyCommitmentCost > option[value="2"]
		 */
	}

	public void createUniversityCommitments(WebDriver driver) {
		/*
		 * click id=lblSection5 select id=ddlNewSpaceRequired no select
		 * id=ddlRentalSpaceRequired no select
		 * id=ddlInstitutionalCommitmentsRequired no click
		 * css=#ddlInstitutionalCommitmentsRequired > option[value="2"]
		 */
	}

	public void createConflictOfInterest(WebDriver driver) {
		/*
		 * click id=lblSection6 select id=ddlFinancialCOI no select
		 * id=ddlDisclosedFinancialCOI no select id=ddlMaterialChanged no click
		 * css=#ddlMaterialChanged > option[value="2"]
		 */
	}

	public void createComplianceInformation(WebDriver driver) {
		/*
		 * click id=ui-id-13 select id=ddlUseHumanSubjects no select
		 * id=ddlUseVertebrateAnimals no select id=ddlInvovleBioSafety no select
		 * id=ddlEnvironmentalConcerns no click css=#ddlEnvironmentalConcerns >
		 * option[value="2"]
		 */
	}

	public void createAdditionalInformation(WebDriver driver) {
		/*
		 * click id=lblSection8 select id=ddlAnticipateForeignNationals no
		 * select id=ddlAnticipateReleaseTime no select
		 * id=ddlAnticipateReleaseTime no select id=ddlRelatedToEnergyStudies
		 * click css=#ddlRelatedToEnergyStudies > option[value="2"]
		 */
	}

	public void createCollaborationInformation(WebDriver driver) {
		/*
		 * click id=lblSection9 select id=ddlInvolveNonFundedCollabs no click
		 * css=#ddlInvolveNonFundedCollabs > option[value="2"]
		 */
	}

	public void createProprietaryConfidentialInformation(WebDriver driver) {
		/*
		 * click id=lblSection10 select id=ddlProprietaryInformation no select
		 * id=ddlOwnIntellectualProperty no click
		 * css=#ddlOwnIntellectualProperty > option[value="2"]
		 */
	}

	public void certificationSignatures(WebDriver driver) {
		/*
		 * click id=lblSection11 type id=pi_signature SIGNATURE type
		 * name=proposalNotes574f638565dbb34d17834b33PI ENTERED SIG click
		 * id=pi_signaturedate
		 */
	}

	public void saveProposal(WebDriver driver) {
		/*
		 * click id=btnSaveProposal storeElementPresent id=BoxAlertBtnOk
		 * assertText id=alert-BoxContenedor Save confirmation click
		 * id=alert-BoxContenedor storeElementPresent id=BoxAlertBtnOk
		 * assertText id=alert-BoxContenedor Successful message
		 */
	}
}
