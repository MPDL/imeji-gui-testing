package test.scripts.edmondScripts;

import org.testng.annotations.Test;

import spot.pages.AdministrationPage;
import spot.pages.ConfigurationPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import test.base.BaseSelenium;

import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;

public class EnablePrivateModeTest extends BaseSelenium {
	
	private AdminHomepage adminHomePage;
	private AdministrationPage adminPage;
	private StartPage startPage;
	
	private ConfigurationPage configurationEditPage;
		
	@BeforeMethod
	public void beforeMethod() {
		super.setup();
		navigateToStartPage();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		
		adminPage = adminHomePage.goToAdminPage();
	}
	
	@Test(priority = 1)
	public void enablePrivateModeTest() {
		configurationEditPage = adminPage.enablePrivateMode();
		
		adminHomePage = (AdminHomepage) configurationEditPage.goToHomepage(adminHomePage);
		adminHomePage.logout();
		
		startPage = new StartPage(driver);
		boolean isCollectionsButtonDisabled = false;
		boolean isSingleUploadButtonDisabled = false;
		
		try {
			startPage.goToCollectionPage();
		}
		catch (NoSuchElementException | NullPointerException exc) {
			isCollectionsButtonDisabled = true;
		}
		
		startPage = new StartPage(driver);
		try {
			startPage.goToSingleUploadPage();
		}
		catch (NoSuchElementException | NullPointerException exc) {
			isSingleUploadButtonDisabled = true;
		}
		
		Assert.assertTrue(isCollectionsButtonDisabled, "Guest shouldn't have access to Collections page in private mode, but does.");
		Assert.assertTrue(isSingleUploadButtonDisabled, "Guest shouldn't have access to Uploads page in private mode, but does.");
		
	}
	
	@Test(priority = 2)
	public void disablePrivateModeTest() {
		configurationEditPage = adminPage.disablePrivateMode();
		
		adminHomePage = (AdminHomepage) configurationEditPage.goToHomepage(adminHomePage);
		adminHomePage.logout();
		
		startPage = new StartPage(driver);
		
		boolean isCollectionsButtonEnabled = false;
		boolean isSingleUploadButtonEnabled = false;
		
		startPage = new StartPage(driver);
		try {
			startPage.goToCollectionPage();
			isCollectionsButtonEnabled = true;
		}
		catch (NoSuchElementException | NullPointerException exc) {
			isCollectionsButtonEnabled = false;
		}
		
		startPage = new StartPage(driver);
		try {
			startPage.goToSingleUploadPage();
			isSingleUploadButtonEnabled = true;
		}
		catch (NoSuchElementException | NullPointerException exc) {
			isSingleUploadButtonEnabled = false;
		}
		
		Assert.assertTrue(isCollectionsButtonEnabled, "Guest should have access to Collections page, but doesn't.");
		Assert.assertTrue(isSingleUploadButtonEnabled, "Guest should have access to Uploads page, but doesn't.");
		
	}
		
}

