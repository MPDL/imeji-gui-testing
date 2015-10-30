package spot.scripts;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.HelpPage;

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

		String windowHandleStartPage = getDriver().getWindowHandle();

		HelpPage helpPage = navigateToHelpPage();

		String windowHandleHelpPage = getDriver().getWindowHandle();

		Set<String> windowHandlesBeforeImejiHomePage = getDriver().getWindowHandles();

		helpPage.lookUpImejiHomePage();

		Set<String> windowHandlesAfterImejiHomePage = getDriver().getWindowHandles();

		windowHandlesAfterImejiHomePage.removeAll(windowHandlesBeforeImejiHomePage);

		for (String winHandle : windowHandlesAfterImejiHomePage) {
			getDriver().switchTo().window(winHandle);
		}

		String currentUrl = getDriver().getCurrentUrl();

		String imejiHomePageUrl = "http://imeji.org/";

		// is it really the Imeji Homepage?
		Assert.assertEquals(currentUrl, imejiHomePageUrl);

		// closing the (imeji home page) window; since that window's no more required
		getDriver().close();

		// switch back to window handle of help page
		getDriver().switchTo().window(windowHandleHelpPage);

		// closing the (help page) window; since that window's no more required
		getDriver().close();

		// switching back to original browser (start page)
		getDriver().switchTo().window(windowHandleStartPage);
	}
	
	@Test
	public void callImejiHomePageFromStartPageTest() {
		String windowHandleStartPage = getDriver().getWindowHandle();
		
		getStartPage().lookUpImejiHomePage();

		Set<String> windowHandlesAfterImejiHomePage = getDriver().getWindowHandles();
		
		for (String winHandle : windowHandlesAfterImejiHomePage) {
			getDriver().switchTo().window(winHandle);
		}
		
		String currentUrl = getDriver().getCurrentUrl();

		String imejiHomePageUrl = "http://imeji.org/";

		// is it really the Imeji Homepage?
		Assert.assertEquals(currentUrl, imejiHomePageUrl);

		// closing the (imeji home page) window; since that window's no more required
		getDriver().close();
		
		// switching back to original browser (start page)
		getDriver().switchTo().window(windowHandleStartPage);
	}

}
