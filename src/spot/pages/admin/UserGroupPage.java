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

	@FindBy(css=".fa-plus")
	private WebElement addUserButton;
	
	@FindBy(css="#j_idt130")
	private WebElement alreadyAddedUsers;
	
	@FindBy(id="groupForm:btnEditUserdata")
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
		String allUsers = alreadyAddedUsers.getText();
		
		return allUsers.contains(userEmail);
	}
	
	/**
	 * TODO: make delete method by user email
	 */
	public UserGroupPage deleteUser(int index) {
		List<WebElement> userDeleteButtons = driver.findElements(By.cssSelector(".fa-times a"));
		userDeleteButtons.get(index).click();
		
		PageFactory.initElements(driver, this);
		return this;
	}
	
	public UserGroupPage changeTitle(String newTitle) {
		editButton.click();
		
		EditUserGroupPage editUserGroup = PageFactory.initElements(driver, EditUserGroupPage.class);
		return editUserGroup.editTitle(newTitle);
	}
	
	public boolean hasGrant(String grantName) {
		List<WebElement> userGroupGrants = driver.findElements(By.cssSelector(".imj_admindataValueEntry span"));
		
		for (WebElement userGrant : userGroupGrants) {
			if (userGrant.getText().contains(grantName))
				return true;
		}
		
		return false;
	}
	
	public String getUserGroupTitle() {
		String headline = driver.findElement(By.tagName("h1")).getText();
		return headline.substring("User Group ".length());
	}

	
}
