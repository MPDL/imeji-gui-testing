package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CreateNewUserGroupPage extends BasePage {

	@FindBy(css=".imj_admindataEdit")
	private WebElement newUserGroupNameTextField;
	
	@FindBy(css=".imj_submitButton")
	private WebElement saveButton;
	
	public CreateNewUserGroupPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public AllUserGroupsOverViewPage createNewUserGroup(String newUserGroupName) {
		newUserGroupNameTextField.sendKeys(newUserGroupName);
		
		saveButton.click();
		
		return PageFactory.initElements(driver, AllUserGroupsOverViewPage.class);
	}
}
