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

	private static String TERMS_OF_USE = "https://www.mpdl.mpg.de/en/terms-of-service-en.html";
	
	private String windowHandleStartPage;
	private String termsHandle;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
	}

	@Test (priority = 1)
	public void openTermsOfUseNRUPublic() {
		boolean termsOfUseDisplayed = termsOfUseTest();
		closeTermsOfUse();
		
		Assert.assertTrue(termsOfUseDisplayed, "Terms of Use page is not displayed.");
	}
	
	@Test (priority = 2)
	public void openTermsOfUseRUPublic() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		boolean termsOfUseDisplayed = termsOfUseTest();
		closeTermsOfUse();
		homePage.logout();
		
		Assert.assertTrue(termsOfUseDisplayed, "Terms of Use page is not displayed to RU in public mode.");
	}
  
	@Test (priority = 3)
	public void openTermsOfUseNRUPrivate() {
		switchPrivateMode(true);
		boolean termsOfUseDisplayed = termsOfUseTest();
		closeTermsOfUse();
		
		Assert.assertTrue(termsOfUseDisplayed, "Terms of Use page is not displayed.");
	}
	
	@Test (priority = 4)
	public void openTermsOfUseRUPrivate() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		boolean termsOfUseDisplayed = termsOfUseTest();
		closeTermsOfUse();
		homePage.logout();
		
		Assert.assertTrue(termsOfUseDisplayed, "Terms of Use page is not displayed.");
	}
	
	private boolean termsOfUseTest() {
		windowHandleStartPage = driver.getWindowHandle();

		new StartPage(driver).lookUpTermsOfUse();

		Set<String> windowHandles = driver.getWindowHandles();
		windowHandles.remove(windowHandleStartPage);
		termsHandle = windowHandles.iterator().next();
		driver.switchTo().window(termsHandle);

		String actualCurrentURL = getCurrentURL();		
		return actualCurrentURL.equals(TERMS_OF_USE);
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
			// closing the (terms of use page) window; since that window's no more required
			driver.switchTo().window(termsHandle);
			driver.close();
			
			// switching back to original browser (start page)
			driver.switchTo().window(windowHandleStartPage);
		}
	}
}
