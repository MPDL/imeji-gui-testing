package spot.pages.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

public class NewUserGroupPage extends BasePage {

	@FindBy(css=".imj_admindataEdit")
	private WebElement newUserGroupNameTextField;
	
	@FindBy(css=".imj_submitButton")
	private WebElement saveButton;
	
	public NewUserGroupPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public UserGroupPage createNewUserGroup(String newUserGroupName) {
		newUserGroupNameTextField.sendKeys(newUserGroupName);
		
		((JavascriptExecutor) driver).executeScript("document.querySelector('.imj_metadataValueEntry .imj_submitButton').click();");
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[contains(@id,'EditUserdata')]")));
		
		return PageFactory.initElements(driver, UserGroupPage.class);
	}
}
