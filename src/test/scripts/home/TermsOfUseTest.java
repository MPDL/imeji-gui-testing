package test.scripts.home;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class TermsOfUseTest extends BaseSelenium {

	private String windowHandleStartPage;
	private String termsHandle;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
	}

	@Test (priority = 1)
	public void openTermsOfUseNRUPublic() {
		termsOfUseTest();
		closeTermsOfUse();
	}
	
	@Test (priority = 2)
	public void openTermsOfUseRUPublic() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		termsOfUseTest();
		closeTermsOfUse();
		homePage.logout();
	}
  
	@Test (priority = 3)
	public void openTermsOfUseNRUPrivate() {
		switchPrivateMode(true);
		termsOfUseTest();
		closeTermsOfUse();
	}
	
	@Test (priority = 4)
	public void openTermsOfUseRUPrivate() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		termsOfUseTest();
		closeTermsOfUse();
		homePage.logout();
	}
	
	private void termsOfUseTest() {
		windowHandleStartPage = driver.getWindowHandle();

		new StartPage(driver).lookUpTermsOfUse();

		Set<String> windowHandles = driver.getWindowHandles();
		windowHandles.remove(windowHandleStartPage);
		termsHandle = windowHandles.iterator().next();
		driver.switchTo().window(termsHandle);

		String actualCurrentURL = getCurrentURL();		
		Assert.assertEquals(actualCurrentURL, "http://qa-imeji.mpdl.mpg.de/imeji/terms_of_use");
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
	
	private void closeTermsOfUse() {
		if (driver.getWindowHandles().size() > 1) {
			// closing the (disclaimer page) window; since that window's no more required
			driver.switchTo().window(termsHandle);
			driver.close();
			
			// switching back to original browser (start page)
			driver.switchTo().window(windowHandleStartPage);
		}
	}
}
