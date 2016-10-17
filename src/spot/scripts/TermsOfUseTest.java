package spot.scripts;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.StartPage;

public class TermsOfUseTest extends BaseSelenium {

	private String windowHandleStartPage;
	private String termsHandle;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
	}

	@Test
	public void openTermsOfUseTest() {
		windowHandleStartPage = driver.getWindowHandle();

		new StartPage(driver).lookUpTermsOfUse();

		Set<String> windowHandles = driver.getWindowHandles();
		windowHandles.remove(windowHandleStartPage);
		termsHandle = windowHandles.iterator().next();
		driver.switchTo().window(termsHandle);

		String actualCurrentURL = getCurrentURL();		
		Assert.assertEquals(actualCurrentURL, "http://qa-edmond.mpdl.mpg.de/imeji/terms_of_use");
	}
	
	@AfterClass
	public void afterClass() {
		if (driver.getWindowHandles().size() > 1) {
			// closing the (disclaimer page) window; since that window's no more required
			driver.switchTo().window(termsHandle);
			driver.close();
			
			// switching back to original browser (start page)
			driver.switchTo().window(windowHandleStartPage);
		}
	}
}
