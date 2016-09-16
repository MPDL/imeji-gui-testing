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

}
