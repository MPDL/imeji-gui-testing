package spot.scripts.collections;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.util.TimeStamp;

/**
 * Provides a helper collection for the share test cases in order to avoid mass creation and deletion.
 *
 */
public class CreatePrivateCollectionTest extends BaseSelenium {

	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;

	@BeforeClass
	public void beforeClass() {
		super.setup();
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute("spotRUUserName"), getPropertyAttribute("spotRUPassword"));
	}

	@Test
	public void createCollection() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		
		String collectionTitle = "Test collection for share test cases: " + TimeStamp.getTimeStamp();
		String collectionDescription = "To be deleted after final share test.";
		
		// store unique test title in properties in order to avoid repetition
		getProperties().put(privateCollectionKey, collectionTitle);
		
		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle, collectionDescription,
				getPropertyAttribute(ruGivenName), getPropertyAttribute(spotRUPassWord), getPropertyAttribute(ruOrganizationName));

		Assert.assertTrue(collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO, "Collection could not be created");
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();		
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title is not correct.");		
	}
	
	@AfterClass
	public void afterClass() {
		homePage.logout();
	}


}
