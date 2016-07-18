package edmondScripts;

import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.DetailedItemViewPage;
import spot.pages.LoginPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.CreateNewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

import org.testng.annotations.BeforeClass;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class DataUploadWithoutMetaDataProfileTest extends BaseSelenium {

	private static final Logger log4j = LogManager.getLogger(DataUploadWithoutMetaDataProfileTest.class.getName());
	
	private LoginPage loginPage;
	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private HashMap<String, String> files;
	
	public final String collectionTitle = "Not published test collection without meta data profile: " + TimeStamp.getTimeStamp();
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
		
		files = new HashMap<String, String>();
		files.put("SamplePDFFile.pdf", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SamplePDFFile.pdf");	
		files.put("SampleWordFile.docx", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SampleWordFile.docx");
	}

	@Test(priority = 1)
	public void createCollectionWithoutMetaDataProfileTest() {
		
		CreateNewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		
		String collectionDescription = "This collection has no meta data profile. It is not being published.";

		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
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
		homePage = collectionEntryPage.goToHomePage(homePage);
		SingleUploadPage singleUploadPage = homePage.goToSingleUploadPage();
		
		DetailedItemViewPage detailedItemViewPage = null;
		
		try {
			detailedItemViewPage = singleUploadPage.upload(filePath, collectionTitle);
			
			Assert.assertTrue(detailedItemViewPage.isDetailedItemViewPageDisplayed(), "Detailed item view page not displayed");		
			
			Assert.assertTrue(detailedItemViewPage.getCollectionTitle().equals(collectionTitle), "Something went wrong with uploading file; collection title not the one that was selected");
			
			Assert.assertTrue(detailedItemViewPage.getFileTitle().equals(fileTitle), "Something went wrong with uploading file; file title not the one that was selected");
			
		} catch (TimeoutException timeOutExc) {
			log4j.error(timeOutExc);
			Assert.assertTrue(false, "Time out error regarding single upload: Either collection couldn't be found or the button 'save' didn't appear. Summarized: The upload failed.");
		}
		
	}
	
	@Test(priority = 3)
	public void discardCollection() {
		CollectionContentPage collectionContentPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntryPage = collectionContentPage.viewCollectionInformation();
		collectionEntryPage.deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
		
}
