package spot.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.HomePage;

public class LoginPage extends BasePage {

	private static final Logger log4j = LogManager.getLogger(LoginPage.class.getName());
	
	@FindBy(xpath =".//*[@id='Header:loginForm']/span/input[1]")
	private WebElement userNameTextField;
	
	@FindBy(xpath =".//*[@id='Header:loginForm']/span/input[2]")
	private WebElement passwordTextField;
		
	@FindBy(xpath =".//*[@id='Header:loginForm:lnkLogin']")
	private WebElement submitLoginInfoButton;
	
	public LoginPage(WebDriver driver) {
		super(driver);		
	}

	private <T> T login(String userName, String password, Class<T> expectedPage){
	    
		ElementLocatorFactory elementLocatorFactory =  new AjaxElementLocatorFactory(driver, 5);
		PageFactory.initElements(elementLocatorFactory, this);
		
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
		userNameTextField.sendKeys(userName);
	}
	
	private void enterPassword(String password) {
		passwordTextField.sendKeys(password);
	}
	
	private void clickOnSignIn() {
		submitLoginInfoButton.click();
	}
	
	/*
	public void login(String userName, String password) {
		
		ElementLocatorFactory elementLocatorFactory =  new AjaxElementLocatorFactory(driver, 5);
		PageFactory.initElements(elementLocatorFactory, this);
		
		enterUserName(userName);
		enterPassword(password);
		clickOnSignIn();		
	}
	
	*/
}