package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchQueryPage extends BasePage {

	@FindBy(xpath="html/body/div[1]/div[4]/div[1]")
	private WebElement searchQueryDisplay;
	
	@FindBy(id="quickSearchString")
	private WebElement searchQueryKeyWord;
	
	public SearchQueryPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public String getSearchQueryDisplayText() {
		return searchQueryKeyWord.getAttribute("value");
	}
	
	/*public String getSearchQueryDisplayText() {
		String searchQueryDisplayText="";
		String tmp = searchQueryDisplay.getText().trim();
		String[] split = tmp.split("\\s+");
		// sth like e.g. albums: or collections:
		String searchCategory = split[2];
		// sth like albums:(test)
		searchQueryDisplayText = searchCategory + searchQueryKeyWord.getText().trim();
		return searchQueryDisplayText;
	}*/

}
