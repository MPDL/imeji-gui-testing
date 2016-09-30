package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class ShareAdminPrivateTest extends BaseSelenium {

	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private String collectionTitle = "Shared collection between two users: " + TimeStamp.getTimeStamp();
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
	}
	
	@Test(priority = 1)
	public void user1CreatesCollection() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName),
				getPropertyAttribute(spotRUPassWord));
		
		NewCollectionPage newCollection = homePage.goToCreateNewCollectionPage();
		collectionEntryPage = newCollection.createCollectionWithStandardMetaDataProfile(collectionTitle, "",
				getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
	}
	
	@Test(priority = 2)
	public void user1SharesAdminRights() {
		SharePage sharePage = collectionEntryPage.goToSharePage();
		sharePage.share(false, getPropertyAttribute(restrUserName), false, false, false, false, false, false, true);
		
		collectionEntryPage = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
	
	@Test(priority = 3)
	public void user2AdminCollection() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginRestricted(getPropertyAttribute(restrUserName),
				getPropertyAttribute(restrPassWord));
		
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
	}
	
	@Test(priority = 4)
	public void user2ChecksGrants() {
		SharePage sharePage = collectionEntryPage.goToSharePage();
		
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(getPropertyAttribute(restrUserName));
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(getPropertyAttribute(restrUserName), true,
				true, true, true, true, true, true);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
		
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
