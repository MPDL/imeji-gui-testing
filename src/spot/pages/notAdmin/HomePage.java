package spot.pages.notAdmin;

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
public class HomePage extends BasePage {

	@FindBy(xpath = ".//*[@id='Header:txtAccountUserName']")
	private WebElement goToUserProfileButton;

	@FindBy(xpath = ".//*[@id='Header:loginForm:lnkLogout']")
	private WebElement logoutButton;

	@FindBy(xpath = "html/body/div[1]/div[2]/div/div[2]/ul/li[1]/a")
	private WebElement createNewCollectionButton;

	@FindBy(xpath = "html/body/div[1]/div[2]/div/div[2]/ul/li[2]/a")
	private WebElement createNewAlbumButton;

	public HomePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public SingleUploadPage goToUploadPage() {
		
		return mainMenuComponent.navigateTo(SingleUploadPage.class);
	}
	
	public void logout() {
		logoutButton.click();
	}

//	public CreateNewCollectionPage createNewCollection() {
//		clickCreateNewButton();
//		clickCreateNewCollectionButton();
//		
//		return PageFactory.initElements(driver, CreateNewCollectionPage.class);
//	}
//	
//	public void createNewAlbum() {
//		clickCreateNewButton();
//		clickCreateNewAlbumButton();
//	}
	
//	private void clickCreateNewButton() {
//		createNewButton.click();
//	}
	
	public String getLoggedInUserFullName() {
		return goToUserProfileButton.getText();
	}

	private void clickCreateNewAlbumButton() {
		createNewAlbumButton.click();
	}

	private void clickCreateNewCollectionButton() {
		createNewCollectionButton.click();
	}
}
