package test.setup;

import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import test.base.BaseSelenium;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;

public class BasicOneAuthorSetup extends BaseSelenium {
  
	private AdminHomepage adminHomepage;
	
	private String termsOfUse = "Terms of use";
	private String license = "CC_BY";
	private String domains = "mpdl.mpg.de";
	
	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
	}
	
	@AfterMethod
	public void resetHomepage() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
	}
  
	@Test(priority = 1)
	public void loginAsAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
	}

	@Test(priority = 2)
	public void enableAutosuggestion() {
		adminHomepage.goToAdminPage().setAutosuggestionMP();
	}
	
	@Test(priority = 3)
	public void createTermsOfUse() {
		adminHomepage.goToAdminPage().setTermsOfUse(termsOfUse);
	}
	
	@Test(priority = 4)
	public void setLicenseParameter() {
		adminHomepage.goToAdminPage().setLicense(license);
	}
	
	@Test(priority = 5)
	public void enableUserRegistration() {
		adminHomepage.goToAdminPage().enableRegistration(true);
	}
	
	@Test(priority = 6)
	public void setAllowedDomains() {
		adminHomepage.goToAdminPage().restrictRegistrationDomains(domains);
	}
  
	@AfterClass
	public void afterClass() {
		adminHomepage.logout();
	}

}
