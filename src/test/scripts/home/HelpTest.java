package test.scripts.home;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.HelpPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

/**
 * Testcase #13 (IMJ-3)
 */
public class HelpTest extends BaseSelenium {
  
	private Homepage homePage; 
	
	@Test (priority = 1)
	public void openHelpNRUPublic() {
		openHelpPage();
	}
	
	@Test (priority = 2)
	public void openHelpRUPublic() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		openHelpPage();
	}
	
	@Test (priority = 3)
	public void logout() {
		homePage = new StartPage(driver).goToHomepage(homePage);
		homePage.logout();
	}
  
	@Test (priority = 4)
	public void openHelpNRUPrivate() {
		switchPrivateMode(true);
		openHelpPage();
	}
	
	@Test (priority = 5)
	public void openHelpRUPrivate() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		openHelpPage();
	}
	
	@Test (priority = 6)
	public void logout2() {
		homePage = new StartPage(driver).goToHomepage(homePage);
		homePage.logout();
	}
	
	@AfterClass
	public void afterClass() {
		switchPrivateMode(false);
	}
	
	// IMJ-3
	private void openHelpPage() {
		//storing the current window handle (right before the help page is opened)
		String windowHandleBeforeHelp = driver.getWindowHandle();
		  
		HelpPage helpPage = new StartPage(driver).goToHelpPage();
		try { Thread.sleep(1000); } catch (InterruptedException exc) {}
		  
		Set<String> windowHandles = driver.getWindowHandles();
		  
		for (String winHandle : windowHandles) {
			driver.switchTo().window(winHandle);
		}
		
		Assert.assertTrue(driver.getTitle().contains("Help"));
		  
		// closing the (help page) window; since that window's no more required
		driver.close();
		  
		// switching back to original browser (start page)
		driver.switchTo().window(windowHandleBeforeHelp);
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
