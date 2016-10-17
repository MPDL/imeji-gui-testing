package spot.pages.admin;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;

public class UserGroupPage extends BasePage {

	@FindBy(css=".imj_admindataSetEdit .imj_submitButton")
	private WebElement addUserButton;
	
	@FindBy(css=".imj_userGrantList")
	private List<WebElement> alreadyAddedUsers;
	
	@FindBy(id="userForm:lnkEditUserdata")
	private WebElement editButton;
	
	public UserGroupPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public UserGroupPage addNewUser(String userEmail) {
		addUserButton.click();
		UsersOverviewPage allUsersOverViewPage = PageFactory.initElements(driver, UsersOverviewPage.class);
		allUsersOverViewPage.addUserToUserGroup(userEmail);
			
		// this page is refreshed; init elements once again
		PageFactory.initElements(driver, this);
		
		return this;
	}

	public boolean isUserPresent(String userEmail) {
		boolean isUserPresent = false;
		
		for (WebElement alreadyAddedUser : alreadyAddedUsers) {
			String userNamePlusEmail = alreadyAddedUser.getText();
			if (userNamePlusEmail.contains(userEmail)) {
				isUserPresent = true;
				break;
			}
		}
		
		return isUserPresent;
	}
	
	public UserGroupPage deleteUser(String userEmail) {
		for (WebElement user : alreadyAddedUsers) {
			if (user.getText().contains(userEmail)) {
				user.findElement(By.className("imj_cancelButton")).click();
				return PageFactory.initElements(driver, UserGroupPage.class);
			}
		}
		
		throw new NoSuchElementException("User with this email was not found");
	}
	
	public UserGroupPage changeTitle(String newTitle) {
		editButton.click();
		
		EditUserGroupPage editUserGroup = PageFactory.initElements(driver, EditUserGroupPage.class);
		return editUserGroup.editTitle(newTitle);
	}
	
	public String getUserGroupTitle() {
		return driver.findElement(By.tagName("h2")).getText();
	}

	
}
