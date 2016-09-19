package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.AlbumPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;

public class CategorySelectionTest extends BaseSelenium {

	private StartPage startPage;

	@BeforeClass
	public void beforeClass() {
		super.setup();
	}
	
	@BeforeMethod
	public void beforeMethodTest() {
		startPage = new StartPage(driver);
	}
	@Test
	public void openUploadPageTest() {
		// guests cannot access upload page
		LoginPage loginPage = startPage.openLoginForm();
		HomePage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
		SingleUploadPage singleUploadPage = homePage.goToSingleUploadPage();
		
		String headline = singleUploadPage.getPageTitle();
		Assert.assertTrue(headline.contains("Upload"), "Upload page was not displayed");
		
		homePage = singleUploadPage.goToHomePage(homePage);
		homePage.logout();
	}

	@Test
	public void openCollectionPageTest() {
		CollectionsPage collectionsPage = startPage.goToCollectionPage();
		String headline = collectionsPage.getPageTitle();
		Assert.assertTrue(headline.contains("Collections"), "Collections page was not displayed");
	}

	@Test
	public void openAlbumPageTest() {
		AlbumPage albumPage = startPage.goToAlbumPage();
		String headline = albumPage.getPageTitle();
		Assert.assertTrue(headline.contains("albums"), "Albums page was not displayed");
	}
}
