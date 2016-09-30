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
import spot.pages.AdministrationPage;
import spot.pages.BrowseItemsPage;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class UserWithdrawsPublishedCollection extends BaseSelenium {
  
	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	private MultipleUploadPage multipleUploadPage;
	
	private HashMap<String, String> files;
	private final String collectionTitle = "Collection in private mode with standard metadata profile: "
			+ TimeStamp.getTimeStamp();
	private String collectionDescription = "For testing purposes";
	
	@BeforeClass
	public void beforeClass() throws AWTException {
		super.setup();
		prepareFiles();
		switchOnPrivateMode(true);
	}
	
	private void prepareFiles() {
		files = new HashMap<String, String>();
		files.put("SampleJPGFile2.jpg", getFilepath("SampleJPGFile2.jpg"));
		files.put("SampleTIFFile.tif", getFilepath("SampleTIFFile.tif"));
	}
	
	private void switchOnPrivateMode(boolean switchOnPrivateMode) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		if (switchOnPrivateMode)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		logout(adminHomePage);
	}
	
	@Test(priority = 1)
	public void createCollectionWithMDP() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
		
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		collectionEntryPage = createNewCollectionPage.createCollectionWithStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
	
	@Test(priority = 2)
	public void uploadFilesAndPublishCollection() throws AWTException {
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
	public void withdrawCollection() {
		CollectionContentPage collectionContentPage = multipleUploadPage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		CollectionEntryPage collectionEntryPage = collectionContentPage.viewCollectionInformation();
		CollectionsPage collectionsPage = collectionEntryPage.deleteCollection();
		
		boolean collectionHasBeenWithdrawn = false;
		
		try {
			collectionsPage.openCollectionByTitle(collectionTitle);
		}
		catch (NoSuchElementException | NullPointerException exc) {
			collectionHasBeenWithdrawn = true;
		}
		Assert.assertTrue(collectionHasBeenWithdrawn);
	}
	
	@Test(priority = 5)
	public void userCannotViewWithdrawnItems() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		BrowseItemsPage browseItemsPage = homePage.navigateToItemPage();
		for (String fileName : files.keySet()) {
			boolean itemIsPresent = browseItemsPage.isItemPresent(fileName);
			Assert.assertFalse(itemIsPresent, "Published file is not displayed on item page.");
		}
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
		switchOnPrivateMode(false);
	}
}
