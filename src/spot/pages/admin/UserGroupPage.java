package spot.pages.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

public class UserGroupPage extends BasePage {

	@FindBy(css=".fa-plus")
	private WebElement addUserButton;
	
	@FindBy(id="userList")
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
	
	private int getUserPositionInUserList(String userEmail) {
		ArrayList<String> userListLines = new ArrayList<>(Arrays.asList(alreadyAddedUsers.getAttribute("innerText").split("\\R")));
		// First line is no user but the "Hide users" element and therefore must be removed from userListLines
		userListLines.remove(0);
		
		int position = 0;
		for (String line : userListLines) {
			if(line.contains("(" + userEmail + ")")) {
				return position;
			}
			position++;
		}
		
		throw new NoSuchElementException("User with email '" + userEmail + "' was not found in user group.");
	}
	
	public UserGroupPage deleteUser(String userEmail) {
		List<WebElement> userDeleteButtons = driver.findElements(By.cssSelector("#userList .fa-times"));
		int userPositionInUserList = this.getUserPositionInUserList(userEmail);
		WebElement userDeleteButton = userDeleteButtons.get(userPositionInUserList);
		userDeleteButton.click();
		
		// Wait until the user group page starts reloading, after clicking userDeleteButton (then the userDeleteButton element is stale)
		wait.until(ExpectedConditions.stalenessOf(userDeleteButton));
		// this page is refreshed; init elements once again
		PageFactory.initElements(driver, this);
		return this;
	}
	
	public UserGroupPage deleteUser(int index) {		
		List<WebElement> userDeleteButtons = driver.findElements(By.cssSelector("#userList .fa-times"));
		WebElement userDeleteButton = userDeleteButtons.get(index);		
		userDeleteButton.click();		
		
		// Wait until the user group page starts reloading, after clicking userDeleteButton (then the userDeleteButton element is stale)
		wait.until(ExpectedConditions.stalenessOf(userDeleteButton));
		// this page is refreshed; init elements once again
		PageFactory.initElements(driver, this);
		return this;
	}
	
	public UserGroupPage changeTitle(String newTitle) {
		editButton.click();
		try { Thread.sleep(2000); } catch (InterruptedException e) {}
		
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
		WebElement breadcrumb = driver.findElement(By.tagName("h1"));
		String headline = retryingNestedElement(breadcrumb, By.cssSelector("span:nth-of-type(3)")).getText();
		return headline.substring("User Group ".length());
	}

	
}
