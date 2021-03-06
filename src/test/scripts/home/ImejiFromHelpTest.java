package test.scripts.home;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.BasePage;
import spot.pages.HelpPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

/**
 * Testcase #13 (IMJ-4, Help page accessible from startpage)
 */
public class ImejiFromHelpTest extends BaseSelenium {

	private String windowHandleImejiPage;
	private String windowHandleHelpPage;
	private String windowHandleStartPage;
	
	@Test(priority = 1)
	public void imejiFromHelpNRUPublic() {
		String url = imejiFromHelp();
		closeImeji();
		Assert.assertEquals(url, BasePage.IMEJI_GITHUB_URL);
	}
	
	@Test(priority = 2)
	public void imejiFromHelpRUPublic() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		String url = imejiFromHelp();
		closeImeji();
		homePage.logout();
		Assert.assertEquals(url, BasePage.IMEJI_GITHUB_URL);
	}
	
	@Test(priority = 3)
	public void imejiFromHelpNRUPrivate() {
		switchPrivateMode(true);
		String url = imejiFromHelp();
		closeImeji();
		Assert.assertEquals(url, BasePage.IMEJI_GITHUB_URL);
	}
	
	@Test(priority = 4)
	public void imejiFromHelpRUPrivate() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		String url = imejiFromHelp();
		closeImeji();
		homePage.logout();
		Assert.assertEquals(url, BasePage.IMEJI_GITHUB_URL);
	}
	
	@AfterClass
	public void afterClass() {
		switchPrivateMode(false);
	}
	
	// IMJ-4
	private String imejiFromHelp() {
		windowHandleStartPage = driver.getWindowHandle();

		HelpPage helpPage = new StartPage(driver).goToHelpPage();
		try { Thread.sleep(1000); } catch (InterruptedException exc) {}
		
		Set<String> windowHandles = driver.getWindowHandles();
		windowHandles.remove(windowHandleStartPage);
		String helpHandle = windowHandles.iterator().next();
		driver.switchTo().window(helpHandle);
		
		windowHandleHelpPage = driver.getWindowHandle();

		Set<String> windowHandlesBeforeImejiHomePage = driver.getWindowHandles();

		// IMJ-4
		helpPage.lookUpImejiHomePage();
		
		try { Thread.sleep(1000); } catch (InterruptedException exc) {}

		Set<String> windowHandlesAfterImejiHomePage = driver.getWindowHandles();
		windowHandlesAfterImejiHomePage.removeAll(windowHandlesBeforeImejiHomePage);

		for (String winHandle : windowHandlesAfterImejiHomePage) {
			windowHandleImejiPage = winHandle;
			driver.switchTo().window(winHandle);
		}

		new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.id("readme")));
		String currentUrl = driver.getCurrentUrl();
		
		return currentUrl;
	}
	
	private void closeImeji() {
		// close imeji window; switch to help page
		driver.switchTo().window(windowHandleImejiPage);
		driver.close();
		driver.switchTo().window(windowHandleHelpPage);

		// close help window; switch to start page
		driver.close();
		driver.switchTo().window(windowHandleStartPage);
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
