package edmondScripts;

import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;
import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.DetailedItemViewPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class DataUploadWithoutMetaDataProfileTest extends BaseSelenium/*extends BaseTest*/ {

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
		files.put("SamplePNGFile.png", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SamplePNGFile.png");
		files.put("SampleWordFile.docx", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SampleWordFile.docx");		
		
		new StartPage(driver).selectLanguage(englishSetup);
	}

	@Test (priority = 1)/*(groups = {"login", "dataUploadWithoutMetaDataProfile"})*/
	public void loginTest() {
		homePage = loginPage.loginAsAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));

		String adminFullName = getPropertyAttribute(ruFamilyName) + ", " + getPropertyAttribute(ruGivenName);
		Assert.assertEquals(homePage.getLoggedInUserFullName(), adminFullName, "User name doesn't match");
	
	}

	@Test(priority = 2)/*(groups={"collectionCreated", "dataUploadWithoutMetaDataProfile"}, dependsOnGroups = "login")*/
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

	@Test (priority = 3)/*(groups="dataUploadWithoutMetaDataProfile", dependsOnGroups="collectionCreated")*/
	public void uploadFilesTest() throws AWTException {				
	
//		navigateToStartPage();
		
		for (Map.Entry<String, String> file : files.entrySet()) {
			String fileTitle = file.getKey();
			String filePath = file.getValue();
			
			testFile(fileTitle, filePath);
		}
		navigateToStartPage();
	}

	private void testFile(String fileTitle, String filePath) throws AWTException {
//		SingleUploadPage singleUploadPage = navigateToUploadPage();
		SingleUploadPage singleUploadPage = homePage.goToSingleUploadPage();
		
		DetailedItemViewPage detailedItemViewPage = singleUploadPage.upload(filePath, collectionTitle);		
		
		Assert.assertTrue(detailedItemViewPage.isDetailedItemViewPageDisplayed(), "Detailed item view page not displayed");		
		
		Assert.assertTrue(detailedItemViewPage.getCollectionTitle().equals(collectionTitle), "Something went wrong with uploading file; collection title not the one that was selected");
		
		Assert.assertTrue(detailedItemViewPage.getFileTitle().equals(fileTitle), "Something went wrong with uploading file; file title not the one that was selected");
		
	}
		
}
