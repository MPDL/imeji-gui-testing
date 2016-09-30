package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AlbumEntryPage;
import spot.pages.KindOfSharePage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;
import spot.pages.notAdmin.NewAlbumPage;

public class SharePrivateAlbumReadAdminTest extends BaseSelenium {
	
	private HomePage homePage;
	private AlbumEntryPage albumEntryPage;
	private SharePage sharePage;
	
	private String albumTitle = "Shared private album";
	
	@Test(priority = 1)
	public void user1CreateAlbum() {
		login(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
		NewAlbumPage newAlbum = homePage.goToCreateNewAlbumPage();
		albumEntryPage = newAlbum.createAlbum(albumTitle, "");
	}
	
	@Test(priority = 2)
	public void user1ShareRead() {
		shareRights(true, false);
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(getPropertyAttribute(restrUserName));
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(getPropertyAttribute(restrUserName), true, false, false, false, false, false, false);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
		
		logout();
	}
	
	@Test(priority = 3)
	public void user2ReadsAlbum() {
		login(getPropertyAttribute(restrUserName), getPropertyAttribute(restrPassWord));
		albumEntryPage = homePage.goToAlbumPage().openAlbumByTitle(albumTitle);
		logout();
	}
	
	@Test(priority = 4)
	public void user1ShareAdmin() {
		login(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
		shareRights(false, true);
		
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(getPropertyAttribute(restrUserName));
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(getPropertyAttribute(restrUserName), true, true, true, true, true, true, true);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
		
		logout();
	}
	
	@Test(priority = 5)
	public void user2DeletesAlbum() {
		login(getPropertyAttribute(restrUserName), getPropertyAttribute(restrPassWord));
		albumEntryPage = homePage.goToAlbumPage().openAlbumByTitle(albumTitle);
		albumEntryPage.deleteAlbum();
		logout();
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
	}
	
	private void logout() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
