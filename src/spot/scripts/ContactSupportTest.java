package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.SeleniumTestSuite;
import spot.pages.HelpPage;
import spot.pages.StartPage;

public class ContactSupportTest extends BaseSelenium {

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
	}
	
	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
	}

	@Test(dependsOnGroups = "helpTest")
	public void contactSupportTest() {
		
		/* instead of navigateToHelpPage using beforeClass is possible in theory;
		 * but beforeClass annotated methods will still run even though the group failed.
		 * -> beforeClass doesn't work with dependsOnGroups
		*/ 
		String windowHandleBeforeHelp = driver.getWindowHandle();
		
		HelpPage helpPage = new StartPage(driver).goToHelpPage();
		
		String supportMailAddress = helpPage.contactSupport();
		
		if (SeleniumTestSuite.testEnvironmentURL.equals(SeleniumTestSuite.qaImeji))
			Assert.assertEquals(supportMailAddress, "spot-support@gwdg.de");
		else if (SeleniumTestSuite.testEnvironmentURL.equals(SeleniumTestSuite.qaEdmond))
			Assert.assertEquals(supportMailAddress, "edmond-support@gwdg.de");
		else 
			Assert.assertEquals(false, true, "Not known test environment url");
		
		
		
		// closing the (help page) window; since that window's no more required
		driver.close();
	  
		// switching back to original browser (start page)
		driver.switchTo().window(windowHandleBeforeHelp);
		
	}
	
	@Test
	public void contactEdmondsupportFromStartPage() {
		String edmondSupportMailAddress = new StartPage(driver).contactEdmondSupport();
		
		Assert.assertEquals(edmondSupportMailAddress, "edmond-support@gwdg.de");		
	  
	}

}
