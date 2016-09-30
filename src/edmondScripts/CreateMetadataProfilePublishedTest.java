package edmondScripts;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.NewMetadataProfilePage;
import spot.pages.KindOfMetaDataProfilePage;
import spot.pages.LoginPage;
import spot.pages.MetaDataOverViewPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class CreateMetadataProfilePublishedTest extends BaseSelenium {

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

		collectionTitle = "Collection with individual meta data profile: " + TimeStamp.getTimeStamp();
	}
	
	@Test(priority = 1)
	public void createCollection() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();

		String collectionDescription = "This is a test description for a new collection with a new individual meta data profile.";

		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
	
	@Test(priority = 2)
	public void createIndividualMetaDataProfileTest() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		KindOfMetaDataProfilePage kindOfMetaDataProfilePage = collectionEntryPage.addMetaDataProfile();
		NewMetadataProfilePage createIndividualMetaDataProfilePage = kindOfMetaDataProfilePage.selectNewIndividualMetaDataProfile();
		
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
	

	@Test(priority = 3)
	public void uploadFiles() throws AWTException {
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		HashMap<String, String> files = new HashMap<String, String>();
		files.put("SampleXLSXFile.xlsx", getFilepath("SampleXLSXFile.xlsx"));
		files.put("SampleSWCFile.swc", getFilepath("SampleSWCFile.swc"));	
		
		MultipleUploadPage multipleUploadPage = collectionEntryPage.uploadContent();
		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}
		multipleUploadPage.startUpload();
		multipleUploadPage.publishCollection();
	}
	
	@Test(priority = 4)
	public void discardCollection() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionContentPage collectionContentPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionContentPage.viewCollectionInformation().discardCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
