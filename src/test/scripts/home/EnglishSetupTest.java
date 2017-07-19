package test.scripts.home;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class EnglishSetupTest extends BaseSelenium {

	private String englishSetup = "English";
	
	@Test(priority = 1)
	public void setEnglishNRUPublic() {
		StartPage startPage = new StartPage(driver);
		startPage.selectLanguage(englishSetup);
		Assert.assertEquals(startPage.getCurrentLanguageSetup().toLowerCase(), englishSetup.toLowerCase());
	}
	
	@Test(priority = 2)
	public void setEnglishRUPublic() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		homePage.selectLanguage(englishSetup);
		Assert.assertEquals(homePage.getCurrentLanguageSetup().toLowerCase(), englishSetup.toLowerCase());
		homePage.logout();
	}
	
	@Test(priority = 3)
	public void setEnglishNRUPrivate() {
		switchPrivateMode(true);
		StartPage startPage = new StartPage(driver);
		startPage.selectLanguage(englishSetup);
		Assert.assertEquals(startPage.getCurrentLanguageSetup().toLowerCase(), englishSetup.toLowerCase());
	}
	
	@Test(priority = 4)
	public void setEnglishRUPrivate() {
		setEnglishRUPublic();
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
