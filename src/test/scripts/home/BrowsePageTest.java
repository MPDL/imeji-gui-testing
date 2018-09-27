package test.scripts.home;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.BrowseItemsPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

/**
 * Testcase #13 (IMJ-17)
 */
public class BrowsePageTest extends BaseSelenium {

	@BeforeClass
	public void beforeClass() {
		switchPrivateMode(false);
	}
	
	@Test (priority = 1)
	public void browseNRUPublic() {
		browse();
	}
	
	@Test (priority = 2)
	public void browseRUPublic() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		browse();
		homePage.logout();
	}
  
	@Test (priority = 3)
	public void browseNRUPrivate() {
		switchPrivateMode(true);
		boolean browseAccessible = new StartPage(driver).browseAccessible();
		Assert.assertFalse(browseAccessible, "Browse page should not be accessed by guest in private mode.");
	}
	
	@Test (priority = 4)
	public void browseRUPrivate() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		browse();
		homePage.logout();
	}
	
	@AfterClass
	public void afterClass() {
		switchPrivateMode(false);
	}
	
	// IMJ-17
	private void browse() {
		StartPage startPage = new StartPage(driver);
		boolean browseAccessible = startPage.browseAccessible();
		Assert.assertTrue(browseAccessible, "Browse button should be displayed.");
		BrowseItemsPage browseItemsPage = startPage.navigateToItemPage();
		boolean itemsDisplayed = browseItemsPage.isItemAreaDisplayed();
		Assert.assertTrue(itemsDisplayed, "Item area is not displayed.");
	}
	
	private void switchPrivateMode(boolean privateMode) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomepage adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		if (privateMode)
			adminHomepage.goToAdminPage().enablePrivateMode();
		else 
			adminHomepage.goToAdminPage().disablePrivateMode();
		adminHomepage.logout();
	}
}
