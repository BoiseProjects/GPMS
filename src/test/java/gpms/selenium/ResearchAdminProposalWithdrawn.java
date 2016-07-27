package gpms.selenium;

import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class ResearchAdminProposalWithdrawn {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	System.setProperty("webdriver.chrome.driver", "E:/REU Internship/Selenium/chromedriver.exe");
    driver = new ChromeDriver();
    baseUrl = "http://seal.boisestate.edu:8080/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testNewProposal() throws Exception {
    driver.get(baseUrl + "GPMS/");
    driver.findElement(By.id("user_email")).clear();
    driver.findElement(By.id("user_email")).sendKeys("nicholaschapa@u.boisestate.edu");
    driver.findElement(By.id("user_password")).clear();
    driver.findElement(By.id("user_password")).sendKeys("Nickman5030");
    Thread.sleep(1000);
    driver.findElement(By.name("commit")).click();
    
    // Warning: assertTextPresent may require manual changes
    
    assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));
    driver.findElement(By.cssSelector("li.sfLevel1 > a > span")).click();
    driver.findElement(By.id("btnAddNew")).click();
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("i.sidebarExpand")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("lblSection2")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("txtProjectTitle")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("txtProjectTitle")).clear();
    
    int randTest = (int)(Math.random() * 9999);
    Thread.sleep(1000);
    driver.findElement(By.id("txtProjectTitle")).sendKeys("Proposal withdraw Test" + randTest);
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("td.cssClassTableRightCol")).click();
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlProjectType"))).selectByVisibleText("Research-Applied");
    Thread.sleep(1000);
    driver.findElement(By.id("txtDueDate")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("ddlTypeOfRequest")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("txtDueDate")).click();
    Thread.sleep(1000);
    driver.findElement(By.linkText("8")).click();
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlTypeOfRequest"))).selectByVisibleText("New Proposal");
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlLocationOfProject"))).selectByVisibleText("On-campus");
    Thread.sleep(1000);
    driver.findElement(By.id("txtProjectPeriodFrom")).click();
    Thread.sleep(1000);
    driver.findElement(By.linkText("2")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("txtProjectPeriodTo")).click();
    Thread.sleep(1000);
    driver.findElement(By.linkText("3")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("lblSection3")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("txtNameOfGrantingAgency")).clear();
    Thread.sleep(1000);
    driver.findElement(By.id("txtNameOfGrantingAgency")).sendKeys("NSF");
    Thread.sleep(1000);
    driver.findElement(By.id("txtDirectCosts")).clear();
    Thread.sleep(1000);
    driver.findElement(By.id("txtDirectCosts")).sendKeys("500");
    Thread.sleep(1000);
    driver.findElement(By.id("txtFACosts")).clear();
    Thread.sleep(1000);
    driver.findElement(By.id("txtFACosts")).sendKeys("900");
    Thread.sleep(1000);
    driver.findElement(By.id("txtTotalCosts")).clear();
    Thread.sleep(1000);
    driver.findElement(By.id("txtTotalCosts")).sendKeys("1100");
    Thread.sleep(1000);
    driver.findElement(By.id("txtFARate")).clear();
    Thread.sleep(1000);
    driver.findElement(By.id("txtFARate")).sendKeys("20");
    Thread.sleep(1000);
    driver.findElement(By.id("lblSection4")).click();
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlInstitutionalCommitmentCost"))).selectByVisibleText("Yes");
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("#ddlInstitutionalCommitmentCost > option[value=\"1\"]")).click();
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlThirdPartyCommitmentCost"))).selectByVisibleText("Yes");
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("#ddlThirdPartyCommitmentCost > option[value=\"1\"]")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("ui-id-9")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("ddlNewSpaceRequired")).click();
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlNewSpaceRequired"))).selectByVisibleText("Yes");
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlRentalSpaceRequired"))).selectByVisibleText("Yes");
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlInstitutionalCommitmentsRequired"))).selectByVisibleText("Yes");
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("#ddlInstitutionalCommitmentsRequired > option[value=\"1\"]")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("lblSection6")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("ddlFinancialCOI")).click();
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlFinancialCOI"))).selectByVisibleText("Yes");
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlDisclosedFinancialCOI"))).selectByVisibleText("Yes");
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("#ddlDisclosedFinancialCOI > option[value=\"1\"]")).click();
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlMaterialChanged"))).selectByVisibleText("Yes");
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("#ddlMaterialChanged > option[value=\"1\"]")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("ui-id-13")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("ddlUseHumanSubjects")).click();
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlUseHumanSubjects"))).selectByVisibleText("Yes");
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("#ddlUseHumanSubjects > option[value=\"1\"]")).click();
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlUseHumanSubjects"))).selectByVisibleText("No");
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("#ddlUseHumanSubjects > option[value=\"2\"]")).click();
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlUseVertebrateAnimals"))).selectByVisibleText("No");
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlInvovleBioSafety"))).selectByVisibleText("No");
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlEnvironmentalConcerns"))).selectByVisibleText("No");
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("#ddlEnvironmentalConcerns > option[value=\"2\"]")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("ui-id-15")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("ddlAnticipateForeignNationals")).click();
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlAnticipateForeignNationals"))).selectByVisibleText("No");
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlAnticipateReleaseTime"))).selectByVisibleText("No");
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlRelatedToEnergyStudies"))).selectByVisibleText("No");
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("#ddlRelatedToEnergyStudies > option[value=\"2\"]")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("ui-id-17")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("ddlInvolveNonFundedCollabs")).click();
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlInvolveNonFundedCollabs"))).selectByVisibleText("No");
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("#ddlInvolveNonFundedCollabs > option[value=\"2\"]")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("ui-id-19")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("ddlProprietaryInformation")).click();
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlProprietaryInformation"))).selectByVisibleText("No");
    Thread.sleep(1000);
    new Select(driver.findElement(By.id("ddlOwnIntellectualProperty"))).selectByVisibleText("No");
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("#ddlOwnIntellectualProperty > option[value=\"2\"]")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("ui-id-21")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("pi_signature")).clear();
    Thread.sleep(1000);
    driver.findElement(By.id("pi_signature")).sendKeys("Nicholas chapa");
    Thread.sleep(1000);
    driver.findElement(By.id("pi_signaturedate")).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath("//table[@id='trSignPICOPI']/tbody/tr/td[3]")).click();
    Thread.sleep(1000);
    driver.findElement(By.name("proposalNotes574f7adb65dbb34d17834b57PI")).clear();
    Thread.sleep(1000);
    driver.findElement(By.name("proposalNotes574f7adb65dbb34d17834b57PI")).sendKeys("Test");
    Thread.sleep(1000);
    driver.findElement(By.id("ui-id-25")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("btnSaveProposal")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("BoxConfirmBtnOk")).click();
    Thread.sleep(1000);
    assertEquals("Save", closeAlertAndGetItsText());
    // Warning: assertTextPresent may require manual changes
    Thread.sleep(1000);
    assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));
    Thread.sleep(1000);
    driver.findElement(By.id("BoxAlertBtnOk")).click();
    Thread.sleep(1000);
    ((JavascriptExecutor) driver)
	.executeScript("var s=document.getElementById('edit0');s.click();");
    Thread.sleep(1000);
    driver.findElement(By.id("btnSubmitProposal")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("BoxConfirmBtnOk")).click();
    Thread.sleep(1000);
    assertEquals("Submit", closeAlertAndGetItsText());
    Thread.sleep(1000);
    // Warning: assertTextPresent may require manual changes
    assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));
    Thread.sleep(1000);
    driver.findElement(By.id("BoxAlertBtnOk")).click();
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s")).click();
    Thread.sleep(1000);
    driver.findElement(By.linkText("Log Out")).click();
    Thread.sleep(7000);
    
    //Chair approval
    driver.get(baseUrl + "GPMS/");
    driver.findElement(By.id("user_email")).clear();
    driver.findElement(By.id("user_email")).sendKeys("chairchemistry@gmail.com");
    driver.findElement(By.id("user_password")).clear();
    driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
    Thread.sleep(1000);
    driver.findElement(By.name("commit")).click();
    Thread.sleep(1000);
    // Warning: assertTextPresent may require manual changes
    assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("li.sfLevel1 > a > span")).click();
    Thread.sleep(1000);
    ((JavascriptExecutor) driver)
	.executeScript("var s=document.getElementById('edit0');s.click();");
    Thread.sleep(1000);
    driver.findElement(By.id("ui-id-21")).click();
    Thread.sleep(1000);
    driver.findElement(By.name("5745f29ebcbb29192ce0d42fDepartment_Chair")).click();
    Thread.sleep(1000);
    driver.findElement(By.name("5745f29ebcbb29192ce0d42fDepartment_Chair")).clear();
    Thread.sleep(1000);
    driver.findElement(By.name("5745f29ebcbb29192ce0d42fDepartment_Chair")).sendKeys("chair");
    Thread.sleep(1000);
    driver.findElement(By.name("signaturedate5745f29ebcbb29192ce0d42fDepartment_Chair")).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath("//table[@id='trSignChair']/tbody/tr/td[3]")).click();
    Thread.sleep(1000);
    driver.findElement(By.name("proposalNotes5745f29ebcbb29192ce0d42fDepartment_Chair")).clear();
    Thread.sleep(1000);
    driver.findElement(By.name("proposalNotes5745f29ebcbb29192ce0d42fDepartment_Chair")).sendKeys("Test");
    Thread.sleep(1000);
    driver.findElement(By.id("btnApproveProposal")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("BoxConfirmBtnOk")).click();
    Thread.sleep(1000);
    assertEquals("Approve", closeAlertAndGetItsText());
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("h2")).click();
    Thread.sleep(1000);
    // Warning: assertTextPresent may require manual changes
    assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));
    Thread.sleep(1000);
    driver.findElement(By.id("BoxAlertBtnOk")).click();
    Thread.sleep(1000);
    // ERROR: Caught exception [ERROR: Unsupported command [selectWindow | null | ]]
    driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s")).click();
    Thread.sleep(1000);
    driver.findElement(By.linkText("Log Out")).click();
    
    //Business manager approval
    driver.get(baseUrl + "GPMS/");
    driver.findElement(By.id("user_email")).clear();
    driver.findElement(By.id("user_email")).sendKeys("bmchemistry1@gmail.com");
    driver.findElement(By.id("user_password")).clear();
    driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
    Thread.sleep(1000);
    driver.findElement(By.name("commit")).click();
    // Warning: assertTextPresent may require manual changes
    Thread.sleep(1000);
    assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("li.sfLevel1 > a > span")).click();
    Thread.sleep(1000);
    
    ((JavascriptExecutor) driver)
	.executeScript("var s=document.getElementById('edit0');s.click();");
        
    Thread.sleep(1000);
    //driver.findElement(By.id("edit0")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("ui-id-21")).click();
    Thread.sleep(1000);
    driver.findElement(By.name("574620c6bcbb29150487642aBusiness_Manager")).clear();
    Thread.sleep(1000);
    driver.findElement(By.name("574620c6bcbb29150487642aBusiness_Manager")).sendKeys("Business Manager");
    Thread.sleep(1000);
    driver.findElement(By.name("proposalNotes574620c6bcbb29150487642aBusiness_Manager")).clear();
    Thread.sleep(1000);
    driver.findElement(By.name("proposalNotes574620c6bcbb29150487642aBusiness_Manager")).sendKeys("Test");
    Thread.sleep(1000);
    driver.findElement(By.name("signaturedate574620c6bcbb29150487642aBusiness_Manager")).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath("//table[@id='trSignBusinessManager']/tbody/tr/td[3]")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("btnApproveProposal")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("BoxConfirmBtnOk")).click();
    Thread.sleep(1000);
    assertEquals("Approve", closeAlertAndGetItsText());
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("h2")).click();
    Thread.sleep(1000);
    // Warning: assertTextPresent may require manual changes
    assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));
    Thread.sleep(1000);
    driver.findElement(By.id("BoxAlertBtnOk")).click();
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s")).click();
    Thread.sleep(1000);
    driver.findElement(By.linkText("Log Out")).click();
    Thread.sleep(7000);
    
    //Dean approval
    driver.get(baseUrl + "GPMS/");
    driver.findElement(By.id("user_email")).clear();
    driver.findElement(By.id("user_email")).sendKeys("deanchemistry1@gmail.com");
    driver.findElement(By.id("user_password")).clear();
    driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
    Thread.sleep(1000);
    driver.findElement(By.name("commit")).click();
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("li.sfLevel1 > a > span")).click();
    Thread.sleep(1000);
    ((JavascriptExecutor) driver)
	.executeScript("var s=document.getElementById('edit0');s.click();");
    Thread.sleep(1000);
    driver.findElement(By.id("btnApproveProposal")).click();
    Thread.sleep(1000);
    driver.findElement(By.name("57460657bcbb29192ce0d483Dean")).clear();
    Thread.sleep(1000);
    driver.findElement(By.name("57460657bcbb29192ce0d483Dean")).sendKeys("Dean");
    Thread.sleep(1000);
    driver.findElement(By.name("proposalNotes57460657bcbb29192ce0d483Dean")).clear();
    Thread.sleep(1000);
    driver.findElement(By.name("proposalNotes57460657bcbb29192ce0d483Dean")).sendKeys("Test");
    Thread.sleep(1000);
    driver.findElement(By.xpath("//table[@id='trSignDean']/tbody/tr/td[3]")).click();
    Thread.sleep(1000);
    driver.findElement(By.name("signaturedate57460657bcbb29192ce0d483Dean")).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath("//table[@id='trSignAdministrator']/tbody/tr/td[3]")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("btnApproveProposal")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("BoxConfirmBtnOk")).click();
    Thread.sleep(1000);
    // Warning: assertTextPresent may require manual changes
    assertEquals("Approve", closeAlertAndGetItsText());
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("h2")).click();
    Thread.sleep(1000);
    assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*$"));
    Thread.sleep(1000);
    driver.findElement(By.id("BoxAlertBtnOk")).click();
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s")).click();
    Thread.sleep(1000);
    driver.findElement(By.linkText("Log Out")).click();
    Thread.sleep(1000);
    
    //Research admin Withdraws proposal
    driver.get(baseUrl + "GPMS/");
    driver.findElement(By.id("user_password")).clear();
    driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
    driver.findElement(By.id("user_email")).clear();
    driver.findElement(By.id("user_email")).sendKeys("racomputerscience@gmail.com");
    Thread.sleep(1000);
    driver.findElement(By.name("commit")).click();
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("li.sfLevel1 > a > span")).click();
    Thread.sleep(1000);
    ((JavascriptExecutor) driver)
	.executeScript("var s=document.getElementById('edit0');s.click();");
    Thread.sleep(1000);
    driver.findElement(By.id("btnWithdrawProposal")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("BoxConfirmBtnOk")).click();
    Thread.sleep(1000);
    assertEquals("Withdraw", closeAlertAndGetItsText());
    Thread.sleep(1000);
    driver.findElement(By.id("BoxAlertBtnOk")).click();
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s")).click();
    Thread.sleep(1000);
    driver.findElement(By.linkText("Log Out")).click();
    Thread.sleep(2000);
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