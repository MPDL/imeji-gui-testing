package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditAlbumPage extends BasePage {

	@FindBy(id = "editContainer:mediaContainerForm:inputTitleText")
	private WebElement albumTitleBox;
	
	@FindBy(id = "editContainer:mediaContainerForm:btn_saveCollection")
	private WebElement saveButton;
	
	public EditAlbumPage(WebDriver driver) {
		super(driver);
	}
	
	public void changeTitle(String newTitle) {
		albumTitleBox.clear();
		albumTitleBox.sendKeys(newTitle);
	}
	
	public AlbumEntryPage saveChanges() {
		saveButton.click();
		
		return PageFactory.initElements(driver, AlbumEntryPage.class);
	}
}
