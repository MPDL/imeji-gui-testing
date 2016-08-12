package edmondScripts;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.CreateNewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class DeleteCollectionTest extends BaseSelenium {

	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private String collectionTitle = "Collection doomed to be deleted: " + TimeStamp.getTimeStamp();
	private HashMap<String, String> files;

	@BeforeClass
	public void beforeClass() throws AWTException {
		super.setup();
		navigateToStartPage();
		loginAsNotAdmin();
	}
	
	private void loginAsNotAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();

		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName),
				getPropertyAttribute(spotRUPassWord));
	}
	
	@Test(priority = 1)
	public void createCollection() {
		CreateNewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();

		String collectionDescription = "This collection is doomed to be deleted. For testing purposes.";

		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
	
	@Test(priority = 2)
	public void uploadFiles() throws AWTException {
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		files = new HashMap<String, String>();
		files.put("SampleJPGFile.jpg", getFilepath("SampleJPGFile.jpg"));
		
		MultipleUploadPage multipleUploadPage = collectionEntryPage.uploadContent();
		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}
		multipleUploadPage.startUpload();
	}

	@Test(priority = 3)
	public void deleteCollectionTest() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		ActionComponent actionComponent = collectionEntryPage.getActionComponent();
		CollectionsPage collectionsPage = (CollectionsPage) actionComponent.doAction(ActionType.DELETE);

		String actualInfoMessage = collectionsPage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Collection " + collectionTitle + " deleted successfully";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage,
				"Collection deletion most probably unsuccessful, since deletion confirmation info message is not displayed.");

	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}

}
