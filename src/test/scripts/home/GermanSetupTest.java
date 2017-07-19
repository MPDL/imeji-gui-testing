package test.scripts.home;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class GermanSetupTest extends BaseSelenium {
	
	private String germanSetup = "German";
	
	@Test(priority = 1)
	public void setGermanNRUPublic() {
		StartPage startPage = new StartPage(driver);
		startPage.selectLanguage(germanSetup);
		Assert.assertEquals(startPage.getCurrentLanguageSetup().toLowerCase(), germanSetup.toLowerCase());
	}
	
	@Test(priority = 2)
	public void setGermanRUPublic() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		homePage.selectLanguage(germanSetup);
		Assert.assertEquals(homePage.getCurrentLanguageSetup().toLowerCase(), germanSetup.toLowerCase());
		homePage.logout();
	}
	
	@Test(priority = 3)
	public void setGermanNRUPrivate() {
		switchPrivateMode(true);
		StartPage startPage = new StartPage(driver);
		startPage.selectLanguage(germanSetup);
		Assert.assertEquals(startPage.getCurrentLanguageSetup().toLowerCase(), germanSetup.toLowerCase());
	}
	
	@Test(priority = 4)
	public void setGermanRUPrivate() {
		setGermanRUPublic();
	}
	
	@AfterClass
	public void afterClass() {
		switchPrivateMode(false);
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
