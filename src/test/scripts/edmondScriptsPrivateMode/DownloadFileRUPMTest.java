package test.scripts.edmondScriptsPrivateMode;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.AdministrationPage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.ItemViewPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class DownloadFileRUPMTest extends BaseSelenium {

	private Homepage homePage;
	private ItemViewPage detailedItemViewPage;

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		
		switchToPrivateMode(true);
		loginAsRegisteredUser();
		
		CollectionsPage collectionPage = new StartPage(driver).goToCollectionPage();
		CollectionEntryPage collectionContentPage = collectionPage.getPageOfLargestCollection();
		
		detailedItemViewPage = collectionContentPage.downloadFirstItemInList();	
		
	}
	
	private void switchToPrivateMode(boolean shouldPrivateModeBeOn) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomepage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername),
				getPropertyAttribute(adminPassword));
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		
		if (shouldPrivateModeBeOn)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		adminHomePage.logout();
	}
	
	private void loginAsRegisteredUser() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}
	
	@Test 
	public void downloadFileTest() {
		boolean isDownloadPossible = detailedItemViewPage.isDownloadPossible();
		
		Assert.assertTrue(isDownloadPossible, "Registered user couldn't download a item. Reason: Download Button most probably not displayed/enabled");	
	}
	
	@AfterClass
	public void afterClass() {
		homePage.logout();
		switchToPrivateMode(false);
	}
	
	
}
