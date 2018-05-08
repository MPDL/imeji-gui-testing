package spot.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;

public class NewUserPage extends BasePage {

	@FindBy(id="userForm:inputEmailText")
	private WebElement emailTextField;
	
	@FindBy(id="userForm:userPerson:inputFamilyNameText")
	private WebElement familyNameTextField;
	
	@FindBy(id="userForm:userPerson:inputGiveNameText")
	private WebElement givenNameTextField;
	
	@FindBy(xpath="//input[contains(@id, 'inputOrgaName')]")
	private WebElement organizationNameTextField;
	
	@FindBy(css=".imj_content>div:nth-last-child(2) input")
	private WebElement canCreateNewCollectionCheckBox;
	
	@FindBy(css=".imj_content>div:nth-last-child(1) input")
	private WebElement sendEmailCheckBox;
	
	@FindBy(css=".imj_submitButton")
	private WebElement saveButton;
	
	public NewUserPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public UserProfilePage createNewUser(String newUserName) {
		fillPersonalData(newUserName);
		fillOrganizationData();
		
		// user shall receive email
		if (!sendEmailCheckBox.isSelected())
			sendEmailCheckBox.click();
		
		// user shall be able to create collections
		if (!canCreateNewCollectionCheckBox.isSelected())
			canCreateNewCollectionCheckBox.click();
		
		saveButton.click();
		
		return PageFactory.initElements(driver, UserProfilePage.class);
	}
	
	public UserProfilePage createdNewRestrictedUser(String newUserName) {
		fillPersonalData(newUserName);
		fillOrganizationData();
		
		// restricted user cannot create collections
		if (canCreateNewCollectionCheckBox.isSelected())
			canCreateNewCollectionCheckBox.click();
		
		saveButton.click();
		return PageFactory.initElements(driver, UserProfilePage.class);
	}

	private void fillPersonalData(String newUserName) {
		emailTextField.sendKeys(newUserName);
		familyNameTextField.sendKeys("Testuser");
		givenNameTextField.sendKeys("Tester");
	}
	
	private void fillOrganizationData() {
		organizationNameTextField.sendKeys("MPDL");
	}

}
