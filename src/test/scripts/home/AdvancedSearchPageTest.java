package test.scripts.home;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.AdvancedSearchPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

/**
 * Testcase #13 (IMJ-219)
 */
public class AdvancedSearchPageTest extends BaseSelenium {
	
	@BeforeClass
	public void beforeClass() {
		switchPrivateMode(false);
	}
	
	@Test (priority = 1)
	public void openAdvancedSearchNRUPublic() {
		openAdvancedSearch();
	}
	
	@Test (priority = 2)
	public void openAdvancedSearchRUPublic() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		openAdvancedSearch();
		homePage.logout();
	}
  
	@Test (priority = 3)
	public void openAdvancedSearchNRUPrivate() {
		switchPrivateMode(true);
		StartPage startPage = new StartPage(driver);
		boolean advancedSearchDisplayed = startPage.advancedSearchAccessible();
		Assert.assertFalse(advancedSearchDisplayed, "Advanced search page should not be available to guest in private mode.");
	}
	
	@Test (priority = 4)
	public void openAdvancedSearchRUPrivate() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		openAdvancedSearch();
		homePage.logout();
	}
	
	@AfterClass
	public void afterClass() {
		switchPrivateMode(false);
	}
	
	// IMJ-219
	private void openAdvancedSearch() {
		StartPage startPage = new StartPage(driver);
		boolean advancedSearchAccessible = startPage.advancedSearchAccessible();
		Assert.assertTrue(advancedSearchAccessible);
		AdvancedSearchPage advancedSearch = startPage.goToAdvancedSearch();
		boolean advancedSearchDisplayed = advancedSearch.advancedSearchDisplayed();
		Assert.assertTrue(advancedSearchDisplayed);
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
