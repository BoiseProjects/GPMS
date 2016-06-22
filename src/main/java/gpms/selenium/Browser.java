package gpms.selenium;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * Created by: Liliana Acevedo
 * last modified: 6/20/16
 */

public class Browser {
	long timeoutInSeconds = 4;

	// Method to call driver and open web app
	// No parameters required
	public WebDriver openWebApp() {
		System.setProperty("webdriver.gecko.driver",
				"C:/Users/Liliana/workspace/Tools/Marionette/wires.exe");

		WebDriver driver = new MarionetteDriver();
		String appUrl = "http://seal.boisestate.edu:8080/GPMS/";
		driver.get(appUrl);

		// maximize the browser window
		driver.manage().window().maximize();
		return driver;
	}

	public boolean compareTitle(String expectedTitle, WebDriver driver) {
		String actualTitle = driver.getTitle();

		// compare actual to expected title
		if (expectedTitle.equals(actualTitle)) {
			System.out
					.println("Verification Successful - Title of page matches expected: "
							+ actualTitle);
			return true;
		} else {
			System.out
					.println("Verification failed - Expected title does not match actual: "
							+ actualTitle);
			return false;
		}
	}

	// Purpose: click the myProfile dropdown list icon
	public void clickProfileDropdown(WebDriver driver) {
		WebElement myProfileDrop = driver.findElement(By
				.cssSelector("span[class='myProfile icon-arrow-s']"));
		myProfileDrop.click();
	}

	// The tricky part here is identifying where the header is actually located
	public boolean verifyPageHeader(String expectedHeader, WebDriver driver) {

		WebElement header;
		String actualHeader;
		header = driver.findElement(By.cssSelector("h1 > span"));
		actualHeader = header.getText();

		if (actualHeader.equals("")) {
			header = driver.findElement(By.id("lblFormHeading"));
			actualHeader = header.getText();
		}
		// compare actual to expected title
		if (expectedHeader.equals(actualHeader)) {
			System.out
					.println("Verification Successful - Header of page matches expected: "
							+ actualHeader);
			return true;
		} else {
			System.out
					.println("Verification failed - Expected header does not match actual: "
							+ actualHeader);
			return false;
		}
	}

	public void waitForPageLoad(WebDriver driver) {

		WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
		driver.manage().timeouts()
				.implicitlyWait(timeoutInSeconds, TimeUnit.SECONDS);
		System.out.println("Completed implicit wait");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By
				.id("topStickybar_hypLogo")));
		System.out.println("Home logo detected");

	}

	// Alert boxes for ERROR messages have the tag
	// <div id="alert-BoxContenedor" class="BoxError">
	public void errorConfirm(WebDriver driver) {
		if (driver.findElement(By.className("BoxError")).isDisplayed()) {
			System.out.println("Element is Visible: Error alert");
			// Select the OKAY button to close the alert
			this.closeAlert(driver);
		} else {
			System.out.println("Element is InVisible: lack of error alert");
		}
	}

	// Purpose: Close the alert box by selecting the OKAY button
	public void closeAlert(WebDriver driver) {
		WebElement alertOkBtn = driver.findElement(By.id("BoxAlertBtnOk"));
		alertOkBtn.click();
	}

	// Purpose: Hover over an icon to present a hidden dropdown menu and
	// select an item from the list
	public void hoverClick(WebDriver driver) {
		Actions act = new Actions(driver);
		WebElement mainMenu = driver.findElement(By
				.cssSelector("div[class='cssClassActionOnClickShow"));
		act.moveToElement(mainMenu);

		WebElement subMenu = driver.findElement(By.id("edit0"));
		act.moveToElement(subMenu);
		act.click().build().perform();
	}
}
