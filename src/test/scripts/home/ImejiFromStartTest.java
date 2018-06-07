package test.scripts.home;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.BasePage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class ImejiFromStartTest extends BaseSelenium {
	
	private String windowHandleStartPage;
	
	@Test(priority = 1)
	public void imejiFromStartNRUPublic() {
		String url = imejiFromStart();
		closeImeji();
		Assert.assertEquals(url, BasePage.IMEJI_GITHUB_URL);
	}
	
	@Test(priority = 2)
	public void imejiFromStartRUPublic() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		String url = imejiFromStart();
		closeImeji();
		homePage.logout();
		Assert.assertEquals(url, BasePage.IMEJI_GITHUB_URL);
	}
	
	@Test(priority = 3)
	public void imejiFromStartNRUPrivate() {
		switchPrivateMode(true);
		String url = imejiFromStart();
		closeImeji();
		Assert.assertEquals(url, BasePage.IMEJI_GITHUB_URL);
	}
	
	@Test(priority = 4)
	public void imejiFromStartRUPrivate() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		String url = imejiFromStart();
		closeImeji();
		homePage.logout();
		Assert.assertEquals(url, BasePage.IMEJI_GITHUB_URL);
	}
	
	@AfterClass
	public void afterClass() {
		switchPrivateMode(false);
	}
	
	private String imejiFromStart() {
		windowHandleStartPage = driver.getWindowHandle();
		
		new StartPage(driver).lookUpImejiHomePage();
		try { Thread.sleep(1000); } catch (InterruptedException exc) {}

		Set<String> windowHandlesAfterImejiHomePage = driver.getWindowHandles();
		
		for (String winHandle : windowHandlesAfterImejiHomePage) {
			driver.switchTo().window(winHandle);
		}
		
		new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.id("readme")));
		String currentUrl = driver.getCurrentUrl();

		return currentUrl;
	}
	
	private void closeImeji() {
		// close imeji window; switch to start page
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
