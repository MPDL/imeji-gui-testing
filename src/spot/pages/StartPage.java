package spot.pages;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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

	@FindBy(id = "lnkshowLogin")
	private WebElement openLoginFormButton;
	
	@FindBy(css = "#headerMenu>div>a:nth-of-type(2)")
	private WebElement registrationButton;
	
	@FindBy(className = "fa-upload")
	private WebElement uploadButton;
	
	public StartPage(WebDriver driver) {
		super(driver);
				
		PageFactory.initElements(this.driver, this);
	}	
	
	// IMJ-1, IMJ-22
	public LoginPage openLoginForm() {
		try {
			openLoginFormButton.click();	
		} 
		catch (NoSuchElementException e) {
			List<WebElement> logoutButtonList = driver.findElements(By.id("lnkLogout"));
			
			if(logoutButtonList.isEmpty()) {
				e.printStackTrace();
			}else {
				log4j.error("A User is already logged in. Logging out the current User and opening login form.");
				
				WebElement logoutButton = logoutButtonList.get(0);
				logoutButton.click();
				this.hideMessages();
				
				openLoginFormButton.click();
			}			
		}
		
		return PageFactory.initElements(driver, LoginPage.class);
	}
	
	public RegistrationPage goToRegistrationPage() {
		registrationButton.click();		
		return PageFactory.initElements(driver, RegistrationPage.class);
	}
	
	/**
	 * Is the button that opens the login form present?
	 * @return
	 */
	public boolean isOpenLoginFormButtonPresent() {
		try {
			openLoginFormButton.getTagName();
			return true;
		} 
		catch (NoSuchElementException exception) {
			return false;
		}
	}
}
