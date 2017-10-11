package test.scripts.edmondScriptsPrivateMode;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.components.MessageComponent.MessageType;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class LoginLogoutPMTest extends BaseSelenium {

	private LoginPage loginPage;
	private Homepage homePage;
	private AdminHomepage adminHomePage;
	
	@BeforeClass
	public void beforeClass() {
		switchToPrivateMode(true);
	}
	
	private void switchToPrivateMode(boolean switchOnPrivateMode) {
		loginPage = new StartPage(driver).openLoginForm();
		AdminHomepage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		if (switchOnPrivateMode)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		adminHomePage = (AdminHomepage) adminPage.goToHomepage(adminHomePage);
		adminHomePage.logout();
	}
	
	@Test(priority = 1)
	public void logInElementsExistTest() {
		loginPage = new StartPage(driver).openLoginForm();
		boolean loginFormIsOpen = loginPage.loginFormIsOpen();
		
		Assert.assertTrue(loginFormIsOpen, "Login form is not displayed after clicking the link to login.");
	}
	
	@Test(priority = 2)
	public void logInWithInvalidCredentials() {
		//loginPage = new StartPage(driver).openLoginForm();
		loginPage.loginWithBadCredentials("invalid", "credentials");
		MessageType messageType = loginPage.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertTrue(messageType == MessageType.ERROR, "Login was proceeded with invalid credentials, but no error message appeared on the message area of the page");
	}
	
	@Test(priority = 3)
	public void loginAsRegisteredUser() {
		loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		
		MessageType messageType = loginPage.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertTrue(messageType == MessageType.INFO, "Login was proceeded with valid credentials, but no info message appeared on the message area of the page");
		
		String expectedUserFullName = getPropertyAttribute(ruFamilyName) + getPropertyAttribute(ruGivenName);
		String actualUserFullName = homePage.getLoggedInUserFullName();
		Assert.assertEquals(actualUserFullName.trim().toLowerCase(), expectedUserFullName.trim().toLowerCase(), "Names do not match.");
	}
	
	@Test(priority = 4)
	public void logoutAsRegisteredUser() {
		homePage.logout();
		StartPage startPage = new StartPage(driver);
		Assert.assertTrue(startPage.isOpenLoginFormButtonPresent(), "Logout failed");
	}
	
	@Test(priority = 5)
	public void loginAsAdmin() {
		loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), 
				getPropertyAttribute(adminPassword));
		
		MessageType messageType = loginPage.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertTrue(messageType == MessageType.INFO, "Login was proceeded with valid credentials, but no info message appeared on the message area of the page");
		
		String expectedUserFullName = getPropertyAttribute(adminFamilyName) + ", " + getPropertyAttribute(adminGivenName);
		String actualUserFullName = adminHomePage.getLoggedInUserFullName();
		Assert.assertEquals(actualUserFullName.toLowerCase(), expectedUserFullName.toLowerCase(), "Names do not match.");
	}
	
	@Test(priority = 6)
	public void logoutAsAdmin() {
		adminHomePage.logout();
		StartPage startPage = new StartPage(driver);
		Assert.assertTrue(startPage.isOpenLoginFormButtonPresent(), "Logout failed");
	}
	
	@AfterClass
	public void afterClass() {
		switchToPrivateMode(false);
	}
}
