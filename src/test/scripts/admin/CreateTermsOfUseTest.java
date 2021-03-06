package test.scripts.admin;

import java.io.IOException;
import java.util.Set;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import test.base.BaseSelenium;

public class CreateTermsOfUseTest extends BaseSelenium {

	private AdminHomepage adminHomePage;
	private AdministrationPage adminPage;
	
	private String termsOfUse = "Testing terms of use.";
	private String originalTerms;
	private String windowHandleStartPage;
	private String termsHandle;
	
	@Test(priority = 1)
	public void goToAdministration() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		adminPage = adminHomePage.goToAdminPage();
	}
	
	@Test(priority = 2)
	public void changeTermsOfUse() {
		adminPage.setTermsOfUse(termsOfUse);
	}
	
	@Test(priority = 3)
	public void checkTermsOfUse() {
		adminHomePage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomePage);
		windowHandleStartPage = driver.getWindowHandle();

		adminHomePage.lookUpTermsOfUse();

		Set<String> windowHandles = driver.getWindowHandles();
		windowHandles.remove(windowHandleStartPage);
		termsHandle = windowHandles.iterator().next();
		driver.switchTo().window(termsHandle);
		
		String actualTerms = driver.findElement(By.className("imj_mainContentWrapper")).getText();
		Assert.assertEquals(actualTerms, termsOfUse, "Terms of use were not correctly uploaded.");
	}
	
	//TODO: Add Terms of Use URL tests
	
	@Test(priority = 4)
	public void revertTermsOfUse() throws IOException {
		if (driver.getWindowHandles().size() > 1) {
			driver.switchTo().window(termsHandle);
			driver.close();
			driver.switchTo().window(windowHandleStartPage);
		}
		
		adminHomePage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomePage);
		adminPage = adminHomePage.goToAdminPage();
		originalTerms = "<p> \n Questions about the Terms of Use should be sent to: <a href=\"mailto:edmond@mpdl.mpg.de\" target=\"_blank\">edmond@mpdl.mpg.de</a>. </p>";
		adminPage.setTermsOfUse(originalTerms);
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomePage);
		adminHomePage.logout();
	}
}
