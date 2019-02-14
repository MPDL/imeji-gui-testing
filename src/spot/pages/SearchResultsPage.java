package spot.pages;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

//TODO: Remove this generic class -> Insteaad the items-, collections- and users-page should have a searchQuery component -> class: DisplaySearchQueryComponent
public class SearchResultsPage extends BasePage {

	@FindBy(className="imj_searchQueryText")
	private WebElement searchQueryDisplay;
	
	@FindBy(id = "selPanel:selectionInfoPanel")
	private WebElement resultCount;
	
	@FindBy(css = "#pageTitle>h1")
	private WebElement filterInfoPanel; 
	
	public SearchResultsPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	// assumes search query does not contain equal sign
	public String getItemSearchQuery() {
		return StringUtils.substringAfterLast(searchQueryDisplay.getText(), "=").replace(')', ' ').trim();
	}
	
	public String getCollectionSearchQuery() {
		return searchQueryDisplay.getText().trim();
	}
	
	public int getResultCount() {
		String numResults = filterInfoPanel.getText().split(" ")[0];
		return Integer.parseInt(numResults);
	}

}
