package spot.scripts;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.HelpPage;
import spot.pages.StartPage;

public class ImejiHomePageTest extends BaseSelenium {

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
	}
	
	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
	}

	@Test
	public void callImejiHomePageFromHelpPageTest() {
		/*
		 * instead of navigateToHelpPage using beforeClass is possible in
		 * theory; but beforeClass annotated methods will still run even though
		 * the group failed. -> beforeClass doesn't work with dependsOnGroups
		 */

		String windowHandleStartPage = driver.getWindowHandle();

		HelpPage helpPage = new StartPage(driver).goToHelpPage();

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

		// is it really the Imeji Homepage?
		Assert.assertEquals(currentUrl, imejiHomePageUrl);

		// closing the (imeji home page) window; since that window's no more required
		driver.close();

		// switch back to window handle of help page
		driver.switchTo().window(windowHandleHelpPage);

		// closing the (help page) window; since that window's no more required
		driver.close();

		// switching back to original browser (start page)
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

		// is it really the Imeji Homepage?
		Assert.assertEquals(currentUrl, imejiHomePageUrl);

		// closing the (imeji home page) window; since that window's no more required
		driver.close();
		
		// switching back to original browser (start page)
		driver.switchTo().window(windowHandleStartPage);
	}

}
