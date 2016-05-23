package gpms.sampleTest;

import junit.framework.Assert;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SampleTest {
	@Test
	public void verifyTitle() {
		WebDriver driver = new FirefoxDriver();
		driver.get("http:/www.gmail.com");
		String Actual = driver.getTitle();
		Assert.assertEquals(Actual, "Gmail");
	}

	@Test
	public void testA() {
		Assert.assertEquals("Google", "Google");
	}

	@Test
	public void testC() {
		Assert.assertEquals("Gmail", "Google");
	}

	@Test
	public void testB() {
		Assert.assertEquals("Yahoo", "Yahoo");
	}
}
