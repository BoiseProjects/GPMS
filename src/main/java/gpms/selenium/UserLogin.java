package gpms.selenium;

import org.openqa.selenium.WebDriver;

public class UserLogin 
{

	public String lilyLogin(WebDriver driver)
	{
		User uu = new User();
		  String currentUser = "liliana"; 
		  String userEmail =
		  "lva34bsu@gmail.com"; 
		  String pword = "password";
		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
	
	public String bmceLogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "bmcomputerengineering1";
		String userEmail = "bmcomputerengineering1@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
}
