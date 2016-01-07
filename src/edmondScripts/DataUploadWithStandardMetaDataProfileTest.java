package edmondScripts;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.DetailedItemViewPage;
import spot.pages.LoginPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;
import spot.util.DefaultMetaDataProfile;
import spot.util.TimeStamp;

public class DataUploadWithStandardMetaDataProfileTest extends BaseSelenium {

	private LoginPage loginPage;
	private AdminHomePage adminHomePage;
	private CollectionEntryPage collectionEntryPage;

	private HashMap<String, String> files;

	public final String collectionTitle = "Not published test collection with default meta data profile: " + TimeStamp.getTimeStamp();

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();

		loginPage = new StartPage(driver).openLoginForm();

		files = new HashMap<String, String>();
		files.put("SampleTIFFFile.tiff", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SampleTIFFFile.tiff");
		
		new StartPage(driver).selectLanguage(englishSetup);
	}

	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}

	@Test(priority=1)/*(groups = { "login", "dataUploadWithStandardMetaDataProfile" })*/
	public void loginTest() {
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotRUUserName),
				getPropertyAttribute(spotRUPassWord));

		String adminFullName = getPropertyAttribute(ruFamilyName) + ", " + getPropertyAttribute(ruGivenName);
		Assert.assertEquals(adminHomePage.getLoggedInUserFullName(), adminFullName, "User name doesn't match");
	}

	@Test(priority=2)/*(groups = { "collectionCreated", "dataUploadWithStandardMetaDataProfile" }, dependsOnGroups = "login")*/
	public void createCollectionWithMetaDataProfileTest() {
		CreateNewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();

		String collectionDescription = "This collection has a meta data profile. It is not being published.";

		collectionEntryPage = createNewCollectionPage.createCollectionWithStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));

		Assert.assertTrue(
				collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO,
				"Collection couldn't be created");

		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}

	@Test(priority=3)/*(groups = {"collectionUploaded", "dataUploadWithStandardMetaDataProfile"}, dependsOnGroups = "collectionCreated")*/
	public void uploadFilesTest() throws AWTException {
		navigateToStartPage();
		
		for (Map.Entry<String, String> file : files.entrySet()) {
			String fileTitle = file.getKey();
			String filePath = file.getValue();
			
			uploadFile(fileTitle, filePath);
		}
		navigateToStartPage();
	}

	private void uploadFile(String fileTitle, String filePath) throws AWTException {
		SingleUploadPage singleUploadPage = adminHomePage.goToSingleUploadPage();
		
		DetailedItemViewPage detailedItemViewPage = singleUploadPage.uploadAndFillMetaData(filePath, collectionTitle);		
		
		DefaultMetaDataProfile defaultMetaDataProfile = DefaultMetaDataProfile.getInstance();
		
		// is detailed item view page displayed
		Assert.assertTrue(detailedItemViewPage.isDetailedItemViewPageDisplayed(), "Detailed item view page not displayed");		

		// is collection title correct
		Assert.assertTrue(detailedItemViewPage.getCollectionTitle().equals(collectionTitle), "Something went wrong with uploading file; collection title not the one that was selected");
		
		// is file name correct		
		Assert.assertTrue(detailedItemViewPage.getFileTitle().equals(fileTitle), "Something went wrong with uploading file; file name title not the one that was selected");
		
		// is meta data title correct
		Assert.assertTrue(detailedItemViewPage.getTitleLabel().equals(defaultMetaDataProfile.getTitle()), "Something went wrong with uploading file; title not correct");

		// is meta data id correct
		Assert.assertTrue(detailedItemViewPage.getIDLabel().equals(defaultMetaDataProfile.getId()), "Something went wrong with uploading file; id not correct");
		
		// is meta data author family name correct
		Assert.assertTrue(detailedItemViewPage.getAuthorFamilyNameLabel().equals(defaultMetaDataProfile.getAuthor()), "Something went wrong with uploading file; autor not correct");
		
		// is publication link correct
		Assert.assertTrue(detailedItemViewPage.getPublicationLinkLabel().equals(defaultMetaDataProfile.getPublicationLink()), "Something went wrong with uploading file; publication link not correct");
		
		// is date correct
		Assert.assertTrue(detailedItemViewPage.getDateLabel().equals(defaultMetaDataProfile.getDate()), "Something went wrong with uploading file; date not correct");
	}	
	
}
