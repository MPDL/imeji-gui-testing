package test.scripts.admin;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.AdministrationPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.UsersOverviewPage;
import test.base.BaseSelenium;

public class AllUsersOverviewTest extends BaseSelenium {

	private AdminHomepage adminHomePage;
	private UsersOverviewPage allUsersOverViewPage;
	
	@BeforeClass
	public void setup() {
		super.setup();
		navigateToStartPage();
	}
	
	@Test(priority = 1)
	public void loginAsAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"), getPropertyAttribute("aSpotPassword"));
	}
	
	@Test(priority = 2)
	public void viewAllUsers() {
		AdministrationPage administrationPage = adminHomePage.goToAdminPage();
		allUsersOverViewPage = administrationPage.viewAllUsers();
		int userCount = allUsersOverViewPage.userCount();
		Assert.assertTrue(userCount > 1, "User overview page was not displayed.");
	}
	
	@AfterClass
	public void logout() {
		adminHomePage = (AdminHomepage) allUsersOverViewPage.goToHomepage(adminHomePage);
		adminHomePage.logout();
	}
	
	
}
