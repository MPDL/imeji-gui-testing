package spot.scripts;

import spot.BaseSelenium;

import org.testng.annotations.*;

import java.util.Set;

import org.testng.Assert;

public class MpdlHomePageTest extends BaseSelenium {

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
	}

	@Test
	public void callMpdlHomePageTest() {
		String windowHandleStartPage = getDriver().getWindowHandle();

		getStartPage().lookUpMpdlHomePage();

		Set<String> windowHandlesAfterMpdlHomePage = getDriver().getWindowHandles();

		for (String winHandle : windowHandlesAfterMpdlHomePage) {
			getDriver().switchTo().window(winHandle);
		}

		String currentURL = getCurrentURL();
		String mpdlHomePageUrl = "https://www.mpdl.mpg.de/";

		Assert.assertEquals(currentURL, mpdlHomePageUrl);

		// closing the (mpdl home page) window; since that window's no more
		// required
		getDriver().close();

		// switching back to original browser (start page)
		getDriver().switchTo().window(windowHandleStartPage);
	}

}
