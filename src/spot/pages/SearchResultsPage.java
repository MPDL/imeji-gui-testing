package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchResultsPage extends BasePage {

	@FindBy(className="imj_searchQueryText")
	private WebElement searchQueryDisplay;
	
	@FindBy(id="quickSearchString")
	private WebElement searchQueryKeyWord;
	
	@FindBy(xpath = "/html/body/div[1]/div[1]/div[6]/div/div[2]/form/div[1]")
	private WebElement resultCount;
	
	@FindBy(xpath = "//div[contains(@id, 'filterInfoPanel')]")
	private WebElement filterInfoPanel; 
	
	public SearchResultsPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public String getSearchQueryDisplayText() {
		return searchQueryKeyWord.getAttribute("value");
	}
	
	public int getResultCount() {
		String numResults = resultCount.getText().split(" ")[0];
		return Integer.parseInt(numResults);
	}
	
	public int getResultCountCollection() {
		String numResults = filterInfoPanel.getText().split(" ")[0];
		return Integer.parseInt(numResults);
	}
	
	public int getResultCountAlbum() {
		return getResultCountCollection();
	}

}
