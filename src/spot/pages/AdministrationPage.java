package spot.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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
		
		CreateNewUserPage createNewUserPage = new CreateNewUserPage(driver);
		
		return createNewUserPage.createNewUser(newUserName);
	}
	
	public AllUsersOverViewPage viewAllUsers() {
		
		viewAllUsers.click();
		
		return PageFactory.initElements(driver, AllUsersOverViewPage.class);
	}

	public AllUserGroupsOverViewPage createNewUserGroup(String newUserGroupName) {
		
		createNewUserGroup.click();
		
		CreateNewUserGroupPage createNewUserGroupPage = new CreateNewUserGroupPage(driver);
		
		return createNewUserGroupPage.createNewUserGroup(newUserGroupName);
	}
	
	public AllUserGroupsOverViewPage viewAllUserGroups() {
		
		viewAllUserGroups.click();
		
		return PageFactory.initElements(driver, AllUserGroupsOverViewPage.class);
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
