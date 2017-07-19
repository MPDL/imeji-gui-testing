package test.scripts.home;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import test.base.BaseSelenium;

public class LimitedNRUAccessTest extends BaseSelenium {
	
	@Test(priority = 1)
	public void noNewButtonPublic() {
		noNewButton();
	}
	
	@Test(priority = 2)
	public void noNewButtonPrivate() {
		switchPrivateMode(true);
		noNewButton();
	}
	
	@AfterClass
	public void afterClass() {
		switchPrivateMode(false);
	}
	
	private void noNewButton() {
		StartPage startPage = new StartPage(driver);
		// TODO boolean canCreate = startPage.isNewButtonPresent();
		// Assert.assertFalse(canCreate, "New button should not be visible to NRU.");
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
