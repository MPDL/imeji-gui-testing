package edmondScriptsPrivateMode;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.CategoryType;
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
	public void searchPrivateModeRUItem() {
		searchPrivateModeAsRU("jellyfish", CategoryType.ITEM);
	}
	
	@Test(priority = 2)
	public void searchPrivateModeRUCollection() {
		searchPrivateModeAsRU("jellyfish", CategoryType.COLLECTION);
	}
	
	@Test(priority = 2)
	public void searchPrivateModeRUAlbum() {
		searchPrivateModeAsRU("jellyfish", CategoryType.ALBUM);
	}
	
	private void searchPrivateModeAsRU(String searchQuery, CategoryType category) {
		startPage = new StartPage(driver);
		LoginPage loginPage = startPage.openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), 
				getPropertyAttribute(spotRUPassWord));
		
		boolean ruAccessSearchField = true;
		try {
			searchFor(searchQuery, category);
		}
		catch (NoSuchElementException exc) {
			ruAccessSearchField = false;
		}
		
		Assert.assertTrue(ruAccessSearchField, "Registered user should be able to search in private mode.");
		
		homePage = startPage.goToHomePage(homePage);
		homePage.logout();
	}
	
	@Test(priority = 3)
	public void searchPrivateModeAdminItem() {
		searchPrivateModeAsAdmin("jellyfish", CategoryType.ITEM);
	}
	
	@Test(priority = 3)
	public void searchPrivateModeAdminCollection() {
		searchPrivateModeAsAdmin("jellyfish", CategoryType.COLLECTION);
	}
	
	@Test(priority = 3)
	public void searchPrivateModeAdminAlbum() {
		searchPrivateModeAsAdmin("jellyfish", CategoryType.ALBUM);
	}
	
	private void searchPrivateModeAsAdmin(String searchQuery, CategoryType category) {
		startPage = new StartPage(driver);
		LoginPage loginPage = startPage.openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), 
				getPropertyAttribute(spotAdminPassWord));
		
		boolean adminAccessSearchField = true;
		try {
			searchFor(searchQuery, category);
		}
		catch (NoSuchElementException exc) {
			adminAccessSearchField = false;
		}
		
		Assert.assertTrue(adminAccessSearchField, "Admin should be able to search in private mode.");
		
		adminHomePage = (AdminHomePage)(startPage.goToHomePage(adminHomePage));
		adminHomePage.logout();
	}
	
	@Test(priority = 4)
	public void searchPrivateModeRestrictedItem() {
		searchPrivateModeRestricted("jellyfish", CategoryType.ITEM);
	}
	
	@Test(priority = 4)
	public void searchPrivateModeRestrictedCollection() {
		searchPrivateModeRestricted("jellyfish", CategoryType.COLLECTION);
	}
	
	@Test(priority = 4)
	public void searchPrivateModeRestrictedAlbum() {
		searchPrivateModeRestricted("jellyfish", CategoryType.ALBUM);
	}
	
	private void searchPrivateModeRestricted(String searchQuery, CategoryType category) {
		startPage = new StartPage(driver);
		LoginPage loginPage = startPage.openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(restrUserName), 
				getPropertyAttribute(restrPassWord));
		
		boolean ruAccessSearchField = true;
		try {
			searchFor(searchQuery, category);
		}
		catch (NoSuchElementException exc) {
			ruAccessSearchField = false;
		}
		
		Assert.assertTrue(ruAccessSearchField, "Restricted user should be able to search in private mode.");
		
		homePage = startPage.goToHomePage(homePage);
		homePage.logout();
	}
	
	private void searchFor(String searchQueryKeyWord) {
		SearchResultsPage searchQueryPage = startPage.getSearchComponent().searchFor(searchQueryKeyWord);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQueryKeyWord);
		
		navigateToStartPage();
		startPage = new StartPage(driver);
	}
	
	private void searchFor(String searchQueryKeyWord, CategoryType category) {
		SearchResultsPage searchQueryPage = startPage.getSearchComponent().searchByCategory(searchQueryKeyWord, category);
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
