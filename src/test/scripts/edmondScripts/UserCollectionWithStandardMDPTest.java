package test.scripts.edmondScripts;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.EditCollectionPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class UserCollectionWithStandardMDPTest extends BaseSelenium {

	private Homepage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private String collectionTitle = "Non-published collection with standard metadata profile: " + TimeStamp.getTimeStamp();
	// private String defaultProfileIdentifier = "default profile";
	private String defaultProfileIdentifier = "Default Metadata Profile";
	private HashMap<String, String> files;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		prepareFiles();
		logInAsRegisteredUser();
	}
	
	private void prepareFiles() {
		files = new HashMap<String, String>();
		files.put("SampleTIFFile.tif", getFilepath("SampleTIFFile.tif"));
	}
	
	private void logInAsRegisteredUser() {
		navigateToStartPage();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}
	
	@Test(priority = 1)
	public void createCollectionStandardMDP() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		
		String collectionDescription = "For testing purposes";
		
		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollection(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		Assert.assertTrue(collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO,
				"Collection couldn't be created");

		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
	
	@Test (priority = 2)
	public void uploadFilesTest() throws AWTException {
		homePage = new StartPage(driver).goToHomepage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).openDescription();
		
		MultipleUploadPage multipleUploadPage = collectionEntryPage.uploadContent();
		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}
		
		multipleUploadPage.startUpload();
	}
	
	@Test(priority = 3)
	public void addExternalReference() {
		homePage = new StartPage(driver).goToHomepage(homePage);
		CollectionEntryPage createdCollection = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = createdCollection.openDescription().editInformation();
		
		editCollection.addInformation("Test collection", "http://imeji.org/");
		editCollection.addLogo(getFilepath("SampleTIFFile.tif"));
		editCollection.addAuthor("Restricted", "MPDL");
		
		CollectionEntryPage collectionEntryPage = editCollection.submitChanges();
		
		MessageType messageType = collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertEquals(messageType, MessageType.INFO, "Collection was not successfully changed.");
	}
	
	@Test(priority = 4)
	public void deleteCollection() {
		homePage = new StartPage(driver).goToHomepage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).openDescription();
		collectionEntryPage.deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomepage(homePage);
		homePage.logout();
	}
}
