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
import spot.pages.notAdmin.HomePage;

public class DownloadFileRUTest extends BaseSelenium {

	private LoginPage loginPage;
	private HomePage homePage;
	private DetailedItemViewPage detailedItemViewPage;

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		
		loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName),
				getPropertyAttribute(spotRUPassWord));
		
		CollectionsPage collectionPage = new StartPage(driver).goToCollectionPage();
		CollectionContentPage collectionContentPage = collectionPage.getPageOfLargestCollection();
		
		detailedItemViewPage = collectionContentPage.downloadFirstItemInList();	
	}
	
	@AfterClass
	public void afterClass() {
		homePage.logout();
	}
	
	@Test 
	public void downloadFileTest() {
		
		boolean isDownloadPossible = detailedItemViewPage.isDownloadPossible();		
		
		Assert.assertTrue(isDownloadPossible, "Registered user couldn't download a item. Reason: Download Button most probably not displayed/enabled");	
	}
}
