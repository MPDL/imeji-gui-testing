package spot.pages;

import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * 
 * Start Page for guests with the possibility to
 * -> login
 * -> register
 * -> ...
 * 
 * @author kocar
 *
 */
public class StartPage extends BasePage {

	private static final Logger log4j = LogManager.getLogger(StartPage.class.getName());
	
	@FindBy(xpath =".//*[@id='Header:loginForm:lnkLogin1']")
	private WebElement openLoginFormButton;
	
	@FindBy(xpath =".//*[@id='Header:j_idt38']/a")
	private WebElement registrationButton;
	
	public StartPage(WebDriver driver) {
		super(driver);
				
		PageFactory.initElements(driver, this);
	}	
	
	public LoginPage openLoginForm() {
		openLoginFormButton.click();
		
		return PageFactory.initElements(driver, LoginPage.class);
	}
	
	public RegistrationPage goToRegistrationPage() {
		registrationButton.click();		
		return PageFactory.initElements(driver, RegistrationPage.class);
	}
	
	/**
	 * Is the button that opens the login form, present?
	 * @return
	 */
	public boolean isOpenLoginFormButtonPresent() {
		
		try {
			openLoginFormButton.getTagName();
			return true;
		} catch (NoSuchElementException exception) {
			return false;
		}
		
	}
}
