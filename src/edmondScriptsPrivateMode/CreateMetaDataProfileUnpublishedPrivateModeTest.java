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
import spot.pages.CreateIndividualMetaDataProfilePage;
import spot.pages.KindOfMetaDataProfilePage;
import spot.pages.LoginPage;
import spot.pages.MetaDataOverViewPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class CreateMetaDataProfileUnpublishedPrivateModeTest extends BaseSelenium {

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
		CreateNewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle, 
						collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
						getPropertyAttribute(ruOrganizationName));
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
	
	@Test(priority = 2)
	public void createNewMetadataProfileTest() {
		KindOfMetaDataProfilePage kindOfMetaDataProfilePage = collectionEntryPage.addMetaDataProfile();
		CreateIndividualMetaDataProfilePage createIndividualMetaDataProfilePage = kindOfMetaDataProfilePage.selectNewIndividualMetaDataProfile();
		
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
		
		Assert.assertTrue(numberOfAvailableMetaDataFields - 1 == metaDataTypes.size(), "One or more of the required meta data fields are missing.");
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
