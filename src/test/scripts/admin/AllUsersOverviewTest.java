package test.scripts.admin;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.admin.BrowseUsersPage;
import test.base.BaseSelenium;

public class AllUsersOverviewTest extends BaseSelenium {

	private AdminHomepage adminHomePage;
	private BrowseUsersPage allUsersOverViewPage;
	
	@Test(priority = 1)
	public void loginAsAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
	}
	
	@Test(priority = 2)
	public void viewAllUsers() {
		AdministrationPage administrationPage = adminHomePage.goToAdminPage();
		allUsersOverViewPage = administrationPage.browseAllUsers();
		int userCount = allUsersOverViewPage.userCount();
		Assert.assertTrue(userCount > 1, "User overview page was not displayed.");
	}
	
	@AfterClass
	public void logout() {
		adminHomePage = (AdminHomepage) allUsersOverViewPage.goToHomepage(adminHomePage);
		adminHomePage.logout();
	}
	
	
}
