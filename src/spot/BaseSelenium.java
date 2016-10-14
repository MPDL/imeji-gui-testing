package spot;

import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;

import spot.pages.HelpPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
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
	
	protected String spotRUUserName;
	protected String spotRUPassWord;
	protected String ruFamilyName;
	protected String ruGivenName;
	protected String ruOrganizationName;
	
	protected String spotAdminUserName;
	protected String spotAdminPassWord;
	protected String adminFamilyName;
	protected String adminGivenName;
	protected String adminOrganizationName;
	
	protected String restrUserName;
	protected String restrPassWord;
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
		spotAdminUserName = "aSpotUserName";
		spotAdminPassWord = "aSpotPassword";
		adminFamilyName = "aFamilyName";
		adminGivenName = "aGivenName";
		adminOrganizationName = "aOrganizationName";
	}

	private void setupRegisteredUser() {
		spotRUUserName = "tuEmailAddress";
		spotRUPassWord = "tuEmailPassword";
		ruGivenName = "tuGivenName";
		ruFamilyName = "tuFamilyName";
		ruOrganizationName = "tuOrganization";
	}
	
	private void setupRestrictedUser() {
		restrUserName = "reEmailAddress";
		restrPassWord = "reEmailPassword";
		restrFamilyName = "reFamilyName";
		restrGivenName = "reGivenName";
		restrOrganizationName = "reOrganization";
	}

	public boolean isElementDisplayed(WebElement webElement) {
		
		boolean isDisplayed = true;
		
		if (!webElement.isDisplayed())
			isDisplayed = false;
		
		return isDisplayed;
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
		navigateDriverTo("http://qa-edmond.mpdl.mpg.de/imeji/singleupload");
		
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
	
	public void logout(HomePage homePage) {
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
	
}
