package edmondScripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.CollectionEntryPage;
import spot.pages.NewMetadataProfilePage;
import spot.pages.MetadataTransitionPage;
import spot.pages.LoginPage;
import spot.pages.MetadataOverviewPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;
import spot.pages.notAdmin.NewCollectionPage;

public class CreateIndividualMetaDataProfileTest extends BaseSelenium {

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

		collectionTitle = "Collection with individual meta data profile";

		createCollection(collectionTitle);
	}
	
	@AfterClass
	public void afterClass() {
		homePage.logout();
	}
	
	@Test
	public void createNewMetadataProfileTest() {
		MetadataTransitionPage kindOfMetaDataProfilePage = collectionEntryPage.addMetaDataProfile();
		NewMetadataProfilePage createIndividualMetaDataProfilePage = kindOfMetaDataProfilePage.selectNewIndividualMetaDataProfile();
		
		// 8 metadata fields are needed; one already exists, create seven more
		Map<String, String> metadataTypes = setLabels();
		
		MetadataTransitionPage metadataTransition = createIndividualMetaDataProfilePage.editProfile(metadataTypes);
		MetadataOverviewPage metaDataOverViewPage = metadataTransition.goToCollectionPage().openCollectionByTitle(collectionTitle).openMetaDataProfile();
		
		int numberOfAvailableMetaDataFields = metaDataOverViewPage.getNumberOfAvailableMetaDataFields();
		Assert.assertTrue(numberOfAvailableMetaDataFields - 1 == metadataTypes.size(), "One or more of the required meta data fields are missing.");
	}
	
	private Map<String, String> setLabels() {
		Map<String, String> metadataTypes = new HashMap<String, String>();
		metadataTypes.put("Person", "This is a person metadata field");
		metadataTypes.put("Number", "This is a number metadata field");
		metadataTypes.put("Date", "This is a date meta datafield");
		metadataTypes.put("Geolocation", "This is a geolocation metadata field");
		metadataTypes.put("License", "This is a license metadata field");
		metadataTypes.put("Link", "This is a link metadata field");
		metadataTypes.put("Publication", "This is a publication metadata field");
		
		return metadataTypes;
	}
	
	private void createCollection(String collectionTitle) {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();

		String collectionDescription = "This is a test description for a new collection with a new individual meta data profile.";

		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
	}
}
