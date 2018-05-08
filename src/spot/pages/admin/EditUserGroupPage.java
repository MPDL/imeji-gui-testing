package spot.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

public class EditUserGroupPage extends BasePage {

	@FindBy(css = ".imj_metadataSetEdit>input")
	private WebElement titleBox;
	
	@FindBy(css = ".imj_submitButton")
	private WebElement saveButton;
	
	public EditUserGroupPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public UserGroupPage editTitle(String newTitle) {
		wait.until(ExpectedConditions.visibilityOf(titleBox));
		titleBox.clear();
		titleBox.sendKeys(newTitle);
		saveButton.click();
		try { Thread.sleep(2000); } catch (InterruptedException e) {}
		
		return PageFactory.initElements(driver, UserGroupPage.class);
	}
}
