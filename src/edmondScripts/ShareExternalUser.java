package edmondScripts;

import java.awt.AWTException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.DetailedItemViewPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.util.TimeStamp;

public class ShareExternalUser extends BaseSelenium {

	private String collectionTitle = "Shared collection with external user: " + TimeStamp.getTimeStamp();
	private String externalEmails = "nonexistentuser@mpdl.mpg.de, nonexistentuser2@mpdl.mpg.de, nonexistentuser3@mpdl.mpg.de";
	private String userFullName;
	
	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		userFullName = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
	}
	
	@Test(priority = 1)
	public void loginRU() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}
	
	@Test(priority = 2)
	public void createCollection() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		collectionEntryPage = createNewCollectionPage.createCollectionWithStandardMetaDataProfile(collectionTitle, "",
				getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
	}
	
	@Test(priority = 3)
	public void shareCollectionReadExternal() {
		SharePage sharePage = collectionEntryPage.goToSharePage().shareWithAUser();
		sharePage = sharePage.share(false, false, externalEmails.split(",")[0], true, false, false, false, false, false, false);
		
		Assert.assertTrue(sharePage.messageDisplayed(), "No message on the external emails was displayed.");
		
		Assert.assertTrue(sharePage.inviteButtonEnabled(), "External user cannot be invited to register.");
		
		List<WebElement> emails = sharePage.getExternalEmails();
		for (WebElement email : emails) {
			String userEmail = email.getText().trim();
			Assert.assertTrue(externalEmails.contains(userEmail), "User email " + userEmail + " is not in list.");
		}
	}
	
	@Test(priority = 4)
	public void uploadItem() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionContentPage collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		MultipleUploadPage multipleUpload = collectionContent.viewCollectionInformation().uploadContent();
		try {
			multipleUpload.addFile(getFilepath("SampleJPGFile.jpg"));
			multipleUpload.startUpload();
		} 
		catch (AWTException exc) {}
	}
	
	@Test(priority = 5)
	public void shareItemReadExternal() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionContentPage collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		DetailedItemViewPage itemView = collectionContent.openItem(0);
		SharePage sharePage = itemView.shareItem().shareWithAUser();
		sharePage = sharePage.share(externalEmails.split(",")[0], true);
		
		Assert.assertTrue(sharePage.messageDisplayed(), "No message on the external emails was displayed.");
		
		Assert.assertTrue(sharePage.inviteButtonEnabled(), "External user cannot be invited to register.");
		
		List<WebElement> emails = sharePage.getExternalEmails();
		for (WebElement email : emails) {
			String userEmail = email.getText().trim();
			Assert.assertTrue(externalEmails.contains(userEmail), "User email " + userEmail + " is not in list.");
		}
	}
	
	@Test(priority = 6)
	public void shareCollectionAdminExternal() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionContentPage collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		SharePage sharePage = collectionContent.share().shareWithAUser();
		sharePage = sharePage.share(false, false, externalEmails.split(",")[0], true, true, true, true, true, true, true);
		
		Assert.assertTrue(sharePage.messageDisplayed(), "No message on the external emails was displayed.");
		
		Assert.assertTrue(sharePage.inviteButtonEnabled(), "External user cannot be invited to register.");
		
		List<WebElement> emails = sharePage.getExternalEmails();
		for (WebElement email : emails) {
			String userEmail = email.getText().trim();
			Assert.assertTrue(externalEmails.contains(userEmail), "User email " + userEmail + " is not in list.");
		}
	}
	
	@Test(priority = 7)
	public void shareCollectionAdminManyUsers() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionContentPage collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		SharePage sharePage = collectionContent.share().shareWithAUser();
		sharePage = sharePage.share(false, false, externalEmails, true, true, true, true, true, true, true);
		
		Assert.assertTrue(sharePage.messageDisplayed(), "No message on the external emails was displayed.");
		
		Assert.assertTrue(sharePage.inviteButtonEnabled(), "External user cannot be invited to register.");
		
		List<WebElement> emails = sharePage.getExternalEmails();
		for (WebElement email : emails) {
			String userEmail = email.getText().trim();
			Assert.assertTrue(externalEmails.contains(userEmail), "User email " + userEmail + " is not in list.");
		}
	}
	
	@Test(priority = 8)
	public void shareGrantCreateItems() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		SharePage sharePage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).share().shareWithAUser();
		sharePage = sharePage.share(false, false, getPropertyAttribute(restrUserName), true, true, false, false, false, false, false);
		
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(userFullName);
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(false, userFullName, true, true, false, false, false, false, false);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
	}
	
	@Test(priority = 9)
	public void shareGrantEditItems() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		SharePage sharePage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).share().shareWithAUser();
		sharePage = sharePage.share(false, false, getPropertyAttribute(restrUserName), true, false, true, false, false, false, false);
		
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(userFullName);
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(false, userFullName, true, false, true, false, false, false, false);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
	}
	
	@Test(priority = 10)
	public void shareGrantEditInfo() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		SharePage sharePage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).share().shareWithAUser();
		sharePage = sharePage.share(false, false, getPropertyAttribute(restrUserName), true, false, false, false, true, false, false);
		
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(userFullName);
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(false, userFullName, true, false, false, false, true, false, false);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
	}
	
	@Test(priority = 11)
	public void shareGrantEditProfile() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		SharePage sharePage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).share().shareWithAUser();
		sharePage = sharePage.share(false, false, getPropertyAttribute(restrUserName), true, false, false, false, false, true, false);
		
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(userFullName);
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(false, userFullName, true, false, false, false, false, true, false);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
	}
	
	@Test(priority = 12)
	public void shareCollectionReadManyUsers() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		SharePage sharePage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation().goToSharePage().shareWithAUser();
		sharePage = sharePage.share(false, false, externalEmails, true, false, false, false, false, false, false);
		
		Assert.assertTrue(sharePage.messageDisplayed(), "No message on the external emails was displayed.");
		
		Assert.assertTrue(sharePage.inviteButtonEnabled(), "External user cannot be invited to register.");
		
		List<WebElement> emails = sharePage.getExternalEmails();
		for (WebElement email : emails) {
			String userEmail = email.getText().trim();
			Assert.assertTrue(externalEmails.contains(userEmail), "User email " + userEmail + " is not in list.");
		}
	}
	
	@Test(priority = 13)
	public void deleteCollection() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation().deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
