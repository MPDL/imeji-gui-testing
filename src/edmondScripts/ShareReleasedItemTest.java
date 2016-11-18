package edmondScripts;

import java.awt.AWTException;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionEntryPage;
import spot.pages.DetailedItemViewPage;
import spot.pages.KindOfSharePage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;

public class ShareReleasedItemTest extends BaseSelenium {

	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	private SharePage sharePage;
	private DetailedItemViewPage itemViewPage;
	
	private String collectionTitle;
	private String itemTitle = "SampleTIFFile.tif";
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
	}
	
	@Test(priority = 1)
	public void user1Uploadsitem() {
		collectionTitle = getPropertyAttribute(releasedCollectionKey);
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
		sharePage = homePage.navigateToItemPage().openItemByTitle(itemTitle).shareItem().shareWithAUser();
		
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(userFullName);
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(userFullName, true);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
	}
	
	@Test(priority = 4)
	public void user2ReadsItem() {
		login(restrUserName, restrPassWord);
		itemViewPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).downloadFirstItemInList();
		
		boolean pageDisplayed = itemViewPage.isDetailedItemViewPageDisplayed();
		Assert.assertTrue(pageDisplayed, "User cannot view item.");
	}
	
	@Test(priority = 5)
	public void user1RevokesGrant() {
		login(spotRUUserName, spotRUPassWord);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		sharePage = collectionEntryPage.goToSharePage().shareWithAUser();
		sharePage.share(getPropertyAttribute(restrUserName), false);
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
