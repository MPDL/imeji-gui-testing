package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.BasePage.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;

/**
 * Creating Collection only possible when logged in.
 * 
 * @author kocar
 *
 */
public class CreateCollectionTest extends BaseSelenium {

	private AdminHomePage adminHomePage;

	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
	}

	@AfterMethod
	public void afterMethod() {
	}

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
		LoginPage loginPage = getStartPage().openLoginForm();

		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"),
				getPropertyAttribute("aSpotPassword"));
	}

	@AfterClass
	public void afterClass() {
	}

	@Test
	public void createCollectionFromStartPage() {

		CreateNewCollectionPage createNewCollectionPage = adminHomePage.createNewCollection();
		
		String collectionTitle = "Testsammlung Montag";
		CollectionEntryPage collectionEntryPage = createNewCollectionPage.fillForm(collectionTitle, getPropertyAttribute("aGivenName"), getPropertyAttribute("aFamilyName"),
				getPropertyAttribute("aOrganizationName"));

		Assert.assertTrue(collectionEntryPage.getMessageTypeOfPageMessageArea() == MessageType.INFO, "Collection couldn't be created");
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();		
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");		
	}

	@Test
	public void createCollectionFromCollectionStartPage() {
		CollectionsPage collectionsPage = adminHomePage.goToCollectionPage();
//		collectionsPage.
	}

}
