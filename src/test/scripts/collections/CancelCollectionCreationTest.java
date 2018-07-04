package test.scripts.collections;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.NewCollectionPage;
import test.base.BaseSelenium;

public class CancelCollectionCreationTest extends BaseSelenium {

	private AdminHomepage adminHomePage;

	@BeforeClass
	public void beforeClass() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(ruUsername),getPropertyAttribute(ruPassword));
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
