package test.scripts.edmondScripts;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class UserDeletesPrivateCollectionTest extends BaseSelenium {

	private Homepage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private HashMap<String, String> files;
	private final String collectionTitle = "Private collection with default metadata profile: " + TimeStamp.getTimeStamp();
	private String collectionDescription = "For testing purposes";
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
	}
	
	@Test(priority = 1)
	public void createPrivateCollection() throws AWTException {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		collectionEntryPage = createNewCollectionPage.createCollection(collectionTitle, collectionDescription,
				getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
		
		files = new HashMap<String, String>();
		files.put("SampleTIFFile.tif", getFilepath("SampleTIFFile.tif"));
	}
	
	@Test(priority = 2)
	public void uploadFiles() throws AWTException {
		homePage = new StartPage(driver).goToHomepage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).openDescription();
		MultipleUploadPage multipleUploadPage = collectionEntryPage.uploadContent();
		
		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}
		
		List<String> fileNames = new ArrayList<String>(files.keySet());
		
		multipleUploadPage.startUpload();
		
		boolean isVerificationSuccessful = multipleUploadPage.verifyUploadedFiles(fileNames);
		
		Assert.assertTrue(isVerificationSuccessful, "The list of uploaded files is probably incomplete.");
	}
	
	@Test(priority = 3)
	public void deletePrivateCollection() {
		homePage = new StartPage(driver).goToHomepage(homePage);
		CollectionsPage collectionsPage = homePage.goToCollectionPage();
		CollectionEntryPage collectionContentPage = collectionsPage.openCollectionByTitle(collectionTitle);
		collectionsPage = collectionContentPage.openDescription().deleteCollection();
		homePage = collectionsPage.goToHomepage(homePage);
		
		collectionsPage = homePage.goToCollectionPage();
		boolean collectionSuccessfullyDeleted = false;
		try {
			collectionsPage.openCollectionByTitle(collectionTitle);
		}
		catch (NoSuchElementException | NullPointerException exc) {
			collectionSuccessfullyDeleted = true;
		}
		
		Assert.assertTrue(collectionSuccessfullyDeleted, "Private collection was not successfully deleted");
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomepage(homePage);
		homePage.logout();
	}
}
