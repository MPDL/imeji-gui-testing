package test.scripts.admin;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import test.base.BaseSelenium;

public class EnterUserManagementTest extends BaseSelenium {
	
	private AdminHomepage adminHomePage;
	private AdministrationPage administrationPage;
	
	@Test(priority = 1)
	private void loginAsAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
	}
	
	@Test(priority = 2)
	public void enterUserManagement() {
		administrationPage = adminHomePage.goToAdminPage();
		boolean allComponentsDisplayed = administrationPage.areAllComponentsDisplayed();
		Assert.assertTrue(allComponentsDisplayed);
	}
	
	@AfterClass
	public void logout() {
		adminHomePage = (AdminHomepage) administrationPage.goToHomepage(adminHomePage);
		logout(PageFactory.initElements(driver, AdminHomepage.class));
	}
}
