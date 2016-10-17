package spot.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;

public class EditUserGroupPage extends BasePage {

	@FindBy(name = "userForm:j_idt334")
	private WebElement titleBox;
	
	@FindBy(name = "userForm:j_idt338")
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
