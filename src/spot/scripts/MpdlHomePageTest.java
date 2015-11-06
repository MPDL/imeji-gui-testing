package spot.scripts;

import spot.BaseSelenium;
import spot.pages.StartPage;

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
		String windowHandleStartPage = driver.getWindowHandle();

		new StartPage(driver).lookUpMpdlHomePage();

		Set<String> windowHandlesAfterMpdlHomePage = driver.getWindowHandles();

		for (String winHandle : windowHandlesAfterMpdlHomePage) {
			driver.switchTo().window(winHandle);
		}

		String currentURL = getCurrentURL();
		String mpdlHomePageUrl = "https://www.mpdl.mpg.de/";

		Assert.assertEquals(currentURL, mpdlHomePageUrl);

		// closing the (mpdl home page) window; since that window's no more
		// required
		driver.close();

		// switching back to original browser (start page)
		driver.switchTo().window(windowHandleStartPage);
	}

}
