package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AllUserGroupsOverViewPage extends BasePage {

	@FindBy(css=".imj_admindataSet")
	private List<WebElement> userGroupList;
	
	public AllUserGroupsOverViewPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public AllUserGroupsOverViewPage deleteUserGroupByName(String userGroupName) {
		
		if (isNewUserGroupPresent(userGroupName)) {
			WebElement toBeDeletedUserGroup = findUserGroupByName(userGroupName);
			WebElement deleteButton = toBeDeletedUserGroup.findElement(By.cssSelector(".imj_adminEditPanel>.imj_cancelButton"));
			deleteButton.click();
			
			wait.until(ExpectedConditions.visibilityOf(toBeDeletedUserGroup));
			
			// TODO delete currently dependent on number in code
			WebElement confirmDelete = toBeDeletedUserGroup.findElement(By.cssSelector("div[id^=deleteUserGroup] .imj_submitPanel form .imj_submitButton"));

			wait.until(ExpectedConditions.visibilityOf(confirmDelete));
			confirmDelete.click();
		}
		
		return PageFactory.initElements(driver, AllUserGroupsOverViewPage.class);
	}
	
	private WebElement findUserGroupByName(String userGroupNameInQuestion) {
		WebElement userGroupInQuestion = null;
		
		for (WebElement userGroup : userGroupList) {
			WebElement userGroupIdentification = userGroup.findElement(By.className("imj_headline"));
			String userGroupName  = userGroupIdentification.getText();
			
			if (userGroupName.equals(userGroupNameInQuestion)) {
				userGroupInQuestion = userGroup;
				break;
			}
		}
		return userGroupInQuestion;
	}

	public boolean isNewUserGroupPresent(String newUserGroupName) {
		boolean isNewUserGroupPresent = false;
		
		if (findUserGroupByName(newUserGroupName) != null) {
			isNewUserGroupPresent = true;
		}
		
		return isNewUserGroupPresent;
	}

	public boolean isNewUserGroupListenOnTopOfList(String newUserGroupNameInQuestion) {
		boolean isNewUserGroupOnTopOfList = false;
		
		WebElement userGroupOnTopOfList = userGroupList.get(0);
		
		WebElement userGroupIdentification = userGroupOnTopOfList.findElement(By.cssSelector("h2"));
		String userGroupName  = userGroupIdentification.getText();
		
		if (userGroupName.equals(newUserGroupNameInQuestion)) {
			isNewUserGroupOnTopOfList = true;
		}
		
		return isNewUserGroupOnTopOfList;
	}

	public UserGroupPage viewUserGroupDetails(String userGroupNameInQuestion) {
		WebElement userGroupInQuestion = findUserGroupByName(userGroupNameInQuestion);
		
		WebElement viewButton = userGroupInQuestion.findElement(By.cssSelector(".imj_adminEditPanel>.imj_editButton"));
		viewButton.click();
		
		return PageFactory.initElements(driver, UserGroupPage.class);
	}
}
