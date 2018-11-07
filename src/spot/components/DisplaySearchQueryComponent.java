package spot.components;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BrowseItemsPage;
import spot.pages.CollectionsPage;
import spot.pages.admin.BrowseUsersPage;

/**
 * This class represents the SearchQuery-Display Component. <br>
 * The searchQuery is displayed on the {@link CollectionsPage}, the {@link BrowseItemsPage} and the {@link BrowseUsersPage},
 * after a search, when the search results are shown.
 * 
 * @author helk
 *
 */
public class DisplaySearchQueryComponent {

	private WebDriver driver;
	
	@FindBy(className="imj_searchQueryText")
	private WebElement searchQueryDisplay;
	
	public DisplaySearchQueryComponent(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	// assumes search query does not contain equal sign
	public String getItemSearchQuery() {
		return StringUtils.substringAfterLast(searchQueryDisplay.getText(), "=").replace(')', ' ').trim();
	}
	
	public String getCollectionSearchQuery() {
		return searchQueryDisplay.getText().trim();
	}
	
}
