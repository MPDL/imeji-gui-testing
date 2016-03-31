package edmondScripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.CollectionEntryPage;
import spot.pages.CreateIndividualMetaDataProfilePage;
import spot.pages.KindOfMetaDataProfilePage;
import spot.pages.LoginPage;
import spot.pages.MetaDataOverViewPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;

public class CreateIndividualMetaDataProfileTest extends BaseSelenium {

	private LoginPage loginPage;
	private AdminHomePage adminHomePage;
	
	private String collectionTitle;
	private CollectionEntryPage collectionEntryPage;	
	
	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
		
		loginPage = new StartPage(driver).openLoginForm();
		
//		new StartPage(driver).selectLanguage(englishSetup);
		
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotRUUserName),
				getPropertyAttribute(spotRUPassWord));

		collectionTitle = "Collection with individual meta data profile";

		createCollection(collectionTitle);
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}
	
	@Test
	public void createIndividualMetaDataProfileTest() {
		KindOfMetaDataProfilePage kindOfMetaDataProfilePage = collectionEntryPage.addMetaDataProfile();
		CreateIndividualMetaDataProfilePage createIndividualMetaDataProfilePage = kindOfMetaDataProfilePage.selectNewIndividualMetaDataProfile();
		//hello
		// 8 metadata fields are needed; one already exists, create seven more
		Map<String, String> metaDataTypes = new HashMap<String, String>();
		metaDataTypes.put("Person", "This is a person meta data field");
		metaDataTypes.put("Number", "This is a number meta data field");
		metaDataTypes.put("Date", "This is a date meta data field");
		metaDataTypes.put("Geolocation", "This is a geolocation meta data field");
		metaDataTypes.put("License", "This is a license meta data field");
		metaDataTypes.put("Link", "This is a link meta data field");
		metaDataTypes.put("Publication", "This is a publication meta data field");
		
		MetaDataOverViewPage metaDataOverViewPage = createIndividualMetaDataProfilePage.editProfile(metaDataTypes);
		
		int numberOfAvailableMetaDataFields = metaDataOverViewPage.getNumberOfAvailableMetaDataFields();
		
		Assert.assertTrue(numberOfAvailableMetaDataFields-1==metaDataTypes.size(), "One or more of the required meta data fields are missing.");
	}
	
	private void createCollection(String collectionTitle) {
		CreateNewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();

		String collectionDescription = "This is a test description for a new collection with a new individual meta data profile.";

		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
	}
}
