package test.scripts.home;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class DisclaimerTest extends BaseSelenium {

	private String windowHandleStartPage;
	private String disclaimerHandle;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
	}
	
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

	private void openDisclaimerTest() {
		windowHandleStartPage = driver.getWindowHandle();

		new StartPage(driver).lookUpDisclaimer();

		Set<String> windowHandles = driver.getWindowHandles();
		windowHandles.remove(windowHandleStartPage);
		disclaimerHandle = windowHandles.iterator().next();
		driver.switchTo().window(disclaimerHandle);

		String actualCurrentURL = getCurrentURL();		
		Assert.assertEquals(actualCurrentURL, "http://qa-imeji.mpdl.mpg.de/imeji/imprint");
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
