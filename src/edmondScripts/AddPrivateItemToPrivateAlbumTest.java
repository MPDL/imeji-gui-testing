package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AlbumEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.CreateNewAlbumPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class AddPrivateItemToPrivateAlbumTest extends BaseSelenium {

	private HomePage homePage;
	
	public final String albumTitle = "Private test album: " + TimeStamp.getTimeStamp();
	public final String albumDescription = "Private test album with items from private test collection";
	
	@BeforeClass
	public void beforeClass() {	
		super.setup();
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));	
	}
	
	@Test(priority = 1)
	public void createPrivateAlbum() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CreateNewAlbumPage createNewAlbumPage = homePage.goToCreateNewAlbumPage();
		AlbumEntryPage albumEntryPage = createNewAlbumPage.createAlbum(albumTitle, albumDescription);
		
		String siteContentHeadline = albumEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(albumTitle), "Album title not correct");
	}
	
	@Test(priority = 2)
	public void addPrivateItemToAlbum() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.goToCollectionPage().openSomeNotPublishedCollection().addFirstItemToAlbum();
	}

	@Test(priority = 3)
	public void deleteAlbum() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.openActiveAlbumEntryPage().deleteAlbum();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
	
}
