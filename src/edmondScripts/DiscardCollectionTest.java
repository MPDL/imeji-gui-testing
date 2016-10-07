package edmondScripts;

import java.awt.AWTException;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionEntryPage;
import spot.pages.DiscardedCollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;

public class DiscardCollectionTest extends BaseSelenium {
 	
	private String collectionTitle;
	
	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	private DiscardedCollectionEntryPage discardedCollectionEntryPage;

	@BeforeClass
	public void beforeClass() throws AWTException {
		super.setup();
		navigateToStartPage();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName),
				getPropertyAttribute(spotRUPassWord));
	}
	
	@Test(priority = 1)
	public void discardCollectionTest() {
		collectionTitle = getPropertyAttribute(releasedCollectionKey);
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		discardedCollectionEntryPage = collectionEntryPage.discardCollection();
		
		Assert.assertTrue(discardedCollectionEntryPage.isDiscarded(), "Collection was probably not discarded.");
	}
	
	@AfterClass
	public void afterClass() {
		getProperties().remove("sharedReleasedCollection");
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
	
	
	
}
