package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class EmptyCollectionPage extends BasePage {

	@FindBy(css=".imj_contentMenu")
	private WebElement contentMenu;
	
	public EmptyCollectionPage(WebDriver driver) {
		super(driver);
	}

}
