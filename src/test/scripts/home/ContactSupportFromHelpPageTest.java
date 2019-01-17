package test.scripts.home;

import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import spot.pages.HelpPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

/**
 * Testcase #13 (IMJ-5)
 */
public class ContactSupportFromHelpPageTest extends BaseSelenium {

	private String imejiSupportEmail = "saquet@mpdl.mpg.de";
	
	private String windowHandleBeforeHelp;
	private String helpHandle;
	
	@BeforeClass
	public void beforeClass() {
		switchPrivateMode(false);
	}
	
	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
	}

	@Test (priority = 1)
	public void contactSupportNRUPublic() {
		contactSupportTest();
		closeHelp();
	}
	
	@Test (priority = 2)
	public void contactSupportRUPublic() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		contactSupportTest();
		closeHelp();
		homePage.logout();
	}
  
	@Test (priority = 3)
	public void contactSupportNRUPrivate() {
		switchPrivateMode(true);
		contactSupportTest();
		closeHelp();
	}
	
	@Test (priority = 4)
	public void contactSupportRUPrivate() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		contactSupportTest();
		closeHelp();
		homePage.logout();
	}
	
	// IMJ-5
	private void contactSupportTest() {
		windowHandleBeforeHelp = driver.getWindowHandle();
		
		HelpPage helpPage = new StartPage(driver).goToHelpPage();
		// necessary for test to recognise page
		try { Thread.sleep(2500); } catch (InterruptedException exc) {}
		
		Set<String> windowHandles = driver.getWindowHandles();
		if (windowHandles.size() > 1) {
			windowHandles.remove(windowHandleBeforeHelp);
			helpHandle = windowHandles.iterator().next();
			driver.switchTo().window(helpHandle);
		}
		
		List<String> supportMailAddresses = helpPage.contactSupport();
		
		for (String supportEmail : supportMailAddresses)
		    //TODO: Get the support email address from the Admin configuration
			Assert.assertEquals(supportEmail, imejiSupportEmail, "Support mail address cannot be accessed.");
	}
	
	private void closeHelp() {
		if (driver.getWindowHandles().size() > 1) {
			driver.switchTo().window(helpHandle);
			driver.close();
			// switching back to original browser (start page)
			driver.switchTo().window(windowHandleBeforeHelp);
		}
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
