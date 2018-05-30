package test.base;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import spot.pages.HelpPage;
import spot.pages.StartPage;
import spot.pages.registered.Homepage;

/**
 * WebDriver used by all test classes is set in this class.
 * 
 * @author kocar
 *
 */
public abstract class BaseSelenium {

	/**
	 * Driver instance. Even within own class; access through getter strongly
	 * recommended
	 */
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

	// TestNG does not set superclass priority in BeforeClass methods
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

	/**
	 * Get the path to the given file name.
	 * @param fileName
	 * @return the system-independent file path
	 */
	public final String getFilepath(String fileName) {
		String filePath = this.getClass().getClassLoader().getResource(fileName).getPath();
		File systemIndependentFile = new File(filePath);
		String systemIndependentFilePath = systemIndependentFile.getPath();
		return systemIndependentFilePath;
	}

	/**
	 * Handles failed tests.
	 * @param testResult
	 */
	@AfterMethod
	public void handleTestFailure(ITestResult testResult) {
		if (testResult.getStatus() == ITestResult.FAILURE) {
			if (!SeleniumTestSuite.HEADLESS) {
				takeScreenshot(testResult.getName());
			}
			logTestResult(testResult);
			navigateToStartPage();
		}
	}

	/**
	 * Takes a screenshot of the current test step.
	 * @param screenshotName
	 */
	public void takeScreenshot(String screenshotName) {
		try {
			String screenshotPath = "./target/" + screenshotName + "_Screenshot.jpg";
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
			FileUtils.copyFile(screenshot, new File(screenshotPath));
		} catch (IOException exc) {
		}
	}

	/**
	 * Logs the test result.
	 * @param testResult
	 */
	public void logTestResult(ITestResult testResult) {
		System.out.println(testResult.getTestClass().getName() + "." + testResult.getName());
		Throwable testError = testResult.getThrowable();
		if (testError != null) {
			System.out.println(testError.getMessage());
		}
		System.out.println();
	}

}
