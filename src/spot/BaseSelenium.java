package spot;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;

import spot.pages.SingleUploadPage;
import spot.pages.notAdmin.HomePage;
import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIException;

/**
 * WebDriver used by all test classes is set in this class. 
 * @author kocar
 *
 */
public abstract class BaseSelenium {

	/** Driver instance. 
	 *  Even within own class; access through getter strongly recommended */
	protected WebDriver driver;
	
	public static String DEVKEY = "55d98b5f4eb5139f57418541506f075c";

	public static String URL = "http://rd.mpdl.mpg.de/testlink/lib/api/xmlrpc/v1/xmlrpc.php";
	
	protected String testProject = "Imeji GUI testing";

	protected String testPlan = "Imeji Release December 2015";
	
	protected String build = "Imeji Release December 1";
	
	/** TestLink test case ID*/
	protected String testLinkTestCaseID;
	
	protected static final String germanSetup = "de - German";
	protected static final String englishSetup = "en - English";
	protected static final String spanishSetup = "es - Spanish";
	protected static final String japaneseSetup = "ja - Japanese";
	
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
	
	public Properties getProperties() {				
		Properties properties = SeleniumTestSuite.getProperties();
		return properties;
	}
	
	@BeforeClass
	public void setup() {
		configureDriver();
		setupUsers();
	}
	
	private void setupUsers() {		
		setupRegisteredUser();
		setupAdmin();
	}

	private void setupAdmin() {
		spotAdminUserName = "aSpotUserName";
		spotAdminPassWord = "aSpotPassword";
		adminFamilyName = "aFamilyName";
		adminGivenName = "aGivenName";
		adminOrganizationName = "aOrganizationName";
	}

	private void setupRegisteredUser() {
		spotRUUserName = "tuSpotUserName";
		spotRUPassWord = "tuSpotPassword";
		ruGivenName = "tuGivenName";
		ruFamilyName = "tuFamilyName";
		ruOrganizationName = "tuOrganization";
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
	
	public SingleUploadPage navigateToUploadPage() {		
		navigateDriverTo("http://qa-edmond.mpdl.mpg.de/imeji/singleupload");
		
		return PageFactory.initElements(driver, SingleUploadPage.class);
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
	
	public static void reportResult(String TestProject, String TestPlan, String Testcase, String Build, String Notes,
			String Result) throws TestLinkAPIException {

		TestLinkAPIClient api = new TestLinkAPIClient(DEVKEY, URL);

		api.reportTestCaseResult(TestProject, TestPlan, Testcase, Build, Notes, Result);
	}
	
//	public abstract void setTestLinkTestCaseID();
}
