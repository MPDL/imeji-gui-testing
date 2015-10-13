package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.notAdmin.HomePage;

public class RegistrationPage extends BasePage {

	@FindBy(xpath =".//*[@id='userForm']/div[1]/div[2]/div/input")
	private WebElement emailTextField;
	
	@FindBy(xpath =".//*[@id='userForm:userPersonRegister:inputFamilyNameText1']")
	private WebElement familyNameTextField;
	
	@FindBy(xpath =".//*[@id='userForm:userPersonRegister:inputGiveNameText1']")
	private WebElement givenNameTextField;
	
	@FindBy(xpath =".//*[@id='userForm:userPersonRegister:j_idt145:0:inputOrgaName']")
	private WebElement organizationTextField;
	
	@FindBy(xpath =".//*[@id='userForm:j_idt179']")
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
	
	private void submitRegistration() {
		submitRegistrationButton.click();
	}
	
	public HomePage activateAccount(String spotPassword) {
		passwordTextfield.sendKeys(spotPassword);
		completeRegistrationButton.click();
		
		return PageFactory.initElements(driver, HomePage.class);
	}
}
