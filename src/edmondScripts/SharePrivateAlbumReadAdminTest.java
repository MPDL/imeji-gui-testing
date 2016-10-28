package edmondScripts;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AlbumEntryPage;
import spot.pages.CollectionContentPage;
import spot.pages.DetailedItemViewPage;
import spot.pages.KindOfSharePage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;
import spot.pages.notAdmin.NewAlbumPage;
import spot.util.TimeStamp;

public class SharePrivateAlbumReadAdminTest extends BaseSelenium {
	
	private HomePage homePage;
	private AlbumEntryPage albumEntryPage;
	private SharePage sharePage;
	
	private String albumTitle = "Shared private album: " + TimeStamp.getTimeStamp();
	private String userFullName;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
	}
	
	@Test(priority = 1)
	public void user1CreateAlbum() {
		userFullName = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		login(spotRUUserName, spotRUPassWord);
		NewAlbumPage newAlbum = homePage.goToCreateNewAlbumPage();
		albumEntryPage = newAlbum.createAlbum(albumTitle, "");
	}
	
	@Test(priority = 2)
	public void user1ShareRead() {
		login(spotRUUserName, spotRUPassWord);
		shareRights(true, false);
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(userFullName);
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(userFullName, true, false, false, false);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
	}
	
	@Test(priority = 3)
	public void user2ReadsAlbum() {
		login(restrUserName, restrPassWord);
		albumEntryPage = homePage.goToAlbumPage().openAlbumByTitle(albumTitle);
	}
	
	@Test(priority = 4)
	public void user1ShareAdmin() {
		login(spotRUUserName, spotRUPassWord);
		shareRights(true, true);
		
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(userFullName);
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(userFullName, true, true, true, true);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
	}
	
	@Test(priority = 5)
	public void user1RevokesAdminGrant() {
		login(spotRUUserName, spotRUPassWord);
		albumEntryPage = homePage.goToAlbumPage().openAlbumByTitle(albumTitle);
		shareRights(true, false);
		
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(userFullName);
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(userFullName, true, false, false, false);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
	}
	
	@Test(priority = 6)
	public void user1RevokesReadGrant() {
		login(spotRUUserName, spotRUPassWord);
		albumEntryPage = homePage.goToAlbumPage().openAlbumByTitle(albumTitle);
		shareRights(false, false);
		
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(userFullName);
		Assert.assertFalse(nameInShareList, "User 2 should not be in share list.");
	}
	
	@Test(priority = 7)
	public void shareAlbumReadExternal() {
		String externalEmail = "nonexistentuser@mpdl.mpg.de";
		login(spotRUUserName, spotRUPassWord);
		
		albumEntryPage = homePage.goToAlbumPage().openAlbumByTitle(albumTitle);
		sharePage = albumEntryPage.shareAlbum().shareWithAUser();
		sharePage.share(externalEmail, true, false, false, false);
		
		Assert.assertTrue(sharePage.messageDisplayed(), "No message on the external emails was displayed.");
		
		Assert.assertTrue(sharePage.inviteButtonEnabled(), "External user cannot be invited to register.");
		
		List<WebElement> emails = sharePage.getExternalEmails();
		String userEmail = emails.get(0).getText().trim();
		Assert.assertEquals(externalEmail, userEmail, "User email " + userEmail + " is not in list.");
	}
	
	@Test(priority = 8)
	public void deleteAlbum() {
		login(spotRUUserName, spotRUPassWord);
		albumEntryPage = homePage.goToAlbumPage().openAlbumByTitle(albumTitle);
		albumEntryPage.deleteAlbum();
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
