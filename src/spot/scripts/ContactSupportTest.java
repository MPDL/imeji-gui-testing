package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.HelpPage;
import spot.pages.StartPage;

public class ContactSupportTest extends BaseSelenium {

	private String edmondSupportEmail = "edmond-support@gwdg.de";
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
	}
	
	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
	}

	@Test
	public void contactSupportTest() {
		
		/* instead of navigateToHelpPage using beforeClass is possible in theory;
		 * but beforeClass annotated methods will still run even though the group failed.
		 * -> beforeClass doesn't work with dependsOnGroups
		*/ 
		String windowHandleBeforeHelp = driver.getWindowHandle();
		
		HelpPage helpPage = new StartPage(driver).goToHelpPage();
		
		String supportMailAddress = helpPage.contactSupport();
		
		Assert.assertEquals(supportMailAddress, edmondSupportEmail, "Support mail adress can't be accessed");
		
		// closing the (help page) window; since that window's no more required
		driver.close();
	  
		// switching back to original browser (start page)
		driver.switchTo().window(windowHandleBeforeHelp);
		
	}
	
	@Test
	public void contactEdmondsupportFromStartPage() {
		String edmondSupportMailAddress = new StartPage(driver).contactEdmondSupport();
		
		Assert.assertEquals(edmondSupportMailAddress, edmondSupportEmail, "Support mail adress can't be accessed");		
	  
	}

}
