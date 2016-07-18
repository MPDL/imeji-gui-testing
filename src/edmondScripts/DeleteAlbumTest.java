package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AlbumEntryPage;
import spot.pages.AlbumPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewAlbumPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class DeleteAlbumTest extends BaseSelenium {

	private HomePage homePage;
	private AlbumEntryPage albumEntryPage;
	
	private String albumTitle;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		enableAlbumsAsAdmin();
		logInAsRU();
	}
	
	private void enableAlbumsAsAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName),
				getPropertyAttribute(spotAdminPassWord));
		
		adminHomePage.goToAdminPage().enableAlbums();
		adminHomePage.logout();
	}
	
	private void logInAsRU() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}
	
	@Test(priority = 1)
	private void createAlbum() {
		CreateNewAlbumPage createNewAlbumPage = homePage.goToCreateNewAlbumPage();
		
		albumTitle = "Non-published album to be deleted: " + TimeStamp.getTimeStamp();
		String albumDescription = "For testing purposes";
		
		albumEntryPage = createNewAlbumPage.createAlbum(albumTitle, albumDescription);
		
		String siteContentHeadline = albumEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(albumTitle), "Album title not correct");
	}
	
	@Test(priority = 2)
	public void deleteAlbumTest() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		albumEntryPage = homePage.openActiveAlbumEntryPage();
		AlbumPage albumPage = albumEntryPage.deleteAlbum();
		boolean isAlbumInAlbumList = albumPage.isAlbumPresent(albumTitle);
		Assert.assertFalse(isAlbumInAlbumList);
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
