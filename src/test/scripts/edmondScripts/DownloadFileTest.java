package test.scripts.edmondScripts;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.ItemViewPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class DownloadFileTest extends BaseSelenium {

	private LoginPage loginPage;
	private Homepage homePage;
	private ItemViewPage detailedItemViewPage;

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
	}
	
	private boolean openItem() {
		CollectionsPage collectionPage = new StartPage(driver).goToCollectionPage();
		CollectionEntryPage collectionContentPage = collectionPage.getPageOfLargestCollection();
		
		detailedItemViewPage = collectionContentPage.downloadFirstItemInList();	
		return detailedItemViewPage.isDownloadPossible();
	}
	
	@Test 
	public void downloadFileRU() {
		loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername),
				getPropertyAttribute(ruPassword));
		
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
		homePage = loginPage.loginRestricted(getPropertyAttribute(restrUsername),
				getPropertyAttribute(restrPassword));
		
		boolean isDownloadPossible = openItem();
		homePage.logout();
		Assert.assertTrue(isDownloadPossible, "Restricted cannot download item. Reason: Download Button probably not displayed/enabled");
	}
	
	@Test 
	public void downloadFileAdmin() {
		loginPage = new StartPage(driver).openLoginForm();
		AdminHomepage adminHomePage = (AdminHomepage) loginPage.loginAsAdmin(getPropertyAttribute(adminUsername),
				getPropertyAttribute(adminPassword));
		
		boolean isDownloadPossible = openItem();
		adminHomePage.logout();
		Assert.assertTrue(isDownloadPossible, "Admin cannot download item. Reason: Download Button probably not displayed/enabled");
	}
	
}
