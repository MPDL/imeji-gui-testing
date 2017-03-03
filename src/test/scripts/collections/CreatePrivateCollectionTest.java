package test.scripts.collections;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

/**
 * Provides a helper collection for the share test cases in order to avoid mass creation and deletion.
 *
 */
public class CreatePrivateCollectionTest extends BaseSelenium {

	private Homepage homePage;
	private CollectionEntryPage collectionEntryPage;

	@BeforeClass
	public void beforeClass() {
		super.setup();
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}

	@Test
	public void createCollection() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		
		String collectionTitle = "Test collection for share test cases: " + TimeStamp.getTimeStamp();
		String collectionDescription = "To be deleted after final share test.";
		
		// store unique test title in properties in order to avoid repetition
		getProperties().put(privateCollectionKey, collectionTitle);
		
		collectionEntryPage = createNewCollectionPage.createCollection(collectionTitle, collectionDescription,
				getPropertyAttribute(ruGivenName), getPropertyAttribute(ruPassword), getPropertyAttribute(ruOrganizationName));

		Assert.assertTrue(collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO, "Collection could not be created");
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();		
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title is not correct.");		
	}
	
	@AfterClass
	public void afterClass() {
		homePage.logout();
	}


}
