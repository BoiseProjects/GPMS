package gpms.selenium;

import java.awt.AWTException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/*
 * Created by: Liliana Acevedo
 * last modified: 7/14/16
 */

public class TestGPMS {

	public static void main(String[] args) 
	{
		Driver dd = new Driver();
		WebDriver driver = null;
		
		dd.wholeFlowInChrome(driver);

	}

}
