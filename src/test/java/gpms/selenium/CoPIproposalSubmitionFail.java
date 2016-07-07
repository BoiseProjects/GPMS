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

public class CoPIproposalSubmitionFail {
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
  public void testCoPIcantSubmitProposal() throws Exception {
    driver.get(baseUrl + "GPMS/");
    driver.findElement(By.id("user_password")).clear();
    driver.findElement(By.id("user_password")).sendKeys("gpmspassword");
    driver.findElement(By.id("user_email")).clear();
    driver.findElement(By.id("user_email")).sendKeys("selena");
    Thread.sleep(1000);
    driver.findElement(By.name("commit")).click();
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("li.sfLevel1 > a > span")).click();
    Thread.sleep(1000);
    ((JavascriptExecutor) driver)
	.executeScript("var s=document.getElementById('edit0');s.click();");
    Thread.sleep(1000);
    driver.findElement(By.id("ui-id-21")).click();
    Thread.sleep(1000);
    driver.findElement(By.name("57505b2d65dbb34d173fc6f9Co-PI")).clear();
    Thread.sleep(1000);
    driver.findElement(By.name("57505b2d65dbb34d173fc6f9Co-PI")).sendKeys("Selena");
    Thread.sleep(1000);
    driver.findElement(By.name("proposalNotes57505b2d65dbb34d173fc6f9Co-PI")).clear();
    Thread.sleep(1000);
    driver.findElement(By.name("proposalNotes57505b2d65dbb34d173fc6f9Co-PI")).sendKeys("Test");
    Thread.sleep(1000);
    driver.findElement(By.name("signaturedate57505b2d65dbb34d173fc6f9Co-PI")).click();
    Thread.sleep(1000);
    driver.findElement(By.xpath("//table[@id='trSignPICOPI']/tbody/tr[3]/td[3]")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("btnSubmitProposal")).click();
    Thread.sleep(1000);
    driver.findElement(By.id("BoxConfirmBtnOk")).click();
    Thread.sleep(1000);
    assertEquals("Submit", closeAlertAndGetItsText());
    Thread.sleep(1000);
    driver.findElement(By.id("BoxAlertBtnOk")).click();
    Thread.sleep(1000);
    driver.findElement(By.cssSelector("span.myProfile.icon-arrow-s")).click();
    Thread.sleep(1000);
    driver.findElement(By.linkText("Log Out")).click();
    Thread.sleep(1000);
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
