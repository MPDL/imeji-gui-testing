package test.scripts;

import java.util.List;
import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.pages.HelpPage;
import spot.pages.StartPage;
import test.base.BaseSelenium;

public class ContactSupportTest extends BaseSelenium {

	private String edmondSupportEmail = "edmond@mpdl.mpg.de";
	
	private String windowHandleBeforeHelp;
	private String helpHandle;
	
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
		windowHandleBeforeHelp = driver.getWindowHandle();
		
		HelpPage helpPage = new StartPage(driver).goToHelpPage();
		
		Set<String> windowHandles = driver.getWindowHandles();
		windowHandles.remove(windowHandleBeforeHelp);
		helpHandle = windowHandles.iterator().next();
		driver.switchTo().window(helpHandle);
		
		List<String> supportMailAddresses = helpPage.contactSupport();
		
		for (String supportEmail : supportMailAddresses)
			Assert.assertEquals(supportEmail, edmondSupportEmail, "Support mail address can't be accessed.");
		
	}
	
	@Test
	public void contactEdmondSupportFromStartPage() {
		String edmondSupportMailAddress = new StartPage(driver).contactEdmondSupport();
		
		Assert.assertEquals(edmondSupportMailAddress, edmondSupportEmail, "Support mail address can't be accessed.");		
	  
	}
	
	@AfterClass
	public void afterClass() {
		if (driver.getWindowHandles().size() > 1) {
			// closing the (help page) window; since that window's no more required
			driver.switchTo().window(helpHandle);
			driver.close();
			
			// switching back to original browser (start page)
			driver.switchTo().window(windowHandleBeforeHelp);
		}
	}

}
