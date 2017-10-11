package test.scripts.edmondScriptsPrivateMode;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.BrowseItemsPage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class UserWithdrawsPublishedCollection extends BaseSelenium {
  
	private Homepage homePage;
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
		AdminHomepage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		
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
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		collectionEntryPage = createNewCollectionPage.createCollection(collectionTitle,
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
		CollectionEntryPage collectionContentPage = multipleUploadPage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		CollectionEntryPage collectionEntryPage = collectionContentPage.openDescription();
		CollectionsPage collectionsPage = collectionEntryPage.deleteCollection();
		
		boolean collectionHasBeenWithdrawn;
		
		try {
			collectionsPage.openCollectionByTitle(collectionTitle);
			collectionHasBeenWithdrawn = true;
		}
		catch (NoSuchElementException | NullPointerException exc) {
			collectionHasBeenWithdrawn = false;
		}
		Assert.assertFalse(collectionHasBeenWithdrawn);
	}
	
	/*@Test(priority = 5)
	public void userCanViewWithdrawnItems() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		BrowseItemsPage browseItemsPage = homePage.navigateToItemPage();
		browseItemsPage = browseItemsPage.discardedOnly();
		for (String fileName : files.keySet()) {
			boolean itemIsPresent = browseItemsPage.isItemPresent(fileName);
			Assert.assertTrue(itemIsPresent, "Published file should be displayed on item page.");
		}
	}*/
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomepage(homePage);
		homePage.logout();
		switchOnPrivateMode(false);
	}
}
