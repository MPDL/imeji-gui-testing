package spot;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;
import spot.pages.RegistrationPage;

/**
 * WebDriver used by all test classes is set in this class. 
 * @author kocar
 *
 */
public abstract class BaseSelenium {

	/** Driver instance. 
	 *  Even within own class; access through getter strongly recommended */
	private WebDriver driver;
	
	private StartPage startPage;
	private RegistrationPage  registrationPage;
	
	public Properties getProperties() {				
		return SeleniumTestSuite.getProperties(); 
	}
	
	public WebDriver getDriver() {
		driver = SeleniumTestSuite.getDriver();
        if(driver != null) 
        	return driver; 
        
        return driver = new FirefoxDriver();
    }
	
	public String getPropertyAttribute(String key) {
		return getProperties().getProperty(key);
	}
	
	public StartPage getStartPage() {
		if (startPage == null)
			return startPage = new StartPage(getDriver());
		return startPage;
	}
	
	public RegistrationPage getRegistrationPage() {
		if (registrationPage == null)
			return registrationPage = new RegistrationPage(getDriver());
		return registrationPage;
	}
	
	public String getCurrentURL() {
		return getDriver().getCurrentUrl();
	}
	
	public void navigateDriverTo(String URLtoLoad) {
		getDriver().navigate().to(URLtoLoad);
	}
	
	public void navigateDriverBack() {
		getDriver().navigate().back();
	}
	
	public void logout(HomePage homePage) {
		homePage.logout();
	}
}
