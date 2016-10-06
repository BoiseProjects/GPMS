package gpms.selenium;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class DelegationByChair {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver",
				"D:/!!#!@Playground/chromedriver.exe");
		driver = new ChromeDriver();
		baseUrl = "http://seal.boisestate.edu:8080/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testWeb() throws Exception {
		// Add Proposal with Computer Science
		driver.get(baseUrl + "GPMS/");
		driver.findElement(By.id("user_email")).clear();
		driver.findElement(By.id("user_email")).sendKeys("milson");
		driver.findElement(By.id("user_password")).clear();
		driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
		Thread.sleep(200);
		driver.findElement(By.name("commit")).click();
		Thread.sleep(200);
		driver.findElement(By.linkText("My Proposals")).click();
		Thread.sleep(200);
		driver.findElement(By.id("btnAddNew")).click();
		Thread.sleep(200);
		driver.findElement(By.cssSelector("i.sidebarExpand")).click();
		Thread.sleep(5000);
		driver.findElement(By.id("lblSection2")).click();
		Thread.sleep(200);
		driver.findElement(By.id("txtProjectTitle")).click();
		Thread.sleep(200);
		driver.findElement(By.id("txtProjectTitle")).clear();
		Thread.sleep(200);
		int randTest = (int) (Math.random() * 9999);
		driver.findElement(By.id("txtProjectTitle")).sendKeys(
				"Proposal test" + randTest);
		Thread.sleep(200);
		driver.findElement(By.cssSelector("td.cssClassTableRightCol")).click();
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlProjectType")))
				.selectByVisibleText("Research-Applied");
		Thread.sleep(200);
		driver.findElement(By.id("txtDueDate")).click();
		Thread.sleep(200);
		driver.findElement(By.id("ddlTypeOfRequest")).click();
		Thread.sleep(200);
		driver.findElement(By.id("txtDueDate")).click();
		Thread.sleep(200);
		driver.findElement(By.linkText("8")).click();
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlTypeOfRequest")))
				.selectByVisibleText("New Proposal");
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlLocationOfProject")))
				.selectByVisibleText("On-campus");
		Thread.sleep(200);
		driver.findElement(By.id("txtProjectPeriodFrom")).click();
		Thread.sleep(200);
		driver.findElement(By.linkText("2")).click();
		Thread.sleep(200);
		driver.findElement(By.id("txtProjectPeriodTo")).click();
		Thread.sleep(200);
		driver.findElement(By.linkText("3")).click();
		Thread.sleep(200);
		driver.findElement(By.id("lblSection3")).click();
		Thread.sleep(200);
		driver.findElement(By.id("txtNameOfGrantingAgency")).clear();
		Thread.sleep(200);
		driver.findElement(By.id("txtNameOfGrantingAgency")).sendKeys("NSF");
		Thread.sleep(200);
		driver.findElement(By.id("txtDirectCosts")).clear();
		Thread.sleep(200);
		driver.findElement(By.id("txtDirectCosts")).sendKeys("500");
		Thread.sleep(200);
		driver.findElement(By.id("txtFACosts")).clear();
		Thread.sleep(200);
		driver.findElement(By.id("txtFACosts")).sendKeys("900");
		Thread.sleep(200);
		driver.findElement(By.id("txtTotalCosts")).clear();
		Thread.sleep(200);
		driver.findElement(By.id("txtTotalCosts")).sendKeys("1100");
		Thread.sleep(200);
		driver.findElement(By.id("txtFARate")).clear();
		Thread.sleep(200);
		driver.findElement(By.id("txtFARate")).sendKeys("20");
		Thread.sleep(200);
		driver.findElement(By.id("lblSection4")).click();
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlInstitutionalCommitmentCost")))
				.selectByVisibleText("Yes");
		Thread.sleep(200);
		driver.findElement(
				By.cssSelector("#ddlInstitutionalCommitmentCost > option[value=\"1\"]"))
				.click();
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlThirdPartyCommitmentCost")))
				.selectByVisibleText("Yes");
		Thread.sleep(200);
		driver.findElement(
				By.cssSelector("#ddlThirdPartyCommitmentCost > option[value=\"1\"]"))
				.click();
		Thread.sleep(200);
		driver.findElement(By.id("ui-id-9")).click();
		Thread.sleep(200);
		driver.findElement(By.id("ddlNewSpaceRequired")).click();
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlNewSpaceRequired")))
				.selectByVisibleText("Yes");
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlRentalSpaceRequired")))
				.selectByVisibleText("Yes");
		Thread.sleep(200);
		new Select(driver.findElement(By
				.id("ddlInstitutionalCommitmentsRequired")))
				.selectByVisibleText("Yes");
		Thread.sleep(200);
		driver.findElement(
				By.cssSelector("#ddlInstitutionalCommitmentsRequired > option[value=\"1\"]"))
				.click();
		Thread.sleep(200);
		driver.findElement(By.id("lblSection6")).click();
		Thread.sleep(200);
		driver.findElement(By.id("ddlFinancialCOI")).click();
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlFinancialCOI")))
				.selectByVisibleText("Yes");
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlDisclosedFinancialCOI")))
				.selectByVisibleText("Yes");
		Thread.sleep(200);
		driver.findElement(
				By.cssSelector("#ddlDisclosedFinancialCOI > option[value=\"1\"]"))
				.click();
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlMaterialChanged")))
				.selectByVisibleText("Yes");
		Thread.sleep(200);
		driver.findElement(
				By.cssSelector("#ddlMaterialChanged > option[value=\"1\"]"))
				.click();
		Thread.sleep(200);
		driver.findElement(By.id("ui-id-13")).click();
		Thread.sleep(200);
		driver.findElement(By.id("ddlUseHumanSubjects")).click();
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlUseHumanSubjects")))
				.selectByVisibleText("Yes");
		Thread.sleep(200);
		driver.findElement(
				By.cssSelector("#ddlUseHumanSubjects > option[value=\"1\"]"))
				.click();
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlUseHumanSubjects")))
				.selectByVisibleText("No");
		Thread.sleep(200);
		driver.findElement(
				By.cssSelector("#ddlUseHumanSubjects > option[value=\"2\"]"))
				.click();
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlUseVertebrateAnimals")))
				.selectByVisibleText("No");
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlInvovleBioSafety")))
				.selectByVisibleText("No");
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlEnvironmentalConcerns")))
				.selectByVisibleText("No");
		Thread.sleep(200);
		driver.findElement(
				By.cssSelector("#ddlEnvironmentalConcerns > option[value=\"2\"]"))
				.click();
		Thread.sleep(200);
		driver.findElement(By.id("ui-id-15")).click();
		Thread.sleep(200);
		driver.findElement(By.id("ddlAnticipateForeignNationals")).click();
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlAnticipateForeignNationals")))
				.selectByVisibleText("No");
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlAnticipateReleaseTime")))
				.selectByVisibleText("No");
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlRelatedToEnergyStudies")))
				.selectByVisibleText("No");
		Thread.sleep(200);
		driver.findElement(
				By.cssSelector("#ddlRelatedToEnergyStudies > option[value=\"2\"]"))
				.click();
		Thread.sleep(200);
		driver.findElement(By.id("ui-id-17")).click();
		Thread.sleep(200);
		driver.findElement(By.id("ddlInvolveNonFundedCollabs")).click();
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlInvolveNonFundedCollabs")))
				.selectByVisibleText("No");
		Thread.sleep(200);
		driver.findElement(
				By.cssSelector("#ddlInvolveNonFundedCollabs > option[value=\"2\"]"))
				.click();
		Thread.sleep(200);
		driver.findElement(By.id("ui-id-19")).click();
		Thread.sleep(200);
		driver.findElement(By.id("ddlProprietaryInformation")).click();
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlProprietaryInformation")))
				.selectByVisibleText("No");
		Thread.sleep(200);
		new Select(driver.findElement(By.id("ddlOwnIntellectualProperty")))
				.selectByVisibleText("No");
		Thread.sleep(200);
		driver.findElement(
				By.cssSelector("#ddlOwnIntellectualProperty > option[value=\"2\"]"))
				.click();
		Thread.sleep(200);
		driver.findElement(By.id("ui-id-21")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("pi_signature")).clear();
		Thread.sleep(200);
		driver.findElement(By.id("pi_signature")).sendKeys("Milson Munakami");
		Thread.sleep(200);
		driver.findElement(
				By.xpath("//table[@id='trSignPICOPI']/tbody/tr/td[3]")).click();
		Thread.sleep(200);
		driver.findElement(By.name("proposalNotes57489f2565dbb306cef6702cPI"))
				.clear();
		Thread.sleep(200);
		driver.findElement(By.name("proposalNotes57489f2565dbb306cef6702cPI"))
				.sendKeys("Test");
		Thread.sleep(200);
		driver.findElement(By.id("ui-id-25")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("btnSaveProposal")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("BoxConfirmBtnOk")).click();
		Thread.sleep(5000);
		assertTrue(driver.findElement(By.cssSelector("BODY")).getText()
				.matches("^[\\s\\S]*$"));
		Thread.sleep(200);
		driver.findElement(By.id("BoxAlertBtnOk")).click();
		Thread.sleep(200);

		((JavascriptExecutor) driver)
				.executeScript("var s=document.getElementById('edit0');s.click();");
		Thread.sleep(1000);
		driver.findElement(By.id("btnSubmitProposal")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("BoxConfirmBtnOk")).click();
		Thread.sleep(2000);
		assertTrue(driver.findElement(By.cssSelector("BODY")).getText()
				.matches("^[\\s\\S]*$"));
		Thread.sleep(200);
		driver.findElement(By.id("BoxAlertBtnOk")).click();
		Thread.sleep(200);
		driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s"))
				.click();
		Thread.sleep(200);
		driver.findElement(By.linkText("Log Out")).click();
		Thread.sleep(10000);

		// Associate Chair Approve Failure
		driver.get(baseUrl + "GPMS/");
		driver.findElement(By.id("user_email")).clear();
		driver.findElement(By.id("user_email")).sendKeys("edmund");
		driver.findElement(By.id("user_password")).clear();
		driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
		Thread.sleep(200);
		driver.findElement(By.name("commit")).click();
		Thread.sleep(200);
		driver.findElement(By.linkText("My Proposals")).click();
		Thread.sleep(200);

		((JavascriptExecutor) driver)
				.executeScript("var s=document.getElementById('edit0');s.click();");
		Thread.sleep(1000);

		// assertTrue(isElementPresent(By.id("btnApproveProposal")));

		assertFalse(driver.findElement(By.id("btnApproveProposal"))
				.isDisplayed());
		Thread.sleep(1000);

		driver.findElement(By.id("ui-id-21")).click();
		Thread.sleep(10000);

		driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s"))
				.click();
		Thread.sleep(200);
		driver.findElement(By.linkText("Log Out")).click();
		Thread.sleep(2500);

		// Chair Delegate Associate Chair
		driver.get(baseUrl + "GPMS/");
		driver.findElement(By.id("user_email")).clear();
		driver.findElement(By.id("user_email"))
				.sendKeys("chaircomputerscience");
		driver.findElement(By.id("user_password")).clear();
		driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
		Thread.sleep(200);
		driver.findElement(By.name("commit")).click();
		Thread.sleep(200);
		driver.findElement(By.linkText("Delegation")).click();
		Thread.sleep(200);

		driver.findElement(By.id("btnAddNew")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("chkAction_0")).click();
		Thread.sleep(400);
		driver.findElement(By.id("txtDelegationFrom")).click();
		Thread.sleep(400);
		driver.findElement(By.linkText("1")).click();
		Thread.sleep(400);
		driver.findElement(By.id("txtDelegationTo")).click();
		Thread.sleep(400);
		driver.findElement(By.linkText("30")).click();
		Thread.sleep(400);
		driver.findElement(By.id("txtDelegationReason")).clear();
		Thread.sleep(400);
		driver.findElement(By.id("txtDelegationReason"))
				.sendKeys("Sick Leave!");
		Thread.sleep(400);

		driver.findElement(By.id("btnSaveDelegation")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("BoxConfirmBtnOk")).click();
		Thread.sleep(5000);

		assertTrue(driver.findElement(By.cssSelector("BODY")).getText()
				.matches("^[\\s\\S]*$"));
		Thread.sleep(200);
		driver.findElement(By.id("BoxAlertBtnOk")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s"))
				.click();
		Thread.sleep(200);
		driver.findElement(By.linkText("Log Out")).click();
		Thread.sleep(2500);

		// Associate Chair Approve Success
		driver.get(baseUrl + "GPMS/");
		driver.findElement(By.id("user_email")).clear();
		driver.findElement(By.id("user_email")).sendKeys("edmund");
		driver.findElement(By.id("user_password")).clear();
		driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
		Thread.sleep(200);
		driver.findElement(By.name("commit")).click();
		Thread.sleep(200);
		driver.findElement(By.linkText("My Proposals")).click();
		Thread.sleep(200);

		((JavascriptExecutor) driver)
				.executeScript("var s=document.getElementById('edit0');s.click();");
		Thread.sleep(1000);
		driver.findElement(By.id("ui-id-21")).click();
		Thread.sleep(2000);
		driver.findElement(By.name("57505b9e65dbb34d173fc701Department_Chair"))
				.clear();
		Thread.sleep(400);
		driver.findElement(By.name("57505b9e65dbb34d173fc701Department_Chair"))
				.sendKeys("Associate chair");
		Thread.sleep(400);
		driver.findElement(
				By.xpath("//table[@id='trSignChair']/tbody/tr/td[3]")).click();
		Thread.sleep(400);
		driver.findElement(
				By.name("proposalNotes57505b9e65dbb34d173fc701Department_Chair"))
				.clear();
		Thread.sleep(400);
		driver.findElement(
				By.name("proposalNotes57505b9e65dbb34d173fc701Department_Chair"))
				.sendKeys("Test");
		Thread.sleep(400);
		driver.findElement(By.id("btnApproveProposal")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("BoxConfirmBtnOk")).click();
		Thread.sleep(10000);

		assertTrue(driver.findElement(By.cssSelector("BODY")).getText()
				.matches("^[\\s\\S]*$"));
		Thread.sleep(200);
		driver.findElement(By.id("BoxAlertBtnOk")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s"))
				.click();
		Thread.sleep(200);
		driver.findElement(By.linkText("Log Out")).click();
		Thread.sleep(5000);

		// Revocation by Chair
		driver.get(baseUrl + "GPMS/");
		driver.findElement(By.id("user_email")).clear();
		driver.findElement(By.id("user_email"))
				.sendKeys("chaircomputerscience");
		driver.findElement(By.id("user_password")).clear();
		driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
		Thread.sleep(200);
		driver.findElement(By.name("commit")).click();
		Thread.sleep(200);
		driver.findElement(By.linkText("Delegation")).click();
		Thread.sleep(200);

		((JavascriptExecutor) driver)
				.executeScript("var s=document.getElementById('edit0');s.click();");
		Thread.sleep(1000);

		driver.findElement(By.id("btnRevokeDelegation")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("BoxConfirmBtnOk")).click();
		Thread.sleep(10000);

		assertTrue(driver.findElement(By.cssSelector("BODY")).getText()
				.matches("^[\\s\\S]*$"));
		Thread.sleep(200);
		driver.findElement(By.id("BoxAlertBtnOk")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s"))
				.click();
		Thread.sleep(200);
		driver.findElement(By.linkText("Log Out")).click();
		Thread.sleep(1500);
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
