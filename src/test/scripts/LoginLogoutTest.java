package test.scripts;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.components.MessageComponent.MessageType;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

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

	private LoginPage loginPage;

	@BeforeMethod
	public void beforeMethod() {	
		loginPage = new StartPage(driver).openLoginForm();
	}
	
	/**
	 * Test login with invalid credentials.
	 * Resulting into unsuccessful login. 
	 * 
	 */
	@Test 
	public void testLogInWithInvalidCredentials() {
		System.out.println("testLogInWithInvalidCredentials");
		
		loginPage = loginPage.loginWithBadCredentials("invalid", "credentials");
		
		Assert.assertTrue(loginPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.ERROR, "Login was proceeded with invalid credentials, but no error message appeared on the message area of the page");
	}
	
	/**
	 * Test successful login as admin.
	 * 
	 */
	@Test 
	public void testLogInAsAdmin() {
		System.out.println("testLogInAsAdmin");
		
		AdminHomepage loginAsAdmin = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"), getPropertyAttribute("aSpotPassword"));
		
		String adminFullName = getPropertyAttribute("aGivenName") + " " + getPropertyAttribute("aFamilyName");
		Assert.assertEquals(loginAsAdmin.getLoggedInUserFullName(), adminFullName, "User name doesn't match");
		
	}
	
//	@Test
//	public void testLoginAsRegisteredUser() {
//		System.out.println("testLoginAsRegisteredUser");
//		
////		HomePage homePage = loginPage.loginAsNotAdmin(user, pw)
//	}
	
	@AfterMethod
	public void logout(Method method) {
		String methodName = method.getName();
		
		// since logout doesn't apply for testLogInWithInvalidCredentials
		if (!methodName.equals("testLogInWithInvalidCredentials")) {
			Homepage homePage = new Homepage(driver);
			homePage.logout();
			
			// if logout was successful -> openLoginForm Button is displayed on StartPage
			StartPage startPage = new StartPage(driver);
			Assert.assertTrue(startPage.isOpenLoginFormButtonPresent(), "Logout failed");
		}
	}

}
