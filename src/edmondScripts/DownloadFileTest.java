package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionsPage;
import spot.pages.DetailedItemViewPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.HomePage;

public class DownloadFileTest extends BaseSelenium {

	private LoginPage loginPage;
	private HomePage homePage;
	private DetailedItemViewPage detailedItemViewPage;

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
	}
	
	private boolean openItem() {
		CollectionsPage collectionPage = new StartPage(driver).goToCollectionPage();
		CollectionContentPage collectionContentPage = collectionPage.getPageOfLargestCollection();
		
		detailedItemViewPage = collectionContentPage.downloadFirstItemInList();	
		return detailedItemViewPage.isDownloadPossible();
	}
	
	@Test 
	public void downloadFileRU() {
		loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName),
				getPropertyAttribute(spotRUPassWord));
		
		boolean isDownloadPossible = openItem();
		homePage.logout();
		Assert.assertTrue(isDownloadPossible, "Registered user cannot download item. Reason: Download Button probably not displayed/enabled");	
	}
	
	@Test
	public void downloadFileGuest() {
		boolean isDownloadPossible = openItem();		
		Assert.assertTrue(isDownloadPossible, "Guest cannot download item. Reason: Download Button probably not displayed/enabled");
	}
	
	@Test
	public void downloadFileRestricted() {
		loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginRestricted(getPropertyAttribute(restrUserName),
				getPropertyAttribute(restrPassWord));
		
		boolean isDownloadPossible = openItem();
		homePage.logout();
		Assert.assertTrue(isDownloadPossible, "Restricted cannot download item. Reason: Download Button probably not displayed/enabled");
	}
	
	@Test 
	public void downloadFileAdmin() {
		loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = (AdminHomePage) loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName),
				getPropertyAttribute(spotAdminPassWord));
		
		boolean isDownloadPossible = openItem();
		adminHomePage.logout();
		Assert.assertTrue(isDownloadPossible, "Admin cannot download item. Reason: Download Button probably not displayed/enabled");
	}
	
}
