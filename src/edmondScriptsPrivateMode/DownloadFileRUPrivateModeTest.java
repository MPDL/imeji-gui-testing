package edmondScriptsPrivateMode;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionsPage;
import spot.pages.DetailedItemViewPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.HomePage;

public class DownloadFileRUPrivateModeTest extends BaseSelenium {

	private HomePage homePage;
	
	private DetailedItemViewPage detailedItemViewPage;

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		
		switchToPrivateMode(true);
		loginAsRegisteredUser();
		
		CollectionsPage collectionPage = new StartPage(driver).goToCollectionPage();
		CollectionContentPage collectionContentPage = collectionPage.getPageOfLargestCollection();
		
		detailedItemViewPage = collectionContentPage.downloadFirstItemInList();	
		
	}
	
	private void switchToPrivateMode(boolean shouldPrivateModeBeOn) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName),
				getPropertyAttribute(spotAdminPassWord));
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		
		if (shouldPrivateModeBeOn)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		adminHomePage.logout();
	}
	
	public void loginAsRegisteredUser() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
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
