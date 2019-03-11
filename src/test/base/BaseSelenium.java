package test.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

  private static final Logger log4j = LogManager.getLogger(SeleniumTestSuite.class.getName());

  /**
   * Driver instance. Even within own class; access through getter strongly recommended
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

  protected Instant testMethodStartTime;

  public Properties getProperties() {
    Properties properties = SeleniumTestSuite.getProperties();
    return properties;
  }

  // TestNG does not set superclass priority in BeforeClass methods
  @BeforeClass
  public void setup() {
    configureDriver();
    setupUsers();

    //Each test run starts at the imeji StartPage
    StartPage startPage = navigateToStartPage();
    //Set English as language, because some tests use English labels to select elements
    startPage.switchToEnglish();
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
  
  public void restartDriver() {
	  SeleniumTestSuite.restartDriver();
	  this.setup();
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

  /**
   * Navigate to the imeji SartPage.
   * 
   * @return the SartPage
   */
  public StartPage navigateToStartPage() {
    if (!getCurrentURL().equals(SeleniumTestSuite.TEST_ENV_URL)) {
      navigateDriverTo(SeleniumTestSuite.TEST_ENV_URL);
    }

    return new StartPage(driver);
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
   * 
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
   * Get the paths of all files of the given file directory. <br>
   * Returns the paths of all files separated by "\n", so that the files can be uploaded at once
   * using the selenium sendKeys() method!
   * 
   * @param directoryName The name of the directory
   * @return the system-independent file paths, separated by "\n"
   */
  public final String getPathsOfAllFilesInDirectory(String directoryName) {
    String systemIndependentDirectoryPath = this.getDirectoryPath(directoryName);

    File directory = new File(systemIndependentDirectoryPath);
    File[] files = directory.listFiles();
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      if (file.isFile()) {
        stringBuilder.append(file.getPath());
        if (i < files.length - 1) {
          stringBuilder.append("\n");
        }
      }
    }
    String filespaths = stringBuilder.toString();

    return filespaths;
  }


  public final String getDirectoryPath(String directoryName) {
    //TODO: Refactor this method: make it independent of maven, handle not existing directory
    String classesDirectoryPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    String directoryPath = classesDirectoryPath + File.separator + directoryName;

    File systemIndependentDirectory = new File(directoryPath);
    String systemIndependentDirectoryPath = systemIndependentDirectory.getPath();

    return systemIndependentDirectoryPath;
  }

  /**
   * Handles failed tests.
   * 
   * @param testResult
   */
  @AfterMethod
  public void handleTestFailure(ITestResult testResult) {
    if (testResult.getStatus() == ITestResult.FAILURE) {
      takeScreenshot(testResult.getInstanceName() + "_" + testResult.getName());
      logTestResult(testResult);
      navigateToStartPage();
    }
  }

  /**
   * Takes a screenshot of the current test step.
   * 
   * @param testResult
   * @param className
   * @param methodName
   */
  public void takeScreenshot(String screenshotFileName) {
    try {
      String screenshotPath = "./target/" + screenshotFileName + "_Screenshot.jpg";
      File screenshot = ((TakesScreenshot) driver).getScreenshotAs(org.openqa.selenium.OutputType.FILE);
      Files.copy(screenshot.toPath(), new File(screenshotPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException exc) {
      log4j.error("Error copying the screenshot file.", exc);
    }
  }

  /**
   * Logs the test result.
   * 
   * @param testResult
   */
  public void logTestResult(ITestResult testResult) {
    log4j.info(testResult.getTestClass().getName() + "." + testResult.getName());
    Throwable testError = testResult.getThrowable();
    if (testError != null) {
      log4j.error(testError.getMessage());
    }
  }

	// TODO: Instead of this method, put log4j instance in all the classes!?
	@BeforeClass
	public void logTestClass() {
	  String testClassName = this.getClass().getName();
	  log4j.info("Testing - " + testClassName);
	}

}
