package test.scripts.edmondScripts;

import java.awt.AWTException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class DeleteCollectionTest extends BaseSelenium {

	private Homepage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private String collectionTitle;

	@BeforeClass
	public void beforeClass() throws AWTException {
		super.setup();
		loginCreator();
	}
	
	private void loginCreator() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();

		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}
	
	@Test
	public void deleteCollectionTest() {
		collectionTitle = getPropertyAttribute(privateCollectionKey);
		homePage = new StartPage(driver).goToHomepage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).openDescription();
		collectionEntryPage.deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		getProperties().remove("sharedPrivateCollection");
		homePage = new StartPage(driver).goToHomepage(homePage);
		homePage.logout();
	}

}
