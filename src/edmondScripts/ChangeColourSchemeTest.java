package edmondScripts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.StartPage;

public class ChangeColourSchemeTest extends BaseSelenium {

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
	}
	
	@Test
	public void enableDarkMode() {
		new StartPage(driver).enableDarkMode();
		WebElement theme = driver.findElement(By.id("themeDefault"));
		String themeHref = theme.getAttribute("href");
		Assert.assertTrue(themeHref.contains("dark.css"), "Color scheme has not changed.");
		
		WebElement body = driver.findElement(By.tagName("body"));
		String bodyCssBackgroundColor = body.getCssValue("background-color");
		Assert.assertEquals(bodyCssBackgroundColor, "rgba(26, 26, 26, 1)", "Color scheme has not changed.");
	}
	
	@AfterClass
	public void afterClass() {
		new StartPage(driver).enableLightMode();
	}
}
