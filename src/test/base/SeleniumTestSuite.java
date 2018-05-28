package test.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class SeleniumTestSuite {
	
	private static WebDriver driver;
	
	/** properties file with required login information for test user and admin **/
	private static Properties properties;	
	public static final String propertiesFileName = "testData";
	
	public static final String qaEdmond = "http://qa-edmond.mpdl.mpg.de/imeji/";
	public static final String qaImeji = "http://qa-imeji.mpdl.mpg.de/";
	
	public static final String TEST_ENV_URL = qaImeji;

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
	}

	private void loadPropertiesFile() throws FileNotFoundException {
		String propertiesEnvName = System.getProperty(propertiesFileName);
		log4j.info("Reading properties file from: " + propertiesEnvName);
		properties = new Properties();
		FileInputStream input = new FileInputStream(new File(propertiesEnvName));

		try {	
			properties.load(input);
		} 
		catch (IOException e) {
			log4j.error("Properties file with login data couldn't be loaded");
			e.printStackTrace();
		}
		finally {
        	if(input != null) {
        		try {
        			input.close();
        		}
        		catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        }
	}

	/**
	 * Dependent on the specified browserType, the corresponding browser will be launched.
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
				log4j.warn("Browser : " + browserType 
						+ " is invalid. Launching default browser instead (Firefox)...");
				driver = initFirefoxDriver();
		}
		driver.manage().window().maximize();
		log4j.info("Window maximised.");
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	private WebDriver initFirefoxDriver() {
		log4j.info("Launching Firefox browser...");
		log4j.info("Found system property webdriver.gecko.driver: " + System.getProperty("webdriver.gecko.driver"));
		FirefoxOptions options = new FirefoxOptions();
		options.setCapability("marionette", true);
		
		FirefoxBinary binary = new FirefoxBinary();
		options.setBinary(binary);
//		options.setHeadless(true);
		FirefoxProfile profile = initFirefoxProfile();
		options.setProfile(profile);

		return new FirefoxDriver(options);
	}
	
	private WebDriver initChromeDriver() {
		ChromeOptions options = new ChromeOptions();
		log4j.info("Found system property webdriver.chrome.driver: " + System.getProperty("webdriver.chrome.driver"));
		options.setCapability("marionette", true);
//		options.setHeadless(true);
		options.addArguments("--window-size=1920,1200");
		return new ChromeDriver(options);
	}
	
	private FirefoxProfile initFirefoxProfile() {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.folderList",2);
		profile.setPreference("browser.download.manager.showWhenStarting", false);
		profile.setPreference("browser.download.dir","./target/downloads");
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/msword, application/csv, application/ris, text/csv, image/png, application/pdf, text/html, text/plain, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream");
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
