package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.MetaDataOverViewPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class SelectMetadataProfileReferenceTest extends BaseSelenium {

	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private String collectionTitle;
	private final String purpleProfileIdentifier = "Sammlung lila (Metadata profile)";
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName),
				getPropertyAttribute(spotRUPassWord));

		collectionTitle = "Collection with a meta data profile as a reference: " + TimeStamp.getTimeStamp();
	}
	
	@Test(priority = 1)
	public void createCollection() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();

		String collectionDescription = "This is a test description for a new collection with a referenced meta data profile.";

		collectionEntryPage = createNewCollectionPage.createCollectionWithMetaDataProfileAsReference(purpleProfileIdentifier, collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
	
	@Test(priority = 2)
	public void presenceInCollectionPage() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionsPage collectionPage = homePage.goToCollectionPage();
		collectionPage.openCollectionByTitle(collectionTitle);
	}
	
	@Test(priority = 3)
	public void cannotEditMetadataProfile() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		MetaDataOverViewPage metaDataOverViewPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).openMetaDataProfile();
		boolean profileCanBeModified = metaDataOverViewPage.profileCanBeModified();
		Assert.assertFalse(profileCanBeModified);
	}
	
	@Test(priority = 4)
	public void deleteCollection() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		collectionEntryPage.deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
