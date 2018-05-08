package spot.pages.registered;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;

public class DiscardedCollectionEntryPage extends BasePage {

	@FindBy(css=".fa-minus-circle")
	private WebElement discardedIcon;
	
	@FindBy(className = "imj_tileItem")
	private List<WebElement> itemList;
	
	public DiscardedCollectionEntryPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public boolean discardedIconDisplayed() {
		return discardedIcon.isDisplayed();
	}
	
	public boolean noItemsDisplayed() {
		return itemList.size() == 0;
	}

}
