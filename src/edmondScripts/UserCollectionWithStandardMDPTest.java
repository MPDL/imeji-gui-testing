package edmondScripts;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.EditCollectionPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class UserCollectionWithStandardMDPTest extends BaseSelenium {

	private HomePage homePage;
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
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}
	
	@Test(priority = 1)
	public void createCollectionStandardMDP() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		
		String collectionDescription = "For testing purposes";
		
		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollectionWithStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		Assert.assertTrue(collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO,
				"Collection couldn't be created");

		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
	
	@Test (priority = 2)
	public void uploadFilesTest() throws AWTException {
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		MultipleUploadPage multipleUploadPage = collectionEntryPage.uploadContent();
		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}
		
		multipleUploadPage.startUpload();
	}
	
	@Test(priority = 3)
	public void defaultMetaDataProfileTest() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionContentPage createdCollection = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean isMetaDataProfileDefined = createdCollection.isMetaDataProfileDefined();
		Assert.assertTrue(isMetaDataProfileDefined, "This collection should have a metadata profile.");
		
		createdCollection.openMetaDataProfile();
		String metaDataProfileTitle = driver.findElement(By.tagName("h3")).getText();
		boolean metaDataProfileIsDefault = metaDataProfileTitle.contains(defaultProfileIdentifier);
		Assert.assertTrue(metaDataProfileIsDefault, "This collection should have a default metadata profile.");
	}
	
	@Test(priority = 4)
	public void addExternalReference() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionContentPage createdCollection = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = createdCollection.viewCollectionInformation().editInformation();
		
		editCollection.addInformation("Test collection", "http://imeji.org/");
		editCollection.addLogo(getFilepath("SampleTIFFile.tif"));
		editCollection.addAuthor("Restricted", "MPDL");
		
		CollectionEntryPage collectionEntryPage = editCollection.submitChanges();
		
		MessageType messageType = collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertEquals(messageType, MessageType.INFO, "Collection was not successfully changed.");
	}
	
	@Test(priority = 5)
	public void viewCollectionEntryPage() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionEntryPage createdCollection = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		boolean infoDisplayed = createdCollection.descriptionMetadataDisplayed();
		Assert.assertTrue(infoDisplayed, "Logo is not displayed");
	}
	
	@Test(priority = 6)
	public void deleteCollection() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		collectionEntryPage.deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
