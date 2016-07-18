package edmondScripts;

import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.ConfigurationEditPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;

import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;

public class EnablePrivateModeTest extends BaseSelenium {
	
	private AdminHomePage adminHomePage;
	private AdministrationPage adminPage;
	private StartPage startPage;
	
	private ConfigurationEditPage configurationEditPage;
		
	@BeforeMethod
	public void beforeMethod() {
		super.setup();
		navigateToStartPage();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		
		adminPage = adminHomePage.goToAdminPage();
	}
	
	@Test(priority = 1)
	public void enablePrivateModeTest() {
		configurationEditPage = adminPage.enablePrivateMode();
		
		adminHomePage = (AdminHomePage) configurationEditPage.goToHomePage(adminHomePage);
		adminHomePage.logout();
		
		startPage = new StartPage(driver);
		
		boolean isAlbumsButtonDisabled = false;
		boolean isCollectionsButtonDisabled = false;
		boolean isSingleUploadButtonDisabled = false;
		
		try {
			startPage.goToAlbumPage();
		}
		catch (NoSuchElementException | NullPointerException exc) {
			isAlbumsButtonDisabled = true;
		}
		
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
		
		Assert.assertTrue(isAlbumsButtonDisabled, "Guest shouldn't have access to Albums page in private mode, but does.");
		Assert.assertTrue(isCollectionsButtonDisabled, "Guest shouldn't have access to Collections page in private mode, but does.");
		Assert.assertTrue(isSingleUploadButtonDisabled, "Guest shouldn't have access to Uploads page in private mode, but does.");
		
	}
	
	@Test(priority = 2)
	public void disablePrivateModeTest() {
		configurationEditPage = adminPage.disablePrivateMode();
		
		adminHomePage = (AdminHomePage) configurationEditPage.goToHomePage(adminHomePage);
		adminHomePage.logout();
		
		startPage = new StartPage(driver);
		
		boolean isAlbumsButtonEnabled = false;
		boolean isCollectionsButtonEnabled = false;
		boolean isSingleUploadButtonEnabled = false;
		
		try {
			startPage.goToAlbumPage();
			isAlbumsButtonEnabled = true;
		}
		catch (NoSuchElementException | NullPointerException exc) {
			isAlbumsButtonEnabled = false;
		}
		
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
		
		Assert.assertTrue(isAlbumsButtonEnabled, "Guest should have access to Albums page, but doesn't.");
		Assert.assertTrue(isCollectionsButtonEnabled, "Guest should have access to Collections page, but doesn't.");
		Assert.assertTrue(isSingleUploadButtonEnabled, "Guest should have access to Uploads page, but doesn't.");
		
	}
		
}

