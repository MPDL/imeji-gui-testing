package edmondScripts;


import org.testng.Assert;
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
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		KindOfSharePage shareTransitionPage = collectionEntryPage.goToSharePage();
		SharePage sharePage = shareTransitionPage.shareWithAUser();
		sharePage.share(false, getPropertyAttribute(restrUserName), false, false, false, false, false, false, true);
		
		collectionEntryPage = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
	}
	
	@Test (priority = 2)
	public void user1ChecksGrants() {
		KindOfSharePage shareTransitionPage = collectionEntryPage.goToSharePage();
		SharePage sharePage = shareTransitionPage.shareWithAUser();
		
		boolean nameInShareList = sharePage.checkPresenceOfSharedPersonInList(getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName));
		Assert.assertTrue(nameInShareList, "User 2 is not in share list.");
		
		boolean grantIsCorrect = sharePage.checkGrantSelections(getPropertyAttribute(restrUserName), true,
				true, true, true, true, true, true);
		Assert.assertTrue(grantIsCorrect, "Grant is not correct.");
		
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
	
	@Test (priority = 3)
	public void user2AdminCollection() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginRestricted(getPropertyAttribute(restrUserName),
				getPropertyAttribute(restrPassWord));
		
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		
		collectionEntryPage.addMetaDataProfile();
		navigateDriverBack();
		collectionEntryPage.editInformation();
		navigateDriverBack();
		collectionEntryPage.uploadContent();
		navigateDriverBack();
		
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
