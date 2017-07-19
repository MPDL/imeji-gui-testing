package test.scripts.home;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class ContactSupportFromStartPageTest extends BaseSelenium {

	private String edmondSupportEmail = "saquet@mpdl.mpg.de";
	
	@Test (priority = 1)
	public void openHelpNRUPublic() {
		contactEdmondSupportFromStartPage();
	}
	
	@Test (priority = 2)
	public void openHelpRUPublic() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		contactEdmondSupportFromStartPage();
		homePage.logout();
	}
  
	@Test (priority = 3)
	public void openHelpNRUPrivate() {
		switchPrivateMode(true);
		contactEdmondSupportFromStartPage();
	}
	
	@Test (priority = 4)
	public void openHelpRUPrivate() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		contactEdmondSupportFromStartPage();
		homePage.logout();
	}
	
	@AfterClass
	public void afterClass() {
		switchPrivateMode(false);
	}
	
	private void contactEdmondSupportFromStartPage() {
		String edmondSupportMailAddress = new StartPage(driver).contactEdmondSupport();
		
		Assert.assertEquals(edmondSupportMailAddress, edmondSupportEmail, "Support mail address can't be accessed.");
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
