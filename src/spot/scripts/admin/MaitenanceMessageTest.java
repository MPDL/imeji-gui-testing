package spot.scripts.admin;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.ConfigurationEditPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;

public class MaitenanceMessageTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
	private AdministrationPage adminPage;
	
	private String message = "Testing maintenance message functionality";
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
	}
	
	@Test(priority = 1)
	public void goToAdministration() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		adminPage = adminHomePage.goToAdminPage();
	}
	
	@Test(priority = 2)
	public void createMaintenanceMessage() {
		adminPage.setMaintenanceMessage(message);
		// TODO go to start page and check if message appears
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
		adminPage = adminHomePage.goToAdminPage();
		ConfigurationEditPage configurationEdit = adminPage.setMaintenanceMessage("");
		configurationEdit.goToHomePage(adminHomePage).logout();
	}
}
