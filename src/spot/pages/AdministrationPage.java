package spot.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.admin.CreateStatementPage;
import spot.pages.admin.NewUserGroupPage;
import spot.pages.admin.NewUserPage;
import spot.pages.admin.UserGroupPage;
import spot.pages.admin.UserGroupsOverviewPage;
import spot.pages.admin.UserProfilePage;
import spot.pages.admin.UsersOverviewPage;

public class AdministrationPage extends BasePage {

	@FindBy(css=".imj_listBody>div:nth-of-type(1) li:nth-of-type(1)>a")
	private WebElement createNewUser;
	
	@FindBy(css=".imj_listBody>.imj_adminPanel:nth-of-type(2)>.imj_content li>a")
	private WebElement createNewUserGroup;
	
	@FindBy(css=".imj_listBody>div:nth-of-type(1) li:nth-of-type(2)>a")
	private WebElement viewAllUsers;
	
	@FindBy(css=".imj_listBody>.imj_adminPanel:nth-of-type(2)>.imj_content li:nth-of-type(2)>a")
	private WebElement viewAllUserGroups;
	
	@FindBy(css=".imj_administrationTiledList .imj_config a")
	private WebElement configurationEdit;
	
	@FindBy(linkText = "http://qa-imeji.mpdl.mpg.de/imeji/createstatement")
	private WebElement createStatement;
	
	public AdministrationPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public UserProfilePage createNewUser(String newUserName) {
		createNewUser.click();
		
		NewUserPage createNewUserPage = new NewUserPage(driver);
		
		return createNewUserPage.createNewUser(newUserName);
	}
	
	public UserProfilePage createNewRestrictedUser(String newUserName) {
		createNewUser.click();
		
		NewUserPage createNewUserPage = new NewUserPage(driver);
		
		return createNewUserPage.createdNewRestrictedUser(newUserName);
	}
	
	public UsersOverviewPage viewAllUsers() {
		viewAllUsers.click();
		
		return PageFactory.initElements(driver, UsersOverviewPage.class);
	}

	public UserGroupPage createNewUserGroup(String newUserGroupName) {
		createNewUserGroup.click();
		
		NewUserGroupPage createNewUserGroupPage = new NewUserGroupPage(driver);
		
		return createNewUserGroupPage.createNewUserGroup(newUserGroupName);
	}
	
	public UserGroupsOverviewPage viewAllUserGroups() {
		viewAllUserGroups.click();
		
		return PageFactory.initElements(driver, UserGroupsOverviewPage.class);
	}
	
	public ConfigurationPage enablePrivateMode() {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.enablePrivateMode();
	}
	
	public ConfigurationPage disablePrivateMode() {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.disablePrivateMode();
	}
	
	public ConfigurationPage browseDefaultViewThumbnails() {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.browseDefaultViewThumbnails();
	}
	
	public ConfigurationPage browseDefaultViewList() {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.browseDefaultViewList();
	}
	
	public ConfigurationPage setMaintenanceMessage(String message) {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.setMaintenanceMessage(message);
	}
	
	public ConfigurationPage setTermsOfUse(String termsOfUse) {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.setTermsOfUse(termsOfUse);
	}
	
	public ConfigurationPage setLicense(String license) {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.setLicense(license);
	}
	
	public ConfigurationPage setAutosuggestionMP() {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.setAutosuggestionMP();
	}
	
	public ConfigurationPage enableRegistration(boolean usersCanRegister) {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.enableRegistration();
	}
	
	public ConfigurationPage restrictRegistrationDomains(String domains) {
		configurationEdit.click();
		ConfigurationPage configurationEditPage = new ConfigurationPage(driver);
		return configurationEditPage.restrictRegistrationDomains(domains);
	}
	
//	public CreateStatementPage defaultStatementNumber(int id) {
//		createStatement.click();
//		CreateStatementPage statementsPage = new CreateStatementPage(driver);
//		return statementsPage.defaultStatementNumber(int id);
//	}
	
	public boolean areAllComponentsDisplayed() {
		try {
			driver.findElement(By.className("imj_userConfig"));
			driver.findElement(By.className("imj_config"));
			driver.findElement(By.className("imj_spaceConfig"));
			driver.findElement(By.className("imj_storageConfig"));
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
}
