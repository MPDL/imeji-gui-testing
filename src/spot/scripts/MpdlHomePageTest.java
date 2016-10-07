package spot.scripts;

import spot.BaseSelenium;
import spot.pages.StartPage;

import org.testng.annotations.*;

import java.util.Set;

import org.testng.Assert;

public class MpdlHomePageTest extends BaseSelenium {

	@BeforeClass
	public void beforeClass() {
		super.setup();
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
		String mpdlHomePageENUrl = "https://www.mpdl.mpg.de/en/";
		String mpdlHomePageDEUrl = "https://www.mpdl.mpg.de/";

		try {
			Assert.assertEquals(currentURL, mpdlHomePageDEUrl);
		}
		catch (AssertionError exc) {
			Assert.assertEquals(currentURL, mpdlHomePageENUrl);
		}

		// close MPDL window; switch to start page
		driver.close();
		driver.switchTo().window(windowHandleStartPage);
	}

}
