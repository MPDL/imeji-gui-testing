package spot.scripts;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.SeleniumTestSuite;
import spot.pages.StartPage;

public class DisclaimerTest extends BaseSelenium {

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
	}

	@Test
	public void openDisclaimerTest() {

		String windowHandleStartPage = driver.getWindowHandle();

		new StartPage(driver).lookUpDisclaimer();

		Set<String> windowHandlesAfterDisclaimer = driver.getWindowHandles();

		for (String winHandle : windowHandlesAfterDisclaimer) {
			driver.switchTo().window(winHandle);
		}

		String actualCurrentURL = getCurrentURL();		
		Assert.assertEquals(actualCurrentURL, "http://qa-edmond.mpdl.mpg.de/imeji/imprint");

		// closing the (disclaimer page) window; since that window's no more required
		driver.close();

		// switching back to original browser (start page)
		driver.switchTo().window(windowHandleStartPage);
	}

}
