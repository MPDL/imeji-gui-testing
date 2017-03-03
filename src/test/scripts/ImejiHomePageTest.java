package test.scripts;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.pages.HelpPage;
import spot.pages.StartPage;
import test.base.BaseSelenium;

public class ImejiHomePageTest extends BaseSelenium {

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
	}
	
	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
	}

	@Test
	public void callImejiHomePageFromHelpPageTest() {
		String windowHandleStartPage = driver.getWindowHandle();

		HelpPage helpPage = new StartPage(driver).goToHelpPage();
		
		Set<String> windowHandles = driver.getWindowHandles();
		windowHandles.remove(windowHandleStartPage);
		String helpHandle = windowHandles.iterator().next();
		driver.switchTo().window(helpHandle);
		
		String windowHandleHelpPage = driver.getWindowHandle();

		Set<String> windowHandlesBeforeImejiHomePage = driver.getWindowHandles();

		helpPage.lookUpImejiHomePage();

		Set<String> windowHandlesAfterImejiHomePage = driver.getWindowHandles();
		windowHandlesAfterImejiHomePage.removeAll(windowHandlesBeforeImejiHomePage);

		for (String winHandle : windowHandlesAfterImejiHomePage) {
			driver.switchTo().window(winHandle);
		}

		String currentUrl = driver.getCurrentUrl();
		String imejiHomePageUrl = "http://imeji.org/";

		Assert.assertEquals(currentUrl, imejiHomePageUrl);

		// close imeji window; switch to help page
		driver.close();
		driver.switchTo().window(windowHandleHelpPage);

		// close help window; switch to start page
		driver.close();
		driver.switchTo().window(windowHandleStartPage);
	}
	
	@Test
	public void callImejiHomePageFromStartPageTest() {
		String windowHandleStartPage = driver.getWindowHandle();
		
		new StartPage(driver).lookUpImejiHomePage();

		Set<String> windowHandlesAfterImejiHomePage = driver.getWindowHandles();
		
		for (String winHandle : windowHandlesAfterImejiHomePage) {
			driver.switchTo().window(winHandle);
		}
		
		String currentUrl = driver.getCurrentUrl();
		String imejiHomePageUrl = "http://imeji.org/";

		Assert.assertEquals(currentUrl, imejiHomePageUrl);

		// close imeji window; switch to start page
		driver.close();
		driver.switchTo().window(windowHandleStartPage);
	}

}
