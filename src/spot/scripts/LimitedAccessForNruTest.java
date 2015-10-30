package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;

public class LimitedAccessForNruTest extends BaseSelenium {
  
  @BeforeMethod  public void beforeMethod() {
	  
  }

  @AfterMethod
  public void afterMethod() {
  }

  @BeforeClass
  public void beforeClass() {
	  navigateToStartPage();
	  // TODO make sure that no login took place 
  }

  @AfterClass
  public void afterClass() {
  }

  @Test
  public void newActionNonExistentTest() {
	  boolean isExistent = getStartPage().isCreateNewButtonExistent();
	  
	  Assert.assertEquals(isExistent, false, "New Button for creating new collections/albums exist despite being not logged in");
  }
}
