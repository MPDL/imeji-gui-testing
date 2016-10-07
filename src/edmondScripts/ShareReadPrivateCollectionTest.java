package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionEntryPage;
import spot.pages.KindOfSharePage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;

public class ShareReadPrivateCollectionTest extends BaseSelenium {

	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	private SharePage sharePage;
	
	private String collectionTitle;
	
	@Test(priority = 1)
	public void user1SharesReadRights() {
		collectionTitle = getPropertyAttribute(privateCollectionKey);
		login(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
		shareRights(true);
	}
	
	@Test(priority = 2)
	public void checkSharePage() {
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(getPropertyAttribute(restrUserName));
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(getPropertyAttribute(restrUserName), true, false, false, false, false, false, false);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
		
		logout();
	}
	
	@Test(priority = 3)
	public void user2ReadCollection() {
		login(getPropertyAttribute(restrUserName), getPropertyAttribute(restrPassWord));
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		logout();
	}
	
	@Test(priority = 4)
	public void user1RevokeGrant() {
		login(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
		shareRights(false);
		logout();
	}
	
	private void login(String username, String password) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(username),
				getPropertyAttribute(password));
	}
	
	private void shareRights(boolean read) {
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		KindOfSharePage shareTransitionPage = collectionEntryPage.goToSharePage();
		sharePage = shareTransitionPage.shareWithAUser();
		sharePage = sharePage.share(false, getPropertyAttribute(restrUserName), read, false, false, false, false, false, false);
	}
	
	private void logout() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
