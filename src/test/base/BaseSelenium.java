package test.base;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import spot.pages.HelpPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.registered.Homepage;

/**
 * WebDriver used by all test classes is set in this class. 
 * @author kocar
 *
 */
public abstract class BaseSelenium {

	/** Driver instance. 
	 *  Even within own class; access through getter strongly recommended */
	protected WebDriver driver;
	
	protected String ruUsername;
	protected String ruPassword;
	protected String ruFamilyName;
	protected String ruGivenName;
	protected String ruOrganizationName;
	
	protected String adminUsername;
	protected String adminPassword;
	protected String adminFamilyName;
	protected String adminGivenName;
	protected String adminOrganizationName;
	
	protected String restrUsername;
	protected String restrPassword;
	protected String restrFamilyName;
	protected String restrGivenName;
	protected String restrOrganizationName;
	
	protected final String privateCollectionKey = "sharedPrivateCollection";
	protected final String releasedCollectionKey = "sharedReleasedCollection";
	protected final String collectionPMKey = "sharedCollectionPM";
	
	public Properties getProperties() {				
		Properties properties = SeleniumTestSuite.getProperties();
		return properties;
	}
	
	//TestNG does not set superclass priority in BeforeClass methods
	@BeforeClass
	public void setup() {
		configureDriver();
		setupUsers();
	}
	
	private void setupUsers() {		
		setupRegisteredUser();
		setupAdmin();
		setupRestrictedUser();
	}

	private void setupAdmin() {
		adminUsername = "aSpotUserName";
		adminPassword = "aSpotPassword";
		adminFamilyName = "aFamilyName";
		adminGivenName = "aGivenName";
		adminOrganizationName = "aOrganizationName";
	}

	private void setupRegisteredUser() {
		ruUsername = "tuEmailAddress";
		ruPassword = "tuPassword";
		ruGivenName = "tuGivenName";
		ruFamilyName = "tuFamilyName";
		ruOrganizationName = "tuOrganization";
	}
	
	private void setupRestrictedUser() {
		restrUsername = "reEmailAddress";
		restrPassword = "reEmailPassword";
		restrFamilyName = "reFamilyName";
		restrGivenName = "reGivenName";
		restrOrganizationName = "reOrganization";
	}

	public boolean isElementDisplayed(WebElement webElement) {
		
		return webElement.isDisplayed();
	}
	
	private void configureDriver() {
		driver = SeleniumTestSuite.getDriver();
        if (driver == null) 
        	driver = new FirefoxDriver();
	}
	
	public String getPropertyAttribute(String key) {
		return getProperties().getProperty(key);
	}
	
	public String getCurrentURL() {
		return driver.getCurrentUrl();
	}
	
	public void navigateDriverTo(String URLtoLoad) {
		driver.navigate().to(URLtoLoad);
	}
	
	public void navigateToStartPage() {
		if (!getCurrentURL().equals(SeleniumTestSuite.TEST_ENV_URL)) {
			navigateDriverTo(SeleniumTestSuite.TEST_ENV_URL);
		}
	}
	
	public SingleUploadPage navigateToUploadPage() {		
		navigateDriverTo("http://qa-imeji.mpdl.mpg.de/imeji/singleupload");
		
		return PageFactory.initElements(driver, SingleUploadPage.class);
	}
	
	public HelpPage navigateToHelpPage() {
		HelpPage helpPage = new StartPage(driver).goToHelpPage();

		Set<String> windowHandles = driver.getWindowHandles();

		for (String winHandle : windowHandles) {
			driver.switchTo().window(winHandle);
		}
		
		return helpPage;
	}
	
	public void navigateDriverBack() {
		driver.navigate().back();
	}
	
	public void logout(Homepage homePage) {
		homePage.logout();
	}
	
	public final String getFilepath(String fileName) {
		fileName = "/" + fileName;
		String filepath = getClass().getResource(fileName).getPath();
		if (driver instanceof FirefoxDriver)
			filepath = "file:" + filepath;
		if (driver instanceof ChromeDriver)
			filepath = filepath.substring(1, filepath.length());
		return filepath;
	}
	
	@AfterMethod
	public void failureScreenshot(ITestResult result) {
		if (result.getStatus() == ITestResult.FAILURE) {
			try {
				String screenshotPath = "./target/screenshot" + result.getName() + ".jpg";
				File screenshot = ((TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
				FileUtils.copyFile(screenshot, new File(screenshotPath));
			}
			catch (IOException exc) {}
			System.out.println(result.getName());
			System.out.println(result.getThrowable().getMessage());
			System.out.println();
			navigateToStartPage();
		}
	}
	
}
