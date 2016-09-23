package gpms.selenium;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class testDelegation {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {		
		System.setProperty("webdriver.chrome.driver",
				"D:/chromedriver_win32/chromedriver.exe");
		driver = new ChromeDriver();
		baseUrl = "http://seal.boisestate.edu:8080/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testWeb() throws Exception {
		driver.get(baseUrl + "GPMS/");
		driver.findElement(By.id("user_password")).clear();
		driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
		driver.findElement(By.id("user_email")).clear();
		driver.findElement(By.id("user_email")).sendKeys("edmund");
		driver.findElement(By.name("commit")).click();
		driver.findElement(By.cssSelector("li.sfLevel1 > a > span")).click();
		driver.findElement(By.id("edit0")).click();
		driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s"))
				.click();
		driver.findElement(By.linkText("Log Out")).click();
		
		Thread.sleep(2000);
		
		driver.get(baseUrl + "GPMS/");
		driver.findElement(By.id("user_password")).clear();
		driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
		driver.findElement(By.id("user_email")).clear();
		driver.findElement(By.id("user_email"))
				.sendKeys("chaircomputerscience");
		driver.findElement(By.name("commit")).click();
		driver.findElement(By.cssSelector("li.sfLevel1 > a > span")).click();
		driver.findElement(By.id("edit0")).click();
		driver.findElement(By.id("btnApproveProposal")).click();
		driver.findElement(By.linkText("Delegation")).click();
		driver.findElement(By.id("btnAddNew")).click();
		driver.findElement(By.id("chkAction_0")).click();
		driver.findElement(By.id("txtDelegationFrom")).click();
		driver.findElement(By.linkText("1")).click();
		driver.findElement(By.id("txtDelegationTo")).click();
		driver.findElement(By.linkText("25")).click();
		driver.findElement(By.id("txtDelegationReason")).clear();
		driver.findElement(By.id("txtDelegationReason"))
				.sendKeys("Sick Leave!");
		driver.findElement(By.id("txtDelegationTo")).click();
		driver.findElement(By.id("btnSaveDelegation")).click();
		driver.findElement(By.id("BoxConfirmBtnOk")).click();
		driver.findElement(By.id("BoxAlertBtnOk")).click();
		driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s"))
				.click();
		driver.findElement(By.linkText("Log Out")).click();
		
		Thread.sleep(2000);
		
		driver.get(baseUrl + "GPMS/");
		driver.findElement(By.id("user_password")).clear();
		driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
		driver.findElement(By.id("user_email")).clear();
		driver.findElement(By.id("user_email")).sendKeys("edmund");
		driver.findElement(By.id("user_email")).click();
		driver.findElement(By.name("commit")).click();
		driver.findElement(By.cssSelector("li.sfLevel1 > a > span")).click();
		driver.findElement(By.id("edit0")).click();
		driver.findElement(By.id("btnApproveProposal")).click();
		driver.findElement(By.name("57505b9e65dbb34d173fc701Department_Chair"))
				.clear();
		driver.findElement(By.name("57505b9e65dbb34d173fc701Department_Chair"))
				.sendKeys("Edmund");
		driver.findElement(
				By.name("proposalNotes57505b9e65dbb34d173fc701Department_Chair"))
				.click();
		driver.findElement(
				By.name("proposalNotes57505b9e65dbb34d173fc701Department_Chair"))
				.clear();
		driver.findElement(
				By.name("proposalNotes57505b9e65dbb34d173fc701Department_Chair"))
				.sendKeys("Reviewed!");
		driver.findElement(By.id("btnApproveProposal")).click();
		driver.findElement(By.id("BoxConfirmBtnOk")).click();
		driver.findElement(By.id("BoxAlertBtnOk")).click();
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
