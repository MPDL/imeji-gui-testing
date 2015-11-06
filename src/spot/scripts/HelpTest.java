package spot.scripts;

import java.util.Set;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.SeleniumTestSuite;
import spot.pages.HelpPage;
import spot.pages.StartPage;

public class HelpTest extends BaseSelenium {
  
  
  //TODO open Help as RU Test
//  @Test
//  public void openHelpAsRUTest() {
//	  
//  }
  
  @Test(groups="helpTest")
  public void openHelpAsNRUTest() {
	  //storing the current window handle (right before the help page is opened)
	  String windowHandleBeforeHelp = driver.getWindowHandle();
	  
	  HelpPage helpPage = new StartPage(driver).goToHelpPage();
	  
	  Set<String> windowHandles = driver.getWindowHandles();
	  
	  for (String winHandle : windowHandles) {
		  driver.switchTo().window(winHandle);
	  }
	  
	  String actualHelpPageSubtitle = helpPage.getHelpPageSubtitle();
	  
	  if (SeleniumTestSuite.testEnvironmentURL.equals(SeleniumTestSuite.qaImeji))
		  Assert.assertEquals(actualHelpPageSubtitle, "Help");
	  else if (SeleniumTestSuite.testEnvironmentURL.equals(SeleniumTestSuite.qaEdmond))
		  Assert.assertEquals(actualHelpPageSubtitle, "Edmond Help");
	  else 
		  Assert.assertEquals(false, true, "Not known test environment url");
	  
	  // closing the (help page) window; since that window's no more required
	  driver.close();
	  
	  // switching back to original browser (start page)
	  driver.switchTo().window(windowHandleBeforeHelp);
  }

}
