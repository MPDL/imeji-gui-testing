package spot.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.HomePage;

public class LoginPage extends BasePage {

	@FindBy(name ="Header:loginForm:j_idt29")
	private WebElement userNameTextField;
	
	@FindBy(name ="Header:loginForm:j_idt30")
	private WebElement passwordTextField;
		
	@FindBy(name = "Header:loginForm:lnkLogin2")
	private WebElement submitLoginInfoButton;
	
	public LoginPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	private <T> T login(String userName, String password, Class<T> expectedPage){
	    
		enterUserName(userName);
		enterPassword(password);
		clickOnSignIn();
	    return PageFactory.initElements(driver, expectedPage);
	}

	public AdminHomePage loginAsAdmin(String user, String pw){
	    return login(user, pw, AdminHomePage.class);
	}

	public HomePage loginAsNotAdmin(String user, String pw){
	    return login(user, pw, HomePage.class);
	}

	public LoginPage loginWithBadCredentials(String user, String pw){
	    return login(user, pw, LoginPage.class);
	}
	
	private void enterUserName(String userName) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='Header:loginForm']/span/input[1]")));
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
