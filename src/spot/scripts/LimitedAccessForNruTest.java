package spot.scripts;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.components.NewActionComponent;

public class LimitedAccessForNruTest extends BaseSelenium {

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
	}

	@Test
	public void newActionNonExistentTest() {
		WebElement newButton = new NewActionComponent(driver).getNewButton();
		
		boolean isNewButtonDisplayed = isElementDisplayed(newButton);

		Assert.assertEquals(
				isNewButtonDisplayed,
				false,
				"New Button for creating new collections/albums is displayed despite being not logged in");
	}
}
