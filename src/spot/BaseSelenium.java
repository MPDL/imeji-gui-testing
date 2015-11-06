package spot;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.BeforeClass;

import spot.pages.notAdmin.HomePage;

/**
 * WebDriver used by all test classes is set in this class. 
 * @author kocar
 *
 */
public abstract class BaseSelenium {

	/** Driver instance. 
	 *  Even within own class; access through getter strongly recommended */
	protected WebDriver driver;
	
//	protected final StartPage startPage = new StartPage(getDriver());;
	
	public Properties getProperties() {				
		return SeleniumTestSuite.getProperties(); 
	}
	
	@BeforeClass
	public void setup() {
		configureDriver();
	}
	
	public boolean isElementDisplayed(WebElement webElement) {
		
		boolean isDisplayed = true;
		
		if (!webElement.isDisplayed())
			isDisplayed = false;
		
		return isDisplayed;
	}
	
	private void configureDriver() {
		driver = SeleniumTestSuite.getDriver();
        if(driver == null) 
        	driver = new FirefoxDriver();
	}

//	public WebDriver getDriver() {
//		driver = SeleniumTestSuite.getDriver();
//        if(driver != null) 
//        	return driver; 
//        
//        return driver = new FirefoxDriver();
//    }
	
	public String getPropertyAttribute(String key) {
		return getProperties().getProperty(key);
	}
	
//	public StartPage getStartPage() {
//		if (startPage == null)
//			return startPage = new StartPage(getDriver());
//		return startPage;
//	}
	
	public String getCurrentURL() {
//		return getDriver().getCurrentUrl();
		return driver.getCurrentUrl();
	}
	
	public void navigateDriverTo(String URLtoLoad) {
//		getDriver().navigate().to(URLtoLoad);
		driver.navigate().to(URLtoLoad);
	}
	
	public void navigateToStartPage() {
		if (!getCurrentURL().equals(SeleniumTestSuite.testEnvironmentURL)) {
			navigateDriverTo(SeleniumTestSuite.testEnvironmentURL);
		}
	}
	
//	public HelpPage navigateToHelpPage() {
//		HelpPage helpPage = getStartPage().goToHelpPage();
//
//		Set<String> windowHandles = getDriver().getWindowHandles();
//
//		for (String winHandle : windowHandles) {
//			getDriver().switchTo().window(winHandle);
//		}
//		
//		return helpPage;
//	}
	
	public void navigateDriverBack() {
//		getDriver().navigate().back();
		driver.navigate().back();
	}
	
	public void logout(HomePage homePage) {
		homePage.logout();
	}
}
