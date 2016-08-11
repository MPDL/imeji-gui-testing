package edmondScripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.CreateIndividualMetaDataProfilePage;
import spot.pages.KindOfMetaDataProfilePage;
import spot.pages.LoginPage;
import spot.pages.MetaDataOverViewPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.CreateNewCollectionPage;
import spot.pages.notAdmin.HomePage;

public class CreateMetaDataProfileUnpublishedTest extends BaseSelenium {

	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private HashMap<String, String> files;
	String collectionTitle = "Non-published collection without metadata profile";
	String collectionDescription = "For testing purposes";
	
	@BeforeClass
	public void beforeClass() {
		prepareFiles();
		logInAsRegisteredUser();
	}
	
	private void prepareFiles() {
		files = new HashMap<String, String>();
		files.put("SamplePNGFile.png", "file:" + getClass().getResource("/SamplePNGFile.png").getPath());
		files.put("SampleWordFile.docx", "file:" + getClass().getResource("/SampleWordFile.docx").getPath());
	}
	
	private void logInAsRegisteredUser() {
		navigateToStartPage();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}
	
	@Test(priority = 1)
	private void createUnpublishedCollection() {
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
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionContentPage collectionContentPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionContentPage.viewCollectionInformation().deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
