package spot.scripts.collections;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import spot.BaseSelenium;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.NewCollectionPage;

public class CancelCollectionCreationTest extends BaseSelenium {

	private AdminHomePage adminHomePage;

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotRUUserName),getPropertyAttribute(spotRUPassWord));
	}
	
	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
	}

	@Test
	public void cancelCollectionCreationTest() {
		String previousPageUrl = driver.getCurrentUrl();
		
		NewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();
		createNewCollectionPage.cancelCollectionCreation();
		
		String currentUrl = driver.getCurrentUrl();
		Assert.assertTrue(currentUrl.contains(previousPageUrl), "Browser doesn't go to previous page after cancelling collection.");
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}
}
