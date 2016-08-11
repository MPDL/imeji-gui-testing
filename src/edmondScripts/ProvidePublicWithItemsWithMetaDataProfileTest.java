package edmondScripts;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.CreateNewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class ProvidePublicWithItemsWithMetaDataProfileTest extends BaseSelenium {

	private LoginPage loginPage;
	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private HashMap<String, String> files;
	private MultipleUploadPage multipleUploadPage;

	public final String collectionTitle = "Published test collection with default meta data profile "
			+ TimeStamp.getTimeStamp();

	@BeforeClass
	public void beforeClass() {
		super.setup();
		loginPage = new StartPage(driver).openLoginForm();
		
		files = new HashMap<String, String>();
		files.put("SampleXLSXFile.xlsx", "file:" + getClass().getResource("/SampleXLSXFile.xlsx").getPath());
		files.put("SampleSWCFile.swc", "file:" + getClass().getResource("/SampleSWCFile.swc").getPath());	
		
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}
	
	@AfterClass
	public void afterClass() {
		CollectionContentPage createdCollection = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		createdCollection.discardCollection();
		
		homePage.logout();
	}

	@Test(priority = 1)
	public void createCollectionWithMetaDataProfileTest() {
		System.out.println("Attempt to create collection without meta data");
		CreateNewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		
		String collectionDescription = "This collection has a default meta data profile. It is being published.";

		collectionEntryPage = createNewCollectionPage.createCollectionWithStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));

		Assert.assertTrue(
				collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO,
				"Collection couldn't be created");

		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");

	}

	@Test (priority = 2)
	public void uploadFilesTest() throws AWTException {

		multipleUploadPage = collectionEntryPage.uploadContent();
		
		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}
		
		List<String> fileNames = new ArrayList<String>(files.keySet());
		
		multipleUploadPage.startUpload();
		
		boolean isVerificationSuccessfull = multipleUploadPage.verifyUploadedFiles(fileNames);
		
		Assert.assertTrue(isVerificationSuccessfull, "The list of uploaded files is probably incomplete.");
	}

	@Test (priority = 3)
	public void publishCollectionTest() {
		
		multipleUploadPage.publishCollection();
		
		String actualInfoMessage = multipleUploadPage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Collection released successfully";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Something went wrong with the release of the collection.");
	}
	
	@Test (priority = 4)
	public void defaultMetaDataProfileTest() {
		CollectionContentPage createdCollection = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean isMetaDataProfileDefined = createdCollection.isMetaDataProfileDefined();
		Assert.assertFalse(isMetaDataProfileDefined, "This collection should have a metadata profile.");
		
		createdCollection.openMetaDataProfile();
		String metaDataProfileTitle = driver.findElement(By.tagName("h3")).getText();
		boolean metaDataProfileIsDefault = metaDataProfileTitle.contains("default profile");
		Assert.assertTrue(metaDataProfileIsDefault, "This collection should have a default metadata profile.");
	}
}
