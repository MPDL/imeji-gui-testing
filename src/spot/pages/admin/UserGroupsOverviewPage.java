package spot.pages.admin;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

public class UserGroupsOverviewPage extends BasePage {

	@FindBy(css=".imj_setupConfig>.imj_itemContent")
	private List<WebElement> userGroupList;
	
	public UserGroupsOverviewPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public UserGroupsOverviewPage deleteUserGroupByName(String userGroupName) {
		if (!isNewUserGroupPresent(userGroupName))
			throw new NoSuchElementException("User group with this title does not exist.");
		
		
		WebElement toBeDeletedUserGroup = findUserGroupByName(userGroupName);
		WebElement deleteButton = toBeDeletedUserGroup.findElement(By.cssSelector(".imj_adminEditPanel>.imj_cancelButton"));
		deleteButton.click();
			
		wait.until(ExpectedConditions.visibilityOf(toBeDeletedUserGroup));
			
		// TODO delete currently dependent on number in code
		WebElement confirmDelete = toBeDeletedUserGroup.findElement(By.cssSelector("div[id^=deleteUserGroup] .imj_submitPanel form .imj_submitButton"));

		wait.until(ExpectedConditions.visibilityOf(confirmDelete));
		confirmDelete.click();
		
		return PageFactory.initElements(driver, UserGroupsOverviewPage.class);
	}
	
	private WebElement findUserGroupByName(String userGroupNameInQuestion) {
		for (WebElement userGroup : userGroupList) {
			WebElement userGroupIdentification = userGroup.findElement(By.className("imj_admindataLabel"));
			String userGroupName  = userGroupIdentification.getText();
			
			if (userGroupName.equals(userGroupNameInQuestion)) {
				return userGroup;
			}
		}

		throw new NoSuchElementException("User group " + userGroupNameInQuestion + " is not present.");
	}

	public boolean isNewUserGroupPresent(String newUserGroupName) {
		try {
			findUserGroupByName(newUserGroupName);
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}

	public boolean isNewUserGroupOnTop(String newUserGroupNameInQuestion) {
		boolean isNewUserGroupOnTopOfList = false;
		
		WebElement userGroupOnTopOfList = userGroupList.get(0);
		
		WebElement userGroupIdentification = userGroupOnTopOfList.findElement(By.className("imj_admindataLabel"));
		String userGroupName  = userGroupIdentification.getText();
		
		if (userGroupName.equals(newUserGroupNameInQuestion)) {
			isNewUserGroupOnTopOfList = true;
		}
		
		return isNewUserGroupOnTopOfList;
	}

	public UserGroupPage viewUserGroupDetails(String userGroupNameInQuestion) {
		WebElement userGroupInQuestion = findUserGroupByName(userGroupNameInQuestion);
		
		WebElement viewButton = userGroupInQuestion.findElement(By.cssSelector(".imj_admindataValue>.imj_menuButton:nth-of-type(1)"));
		viewButton.click();
		
		return PageFactory.initElements(driver, UserGroupPage.class);
	}
}
