package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.registered.Homepage;

public class RegistrationPage extends BasePage {

	@FindBy(css = ".imj_metadataSet:nth-of-type(1)>input")
	private WebElement emailTextField;
	
	@FindBy(id = "userForm:userPersonRegister:inputFamilyNameText1")
	private WebElement familyNameTextField;
	
	@FindBy(id = "userForm:userPersonRegister:inputGiveNameText")
	private WebElement givenNameTextField;
	
	@FindBy(xpath = "//input[contains(@id, 'OrgaName')]")
	private WebElement organizationTextField;
	
	@FindBy(id = "userForm:accept_terms")
	private WebElement termsCheckbox;
	
	@FindBy(css =".imj_submitButton")
	private WebElement submitRegistrationButton;
	
	@FindBy(xpath =".//*[@id='j_idt198:inputPasswd']")
	private WebElement passwordTextfield;
		
	@FindBy(xpath = ".//*[@id='j_idt198:lnkLogin']")
	private WebElement completeRegistrationButton;
	
	public RegistrationPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}	
	
	public void register(String email, String familyName, String givenName, String organization) {
		enterEmail(email);
		enterFamilyName(familyName);
		enterGivenName(givenName);
		enterOrganization(organization);
		acceptTerms();
		
		submitRegistration();
	}
	
	private void enterEmail(String email) {
		emailTextField.sendKeys(email);
	}
	
	private void enterFamilyName(String familyName) {
		familyNameTextField.sendKeys(familyName);
	}
	
	private void enterGivenName(String givenName) {
		givenNameTextField.sendKeys(givenName);
	}
	
	private void enterOrganization(String organization) {
		organizationTextField.sendKeys(organization);
	}
	
	private void acceptTerms() {
		if (!termsCheckbox.isSelected())
			termsCheckbox.click();
	}
	
	private void submitRegistration() {
		submitRegistrationButton.click();
	}
	
	public Homepage activateAccount(String spotPassword) {
		passwordTextfield.sendKeys(spotPassword);
		completeRegistrationButton.click();
		
		return PageFactory.initElements(driver, Homepage.class);
	}
}
