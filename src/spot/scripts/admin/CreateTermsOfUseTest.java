package spot.scripts.admin;

import java.util.Set;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.ConfigurationEditPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;

public class CreateTermsOfUseTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
	private AdministrationPage adminPage;
	
	private String termsOfUse = "Testing terms of use.";
	private String windowHandleStartPage;
	private String termsHandle;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
	}
	
	@Test(priority = 1)
	public void goToAdministration() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		adminPage = adminHomePage.goToAdminPage();
	}
	
	@Test(priority = 2)
	public void changeTermsOfUse() {
		adminPage.setTermsOfUse(termsOfUse);
	}
	
	@Test(priority = 3)
	public void checkTermsOfUse() {
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
		windowHandleStartPage = driver.getWindowHandle();

		adminHomePage.lookUpTermsOfUse();

		Set<String> windowHandles = driver.getWindowHandles();
		windowHandles.remove(windowHandleStartPage);
		termsHandle = windowHandles.iterator().next();
		driver.switchTo().window(termsHandle);
		
		
	}
	
	@AfterClass
	public void afterClass() {
		if (driver.getWindowHandles().size() > 1) {
			driver.switchTo().window(termsHandle);
			driver.close();
			driver.switchTo().window(windowHandleStartPage);
		}
		
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
		adminPage = adminHomePage.goToAdminPage();
		ConfigurationEditPage configurationEdit = adminPage.setTermsOfUse("");
		configurationEdit.goToHomePage(adminHomePage).logout();
	}
}
