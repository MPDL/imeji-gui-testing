package edmondScripts;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionEntryPage;
import spot.pages.DiscardedCollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.CreateNewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class DiscardCollectionTest extends BaseSelenium {
 	
	private String collectionTitle;
	private HashMap<String, String> files;
	
	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	private DiscardedCollectionEntryPage discardedCollectionEntryPage;

	@BeforeClass
	public void beforeClass() throws AWTException {
		super.setup();
		navigateToStartPage();
		
		files = new HashMap<String, String>();
		files.put("Chrysanthemum.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
		
		collectionTitle = "Collection doomed to be discarded: " + TimeStamp.getTimeStamp();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName),
				getPropertyAttribute(spotRUPassWord));
	}
	
	@Test(priority = 1)
	private void createAndReleaseCollection() throws AWTException {
		CreateNewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();

		String collectionDescription = "This collection is doomed to be discarded. For testing purposes.";

		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
		
		MultipleUploadPage multipleUploadPage = collectionEntryPage.uploadContent();

		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}

		multipleUploadPage.startUpload();
		multipleUploadPage.publishCollection();
	}
	
	@Test(priority = 2)
	public void discardCollectionTest() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		discardedCollectionEntryPage = collectionEntryPage.discardCollection();
		
		Assert.assertTrue(discardedCollectionEntryPage.isDiscarded(), "Collection was probably not discarded.");
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
	
	
	
}
