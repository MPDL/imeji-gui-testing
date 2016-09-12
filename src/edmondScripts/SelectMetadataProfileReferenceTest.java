package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.CreateNewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class SelectMetadataProfileReferenceTest extends BaseSelenium {

	private LoginPage loginPage;
	private HomePage homePage;
	
	private String collectionTitle;
	private CollectionEntryPage collectionEntryPage;	
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		
		loginPage = new StartPage(driver).openLoginForm();
		
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName),
				getPropertyAttribute(spotRUPassWord));

		collectionTitle = "Collection with a meta data profile as a reference: " + TimeStamp.getTimeStamp();
	}
	
	@Test(priority = 1)
	public void createCollection() {
		CreateNewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();

		String collectionDescription = "This is a test description for a new collection with a referenced meta data profile.";

		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
}
