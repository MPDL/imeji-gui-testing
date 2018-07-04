package test.scripts.admin;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.admin.ConfigurationPage;
import test.base.BaseSelenium;

public class MaitenanceMessageTest extends BaseSelenium {

	private AdminHomepage adminHomePage;
	private AdministrationPage adminPage;
	
	private String message = "Testing maintenance message functionality";
	
	@Test(priority = 1)
	public void goToAdministration() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		adminPage = adminHomePage.goToAdminPage();
	}
	
	@Test(priority = 2)
	public void createMaintenanceMessage() {
		adminPage.setMaintenanceMessage(message);
		adminPage.goToHomepage(adminHomePage).logout();
		
		navigateToStartPage();
		// check maintenance message
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomePage);
		adminPage = adminHomePage.goToAdminPage();
		ConfigurationPage configurationEdit = adminPage.setMaintenanceMessage("");
		configurationEdit.goToHomepage(adminHomePage).logout();
	}
}
