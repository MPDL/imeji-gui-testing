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
		
		if (SeleniumTestSuite.testEnvironmentURL.equals(SeleniumTestSuite.qaImeji))
			  Assert.assertEquals(actualCurrentURL, "http://qa-imeji.mpdl.mpg.de/imprint");
		else if (SeleniumTestSuite.testEnvironmentURL.equals(SeleniumTestSuite.qaEdmond))
			  Assert.assertEquals(actualCurrentURL, "http://colab.mpdl.mpg.de/mediawiki/Edmond_Disclaimer");
		else 
			  Assert.assertEquals(false, true, "Not known test environment url");

		// closing the (disclaimer page) window; since that window's no more required
		driver.close();

		// switching back to original browser (start page)
		driver.switchTo().window(windowHandleStartPage);
	}

}
