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
 * Testcase #13 (IMJ-218)
 */
public class DocumentationTest extends BaseSelenium {
	
	private String windowHandleHelpPage;
	private String windowHandleStartPage;
	
	@Test (priority = 1)
	public void openDocumentationNRUPublic() {
		String url = openDocumentation();
		closeDocumentation();
		Assert.assertEquals(url, BasePage.IMEJI_GITHUB_URL, "Documentation URLs don't match.");
	}
	
	@Test (priority = 2)
	public void openDocumentationRUPublic() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		String url = openDocumentation();
		closeDocumentation();
		homePage.logout();
		Assert.assertEquals(url, BasePage.IMEJI_GITHUB_URL, "Documentation URLs don't match.");
	}
  
	@Test (priority = 3)
	public void openDocumentationNRUPrivate() {
		switchPrivateMode(true);
		String url = openDocumentation();
		closeDocumentation();
		Assert.assertEquals(url, BasePage.IMEJI_GITHUB_URL, "Documentation URLs don't match.");
	}
	
	@Test (priority = 4)
	public void openDocumentationRUPrivate() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		String url = openDocumentation();
		closeDocumentation();
		homePage.logout();
		Assert.assertEquals(url, BasePage.IMEJI_GITHUB_URL, "Documentation URLs don't match.");
	}
	
	@AfterClass
	public void afterClass() {
		switchPrivateMode(false);
	}

	// IMJ-218
	private String openDocumentation() {
		windowHandleStartPage = driver.getWindowHandle();

		HelpPage helpPage = new StartPage(driver).goToHelpPage();
		try { Thread.sleep(1000); } catch (InterruptedException exc) {}
		
		Set<String> windowHandles = driver.getWindowHandles();
		windowHandles.remove(windowHandleStartPage);
		String helpHandle = windowHandles.iterator().next();
		driver.switchTo().window(helpHandle);
		
		windowHandleHelpPage = driver.getWindowHandle();

		Set<String> windowHandlesBeforeDocumentation = driver.getWindowHandles();

		helpPage.lookUpImejiHomePage();
		try { Thread.sleep(1000); } catch (InterruptedException exc) {}

		Set<String> windowHandlesAfterDocumentation = driver.getWindowHandles();
		windowHandlesAfterDocumentation.removeAll(windowHandlesBeforeDocumentation);

		for (String winHandle : windowHandlesAfterDocumentation) {
			driver.switchTo().window(winHandle);
		}

		// wait for new window to load
		new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.id("readme")));
		String currentUrl = driver.getCurrentUrl();
		
		return currentUrl;
	}

	private void closeDocumentation() {
		if (driver.getWindowHandles().size() > 1) {
			// close imeji window; switch to help page
			driver.close();
			driver.switchTo().window(windowHandleHelpPage);
	
			// close help window; switch to start page
			driver.close();
			driver.switchTo().window(windowHandleStartPage);
		}
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
