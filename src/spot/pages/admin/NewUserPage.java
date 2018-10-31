package spot.pages.admin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
	
	@FindBy(css=".imj_content>div:nth-last-child(3) input")
	private WebElement canCreateNewCollectionCheckBox;
	
	@FindBy(css=".imj_content>div:nth-last-child(2) input")
	private WebElement sendEmailCheckBox;
	
	@FindBy(css=".imj_submitButton")
	private WebElement saveButton;
	
	public NewUserPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public UserProfilePage createNewUser(String newUserEmailAddress) {
		fillPersonalData(newUserEmailAddress);
		fillOrganizationData();
		
		saveButton.click();
		
		return PageFactory.initElements(driver, UserProfilePage.class);
	}
	
	public UserProfilePage createNewUser(String emailAddress, String familyName, String givenName) {
		fillPersonalData(emailAddress, familyName, givenName);
		fillOrganizationData();
		
		saveButton.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userForm:lnkEditUserdata")));
		
		return PageFactory.initElements(driver, UserProfilePage.class);
	}
	
	public UserProfilePage createdNewRestrictedUser(String newUserEmailAddress) {
		fillPersonalData(newUserEmailAddress);
		fillOrganizationData();
		
		// restricted user cannot create collections
		if (canCreateNewCollectionCheckBox.isSelected())
			canCreateNewCollectionCheckBox.click();
		
		saveButton.click();
		return PageFactory.initElements(driver, UserProfilePage.class);
	}

	private void fillPersonalData(String newUserEmailAddress) {
		emailTextField.sendKeys(newUserEmailAddress);
		familyNameTextField.sendKeys("Testuser");
		givenNameTextField.sendKeys("Tester");
	}
	
	private void fillPersonalData(String emailAddress, String familyName, String givenName) {
		emailTextField.sendKeys(emailAddress);
		familyNameTextField.sendKeys(familyName);
		givenNameTextField.sendKeys(givenName);
	}
	
	private void fillOrganizationData() {
		organizationNameTextField.sendKeys("MPDL");
	}

}
