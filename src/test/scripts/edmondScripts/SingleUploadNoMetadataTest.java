package test.scripts.edmondScripts;

import org.testng.annotations.Test;

import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.ItemViewPage;
import spot.pages.LoginPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

import org.testng.annotations.BeforeClass;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class SingleUploadNoMetadataTest extends BaseSelenium {

	private static final Logger LOG4J = LogManager.getLogger(SingleUploadNoMetadataTest.class.getName());
	
	private LoginPage loginPage;
	private Homepage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private HashMap<String, String> files;
	
	public final String collectionTitle = "Not published test collection without meta data profile: " + TimeStamp.getTimeStamp();
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		
		files = new HashMap<String, String>();
		files.put("SamplePDFFile.pdf", getFilepath("SamplePDFFile.pdf"));	
		files.put("SampleWordFile.docx", getFilepath("SampleWordFile.docx"));
	}

	@Test(priority = 1)
	public void createCollectionWithoutMetaDataProfileTest() {
		
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		
		String collectionDescription = "This collection has no meta data profile. It is not being published.";

		collectionEntryPage = createNewCollectionPage.createCollection(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));

		Assert.assertTrue(collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO,
				"Collection couldn't be created");

		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");	
	}

	@Test (priority = 2)
	public void uploadFilesTest() throws AWTException {				
	
		for (Map.Entry<String, String> file : files.entrySet()) {
			String fileTitle = file.getKey();
			String filePath = file.getValue();
			
			testFile(fileTitle, filePath);
		}
		navigateToStartPage();
	}

	private void testFile(String fileTitle, String filePath) throws AWTException {
		homePage = collectionEntryPage.goToHomepage(homePage);
		SingleUploadPage singleUploadPage = homePage.goToSingleUploadPage();
		
		ItemViewPage detailedItemViewPage = null;
		
		try {
			detailedItemViewPage = singleUploadPage.upload(filePath, collectionTitle);
			
			Assert.assertTrue(detailedItemViewPage.isDetailedItemViewPageDisplayed(), "Detailed item view page not displayed");		
			
			Assert.assertTrue(detailedItemViewPage.getCollectionTitle().equals(collectionTitle), "Something went wrong with uploading file; collection title not the one that was selected");
			
			Assert.assertTrue(detailedItemViewPage.getFileTitle().equals(fileTitle), "Something went wrong with uploading file; file title not the one that was selected");
			
		} catch (TimeoutException timeOutExc) {
			LOG4J.error(timeOutExc);
			Assert.assertTrue(false, "Time out error regarding single upload: Either collection couldn't be found or the button 'save' didn't appear. Summarized: The upload failed.");
		}
		
	}
	
	@Test(priority = 3)
	public void discardCollection() {
		CollectionEntryPage collectionContentPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntryPage = collectionContentPage.openDescription();
		collectionEntryPage.deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomepage(homePage);
		homePage.logout();
	}
		
}
