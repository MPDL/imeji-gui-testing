package test.base;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

public class SeleniumTestSuite {

	private static WebDriver driver;

	/**
	 * properties file with required login information for test user and admin
	 **/
	private static Properties properties;
	public static final String PROPERTIES_FILE_NAME = "testData.properties";

	public static final boolean HEADLESS = true;

	public static final String QA_EDMOND = "http://qa-edmond.mpdl.mpg.de/imeji/";
	public static final String QA_IMEJI = "http://qa-imeji.mpdl.mpg.de/imeji/";
	private static final String QA_CESAR = "http://qa-caesar.mpdl.mpg.de/imeji/";

	public static final String DEV_IMEJI = "https://dev-imeji.mpdl.mpg.de/imeji/";

//	 public static final String TEST_ENV_URL = QA_IMEJI;
	 public static final String TEST_ENV_URL = DEV_IMEJI;
//	public static final String TEST_ENV_URL = QA_CESAR;

	private static final Logger log4j = LogManager.getLogger(SeleniumTestSuite.class.getName());

	@Parameters("browserType")
	@BeforeSuite
	public void launchDriver(String browserType) throws MalformedURLException, FileNotFoundException {
		log4j.info("Launching driver...");
		log4j.info("Browser is " + browserType);

		setDriver(browserType);
		loadPropertiesFile();

		driver.navigate().to(TEST_ENV_URL);
		log4j.info("Test environment url loaded.");
		log4j.info("Running Tests on: " + TEST_ENV_URL);
	}

	private void loadPropertiesFile() throws FileNotFoundException {
		log4j.info("Reading properties file...");
		properties = new Properties();
		InputStream input = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);

		try {
			properties.load(input);
			log4j.info("Successfully loaded testData.properties");
		} catch (IOException e) {
			log4j.error("Properties file with login data couldn't be loaded");
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Dependent on the specified browserType, the corresponding browser will be
	 * launched.
	 * 
	 * @param browserType
	 */
	private void setDriver(String browserType) {
		switch (browserType) {
		case "chrome":
			driver = initChromeDriver();
			break;
		case "firefox":
			driver = initFirefoxDriver();
			break;
		default:
			log4j.warn("Browser : " + browserType + " is invalid. Launching default browser instead (Firefox)...");
			driver = initFirefoxDriver();
		}
		driver.manage().window().maximize();
		log4j.info("Window maximised.");

		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
	}

	private WebDriver initFirefoxDriver() {
		log4j.info("Launching Firefox browser...");
		// The system property webdriver.gecko.driver must be set to the
		// webdriver-executable-file -> this is done by Maven!
		log4j.info("Found system property webdriver.gecko.driver: " + System.getProperty("webdriver.gecko.driver"));
		
		FirefoxOptions options = new FirefoxOptions();
		
		// Set a different binary if another Version of Firefox should be used for the tests
//		options.setBinary("C:/Program Files/Firefox Nightly/firefox.exe");
		
		options.setCapability("marionette", true);
		options.setHeadless(HEADLESS);
		FirefoxProfile profile = initFirefoxProfile();
		options.setProfile(profile);

		WebDriver webDriver = new FirefoxDriver(options);
		Capabilities capabilities = ((RemoteWebDriver) webDriver).getCapabilities();
		
		String browserName = capabilities.getBrowserName();
		String browserVersion = capabilities.getVersion();
		log4j.info("Browser version: " + browserVersion + " (" + browserName + ")");
		
		return webDriver;
	}

	private WebDriver initChromeDriver() {
		log4j.info("Launching Chrome browser...");
		// The system property webdriver.chrome.driver must be set to the
		// webdriver-executable-file -> this is done by Maven!
		log4j.info("Found system property webdriver.chrome.driver: " + System.getProperty("webdriver.chrome.driver"));

		ChromeOptions options = new ChromeOptions();
		
		options.setCapability("marionette", true);
		options.setHeadless(HEADLESS);
		options.addArguments("--window-size=1920,1200");
		 
		// Without the two following proxy-options the tests do not run in headless mode or are very slow:
		// Set proxy-server -> 'direct://' means: Do not use a proxy for all connections
		options.addArguments("--proxy-server='direct://'");
		// Set which addresses should not be proxied -> * means: All. Do not use a proxy without any exception
		options.addArguments("--proxy-bypass-list=*");
		
		WebDriver webDriver = new ChromeDriver(options);
		Capabilities capabilities = ((RemoteWebDriver) webDriver).getCapabilities();
		
		String browserName = capabilities.getBrowserName();
		String browserVersion = capabilities.getVersion();
		log4j.info("Browser version: " + browserVersion + " (" + browserName + ")");
		
		return webDriver;
	}

	private FirefoxProfile initFirefoxProfile() {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.download.manager.showWhenStarting", false);
		profile.setPreference("browser.download.dir", "./target/downloads");
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/msword, application/csv, application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream");
		profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
		profile.setPreference("browser.download.manager.focusWhenStarting", false);
		profile.setPreference("browser.download.useDownloadDir", true);
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);
		profile.setPreference("browser.download.manager.closeWhenDone", true);
		profile.setPreference("browser.download.manager.showAlertOnComplete", false);
		profile.setPreference("browser.download.manager.useWindow", false);
		profile.setPreference("services.sync.prefs.sync.browser.download.manager.showWhenStarting", false);
		profile.setPreference("pdfjs.disabled", true);

		return profile;
	}

	@AfterSuite
	public static void quitDriver() {
		log4j.info("Quitting driver...");
		driver.quit();
	}

	public static WebDriver getDriver() {
		return driver;
	}

	public static Properties getProperties() {
		return properties;
	}

	@BeforeTest
	public void testBeforeTest() {
	}

	@AfterTest
	public void testAfterTest() {
	}
}
