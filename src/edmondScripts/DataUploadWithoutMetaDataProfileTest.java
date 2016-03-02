package edmondScripts;

import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
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

public class DataUploadWithoutMetaDataProfileTest extends BaseSelenium/*extends BaseTest*/ {

	private static final Logger log4j = LogManager.getLogger(DataUploadWithoutMetaDataProfileTest.class.getName());
	
	private LoginPage loginPage;
	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private HashMap<String, String> files;
	
	public final String collectionTitle = "Not published test collection without meta data profile: " + TimeStamp.getTimeStamp();
	
	@AfterClass
	public void afterClass() {
		homePage.logout();
	}
	
	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();		
		
		loginPage = new StartPage(driver).openLoginForm();
		
		files = new HashMap<String, String>();
		files.put("Chrysanthemum.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
//		files.put("SamplePNGFile.png", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SamplePNGFile.png");
//		files.put("SampleWordFile.docx", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SampleWordFile.docx");		
		
//		new StartPage(driver).selectLanguage(englishSetup);
	}

	@Test (priority = 1)
	public void loginTest() {
		homePage = loginPage.loginAsAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));

		String adminFullName = getPropertyAttribute(ruFamilyName) + ", " + getPropertyAttribute(ruGivenName);
		Assert.assertEquals(homePage.getLoggedInUserFullName(), adminFullName, "User name doesn't match");
	
	}

	@Test(priority = 2)
	public void createCollectionWithoutMetaDataProfileTest() {
		
		CreateNewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		
		String collectionDescription = "This collection has no meta data profile. It is not being published.";

		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));

		Assert.assertTrue(
				collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO,
				"Collection couldn't be created");

		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");	
	}

	@Test (priority = 3)
	public void uploadFilesTest() throws AWTException {				
	
		for (Map.Entry<String, String> file : files.entrySet()) {
			String fileTitle = file.getKey();
			String filePath = file.getValue();
			
			testFile(fileTitle, filePath);
		}
		navigateToStartPage();
	}

	private void testFile(String fileTitle, String filePath) throws AWTException {
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
		
}
