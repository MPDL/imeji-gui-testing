package edmondScriptsPrivateMode;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.CollectionEntryPage;
import spot.pages.KindOfSharePage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.HomePage;

public class ShareAdminCollectionPM extends BaseSelenium {

	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	private SharePage sharePage;
	
	private String collectionTitle;
	private String userFullName;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		switchOnPrivateMode(true);
	}
	
	private void switchOnPrivateMode(boolean switchOnPrivateMode) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		if (switchOnPrivateMode)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		adminHomePage = (AdminHomePage) adminPage.goToHomePage(adminHomePage);
		adminHomePage.logout();
	}
	
	@Test(priority = 1)
	public void user1SharesAdminRights() {
		collectionTitle = getPropertyAttribute(collectionPMKey);
		login(spotRUUserName, spotRUPassWord);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		KindOfSharePage shareTransitionPage = collectionEntryPage.goToSharePage();
		sharePage = shareTransitionPage.shareWithAUser();
		sharePage = sharePage.share(false, false, getPropertyAttribute(restrUserName), true, true, true, true, true, true, true);
	}
	
	@Test(priority = 2)
	public void checkSharePage() {
		login(spotRUUserName, spotRUPassWord);
		userFullName = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		sharePage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).share().shareWithAUser();
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(userFullName);
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(false, userFullName, true, true, true, true, true, false, true);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
	}
	
	@Test(priority = 3)
	public void user2AdminCollection() {
		login(restrUserName, restrPassWord);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		boolean shareIconVisible = collectionEntryPage.shareIconVisible();
		Assert.assertTrue(shareIconVisible, "Share icon is not visible.");
		
		collectionEntryPage.addMetaDataProfile();
		navigateDriverBack();
		collectionEntryPage.editInformation();
		navigateDriverBack();
		collectionEntryPage.uploadContent();
		navigateDriverBack();
	}
	
	@Test(priority = 4)
	public void user1RevokeGrant() {
		login(spotRUUserName, spotRUPassWord);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		boolean shareIconVisible = collectionEntryPage.shareIconVisible();
		Assert.assertTrue(shareIconVisible, "Share icon is not visible.");
		
		sharePage = collectionEntryPage.goToSharePage().shareWithAUser();
		sharePage.share(false, false, getPropertyAttribute(restrUserName), false, false, false, false, false, false, false);
	}
	
	@AfterClass
	public void afterClass() {
		switchOnPrivateMode(false);
	}
	
	private void login(String username, String password) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(username),
				getPropertyAttribute(password));
	}
	
	@AfterMethod
	private void logout() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
