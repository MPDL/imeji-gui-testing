package edmondScriptsPrivateMode;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.NewMetadataProfilePage;
import spot.pages.MetadataTransitionPage;
import spot.pages.LoginPage;
import spot.pages.MetadataOverviewPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class CreateMetadataProfilePublishedPMTest extends BaseSelenium {
	
	private LoginPage loginPage;
	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	private MultipleUploadPage multipleUploadPage;
	
	private HashMap<String, String> files;
	private final String collectionTitle = "Collection with metadata profile to be added later: "
			+ TimeStamp.getTimeStamp();
  
	@BeforeClass
	public void beforeClass() throws AWTException {
		super.setup();
		switchToPrivateMode(true);
		
		loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
		
		files = new HashMap<String, String>();
		files.put("SampleJPGFile2.jpg", getFilepath("SampleJPGFile2.jpg"));
	}
	
	private void switchToPrivateMode(boolean shouldPrivateModeBeOn) {
		loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName),
				getPropertyAttribute(spotAdminPassWord));
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		
		if (shouldPrivateModeBeOn)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		adminHomePage.logout();
	}
	
	@Test(priority = 1)
	public void createCollection() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();

		String collectionDescription = "A new metadata for this collection will be created by the user.";

		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
	
	@Test(priority = 2)
	public void uploadFiles() throws AWTException {
		multipleUploadPage = collectionEntryPage.uploadContent();
		
		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}
		
		multipleUploadPage.startUpload();
	}
	
	@Test(priority = 3, expectedExceptions = NoSuchElementException.class)
	public void publishCollection() {
		multipleUploadPage.publishCollection();
	}
	
	@Test(priority = 4)
	public void createNewMetadataProfileTest() {
		MetadataTransitionPage kindOfMetaDataProfilePage = collectionEntryPage.addMetaDataProfile();
		NewMetadataProfilePage createIndividualMetaDataProfilePage = kindOfMetaDataProfilePage.selectNewIndividualMetaDataProfile();
		
		// 8 metadata fields are needed; one already exists, create seven more
		Map<String, String> metadataTypes = setLabels();
		Map<String, String[]> predefinedValues = setPredefinedValues();
		Map<String, String> vocabularies = setVocabularies();
		
		MetadataOverviewPage metaDataOverViewPage = createIndividualMetaDataProfilePage.editProfile(metadataTypes, predefinedValues, vocabularies);
		
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
	
	private Map<String, String[]> setPredefinedValues() {
		Map<String, String[]> predefinedValues = new HashMap<String, String[]>();
		String[] text = {"yes", "no"};
		predefinedValues.put("Text", text);
		String[] numbers = {"12", "1.2"};
		predefinedValues.put("Number", numbers);
		String[] dates = {"2016/09/30", "2016/10/01", "2016/10/02"};
		predefinedValues.put("Date", dates);
		
		return predefinedValues;
	}
	
	private Map<String, String> setVocabularies() {
		Map<String, String> vocabularies = new HashMap<String, String>();
		vocabularies.put("Person", "CoNE Authors");
		vocabularies.put("Geolocation", "Google Geo API (Beta)");
		vocabularies.put("License", "Creative Commons licenses (CC)");
		vocabularies.put("Link", "CoNE Authors");
		
		return vocabularies;
	}
	
	@Test(priority = 5)
	public void deleteCollection() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionContentPage collectionContentPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionContentPage.viewCollectionInformation().deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
		switchToPrivateMode(false);
	}
}
