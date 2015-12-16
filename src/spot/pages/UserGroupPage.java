package spot.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class UserGroupPage extends BasePage {

	@FindBy(css=".imj_admindataSetEdit .imj_submitButton")
	private WebElement addUserButton;
	
	@FindBy(css=".imj_userGrantList")
	private List<WebElement> alreadyAddedUsers;
	
	public UserGroupPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public UserGroupPage addNewUser(String userEmail) {
		addUserButton.click();
		AllUsersOverViewPage allUsersOverViewPage = PageFactory.initElements(driver, AllUsersOverViewPage.class);
		allUsersOverViewPage.addUserToUserGroup(userEmail);
			
		// this page is refreshed; init elements once again
		PageFactory.initElements(driver, this);
		
		return this;
	}

	public boolean isNewlyAddedUserPresent(String userEmail) {
		
		boolean isNewlyAddedUserPresent = false;
		
		for (WebElement alreadyAddedUser : alreadyAddedUsers) {
			String userNamePlusEmail = alreadyAddedUser.getText();
			if (userNamePlusEmail.contains(userEmail)) {
				isNewlyAddedUserPresent = true;
				break;
			}
		}
		
		return isNewlyAddedUserPresent;
	}

	
}
