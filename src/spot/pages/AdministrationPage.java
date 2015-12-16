package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AdministrationPage extends BasePage {

	@FindBy(css=".imj_listBody>div:nth-of-type(1) li:nth-of-type(1)>a")
	private WebElement createNewUser;
	
	@FindBy(css=".imj_listBody>div:nth-of-type(1) li:nth-of-type(4)>a")
	private WebElement createNewUserGroup;
	
	public AdministrationPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public AllUsersOverViewPage createNewUser(String newUserName) {
		
		createNewUser.click();
		
		CreateNewUserPage createNewUserPage = new CreateNewUserPage(driver);
		
		return createNewUserPage.createNewUser(newUserName);
	}

	public AllUserGroupsOverViewPage createNewUserGroup(String newUserGroupName) {
		
		createNewUserGroup.click();
		
		CreateNewUserGroupPage createNewUserGroupPage = new CreateNewUserGroupPage(driver);
		
		return createNewUserGroupPage.createNewUserGroup(newUserGroupName);
	}
}
