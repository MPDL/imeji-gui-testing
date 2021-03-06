package test.scripts.home;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;
import test.base.SeleniumTestSuite;

/**
 * Testcase #13 (IMJ-6)
 */
public class DisclaimerTest extends BaseSelenium {

	private String windowHandleStartPage;
	private String disclaimerHandle;
	
	//TODO: Refactor this class and openDisclaimerTest() method
	
	@Test (priority = 1)
	public void openHelpNRUPublic() {
		openDisclaimerTest();
		closeDisclaimer();
	}
	
	@Test (priority = 2)
	public void openHelpRUPublic() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		openDisclaimerTest();
		closeDisclaimer();
		homePage.logout();
	}
  
	@Test (priority = 3)
	public void openHelpNRUPrivate() {
		switchPrivateMode(true);
		openDisclaimerTest();
		closeDisclaimer();
	}
	
	@Test (priority = 4)
	public void openHelpRUPrivate() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		openDisclaimerTest();
		closeDisclaimer();
		homePage.logout();
	}

	// IMJ-6
	private void openDisclaimerTest() {
		windowHandleStartPage = driver.getWindowHandle();

		new StartPage(driver).lookUpDisclaimer();
		try { Thread.sleep(2500); } catch (InterruptedException exc) {}

		Set<String> windowHandles = driver.getWindowHandles();
		windowHandles.remove(windowHandleStartPage);
		disclaimerHandle = windowHandles.iterator().next();
		driver.switchTo().window(disclaimerHandle);

		String actualCurrentURL = getCurrentURL();
		Assert.assertEquals(actualCurrentURL, SeleniumTestSuite.TEST_ENV_URL + "imprint");
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
	
	private void closeDisclaimer() {
		if (driver.getWindowHandles().size() > 1) {
			// closing the (disclaimer page) window; since that window's no more required
			driver.switchTo().window(disclaimerHandle);
			driver.close();
			
			// switching back to original browser (start page)
			driver.switchTo().window(windowHandleStartPage);
		}
	}
	
	@AfterClass
	public void afterClass() {
		switchPrivateMode(false);
	}

}
