package spot.pages.registered;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.CollectionsPage;

/**
 * HomePage is the page after successful login for non-admin users. For
 * logged-in admins see subclass AdminHomePage
 * 
 * @author kocar
 *
 */
//TODO: Remove the HomePage class! Put its functionality in BasePage
public class Homepage extends BasePage {

	@FindBy(id = "txtAccountUserName")
	private WebElement goToUserProfileButton;

	@FindBy(id = "lnkLogout")
	private WebElement logoutButton;

	public Homepage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	// IMJ-2
	public void logout() {
		logoutButton.click();
		this.hideMessages();
	}

	// IMJ-112, IMJ-113
	public NewCollectionPage goToCreateNewCollectionPage() {
		CollectionsPage collections = goToCollectionPage();
		return collections.createCollection();
	}
	
	public String getLoggedInUserFullName() {
		return goToUserProfileButton.getText();
	}

}
