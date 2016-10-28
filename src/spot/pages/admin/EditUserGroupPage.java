package spot.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;

public class EditUserGroupPage extends BasePage {

	@FindBy(css = "#userForm .imj_admindataEdit")
	private WebElement titleBox;
	
	@FindBy(css = "#userForm .imj_userGlobalInformation .imj_submitButton")
	private WebElement saveButton;
	
	public EditUserGroupPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public UserGroupPage editTitle(String newTitle) {
		titleBox.clear();
		titleBox.sendKeys(newTitle);
		saveButton.click();
		
		return PageFactory.initElements(driver, UserGroupPage.class);
	}
}
