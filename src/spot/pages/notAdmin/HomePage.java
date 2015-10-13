package spot.pages.notAdmin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import spot.pages.BasePage;

/**
 * HomePage is the page after successful login for non-admin users.
 * For logged-in admins see subclass AdminHomePage 
 * 
 * @author kocar
 *
 */
public class HomePage extends BasePage {

	@FindBy(xpath =".//*[@id='Header:txtAccountUserName']")
	private WebElement goToUserProfileButton;
	
	@FindBy(xpath =".//*[@id='Header:loginForm:lnkLogout']")
	private WebElement logoutButton;
	
	
	
	public HomePage(WebDriver driver) {
		super(driver);
	}

	public void logout() {
		logoutButton.click();
	}
	
	public String getLoggedInUserFullName() {
		return goToUserProfileButton.getText();
	}
}
