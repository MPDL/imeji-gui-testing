package spot.pages;

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

	@FindBy(id ="Header:loginForm")
	private WebElement openLoginFormButton;
	
	@FindBy(xpath =".//*[@id='Header:j_idt38']/a")
	private WebElement registrationButton;
	
	@FindBy(css="#columnEN-2 a:nth-of-type(2)")
	private WebElement contactEdmondsupportLink;
	
	public StartPage(WebDriver driver) {
		super(driver);
				
		PageFactory.initElements(this.driver, this);
	}	
	
	public LoginPage openLoginForm() {
		
		try {
			openLoginFormButton.click();	
		} catch (NoSuchElementException e) {
			// the login form is already visible
			// do nothing
		}
		
		return PageFactory.initElements(driver, LoginPage.class);
	}
	
	public RegistrationPage goToRegistrationPage() {
		registrationButton.click();		
		return PageFactory.initElements(driver, RegistrationPage.class);
	}
	
	public String contactEdmondSupport() {
		String href = contactEdmondsupportLink.getAttribute("href");		

		return extractMailDestinationAddressFromLink(href);
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
