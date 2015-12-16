package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AllUserGroupsOverViewPage extends BasePage {

	@FindBy(css=".imj_admindataSet")
	private List<WebElement> userGroupList;
	
	public AllUserGroupsOverViewPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public void deleteUserGroupByName(String userGroupName) {
		//TODO delete user group by name
	}
	
	private WebElement findUserGroupByName(String userGroupNameInQuestion) {
		WebElement userGroupInQuestion = null;
		
		for (WebElement userGroup : userGroupList) {
			WebElement userGroupIdentification = userGroup.findElement(By.cssSelector("h2"));
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
