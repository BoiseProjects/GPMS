package gpms.selenium;

/*CoPIattemptsToDeleteCoPI
 * Made by: Nick chapa
 * The Co PI attempts to delete another Co PI. Program will exit with element not visible exception if test is successful.
 */

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class CoPIattemptsToDeleteCoPI {
	private WebDriver driver;
	private String baseUrl;
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
	public void testPIaddsCoPI() throws Exception {
		driver.get(baseUrl + "GPMS/");
		driver.findElement(By.id("user_password")).clear();
		driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
		driver.findElement(By.id("user_email")).clear();
		driver.findElement(By.id("user_email")).sendKeys(
				"nicholas1234@gmail.com");
		Thread.sleep(1000);
		driver.findElement(By.name("commit")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("li.sfLevel1 > a > span")).click();
		Thread.sleep(1000);

		((JavascriptExecutor) driver)
				.executeScript("var s=document.getElementById('edit0');s.click();");

		Thread.sleep(1000);
		driver.findElement(By.id("ui-id-1")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("AddCoPI")).click();
		Thread.sleep(1000);
		driver.findElement(By.name("AddCoPI")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("btnSaveProposal")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("BoxConfirmBtnOk")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("BoxAlertBtnOk")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s"))
				.click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("Log Out")).click();
		Thread.sleep(2000);

		// CoPI attempts deletion
		driver.get(baseUrl + "GPMS/");
		driver.findElement(By.id("user_password")).clear();
		driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
		driver.findElement(By.id("user_email")).clear();
		driver.findElement(By.id("user_email")).sendKeys("selena");
		Thread.sleep(1000);
		driver.findElement(By.name("commit")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("My Proposals")).click();
		Thread.sleep(1000);
		((JavascriptExecutor) driver)
				.executeScript("var s=document.getElementById('edit0');s.click();");
		Thread.sleep(1000);
		driver.findElement(By.id("ui-id-1")).click();
		Thread.sleep(1000);
		((JavascriptExecutor) driver)
		.executeScript("var s=document.getElementById('DeleteOption');s.click();");
		Thread.sleep(1000);
		driver.findElement(By.id("BoxConfirmBtnOk")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s"))
				.click();
		Thread.sleep(1000);
		driver.findElement(By.id("btnSaveProposal")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("BoxConfirmBtnOk")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("BoxAlertBtnOk")).click();
		Thread.sleep(1000);
		driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s"))
				.click();
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

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

}