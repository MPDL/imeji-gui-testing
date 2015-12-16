package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class EditUserProfilePage extends BasePage {

	@FindBy(css="#userForm\\:userInfos>div:nth-of-type(2) input")	
	private WebElement userFamilyNameTextField;
	
	@FindBy(css=".imj_organisation>div:nth-of-type(1) input")
	private WebElement organizationNameTextField;
	
	@FindBy(css="#userForm .imj_submitButton")
	private WebElement saveButton;
	
	public EditUserProfilePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public UserProfilePage changeUserFamilyName(String newFamilyName) {
		
		wait.until(ExpectedConditions.visibilityOf(userFamilyNameTextField));
		userFamilyNameTextField.clear();
		userFamilyNameTextField.sendKeys(newFamilyName);
		save();
		
		return PageFactory.initElements(driver, UserProfilePage.class);
	}

	private void save() {
		saveButton.click();
	}

	public void changeOrganizationUnit() {
		organizationNameTextField.clear();
		organizationNameTextField.sendKeys("");
	}
}
