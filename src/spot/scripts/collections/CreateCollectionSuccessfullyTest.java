package spot.scripts.collections;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.NewCollectionPage;

/**
 * Creating Collection only possible when logged in.
 * 
 * @author kocar
 *
 */
public class CreateCollectionSuccessfullyTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
	private CollectionEntryPage collectionEntryPage;

	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
	}

	@AfterMethod
	public void afterMethod() {
		if (collectionEntryPage != null)
			collectionEntryPage.discardCollection();
		
		adminHomePage.logout();
	}

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();

		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"),
				getPropertyAttribute("aSpotPassword"));
	}

	@AfterClass
	public void afterClass() {
		navigateToStartPage();
		adminHomePage.logout();
	}

	@Test (groups="createCollectionSuccessfully")
	public void createCollectionFromStartPageTest() {

		NewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();
		
		String collectionTitle = "Testsammlung Montag";
		String collectionDescription = "Das ist eine Testbeschreibung f�r eine neue Sammlung.";
		
		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle, collectionDescription, getPropertyAttribute("aGivenName"), getPropertyAttribute("aFamilyName"),
				getPropertyAttribute("aOrganizationName"));

		Assert.assertTrue(collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO, "Collection couldn't be created");
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();		
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");		
	}


}
