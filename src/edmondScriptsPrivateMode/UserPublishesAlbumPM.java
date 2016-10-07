package edmondScriptsPrivateMode;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.AlbumEntryPage;
import spot.pages.AlbumPage;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.ConfigurationEditPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.NewAlbumPage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class UserPublishesAlbumPM extends BaseSelenium {
	
	private LoginPage loginPage;
	private AdminHomePage adminHomePage;
	private ConfigurationEditPage configurationEditPage;
	private HomePage homePage;
	private AlbumPage albumPage;
	private AlbumEntryPage albumEntryPage;
	private CollectionsPage collectionsPage;
	private MultipleUploadPage multipleUploadPage;
	
	private final String collectionTitle = "Collection which items will be added to album: " + TimeStamp.getTimeStamp();
	private final String albumTitle = "Test album in private mode: " + TimeStamp.getTimeStamp();
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		switchToPrivateMode(true);
		enableAlbums();
		logInAsRU();
	}
	
	private void logInAsAdmin() {
		navigateToStartPage();
		
		loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
	}
	
	private void switchToPrivateMode(boolean shouldPrivateModeBeSwitchedOn) {
		logInAsAdmin();
		AdministrationPage administrationPage = adminHomePage.goToAdminPage();
		
		if (shouldPrivateModeBeSwitchedOn)
			configurationEditPage = administrationPage.enablePrivateMode();
		else
			configurationEditPage = administrationPage.disablePrivateMode();
		
		adminHomePage = (AdminHomePage)(configurationEditPage.goToHomePage(adminHomePage));
		adminHomePage.logout();
	}
	
	private void enableAlbums() {
		logInAsAdmin();
		
		AdministrationPage administrationPage = adminHomePage.goToAdminPage();
		configurationEditPage = administrationPage.enableAlbums();
		
		homePage = configurationEditPage.goToHomePage(adminHomePage);
		homePage.logout();
	}
	
	private void logInAsRU() {
		navigateToStartPage();
		
		loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}
	

	@Test(priority = 1)
	public void createAlbum() {
		NewAlbumPage createNewAlbumPage = homePage.goToCreateNewAlbumPage();
		albumEntryPage = createNewAlbumPage.createAlbum(albumTitle, "Album to be tested in private mode");
		albumPage = albumEntryPage.goToAlbumPage();

		boolean createdAlbumIsInAlbumList = false;
		try {
			albumEntryPage = albumPage.openAlbumByTitle(albumTitle);
			createdAlbumIsInAlbumList = true;
		}
		catch (NoSuchElementException exc) {
			createdAlbumIsInAlbumList = false;
		}
		Assert.assertTrue(createdAlbumIsInAlbumList);
	}
	
	
	@Test(priority = 2)
	public void addPublishedFileToAlbum() throws AWTException {
		homePage = new StartPage(driver).goToHomePage(adminHomePage);
		collectionsPage = homePage.goToCollectionPage();
		
		CollectionContentPage releasedCollectionContentPage = collectionsPage.getPageOfLargestCollection();
		releasedCollectionContentPage.addFirstItemToAlbum();
		
	}
	
	@Test(priority = 3, expectedExceptions = NoSuchElementException.class)
	public void publishAlbum() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.openActiveAlbumEntryPage().publish();
	}
	
	@Test(priority = 4)
	public void deleteAlbum() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		AlbumEntryPage albumEntryPage = homePage.openActiveAlbumEntryPage();
		albumEntryPage.deleteAlbum();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
		switchToPrivateMode(false);
	}
	
}
