package test.scripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

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
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		SingleUploadPage singleUploadPage = homePage.goToSingleUploadPage();
		
		String headline = singleUploadPage.getPageTitle();
		Assert.assertTrue(headline.contains("Upload"), "Upload page was not displayed");
		
		homePage = singleUploadPage.goToHomepage(homePage);
		homePage.logout();
	}

	@Test
	public void openCollectionPageTest() {
		CollectionsPage collectionsPage = startPage.goToCollectionPage();
		String headline = collectionsPage.getPageTitle();
		Assert.assertTrue(headline.contains("Collections"), "Collections page was not displayed");
	}

}
