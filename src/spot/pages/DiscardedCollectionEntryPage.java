package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DiscardedCollectionEntryPage extends BasePage {

	@FindBy(css=".fa-minus-circle")
	private WebElement discardedIcon;
	
	public DiscardedCollectionEntryPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public boolean isDiscarded() {
		return discardedIcon.isDisplayed();
	}

}
