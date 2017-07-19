package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchResultsPage extends BasePage {

	@FindBy(className="imj_searchQueryText")
	private WebElement searchQueryDisplay;
	
	@FindBy(id="quickSearchString")
	private WebElement searchQueryKeyWord;
	
	@FindBy(id = "selPanel:selectionInfoPanel")
	private WebElement resultCount;
	
	@FindBy(id = "filterInfoPanel")
	private WebElement filterInfoPanel; 
	
	public SearchResultsPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public String getSearchQueryDisplayText() {
		return searchQueryKeyWord.getAttribute("value");
	}
	
	public int getResultCount() {
		List<WebElement> results = driver.findElements(By.xpath("//tr[contains(@class, 'order-table')]"));
		return results.size();
	}
	
	public int getResultCountCollection() {
		try {
			String numResults = filterInfoPanel.getText().split(" ")[0];
			return Integer.parseInt(numResults);
		}
		catch (NoSuchElementException exc) {
			return 0;
		}
	}
	
	public int getResultCountAlbum() {
		return getResultCountCollection();
	}

}
