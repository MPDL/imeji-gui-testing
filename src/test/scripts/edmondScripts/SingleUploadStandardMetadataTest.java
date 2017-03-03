package test.scripts.edmondScripts;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.*;

import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.ItemViewPage;
import spot.pages.LoginPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.DefaultMetaDataProfile;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class SingleUploadStandardMetadataTest extends BaseSelenium {

	private static final Logger LOG4J = LogManager.getLogger(SingleUploadStandardMetadataTest.class.getName());
	
	private LoginPage loginPage;
	private Homepage homePage;
	private CollectionEntryPage collectionEntryPage;

	private HashMap<String, String> files;

	public final String collectionTitle = "Not published test collection with default meta data profile: " 
								+ TimeStamp.getTimeStamp();

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();

		loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername),
				getPropertyAttribute(ruPassword));

		files = new HashMap<String, String>();
		files.put("SampleJPGFile.jpg", getFilepath("SampleJPGFile.jpg"));
		files.put("SampleTIFFile.tif", getFilepath("SampleTIFFile.tif"));
	}

	@Test(priority=1)
	public void createCollectionWithMetaDataProfileTest() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();

		String collectionDescription = "This collection has a meta data profile. It is not being published.";

		collectionEntryPage = createNewCollectionPage.createCollection(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));

		Assert.assertTrue(
				collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO,
				"Collection couldn't be created");

		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}

	@Test(priority=2)
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
		SingleUploadPage singleUploadPage = homePage.goToSingleUploadPage();
		
		try {
			DefaultMetaDataProfile defaultMetaDataProfile = DefaultMetaDataProfile.getDefaultMetaDataProfileInstance();
			
			ItemViewPage detailedItemViewPage = singleUploadPage.uploadAndFillMetaData(filePath, collectionTitle);		
			
			// is detailed item view page displayed
			Assert.assertTrue(detailedItemViewPage.isDetailedItemViewPageDisplayed(), "Detailed item view page not displayed");		
	
			// is collection title correct
			Assert.assertTrue(detailedItemViewPage.getCollectionTitle().equals(collectionTitle), "Something went wrong with uploading file; collection title not the one that was selected");
			
			// is file name correct		
			Assert.assertTrue(detailedItemViewPage.getFileTitle().equals(fileTitle), "Something went wrong with uploading file; file name title not the one that was selected");
			
			// is meta data title correct
			Assert.assertTrue(detailedItemViewPage.getTitleLabel().equals(defaultMetaDataProfile.getTitle()), "Something went wrong with uploading file; title not correct");
		
		} catch (TimeoutException timeOutExc) {
			LOG4J.error(timeOutExc);
			Assert.assertTrue(false, "Time out error regarding single upload: Either collection couldn't be found or the button 'save' didn't appear. Summarized: The upload failed.");
		}
	}	
	
	@Test(priority = 3)
	public void deleteCollection() {
		CollectionEntryPage collectionContentPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntryPage = collectionContentPage.openDescription();
		if (collectionEntryPage != null)
			collectionEntryPage.deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomepage(homePage);
		homePage.logout();
	}
}
