package edmondScripts;

import java.awt.AWTException;

import org.testng.Assert;
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

public class SharePrivateItemTest extends BaseSelenium {

	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	private SharePage sharePage;
	private DetailedItemViewPage itemViewPage;
	
	private String collectionTitle = getPropertyAttribute(privateCollectionKey);
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
	}
	
	@Test(priority = 1)
	public void user1Uploadsitem() {
		SingleUploadPage singleUploadPage = homePage.goToSingleUploadPage();
		try {
			singleUploadPage.upload(getFilepath("SampleJPGFile.jpg"), collectionTitle);
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
	public void user2ChecksSharePage() {
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(getPropertyAttribute(restrUserName));
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(getPropertyAttribute(restrUserName), true);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
		
		logout();
	}
	
	@Test(priority = 4)
	public void user2ReadsItem() {
		login(getPropertyAttribute(restrUserName), getPropertyAttribute(restrPassWord));
		itemViewPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).downloadFirstItemInList();
		boolean pageDisplayed = itemViewPage.isDetailedItemViewPageDisplayed();
		Assert.assertTrue(pageDisplayed, "User cannot view item.");
		
		logout();
	}
	
	@Test(priority = 5)
	public void user1RevokesGrant() {
		login(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		sharePage = collectionEntryPage.goToSharePage().shareWithAUser();
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
