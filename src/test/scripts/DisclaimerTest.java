package test.scripts;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.pages.StartPage;
import test.base.BaseSelenium;

public class DisclaimerTest extends BaseSelenium {

	private String windowHandleStartPage;
	private String disclaimerHandle;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
	}

	@Test
	public void openDisclaimerTest() {
		windowHandleStartPage = driver.getWindowHandle();

		new StartPage(driver).lookUpDisclaimer();

		Set<String> windowHandles = driver.getWindowHandles();
		windowHandles.remove(windowHandleStartPage);
		disclaimerHandle = windowHandles.iterator().next();
		driver.switchTo().window(disclaimerHandle);

		String actualCurrentURL = getCurrentURL();		
		Assert.assertEquals(actualCurrentURL, "http://qa-edmond.mpdl.mpg.de/imeji/imprint");
	}
	
	@AfterClass
	public void afterClass() {
		if (driver.getWindowHandles().size() > 1) {
			// closing the (disclaimer page) window; since that window's no more required
			driver.switchTo().window(disclaimerHandle);
			driver.close();
			
			// switching back to original browser (start page)
			driver.switchTo().window(windowHandleStartPage);
		}
	}

}
