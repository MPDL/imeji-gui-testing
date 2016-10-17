package edmondScripts;

import java.awt.AWTException;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AlbumEntryPage;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.NewAlbumPage;
import spot.util.TimeStamp;

public class CreateAlbumTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
	private AlbumEntryPage albumEntryPage;
	
	private String albumTitle = "Test Album " +	TimeStamp.getTimeStamp();

	@BeforeClass
	public void beforeClass() throws AWTException {
		super.setup();
		login();
	}
	
	@Test(priority = 1)
	public void createAlbumTest() throws AWTException {
		NewAlbumPage createNewAlbumPage = adminHomePage.goToCreateNewAlbumPage();		
		
		String albumDescription = "This album is created for testing purposes.";
		albumEntryPage = createNewAlbumPage.createAlbum(albumTitle, albumDescription);
		
		String actualInfoMessage = albumEntryPage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Album created successfully";
		
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Album probably couldn't be be created.");
	}

	@Test(priority = 2)
	public void addPublishedFilesToAlbum() throws AWTException {
		albumEntryPage = new StartPage(driver).openActiveAlbumEntryPage();
		CollectionsPage collectionPage = albumEntryPage.goToCollectionPage();
		
		CollectionContentPage releasedCollectionContentPage = collectionPage.openSomePublishedCollection();
		releasedCollectionContentPage.addFirstItemToAlbum();
	}
	
	@Test(priority = 3)
	public void selectAlbumAdmin() {
		checkAlbum();
	}
	
	@Test(priority = 4)
	public void selectAlbumPublishedAdmin() {
		albumEntryPage = adminHomePage.openActiveAlbumEntryPage();
		albumEntryPage.publish();
		checkAlbum();
	}
	
	@Test(priority = 5)
	public void selectAlbumPublishedGuest() {
		logout();
		checkAlbum();
	}
	
	@Test(priority = 6)
	public void deleteAlbum() {
		login();
		albumEntryPage = adminHomePage.goToAlbumPage().openAlbumByTitle(albumTitle);
		albumEntryPage.discardAlbum();
		logout();
	}

	private void checkAlbum() {
		albumEntryPage = new StartPage(driver).goToAlbumPage().openAlbumByTitle(albumTitle);
		int actualItemCount = albumEntryPage.getItemCount();
		Assert.assertEquals(1, actualItemCount, "Not all items in album can be viewed.");
	}
	
	private void login() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
	}
	
	private void logout() {
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
		adminHomePage.logout();
	}
}
