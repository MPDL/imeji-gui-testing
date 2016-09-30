package spot.pages.notAdmin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.AlbumEntryPage;
import spot.pages.BasePage;

public class NewAlbumPage extends BasePage {

	@FindBy(css="#editContainer\\:mediaContainerForm\\:inputTitleText")
	private WebElement titleTextField;
	
	@FindBy(css="#editContainer\\:mediaContainerForm\\:inputDescription")
	private WebElement descriptionTextField;
	
	@FindBy(css=".imj_submitButton")
	private WebElement saveButton;
	
	public NewAlbumPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public AlbumEntryPage createAlbum(String albumTitle, String albumDescription) {
		fillForm(albumTitle, albumDescription);
		
		saveButton.click();
		
		return PageFactory.initElements(driver, AlbumEntryPage.class);
	}

	private void fillForm(String albumTitle, String albumDescription) {
		titleTextField.sendKeys(albumTitle);
		descriptionTextField.sendKeys(albumDescription);
	}
}
