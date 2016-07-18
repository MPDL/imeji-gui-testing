package edmondScriptsPrivateMode;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.NewActionComponent;
import spot.pages.AdministrationPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;

public class LimitedAccessForNRUPrivateMode extends BaseSelenium {

	@BeforeClass
	public void beforeClass() {
		super.setup();
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
	public void newActionNonExistentTest() {
		WebElement newButton = new NewActionComponent(driver).getNewButton();
		boolean isNewButtonDisplayed = false;
		try {
			newButton.click();
			isNewButtonDisplayed = true;
		}
		catch (NoSuchElementException exc) {
			isNewButtonDisplayed = false;
		}
		
		Assert.assertEquals(isNewButtonDisplayed, false, 
				"New Button for creating new collections/albums is displayed despite not being logged in");
	}
	
	@AfterClass
	public void afterClass() {
		switchToPrivateMode(false);
	}
}
