package spot.pages.registered;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.SingleUploadPage;

/**
 * HomePage is the page after successful login for non-admin users. For
 * logged-in admins see subclass AdminHomePage
 * 
 * @author kocar
 *
 */
public class Homepage extends BasePage {

	@FindBy(xpath = ".//*[@id='Header:txtAccountUserName']")
	private WebElement goToUserProfileButton;

	@FindBy(xpath = ".//*[@id='Header:loginForm:lnkLogout']")
	private WebElement logoutButton;

	@FindBy(id = "createCollection")
	private WebElement createNewCollectionButton;

	public Homepage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public SingleUploadPage goToUploadPage() {
		
		return mainMenuComponent.navigateTo(SingleUploadPage.class);
	}
	
	public void logout() {
		logoutButton.click();
	}

	public NewCollectionPage goToCreateNewCollectionPage() {
		createNewCollectionButton.click();
		
		return PageFactory.initElements(driver, NewCollectionPage.class);
	}
	
	public String getLoggedInUserFullName() {
		return goToUserProfileButton.getText();
	}


}
