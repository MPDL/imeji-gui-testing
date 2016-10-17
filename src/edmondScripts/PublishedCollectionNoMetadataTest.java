package edmondScripts;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;


public class PublishedCollectionNoMetadataTest extends BaseSelenium {

	private LoginPage loginPage;
	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private HashMap<String, String> files;
	private MultipleUploadPage multipleUploadPage;

	public final String collectionTitle = "Published collection without metadata profile: " + TimeStamp.getTimeStamp();	
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();		
		
		loginPage = new StartPage(driver).openLoginForm();
		
		files = new HashMap<String, String>();
		files.put("SampleJPGFile.jpg", getFilepath("SampleJPGFile.jpg"));
		files.put("SamplePDFFile.pdf", getFilepath("SamplePDFFile.pdf"));
		
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}

	@Test(priority = 1)
	public void createCollectionWithoutMetaDataProfileTest() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		
		String collectionDescription = "This collection has no meta data profile. It is being published. ";
		for (int i = 0; i < 100; i++)
			collectionDescription += "It has a very long description text. ";

		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		Assert.assertTrue(
				collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO,
				"Collection couldn't be created");

		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
		
		getProperties().put(releasedCollectionKey, collectionTitle);
	}
	
	@Test(priority = 2)
	public void extensionTest() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.goToCollectionPage().expandCollapseDescription(collectionTitle);
	}

	@Test(priority = 3)
	public void uploadFilesTest() throws AWTException {
		homePage = new StartPage(driver).goToHomePage(homePage);
		multipleUploadPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation().uploadContent();
		
		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}
		
		List<String> fileNames = new ArrayList<String>(files.keySet());
		
		multipleUploadPage.startUpload();
		
		boolean isVerificationSuccessfull = multipleUploadPage.verifyUploadedFiles(fileNames);
		
		Assert.assertTrue(isVerificationSuccessfull, "The list of uploaded files is probably incomplete.");
	}

	@Test (priority = 4)
	public void publishCollectionTest() {
		multipleUploadPage.publishCollection();

		String actualInfoMessage = multipleUploadPage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Collection released successfully";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage,
				"Something went wrong with the release of the collection.");
	}
	
	@Test (priority = 5)
	public void noMetaDataProfileTest() {
		CollectionContentPage createdCollection = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean isMetaDataProfileDefined = createdCollection.isMetaDataProfileDefined();
		Assert.assertFalse(isMetaDataProfileDefined, "This collection should not have a metadata profile.");
	}
	
	@Test (priority = 6)
	public void discardCollection() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		collectionEntryPage.discardCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
