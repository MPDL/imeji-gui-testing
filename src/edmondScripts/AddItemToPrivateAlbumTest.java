package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AlbumEntryPage;
import spot.pages.AlbumPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.NewAlbumPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class AddItemToPrivateAlbumTest extends BaseSelenium {

	private HomePage homePage;
	
	public final String albumTitle = "Private test album: " + TimeStamp.getTimeStamp();
	public final String albumDescription = "Private test album with items from private test collection";
	
	@BeforeClass
	public void beforeClass() {	
		super.setup();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));	
	}
	
	@BeforeMethod
	public void goToHomePage() {
		homePage = new StartPage(driver).goToHomePage(homePage);
	}
	
	@Test(priority = 1)
	public void createPrivateAlbum() {
		NewAlbumPage createNewAlbumPage = homePage.goToCreateNewAlbumPage();
		AlbumEntryPage albumEntryPage = createNewAlbumPage.createAlbum(albumTitle, albumDescription);
		
		String siteContentHeadline = albumEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(albumTitle), "Album title not correct");
	}
	
	@Test(priority = 2)
	public void makeAlbumInactive() {
		AlbumPage albumPage = homePage.goToAlbumPage();
		albumPage = albumPage.makeAlbumInactive(albumTitle);
	}
	
	@Test(priority = 3)
	public void makeAlbumActive() {
		AlbumPage albumPage = homePage.goToAlbumPage();
		albumPage = albumPage.makeAlbumActive(albumTitle);
	}
	
	@Test(priority = 4)
	public void addPrivateItemToAlbum() {
		homePage.goToCollectionPage().openSomeNotPublishedCollection().addFirstItemToAlbum();
		AlbumEntryPage albumEntryPage = new StartPage(driver).openActiveAlbumEntryPage();
		int itemCount = albumEntryPage.getItemCount();
		Assert.assertEquals(itemCount, 1, "Private item was not added to album.");
	}
	
	@Test(priority = 5)
	public void addPublishedItemToAlbum() {
		homePage.goToCollectionPage().openSomePublishedCollection().addFirstItemToAlbum();
		AlbumEntryPage albumEntryPage = new StartPage(driver).openActiveAlbumEntryPage();
		int itemCount = albumEntryPage.getItemCount();
		Assert.assertEquals(itemCount, 2, "Published item was not added to album.");
	}

	@Test(priority = 6)
	public void deleteAlbum() {
		homePage.openActiveAlbumEntryPage().deleteAlbum();
	}
	
	@AfterClass
	public void afterClass() {
		homePage.logout();
	}
	
}
