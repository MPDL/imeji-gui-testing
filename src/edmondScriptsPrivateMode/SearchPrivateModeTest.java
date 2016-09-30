package edmondScriptsPrivateMode;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.LoginPage;
import spot.pages.SearchResultsPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.HomePage;

public class SearchPrivateModeTest extends BaseSelenium {

	private StartPage startPage;
	private HomePage homePage;
	private AdminHomePage adminHomePage;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		switchToPrivateMode(true);
	}
	
	private void switchToPrivateMode(boolean switchOnPrivateMode) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		if (switchOnPrivateMode)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		adminHomePage = (AdminHomePage) adminPage.goToHomePage(adminHomePage);
		adminHomePage.logout();
	}
	
	@Test(priority = 1)
	public void searchPrivateModeGuest() {
		startPage = new StartPage(driver);
		boolean guestAccessSearchField = true;
		try {
			searchFor("koala");
		}
		catch (NoSuchElementException exc) {
			guestAccessSearchField = false;
		}
		
		Assert.assertFalse(guestAccessSearchField, "Guest should not be able to search in private mode.");
	}
	
	@Test(priority = 2)
	public void searchPrivateModeAsRU() {
		startPage = new StartPage(driver);
		LoginPage loginPage = startPage.openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), 
				getPropertyAttribute(spotRUPassWord));
		
		boolean ruAccessSearchField = true;
		try {
			searchFor("jellyfish");
		}
		catch (NoSuchElementException exc) {
			ruAccessSearchField = false;
		}
		
		Assert.assertTrue(ruAccessSearchField, "Registered user should be able to search in private mode.");
		
		homePage = startPage.goToHomePage(homePage);
		homePage.logout();
	}
	
	@Test(priority = 3)
	public void searchPrivateModeAsAdmin() {
		startPage = new StartPage(driver);
		LoginPage loginPage = startPage.openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), 
				getPropertyAttribute(spotAdminPassWord));
		
		boolean adminAccessSearchField = true;
		try {
			searchFor("jellyfish");
		}
		catch (NoSuchElementException exc) {
			adminAccessSearchField = false;
		}
		
		Assert.assertTrue(adminAccessSearchField, "Admin should be able to search in private mode.");
		
		adminHomePage = (AdminHomePage)(startPage.goToHomePage(adminHomePage));
		adminHomePage.logout();
	}
	
	private void searchFor(String searchQueryKeyWord) {
		SearchResultsPage searchQueryPage = startPage.getSearchComponent().searchFor(searchQueryKeyWord);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQueryKeyWord);
		
		navigateToStartPage();
		startPage = new StartPage(driver);
	}
	
	@AfterClass
	public void afterClass() {
		switchToPrivateMode(false);
	}
}
