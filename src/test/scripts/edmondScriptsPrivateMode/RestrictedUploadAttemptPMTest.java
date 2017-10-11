package test.scripts.edmondScriptsPrivateMode;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.components.MessageComponent;
import spot.components.MessageComponent.MessageType;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class RestrictedUploadAttemptPMTest extends BaseSelenium {

	private Homepage homePage;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		switchToPrivateMode(true);
	}
	
	private void switchToPrivateMode(boolean shouldPrivateModeBeOn) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomepage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername),
				getPropertyAttribute(adminPassword));
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		
		if (shouldPrivateModeBeOn)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		adminHomePage.logout();
	}
	
	@Test(priority = 1)
	public void loginRestricted() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginRestricted(getPropertyAttribute(restrUsername), getPropertyAttribute(restrPassword));
	}
	
	@Test(priority = 2)
	public void goToUploadPage() {
		homePage.goToSingleUploadPage();
		MessageComponent messageComponent = homePage.getMessageComponent();
		
		boolean messageDisplayed = messageComponent.messageDisplayed();
		Assert.assertTrue(messageDisplayed, "No message was displayed.");
		
		MessageType messageType = messageComponent.getMessageTypeOfPageMessageArea();
		Assert.assertEquals(messageType, MessageType.INFO, "No information message was displayed.");
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomepage(homePage);
		homePage.logout();
		switchToPrivateMode(false);
	}
}
