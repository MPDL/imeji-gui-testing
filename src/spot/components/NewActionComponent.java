package spot.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NewActionComponent {

	private WebDriver driver;
	
	@FindBy(css="#actionsMenuArea .imj_headerEntry")
	private WebElement newButton;
	
	@FindBy(css=".imj_overlayMenuList>li:nth-of-type(1)>a")
	private WebElement newCollectionDropBoxEntry;
	
	@FindBy(css=".imj_overlayMenuList>li:nth-of-type(2)>a")
	private WebElement newAlbumDropBoxEntry;
	
	public NewActionComponent(WebDriver driver) {
		this.driver = driver;

		PageFactory.initElements(driver, this);
	}
	
	public void clickCreateNewCollection() {
		newButton.click();
		newCollectionDropBoxEntry.click();
	}
	
	public void clickCreateNewAlbum() {
		newButton.click();
		newAlbumDropBoxEntry.click();
	}
	
	public WebElement getNewButton() {
		return newButton;
	}
}
