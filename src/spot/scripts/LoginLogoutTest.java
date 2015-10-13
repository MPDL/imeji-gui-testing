package spot.scripts;

import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.BasePage.MessageType;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.HomePage;

/**
 * + Testing of login 
 * 		-> with invalid credentials
 * 		-> with valid credentials
 * 				-> as admin
 * 				-> as not-admin
 * 
 * + Testing of logout
 * 
 * @author kocar
 *
 */
public class LoginLogoutTest extends BaseSelenium {

	private static final Logger log4j = LogManager.getLogger(LoginLogoutTest.class.getName());
	
	/**
	 * Test login with invalid credentials.
	 * Resulting into unsuccessful login. 
	 * 
	 */
	@Test 
	public void testLogInWithInvalidCredentials() {
		LoginPage loginPage = getStartPage().openLoginForm();
		loginPage = loginPage.loginWithBadCredentials("invalid", "credentials");
		
		Assert.assertTrue(loginPage.getMessageTypeOfPageMessageArea() == MessageType.ERROR, "Login was proceeded with invalid credentials, but no error message appeared on the message area of the page");
	}
	
	/**
	 * Test successful login as admin.
	 * 
	 */
	@Test
	public void testLogInAsAdmin() {
				
		LoginPage loginPage = getStartPage().openLoginForm();
		AdminHomePage loginAsAdmin = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"), getPropertyAttribute("aSpotPassword"));
		
		String adminFullName = getPropertyAttribute("aGivenName") + " " + getPropertyAttribute("aFamilyName");
		Assert.assertEquals(loginAsAdmin.getLoggedInUserFullName(), adminFullName, "User name doesn't match");
	}
	
	@AfterMethod
	public void logout(Method method) {
		String methodName = method.getName();
		
		// since logout doesn't apply for testLogInWithInvalidCredentials
		if (!methodName.equals("testLogInWithInvalidCredentials")) {
			HomePage homePage = PageFactory.initElements(getDriver(), HomePage.class);
			homePage.logout();
			// if logout was successful -> openLoginForm Button is displayed on StartPage
			StartPage startPage = PageFactory.initElements(getDriver(), StartPage.class);
			Assert.assertTrue(startPage.isOpenLoginFormButtonPresent(), "Logout failed");
		}
	}
	
}
