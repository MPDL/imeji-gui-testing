package edmondScripts;

import java.awt.AWTException;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.DetailedItemViewPage;
import spot.pages.KindOfSharePage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;

public class SharePrivateItemTest extends BaseSelenium {

	private HomePage homePage;
	//private CollectionEntryPage collectionEntryPage;
	private SharePage sharePage;
	private DetailedItemViewPage itemViewPage;
	
	private String collectionTitle;
	private String itemTitle = "SampleJPGFile.jpg";
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
	}
	
	@Test(priority = 1)
	public void user1Uploadsitem() {
		collectionTitle = getPropertyAttribute(privateCollectionKey);
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
		KindOfSharePage shareTransitionPage = itemViewPage.shareItem();
		sharePage = shareTransitionPage.shareWithAUser();
		sharePage = sharePage.share(getPropertyAttribute(restrUserName), true);
	}
	
	@Test(priority = 3)
	public void user1ChecksSharePage() {
		String userFullName = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(userFullName);
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(userFullName, true);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
		
		logout();
	}
	
	@Test(priority = 4)
	public void user2ReadsItem() {
		login(restrUserName, restrPassWord);
		itemViewPage = homePage.navigateToItemPage().openItemByTitle(itemTitle);
		boolean pageDisplayed = itemViewPage.isDetailedItemViewPageDisplayed();
		Assert.assertTrue(pageDisplayed, "User cannot view item.");
		
		logout();
	}
	
	@Test(priority = 5)
	public void user1RevokesGrant() {
		login(spotRUUserName, spotRUPassWord);
		
		sharePage = homePage.navigateToItemPage().openItemByTitle(itemTitle).shareItem().shareWithAUser();
		sharePage.share(getPropertyAttribute(restrUserName), false);
		
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
