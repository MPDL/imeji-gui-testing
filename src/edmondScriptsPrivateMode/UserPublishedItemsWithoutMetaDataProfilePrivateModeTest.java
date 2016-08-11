package edmondScriptsPrivateMode;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
import spot.pages.AdministrationPage;
import spot.pages.BrowseItemsPage;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class UserPublishedItemsWithoutMetaDataProfilePrivateModeTest extends BaseSelenium {
  
	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	private MultipleUploadPage multipleUploadPage;
	
	private String collectionTitle = "Collection in private mode without metadata profile: " + TimeStamp.getTimeStamp();
	private HashMap<String, String> files;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		switchOnPrivateMode(true);
		prepareFiles();
		logInAsRegisteredUser();
	}
	
	private void switchOnPrivateMode(boolean switchOnPrivateMode) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), 
				getPropertyAttribute(spotAdminPassWord));
		
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		if (switchOnPrivateMode)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		adminHomePage = (AdminHomePage) adminPage.goToHomePage(adminHomePage);
		adminHomePage.logout();
	}
	
	private void prepareFiles() {
		files = new HashMap<String, String>();
		files.put("SampleJPGFile2.jpg", "file:" + getClass().getResource("/SampleJPGFile2.jpg").getPath());
		files.put("SamplePDFFile.pdf", "file:" + getClass().getResource("/SamplePDFFile.pdf").getPath());
	}
	
	public void logInAsRegisteredUser() {
		navigateToStartPage();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}
	
	@Test(priority = 1)
	public void createCollectionWithoutMetadataProfile() {
		CreateNewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		
		String collectionDescription = "Non-published collection in private mode without metadata profile for testing purposes.";
		
		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		Assert.assertTrue(
				collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO,
				"Collection couldn't be created");

		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
	
	@Test(priority = 2)
	public void uploadFiles() throws AWTException {
		multipleUploadPage = collectionEntryPage.uploadContent();
		
		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}
		
		multipleUploadPage.startUpload();
	}
	
	@Test(priority = 3, expectedExceptions = NoSuchElementException.class)
	public void publishCollection() {
		multipleUploadPage.publishCollection();
	}
	
	@Test(priority = 4)
	public void userViewsUploadedItemsTest() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		BrowseItemsPage browseItemsPage = homePage.navigateToItemPage();
		for (String fileName : files.keySet()) {
			boolean itemIsPresent = browseItemsPage.isItemPresent(fileName);
			Assert.assertTrue(itemIsPresent, "Published file is not displayed on item page.");
		}
		
	}
	
	@Test(priority = 5)
	public void deleteCollection() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		collectionEntryPage.deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
		switchOnPrivateMode(false);
	}
	
}
