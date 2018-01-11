package spot.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BrowseItemsPage;
import spot.pages.CollectionsPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.registered.Homepage;

public class MainMenuComponent {

	private WebDriver driver;
	
	@FindBy (id="lnkHome")
	private WebElement startButton;
	
	@FindBy (id="lnkCollections")
	private WebElement collectionsButton;
	
	@FindBy(id = "lnkItems")
	private WebElement itemsButton;
	
//	@FindBy (className="fa-upload")
//	private WebElement singleUploadButton;
	
	@FindBy (id="lnkAdmin")
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
//		else if (expectedPage == SingleUploadPage.class)
//			singleUploadButton.click();
		else if (expectedPage == AdministrationPage.class)
			adminButton.click();
		else if (expectedPage == Homepage.class || expectedPage == AdminHomepage.class)
			startButton.click();
		else if (expectedPage == BrowseItemsPage.class)
			itemsButton.click();
		
		return PageFactory.initElements(driver, expectedPage);
	}
	
}
