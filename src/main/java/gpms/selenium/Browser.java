package gpms.selenium;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import gpms.rest.UserService;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.MarionetteDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Created by: Liliana Acevedo
 * last modified: 7/7/16
 */

public class Browser {
	long timeoutInSeconds = 4;

	// Method to call driver and open web app
	// No parameters required
	public WebDriver openWebApp(String browser) {
		
		WebDriver driver;
		if (browser.equalsIgnoreCase("firefox"))
		{
			//ATTEMPTING TO FIND A WAY TO SUPPRESS DEBUG LOGS TO CONSOLE -
			//HAVEN'T FIGURED IT OUT... 
			System.setProperty("webdriver.gecko.driver",
					"C:/Users/Liliana/workspace/Tools/Marionette/wires.exe");
			DesiredCapabilities caps = DesiredCapabilities.firefox(); 
			LoggingPreferences logs = new LoggingPreferences(); 
			logs.enable(LogType.DRIVER, Level.INFO); 
			caps.setCapability(CapabilityType.LOGGING_PREFS, logs);
			driver = new MarionetteDriver(caps);
		}
		else //if(browser.equalsIgnoreCase("chrome"))
		{
			System.setProperty("webdriver.chrome.driver",
					"C:/Users/Liliana/workspace/Tools/Chrome/chromedriver.exe");
			driver = new ChromeDriver();
		}
			
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
	public void clickProfileDropdown(WebDriver driver) 
	{
		WebElement myProfileDrop = driver.findElement(By
				.cssSelector("span[class='myProfile icon-arrow-s']"));
		myProfileDrop.click();
	}
	
	/*
	 * Purpose: click on a link from the myProfile dropdown
	 * Requires input string indicating part of the visible link text
	 */
	public void clickProfileDDLOption(WebDriver driver, String option)
	{
		this.clickProfileDropdown(driver);
		WebElement desired = driver.findElement(By.cssSelector(option));
		desired.click();
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
	
	/*
	 * Purpose: wait for a particular element to load
	 * Requires ID of element input parameters
	 */
	public void waitForElementLoad(WebDriver driver, String currentElement)
	{
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(currentElement)));
	}
	
	/*
	 * Purpose: enable disabled element
	 * **** Not sure if this is going to work... My reading indicates the element must be 
	 * 	removed
	 */
	public void enableDisabledElement(WebDriver driver, WebElement element)
	{
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].removeAttribute('disabled');", element);
		if (element.isEnabled())
		{
			System.out.println("Element enabled");
			element.click();
		}
	}

	/*
	 * Purpose: click on a hidden element
	 */
	public void clickHiddenElement(WebDriver driver, WebElement element)
	{
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].click();", element);
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

	
	/* 
	 * Purpose: Hover over an icon to present a hidden dropdown menu and
	 * select an item from the list
	 * Currently works exclusively on the first proposal in the manage proposals list
	 */
	public void hoverClick(WebDriver driver) 
	{
		((JavascriptExecutor) driver)
        .executeScript("var s=document.getElementById('edit0');s.click();");
	}
	
	//Not working... aiming to get userID from Session
	//String ASPNET_SessionId = driver.manage().getCookieNamed("ASP.NET_SessionId").toString();
    //System.out.println(ASPNET_SessionId);
	//from...Set User Session based on selected position detail
	public void getSessionID(WebDriver driver,@Context HttpServletRequest request)
	{
		UserService haa = new UserService();
/*
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);
		String userProfileID = new String();
		String userName = new String();

		
		HttpServletRequest req;
//		HttpSession session = request.getSession();
		if (session.getAttribute("userProfileId") != null) 
			{return (String) session.getAttribute("userProfileId");}
		else 
			return "I don't have what you seek";
*/
		//String sessionID = driver.manage().getCookieNamed("session").toString();
		//System.out.println(sessionID);
	}
	
	//Purpose: click Back button at bottom of proposal page
	public void clickProposalGoBackBtn(WebDriver driver)
	{
		WebElement backBtn = driver.findElement(By.id("btnBack"));
		backBtn.click();
	}
	
	//Purpose: click Reset button at bottom of proposal page
	public void clickProposalResetBtn(WebDriver driver)
	{
		WebElement resetBtn = driver.findElement(By.id("btnReset"));
		resetBtn.click();
	}
		
	//Purpose: click Save button at bottom of proposal page
	//Will not work if any fields have not been filled in, 
	//need to account for these errors
	public void clickProposalSaveBtn(WebDriver driver)
	{
		WebElement saveBtn = driver.findElement(By.id("btnSaveProposal"));
		saveBtn.click();
	}	
	
	//Purpose: click Delete button at bottom of proposal page
	//Will not work if user does not have permission to delete proposal, 
	//need to account for these errors
	public void clickProposalDeleteBtn(WebDriver driver)
	{
		WebElement deleteBtn = driver.findElement(By.id("btnDeleteProposal"));
		deleteBtn.click();
	}	
	
	//Purpose: click Submit button at bottom of proposal page
	//Will not work if user does not have permission to submit this proposal, 
	//need to account for these errors
	public void clickProposalSubmitBtn(WebDriver driver)
	{
		WebElement submitBtn = driver.findElement(By.id("btnSubmitProposal"));
		submitBtn.click();
	}		
	
	//Purpose: click Approve button at bottom of proposal page
	//Will not work if user does not have permission to approve proposal, 
	//need to account for these errors
	public void clickProposalApproveBtn(WebDriver driver)
	{
		WebElement approveBtn = driver.findElement(By.id("btnApproveProposal"));
		approveBtn.click();
	}		
	
	//Purpose: click Disapprove button at bottom of proposal page
	//Will not work if user does not have permission to disapprove proposal, 
	//need to account for these errors
	public void clickProposalDisapproveBtn(WebDriver driver)
	{
		WebElement disapproveBtn = driver.findElement(By.id("btnDisapproveProposal"));
		disapproveBtn.click();
	}		

	//Purpose: click Withdraw button at bottom of proposal page
	//Will not work if user does not have permission to withdraw proposal, 
	//need to account for these errors
	public void clickProposalWithdrawBtn(WebDriver driver)
	{
		WebElement withdrawBtn = driver.findElement(By.id("btnWithdrawProposal"));
		withdrawBtn.click();
	}	
	
	//Purpose: click Archive button at bottom of proposal page
	//Will not work if user does not have permission to archive proposal, 
	//need to account for these errors
	public void clickProposalArchiveBtn(WebDriver driver)
	{
		WebElement archiveBtn = driver.findElement(By.id("btnArchiveProposal"));
		archiveBtn.click();
	}	
	
	/*
	 * Purpose: Navigate to proposals page
	 */
	public void viewMyProposals(WebDriver driver)
	{
		this.waitForPageLoad(driver);
		WebElement viewProposals = driver.findElement(By
				.cssSelector("a[href='./MyProposals.jsp']"));
		viewProposals.click();
	}
}
