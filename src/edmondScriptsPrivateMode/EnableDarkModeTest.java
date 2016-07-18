package edmondScriptsPrivateMode;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;

public class EnableDarkModeTest extends BaseSelenium {

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		switchToPrivateMode(true);
	}
	
	private void switchToPrivateMode(boolean switchOnPrivateMode) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		if (switchOnPrivateMode)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		adminHomePage = (AdminHomePage) adminPage.goToHomePage(adminHomePage);
		adminHomePage.logout();
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
		switchToPrivateMode(false);
	}
}
