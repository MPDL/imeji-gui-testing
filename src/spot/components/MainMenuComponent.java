package spot.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.AlbumPage;
import spot.pages.CollectionsPage;
import spot.pages.DetailedItemViewPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;

public class MainMenuComponent {

	private WebDriver driver;
	
	@FindBy (name="Header:mainMenu:lnkHome")
	private WebElement startButton;
	
	@FindBy (xpath=".//*[@id='Header:mainMenu:lnkBrowse']")
	private WebElement itemsButton;
	
	@FindBy (name="Header:mainMenu:lnkCollections")
	private WebElement collectionsButton;
	
	@FindBy (name="Header:mainMenu:lnkAlbums")
	private WebElement albumsButton;
	
	@FindBy (name="Header:mainMenu:lnkUpload")
	private WebElement singleUploadButton;
	
	public MainMenuComponent(WebDriver driver) {
		this.driver = driver;
		
		PageFactory.initElements(driver, this);
	}
	
	public <T> T navigateTo(Class<T> expectedPage) {
	
		if (expectedPage == DetailedItemViewPage.class)
			itemsButton.click();
		else if (expectedPage == AlbumPage.class)
			albumsButton.click();
		else if (expectedPage == CollectionsPage.class)
			collectionsButton.click();
		else if (expectedPage == StartPage.class)
			startButton.click();
		else if (expectedPage == SingleUploadPage.class)
			singleUploadButton.click();
		
		return PageFactory.initElements(driver, expectedPage);
	}
}
