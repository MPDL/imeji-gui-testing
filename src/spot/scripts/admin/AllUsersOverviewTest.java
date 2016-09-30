package spot.scripts.admin;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.admin.UsersOverviewPage;

public class AllUsersOverviewTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
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
		adminHomePage = (AdminHomePage) allUsersOverViewPage.goToHomePage(adminHomePage);
		logout(PageFactory.initElements(driver, AdminHomePage.class));
	}
	
	
}
