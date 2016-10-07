package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.MetadataOverviewPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class MetadataProfileAsTemplateTest extends BaseSelenium {

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
		collectionTitle = "Collection with existing metadata profile as a template: " + TimeStamp.getTimeStamp();
	}
	
	@Test(priority = 1)
	public void createCollection() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();

		String collectionDescription = "This is a test description for a new collection with an existing metadata profile as a template.";
		
		collectionEntryPage = createNewCollectionPage.createCollectionWithTemplateMetaDataProfile(purpleProfileIdentifier, collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
	
	@Test(priority = 2)
	public void collectionIsEmpty() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionContentPage collectionContentPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		
		int items = collectionContentPage.getItemListSize();
		Assert.assertEquals(items, 0, "Collection is not empty.");
	}
	
	@Test(priority = 3)
	public void canEditMetadataProfile() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		MetadataOverviewPage metaDataOverViewPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).openMetaDataProfile();
		boolean profileCanBeModified = metaDataOverViewPage.profileCanBeModified();
		Assert.assertTrue(profileCanBeModified);
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
