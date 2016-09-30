package edmondScriptsPrivateMode;

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

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
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

public class UserDeletesPrivateCollectionPMTest extends BaseSelenium {
	
	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private HashMap<String, String> files;
	private final String collectionTitle = "Private collection with default metadata profile: " + TimeStamp.getTimeStamp();
	private String collectionDescription = "For testing purposes";
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		switchToPrivateMode(true);
	}
	
	private void switchToPrivateMode(boolean switchOnPrivateMode) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		if (switchOnPrivateMode)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		adminHomePage = (AdminHomePage) adminPage.goToHomePage(adminHomePage);
		adminHomePage.logout();
	}
	
	@Test(priority = 1)
	public void createPrivateCollection() throws AWTException {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
		
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		collectionEntryPage = createNewCollectionPage.createCollectionWithStandardMetaDataProfile(collectionTitle, collectionDescription,
				getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
		
		files = new HashMap<String, String>();
		files.put("SampleTIFFile.tif", getFilepath("SampleTIFFile.tif"));
	}
	
	@Test(priority = 2)
	public void uploadFiles() throws AWTException {
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		MultipleUploadPage multipleUploadPage = collectionEntryPage.uploadContent();
		
		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}
		
		List<String> fileNames = new ArrayList<String>(files.keySet());
		
		multipleUploadPage.startUpload();
		
		boolean isVerificationSuccessfull = multipleUploadPage.verifyUploadedFiles(fileNames);
		
		Assert.assertTrue(isVerificationSuccessfull, "The list of uploaded files is probably incomplete.");
	}
	
	@Test(priority = 3)
	public void deletePrivateCollection() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionsPage collectionsPage = homePage.goToCollectionPage();
		CollectionContentPage collectionContentPage = collectionsPage.openCollectionByTitle(collectionTitle);
		collectionsPage = collectionContentPage.viewCollectionInformation().deleteCollection();
		homePage = collectionsPage.goToHomePage(homePage);
		
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
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
		switchToPrivateMode(false);
	}
	
}
