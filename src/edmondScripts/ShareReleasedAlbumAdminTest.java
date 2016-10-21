package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AlbumEntryPage;
import spot.pages.KindOfSharePage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;
import spot.pages.notAdmin.NewAlbumPage;
import spot.util.TimeStamp;

public class ShareReleasedAlbumAdminTest extends BaseSelenium {

	private HomePage homePage;
	private AlbumEntryPage albumEntryPage;
	private SharePage sharePage;
	
	private String albumTitle = "Shared released album: " + TimeStamp.getTimeStamp();
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
	}
	
	@Test(priority = 1)
	public void user1ReleaseAlbum() {
		login(spotRUUserName, spotRUPassWord);
		NewAlbumPage newAlbum = homePage.goToCreateNewAlbumPage();
		albumEntryPage = newAlbum.createAlbum(albumTitle, "");
		albumEntryPage.goToCollectionPage().openSomePublishedCollection().addFirstItemToAlbum();
		albumEntryPage = new StartPage(driver).openActiveAlbumEntryPage().publish();
	}
	
	@Test(priority = 2)
	public void user1ShareAdmin() {
		login(spotRUUserName, spotRUPassWord);
		shareRights(false, true);
		
		String userFullName = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(userFullName);
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(userFullName, true, true, true, true);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
	}
	
	@Test(priority = 3)
	public void user2DiscardsAlbum() {
		login(restrUserName, restrPassWord);
		albumEntryPage = homePage.goToAlbumPage().openAlbumByTitle(albumTitle);
		albumEntryPage.discardAlbum();
	}
	
	private void login(String username, String password) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(username),
				getPropertyAttribute(password));
	}
	
	private void shareRights(boolean read, boolean administrate) {
		albumEntryPage = homePage.goToAlbumPage().openAlbumByTitle(albumTitle);
		KindOfSharePage shareTransitionPage = albumEntryPage.shareAlbum();
		sharePage = shareTransitionPage.shareWithAUser();
		sharePage = sharePage.share(getPropertyAttribute(restrUserName), read, false, false, administrate);
		sharePage = sharePage.goToAlbumPage().openAlbumByTitle(albumTitle).shareAlbum().shareWithAUser();
	}
	
	@AfterMethod
	private void logout() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
