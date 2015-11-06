package spot.scripts.collections;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import spot.BaseSelenium;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;

public class CancelCollectionCreationTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
	
	private String previousPageUrl; 

	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
	}

	@AfterMethod
	public void afterMethod() {
	}

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();

		adminHomePage = loginPage.loginAsAdmin(
				getPropertyAttribute("aSpotUserName"),
				getPropertyAttribute("aSpotPassword"));
	}

	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}

	@Test
	public void cancelCollectionCreationTest() {
		previousPageUrl = driver.getCurrentUrl();
		
		CreateNewCollectionPage createNewCollectionPage = adminHomePage
				.goToCreateNewCollectionPage();
		
		createNewCollectionPage.cancelCollectionCreation();
		
		String currentUrl = driver.getCurrentUrl();
		
		Assert.assertEquals(currentUrl, previousPageUrl, "After cancelling the creation of collection, browser didn't go back to the page before the collection creation action.");
		
	}
}
