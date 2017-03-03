package test.scripts;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.pages.HelpPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class HelpTest extends BaseSelenium {
  
	@Test
	public void openHelpAsRUTest() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		Homepage homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		openHelpPage();
		homePage.logout();
	}
  
	@Test
	public void openHelpAsNRUTest() {
		openHelpPage();
	}
	
	private void openHelpPage() {
		//storing the current window handle (right before the help page is opened)
		String windowHandleBeforeHelp = driver.getWindowHandle();
		  
		HelpPage helpPage = new StartPage(driver).goToHelpPage();
		  
		Set<String> windowHandles = driver.getWindowHandles();
		  
		for (String winHandle : windowHandles) {
			driver.switchTo().window(winHandle);
		}
		  
		String actualHelpPageSubtitle = helpPage.getHelpPageSubtitle();
		Assert.assertEquals(actualHelpPageSubtitle, "Help");
		  
		// closing the (help page) window; since that window's no more required
		driver.close();
		  
		// switching back to original browser (start page)
		driver.switchTo().window(windowHandleBeforeHelp);
	}

}
