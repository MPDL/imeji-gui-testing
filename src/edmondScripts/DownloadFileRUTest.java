package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionsPage;
import spot.pages.DetailedItemViewPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;

public class DownloadFileRUTest extends BaseSelenium {

	private LoginPage loginPage;
	private AdminHomePage adminHomePage;
	
	private DetailedItemViewPage detailedItemViewPage;

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
		
		CollectionsPage collectionPage = new StartPage(driver).goToCollectionPage();
		CollectionContentPage collectionContentPage = collectionPage.getPageOfLargestCollection();
		
		loginPage = new StartPage(driver).openLoginForm();
		
//		new StartPage(driver).selectLanguage(englishSetup);
		
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotRUUserName),
				getPropertyAttribute(spotRUPassWord));
		
		detailedItemViewPage = collectionContentPage.downloadFirstItemInList();	
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}
	
	@Test 
	public void downloadFileTest() {
		
		boolean isDownloadPossible = detailedItemViewPage.isDownloadPossible();		
		
		Assert.assertTrue(isDownloadPossible, "Registered user couldn't download a item. Reason: Download Button most probabyl not displayed/enabled");	
	}
}
