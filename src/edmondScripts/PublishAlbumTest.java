package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
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

public class PublishAlbumTest extends BaseSelenium {
	
	private AdminHomePage adminHomePage;
	private AlbumEntryPage albumEntryPage;
	
	private String albumTitle = "Published album: " +	TimeStamp.getTimeStamp(); 
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();		
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
		
	}
	
	@AfterMethod
	public void afterMethod() {
		navigateToStartPage();
	}
	
	@Test(priority = 1)
	public void createAlbum() {
		NewAlbumPage createNewAlbumPage = adminHomePage.goToCreateNewAlbumPage();		
		
		String albumDescription = "This album is created for testing purposes.";
		
		albumEntryPage = createNewAlbumPage.createAlbum(albumTitle, albumDescription);
		
		String actualInfoMessage = albumEntryPage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Album created successfully";
		
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Album probably couldn't be created.");
	}

	@Test(priority = 2)
	public void addPublishedFilesToAlbum() {
		CollectionsPage collectionPage = adminHomePage.goToCollectionPage();
		
		CollectionContentPage releasedCollectionContentPage = collectionPage.openSomePublishedCollection();
		int numberItems = releasedCollectionContentPage.addAllItemsToAlbum();
		
		String actualInfoMessage = releasedCollectionContentPage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = numberItems + " items added to active album.";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Items probably couldn't be added to active album.");
	}
	
	@Test(priority = 3)
	public void addNotYetPublishedFilesToAlbum() {
		CollectionsPage collectionPage = adminHomePage.goToCollectionPage();
		
		CollectionContentPage notYetPublishedCollectionContentPage = collectionPage.openSomeNotPublishedCollection();
		notYetPublishedCollectionContentPage.addFirstItemToAlbum();
		
		String actualInfoMessage = notYetPublishedCollectionContentPage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "1 items added to active album.";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Item probably couldn't be added to active album.");
	}
	
	@Test(priority = 4)
	public void publishAlbumTest() {
		 AlbumEntryPage activeAlbumEntryPage = adminHomePage.openActiveAlbumEntryPage();
		 activeAlbumEntryPage.publish();
		 
		 String actualInfoMessage = activeAlbumEntryPage.getMessageComponent().getInfoMessage();
		 String expectedInfoMessage = "Album successfully released";
		 Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Something went wrong with the release of the album.");
	}
	
	@Test(priority = 5)
	public void discardAlbum() {
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
		adminHomePage.goToAlbumPage().openAlbumByTitle(albumTitle).discardAlbum();
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
		adminHomePage.logout();
	}
}
