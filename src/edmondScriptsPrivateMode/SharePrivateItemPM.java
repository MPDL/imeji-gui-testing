package edmondScriptsPrivateMode;

import java.awt.AWTException;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.DetailedItemViewPage;
import spot.pages.KindOfSharePage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.HomePage;

public class SharePrivateItemPM extends BaseSelenium {

	private HomePage homePage;
	private SharePage sharePage;
	private DetailedItemViewPage itemViewPage;
	
	private String collectionTitle;
	private String itemTitle = "SampleJPGFile.jpg";
	
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
	public void user1UploadsItem() {
		collectionTitle = getPropertyAttribute(collectionPMKey);
		login(spotRUUserName, spotRUPassWord);
		SingleUploadPage singleUploadPage = homePage.goToSingleUploadPage();
		try {
			itemViewPage = singleUploadPage.upload(getFilepath(itemTitle), collectionTitle);
		}
		catch (AWTException exc) {}
		
		Assert.assertNotNull(itemViewPage);
	}
	
	@Test(priority = 2)
	public void user1SharesItem() {
		login(spotRUUserName, spotRUPassWord);
		itemViewPage = homePage.navigateToItemPage().openItemByTitle(itemTitle);
		KindOfSharePage shareTransitionPage = itemViewPage.shareItem();
		sharePage = shareTransitionPage.shareWithAUser();
		sharePage = sharePage.share(getPropertyAttribute(restrUserName), true);
	}
	
	@Test(priority = 3)
	public void user1ChecksSharePage() {
		login(spotRUUserName, spotRUPassWord);
		String userFullName = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		sharePage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).share().shareWithAUser();
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(userFullName);
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(userFullName, true);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
	}
	
	@Test(priority = 4)
	public void user2ReadsItem() {
		login(restrUserName, restrPassWord);
		
		itemViewPage = homePage.navigateToItemPage().openItemByTitle(itemTitle);
		boolean pageDisplayed = itemViewPage.isDetailedItemViewPageDisplayed();
		
		boolean shareIconVisible = itemViewPage.shareIconVisible();
		Assert.assertTrue(shareIconVisible, "Share icon is not visible.");
		
		Assert.assertTrue(pageDisplayed, "User cannot view item.");
	}
	
	@Test(priority = 5)
	public void user1RevokesGrant() {
		login(spotRUUserName, spotRUPassWord);
		
		sharePage = homePage.navigateToItemPage().openItemByTitle(itemTitle).shareItem().shareWithAUser();
		sharePage.share(getPropertyAttribute(restrUserName), false);
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
