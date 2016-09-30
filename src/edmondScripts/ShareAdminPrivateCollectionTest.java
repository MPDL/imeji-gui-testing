package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionEntryPage;
import spot.pages.KindOfSharePage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;

public class ShareAdminPrivateCollectionTest extends BaseSelenium {

	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	private SharePage sharePage;
	
	private String collectionTitle = getPropertyAttribute(privateCollectionKey);
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
	}
	
	@Test(priority = 1)
	public void user1SharesAdminRights() {
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		KindOfSharePage shareTransitionPage = collectionEntryPage.goToSharePage();
		sharePage = shareTransitionPage.shareWithAUser();
		sharePage = sharePage.share(false, getPropertyAttribute(restrUserName), false, false, false, false, false, false, true);
	}
	
	@Test(priority = 2)
	public void checkSharePage() {
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(getPropertyAttribute(restrUserName));
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(getPropertyAttribute(restrUserName), true, true, true, true, true, true, true);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
		
		logout();
	}
	
	@Test(priority = 3)
	public void user2AdminCollection() {
		login(getPropertyAttribute(restrUserName), getPropertyAttribute(restrPassWord));
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		collectionEntryPage.addMetaDataProfile();
		navigateDriverBack();
		collectionEntryPage.editInformation();
		navigateDriverBack();
		collectionEntryPage.uploadContent();
		navigateDriverBack();
		
		logout();
	}
	
	@Test(priority = 4)
	public void user1RevokeGrant() {
		login(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		sharePage = collectionEntryPage.goToSharePage().shareWithAUser();
		sharePage.share(false, getPropertyAttribute(restrUserName), false, false, false, false, false, false, false);
		
		logout();
	}
	
	private void login(String username, String password) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(username),
				getPropertyAttribute(password));
	}
	
	private void logout() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}