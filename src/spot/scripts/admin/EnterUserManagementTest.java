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

public class EnterUserManagementTest extends BaseSelenium {
	
	private AdminHomePage adminHomePage;
	private AdministrationPage administrationPage;
	
	@BeforeClass
	public void setup() {
		super.setup();
		navigateToStartPage();
	}
	
	@Test(priority = 1)
	private void loginAsAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"), getPropertyAttribute("aSpotPassword"));
	}
	
	@Test(priority = 2)
	public void enterUserManagement() {
		administrationPage = adminHomePage.goToAdminPage();
		boolean allComponentsDisplayed = administrationPage.areAllComponentsDisplayed();
		Assert.assertTrue(allComponentsDisplayed);
	}
	
	@AfterClass
	public void logout() {
		adminHomePage = (AdminHomePage) administrationPage.goToHomePage(adminHomePage);
		logout(PageFactory.initElements(driver, AdminHomePage.class));
	}
}
