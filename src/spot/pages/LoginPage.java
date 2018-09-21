package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;

public class LoginPage extends BasePage {

	@FindBy(xpath = "//input[contains(@id, 'inputEmail')]")
	private WebElement userNameTextField;
	
	@FindBy(xpath = "//input[contains(@id, 'inputPassword')]")
	private WebElement passwordTextField;
		
	@FindBy(xpath = "//input[contains(@id, 'lnkLogin')]")
	private WebElement submitLoginInfoButton;
	
	public LoginPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	private <T> T login(String userName, String password, Class<T> expectedPage){
	    
		enterUserName(userName);
		enterPassword(password);
		clickOnSignIn();
		this.hideMessages();
	    return PageFactory.initElements(driver, expectedPage);
	}

	// IMJ-21
	public AdminHomepage loginAsAdmin(String user, String pw){
	    return login(user, pw, AdminHomepage.class);
	}

	// IMJ-1, IMJ-22, IMJ-19
	public Homepage loginAsNotAdmin(String user, String pw){
	    return login(user, pw, Homepage.class);
	}
	
	public Homepage loginRestricted(String user, String pw){
	    return login(user, pw, Homepage.class);
	}

	public LoginPage loginWithBadCredentials(String user, String pw){
	    return login(user, pw, LoginPage.class);
	}
	
	private void enterUserName(String userName) {
		wait.until(ExpectedConditions.visibilityOf(userNameTextField));
		userNameTextField.sendKeys(userName);
	}
	
	private void enterPassword(String password) {
		passwordTextField.sendKeys(password);
	}
	
	private void clickOnSignIn() {
		submitLoginInfoButton.click();
	}
	
	public boolean loginFormIsOpen() {
		wait.until(ExpectedConditions.visibilityOf(userNameTextField));
		return userNameTextField.isDisplayed() && passwordTextField.isDisplayed() && submitLoginInfoButton.isEnabled();
	}
	
	
}
