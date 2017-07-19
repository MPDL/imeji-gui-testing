package test.scripts.home;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class UploadPageTest extends BaseSelenium {
	
	@Test (priority = 1)
	public void openHelpNRUPublic() {
		StartPage startPage = new StartPage(driver);
		SingleUploadPage singleUpload = startPage.goToSingleUploadPage();
		Assert.assertTrue(singleUpload.loginAreaDisplayed(), "Login area is not displayed for NRU.");
	}
	
	@Test (priority = 2)
	public void openHelpRUPublic() {
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		SingleUploadPage singleUpload = homePage.goToSingleUploadPage();
		boolean uploadAreaDisplayed = singleUpload.uploadAreaDisplayed();
		homePage.logout();
		Assert.assertTrue(uploadAreaDisplayed, "Upload area is not displayed for RU.");
	}
  
	@Test (priority = 3)
	public void openHelpNRUPrivate() {
		navigateToStartPage();
		switchPrivateMode(true);
		StartPage startPage = new StartPage(driver);
		SingleUploadPage singleUpload = startPage.goToSingleUploadPage();
		Assert.assertTrue(singleUpload.loginAreaDisplayed(), "Login area is not displayed for NRU.");
	}
	
	@Test (priority = 4)
	public void openHelpRUPrivate() {
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		SingleUploadPage singleUpload = homePage.goToSingleUploadPage();
		boolean uploadAreaDisplayed = singleUpload.uploadAreaDisplayed();
		homePage.logout();
		Assert.assertTrue(uploadAreaDisplayed, "Upload area is not displayed for RU.");
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
