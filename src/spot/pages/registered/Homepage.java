package spot.pages.registered;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.CollectionsPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;

/**
 * HomePage is the page after successful login for non-admin users. For
 * logged-in admins see subclass AdminHomePage
 * 
 * @author kocar
 *
 */
public class Homepage extends BasePage {

	@FindBy(id = "txtAccountUserName")
	private WebElement goToUserProfileButton;

	@FindBy(id = "lnkLogout")
	private WebElement logoutButton;

	public Homepage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public SingleUploadPage goToSingleUploadPage() {
		StartPage startPage = goToStartPage();
		return startPage.goToSingleUploadPage();
	}
	
	public void logout() {
		logoutButton.click();
	}

	public NewCollectionPage goToCreateNewCollectionPage() {
		CollectionsPage collections = goToCollectionPage();
		
		return collections.createCollection();
	}
	
	public String getLoggedInUserFullName() {
		return goToUserProfileButton.getText();
	}


}
