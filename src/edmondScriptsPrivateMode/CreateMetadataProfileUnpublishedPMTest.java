package edmondScriptsPrivateMode;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.CollectionEntryPage;
import spot.pages.NewMetadataProfilePage;
import spot.pages.MetadataTransitionPage;
import spot.pages.LoginPage;
import spot.pages.MetadataOverviewPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class CreateMetadataProfileUnpublishedPMTest extends BaseSelenium {

	private LoginPage loginPage;
	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private HashMap<String, String> files;
	String collectionTitle = "Non-published collection without metadata profile: " + TimeStamp.getTimeStamp();
	String collectionDescription = "Testing creation of metadata profile";
	
	@BeforeClass
	public void beforeClass() {
		switchOnPrivateMode(true);
		prepareFiles();
		logInAsRegisteredUser();
	}
	
	private void switchOnPrivateMode(boolean switchOnPrivateMode) {
		loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName),
				getPropertyAttribute(spotAdminPassWord));
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		
		if (switchOnPrivateMode)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		adminHomePage.logout();
	}
	
	private void prepareFiles() {
		files = new HashMap<String, String>();
		files.put("SamplePNGFile.png", getFilepath("SamplePNGFile.png"));
		files.put("SampleWordFile.docx", getFilepath("SampleWordFile.docx"));
	}
	
	private void logInAsRegisteredUser() {
		navigateToStartPage();
		
		loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}
	
	@Test(priority = 1)
	public void createUnpublishedCollection() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle, 
						collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
						getPropertyAttribute(ruOrganizationName));
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
	
	@Test(priority = 2)
	public void createNewMetadataProfileTest() {
		MetadataTransitionPage metadataTransition = collectionEntryPage.addMetaDataProfile();
		NewMetadataProfilePage createIndividualMetaDataProfilePage = metadataTransition.selectNewIndividualMetaDataProfile();
		
		// 8 metadata fields are needed; one already exists, create seven more
		Map<String, String> metadataTypes = setLabels();
		Map<String, String[]> predefinedValues = setPredefinedValues();
		Map<String, String> vocabularies = setVocabularies();
		
		metadataTransition = createIndividualMetaDataProfilePage.editProfile(metadataTypes);
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
		//metadataTypes.put("License", "This is a license metadata field");
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
		//vocabularies.put("License", "Creative Commons licenses (CC)");
		vocabularies.put("Link", "CoNE Authors");
		
		return vocabularies;
	}
	
	@Test(priority = 3)
	public void deleteCollection() {
		collectionEntryPage.deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
		switchOnPrivateMode(false);
	}
}
