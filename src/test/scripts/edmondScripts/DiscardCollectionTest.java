package test.scripts.edmondScripts;

import java.awt.AWTException;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.DiscardedCollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class DiscardCollectionTest extends BaseSelenium {
 	
	private String collectionTitle;
	
	private Homepage homePage;
	private CollectionEntryPage collectionEntryPage;
	private DiscardedCollectionEntryPage discardedCollectionEntryPage;

	@BeforeClass
	public void beforeClass() throws AWTException {
		super.setup();
		navigateToStartPage();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername),
				getPropertyAttribute(ruPassword));
	}
	
	@Test(priority = 1)
	public void discardCollectionTest() {
		collectionTitle = getPropertyAttribute(releasedCollectionKey);
		homePage = new StartPage(driver).goToHomepage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).openDescription();
		discardedCollectionEntryPage = collectionEntryPage.discardCollection();
		
		Assert.assertTrue(discardedCollectionEntryPage.discardedIconDisplayed(), "Collection was probably not discarded.");
	}
	
	@AfterClass
	public void afterClass() {
		getProperties().remove("sharedReleasedCollection");
		homePage = new StartPage(driver).goToHomepage(homePage);
		homePage.logout();
	}
	
	
	
}
