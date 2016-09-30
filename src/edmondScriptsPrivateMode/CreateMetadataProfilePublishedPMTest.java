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
import spot.pages.KindOfMetaDataProfilePage;
import spot.pages.LoginPage;
import spot.pages.MetaDataOverViewPage;
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
	public void createIndividualMetaDataProfile() {
		navigateToStartPage();
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionContentPage collectionContentPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntryPage = collectionContentPage.viewCollectionInformation();
		
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
		
		Assert.assertTrue(numberOfAvailableMetaDataFields - 1 == metaDataTypes.size(),
				"One or more of the required meta data fields are missing.");
		
		homePage = metaDataOverViewPage.goToHomePage(homePage);
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
