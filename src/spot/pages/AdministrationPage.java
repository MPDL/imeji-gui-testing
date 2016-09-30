package spot.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.admin.NewUserGroupPage;
import spot.pages.admin.NewUserPage;
import spot.pages.admin.UserGroupsOverviewPage;
import spot.pages.admin.UserProfilePage;
import spot.pages.admin.UsersOverviewPage;

public class AdministrationPage extends BasePage {

	@FindBy(css=".imj_listBody>div:nth-of-type(1) li:nth-of-type(1)>a")
	private WebElement createNewUser;
	
	@FindBy(css=".imj_listBody>div:nth-of-type(1) li:nth-of-type(4)>a")
	private WebElement createNewUserGroup;
	
	@FindBy(css=".imj_listBody>div:nth-of-type(1) li:nth-of-type(2)>a")
	private WebElement viewAllUsers;
	
	@FindBy(css=".imj_listBody>div:nth-of-type(1) li:nth-of-type(3)>a")
	private WebElement viewAllUserGroups;
	
	@FindBy(css=".imj_listBody>div:nth-of-type(2) li:nth-of-type(1)>a")
	private WebElement configurationEdit;
	
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

	public UserGroupsOverviewPage createNewUserGroup(String newUserGroupName) {
		
		createNewUserGroup.click();
		
		NewUserGroupPage createNewUserGroupPage = new NewUserGroupPage(driver);
		
		return createNewUserGroupPage.createNewUserGroup(newUserGroupName);
	}
	
	public UserGroupsOverviewPage viewAllUserGroups() {
		
		viewAllUserGroups.click();
		
		return PageFactory.initElements(driver, UserGroupsOverviewPage.class);
	}
	
	public ConfigurationEditPage enablePrivateMode() {
		
		configurationEdit.click();
		ConfigurationEditPage configurationEditPage = new ConfigurationEditPage(driver);
		return configurationEditPage.enablePrivateMode();
	}
	
	public ConfigurationEditPage disablePrivateMode() {
		
		configurationEdit.click();
		ConfigurationEditPage configurationEditPage = new ConfigurationEditPage(driver);
		return configurationEditPage.disablePrivateMode();
	}
	
	public ConfigurationEditPage enableAlbums() {
		
		configurationEdit.click();
		ConfigurationEditPage configurationEditPage = new ConfigurationEditPage(driver);
		return configurationEditPage.enableAlbums();
	}
	
	public ConfigurationEditPage disableAlbums() {
		
		configurationEdit.click();
		ConfigurationEditPage configurationEditPage = new ConfigurationEditPage(driver);
		return configurationEditPage.disableAlbums();
	}
	
	public ConfigurationEditPage browseDefaultViewThumbnails() {
		configurationEdit.click();
		ConfigurationEditPage configurationEditPage = new ConfigurationEditPage(driver);
		return configurationEditPage.browseDefaultViewThumbnails();
	}
	
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
