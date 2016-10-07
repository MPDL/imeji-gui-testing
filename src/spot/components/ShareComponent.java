package spot.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.CategoryType;
import spot.pages.BasePage;
import spot.pages.KindOfSharePage;

public class ShareComponent extends BasePage {

	@FindBy(id = "sharingMenu")
	private WebElement shareButton;
	
	@FindBy(className = "fa-users")
	private WebElement shareIcon;
	
	@FindBy(id = "action:actionMenuShareCollection")
	private WebElement shareCollection;
	
	@FindBy(id = "action:actionMenuShareAlbum")
	private WebElement shareAlbum;
	
	@FindBy(id = "action:actionMenuShareItem")
	private WebElement shareItem;
	
	public ShareComponent (WebDriver driver) {
		super(driver);

		PageFactory.initElements(driver, this);
	}
	
	public KindOfSharePage share(CategoryType category) {
		shareButton.click();
		PageFactory.initElements(driver, this);
		switch(category) {
			case COLLECTION:
				shareCollection.click();
				break;
			case ALBUM:
				shareAlbum.click();
				break;
			case ITEM:
				shareItem.click();
				break;
		}
		
		return PageFactory.initElements(driver, KindOfSharePage.class);
	}
}
