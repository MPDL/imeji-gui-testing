package test.scripts.home;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class NewCollectionPageTest extends BaseSelenium {
	
	@Test (priority = 1)
	public void openHelpNRUPublic() {
		StartPage startPage = new StartPage(driver);
		CollectionsPage collections = startPage.goToCollectionPage();
		collections.createCollection();
	}
	
	@Test (priority = 2)
	public void openHelpRUPublic() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		CollectionsPage collections = homePage.goToCollectionPage();
		collections.createCollection();
		homePage.logout();
	}
  
	@Test (priority = 3)
	public void openHelpNRUPrivate() {
		switchPrivateMode(true);
		StartPage startPage = new StartPage(driver);
		// NewCollectionPage newCollection = 
		// assert login area displayed
	}
	
	@Test (priority = 4)
	public void openHelpRUPrivate() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		// NewCollectionPage newCollection = 
		// assert new collection area displayed
		homePage.logout();
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
