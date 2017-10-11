package test.scripts.edmondScriptsPrivateMode;

import org.testng.annotations.Test;

import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

import org.testng.annotations.BeforeClass;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class UserCollectionWithoutMDPPrivateMode extends BaseSelenium {

	private Homepage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private HashMap<String, String> files;
	String collectionTitle = "Non-published collection without metadata profile: " + TimeStamp.getTimeStamp();
	String collectionDescription = "For testing purposes";
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		switchOnPrivateMode(true);
		prepareFiles();
		logInAsRegisteredUser();
		getProperties().put(collectionPMKey, collectionTitle);
	}
	
	private void switchOnPrivateMode(boolean switchOnPrivateMode) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomepage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		if (switchOnPrivateMode)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		adminHomePage = (AdminHomepage) adminPage.goToHomepage(adminHomePage);
		adminHomePage.logout();
	}
	
	private void prepareFiles() {
		files = new HashMap<String, String>();
		files.put("SamplePNGFile.png", getFilepath("SamplePNGFile.png"));
		files.put("SampleWordFile.docx", getFilepath("SampleWordFile.docx"));
	}
	
	private void logInAsRegisteredUser() {
		navigateToStartPage();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}
	
	@Test(priority = 1)
	public void createCollectionTest() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		collectionEntryPage = createNewCollectionPage.createCollection(collectionTitle, 
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		Assert.assertTrue(
				collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO,
				"Collection couldn't be created");

		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
	
	@Test(priority = 2)
	public void uploadFilesTest() throws AWTException {
		homePage = new StartPage(driver).goToHomepage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).openDescription();
		
		MultipleUploadPage multipleUploadPage = collectionEntryPage.uploadContent();
		
		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}
		
		List<String> fileNames = new ArrayList<String>(files.keySet());
		
		multipleUploadPage.startUpload();
		
		boolean isVerificationSuccessfull = multipleUploadPage.verifyUploadedFiles(fileNames);
		
		Assert.assertTrue(isVerificationSuccessfull, "The list of uploaded files is probably incomplete.");
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomepage(homePage);
		homePage.logout();
		switchOnPrivateMode(false);
	}
}
