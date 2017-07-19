package spot.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.AdministrationPage;
import spot.pages.CollectionsPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;

public class MainMenuComponent {

	private WebDriver driver;
	
	@FindBy (name="Header:lnkStartPage")
	private WebElement startButton;
	
	@FindBy (name = "Header:mainMenu:lnkAlbums")
	private WebElement albumsButton;
	
	@FindBy (id="Header:mainMenu:lnkCollections")
	private WebElement collectionsButton;
	
	@FindBy (id="upload")
	private WebElement singleUploadButton;
	
	@FindBy (name="Header:mainMenu:lnkAdmin")
	private WebElement adminButton;
	
	public MainMenuComponent(WebDriver driver) {
		this.driver = driver;
		
		PageFactory.initElements(driver, this);
	}
	
	public <T> T navigateTo(Class<T> expectedPage) {
	
		if (expectedPage == CollectionsPage.class)
			collectionsButton.click();
		else if (expectedPage == StartPage.class)
			startButton.click();
		else if (expectedPage == SingleUploadPage.class)
			singleUploadButton.click();
		else if (expectedPage == AdministrationPage.class)
			adminButton.click();
		else if (expectedPage == Homepage.class || expectedPage == AdminHomepage.class)
			startButton.click();
		
		return PageFactory.initElements(driver, expectedPage);
	}
	
}
