package gpms.selenium;

import org.openqa.selenium.WebDriver;
/*
 *	Created by: Liliana Acevedo 
 *	last modified: 7/7/16
 *
 * User login details for users currently on the system
 */
public class UserLogin 
{
	/*
	 * Word document for users' login:
	 * 		Admin Users Login Details
	 */

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
	
	public String bmCELogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "bmcomputerengineering1";
		String userEmail = "bmcomputerengineering1@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
	
	public String bmCSLogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "bmcomputerscience";
		String userEmail = "bmcomputerscience@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
	
	public String bmEELogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "bmelectricalengineering";
		String userEmail = "bmelectricalengineering@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
	
	public String bmPYLogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "bmphysics1";
		String userEmail = "bmphysics1@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
	
	public String bmCMLogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "bmchemistry1";
		String userEmail = "bmchemistry1@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
	
	
	public String irbCSLogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "irbcomputerscience";
		String userEmail = "irbcomputerscience@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
	
	public String irbEELogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "irbelectricalengineering";
		String userEmail = "irbelectricalengineering@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}

	
	public String chairEELogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "chairelectricalengineering";
		String userEmail = "chairelectricalengineering@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}	
	
	public String chairCELogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "chaircomputerengineering";
		String userEmail = "chaircomputerengineering@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}	
	
	public String chairCSLogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "chaircomputerscience";
		String userEmail = "chaircomputerscience@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
	
	public String chairCMLogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "chairchemistry";
		String userEmail = "chairchemistry@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
	
	public String chairPYLogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "chairphysics1";
		String userEmail = "chairphysics1@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
	
	public String deanCSLogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "deancomputerscience";
		String userEmail = "deancomputerscience@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
	
	public String deanCELogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "deancomputerengineering";
		String userEmail = "deancomputerengineering@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
	
	public String deanEELogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "deanelectricalengineering";
		String userEmail = "deanelectricalengineering@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
	
	public String deanCMLogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "deanchemistry1";
		String userEmail = "deanchemistry1@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}
	
	public String deanPYLogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "deanphysics1";
		String userEmail = "deanphysics1@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}

	public String raCSLogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "racomputerscience";
		String userEmail = "racomputerscience@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}

	public String directorCSLogin(WebDriver driver)
	{
		User uu = new User();

		String currentUser = "directorcomputerscience";
		String userEmail = "directorcomputerscience@gmail.com";
		String pword = "gpmspassword";

		uu.userLogin(userEmail, pword, driver);
		return currentUser;
	}

}
