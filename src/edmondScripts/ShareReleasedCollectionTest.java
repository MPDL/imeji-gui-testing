package edmondScripts;


import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionEntryPage;
import spot.pages.KindOfSharePage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;

public class ShareReleasedCollectionTest extends BaseSelenium {

	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;

	public String collectionTitle;	
	
	@Test (priority = 1)
	public void user1SharesAdminRights() {
		collectionTitle = getPropertyAttribute(releasedCollectionKey);
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
		
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		KindOfSharePage shareTransitionPage = collectionEntryPage.goToSharePage();
		SharePage sharePage = shareTransitionPage.shareWithAUser();
		sharePage.share(true, false, getPropertyAttribute(restrUserName), false, false, false, false, false, false, true);
		
		collectionEntryPage = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
	}
	
	@Test (priority = 2)
	public void user1ChecksGrants() {
		KindOfSharePage shareTransitionPage = collectionEntryPage.goToSharePage();
		SharePage sharePage = shareTransitionPage.shareWithAUser();
		
		String userFullName = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(userFullName);
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(false, userFullName,
				true, true, true, true, true, true, true);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
	}
	
	@Test (priority = 3)
	public void user1Logout() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
	
	@Test (priority = 4)
	public void user2AdminCollection() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginRestricted(getPropertyAttribute(restrUserName),
				getPropertyAttribute(restrPassWord));
		
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		boolean shareIconVisible = collectionEntryPage.shareIconVisible();
		Assert.assertTrue(shareIconVisible, "Share icon is not visible.");
		
		collectionEntryPage.addMetaDataProfile();
		navigateDriverBack();
		collectionEntryPage.editInformation();
		navigateDriverBack();
		collectionEntryPage.uploadContent();
		navigateDriverBack();
	}
	
	@AfterClass
	public void logout() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
