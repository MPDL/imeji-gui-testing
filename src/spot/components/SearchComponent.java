package spot.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.AdvancedSearchPage;
import spot.pages.BrowseItemsPage;
import spot.pages.SearchQueryPage;

public class SearchComponent {

	private WebDriver driver;
	
	@FindBy(id="quickSearchString")
	private WebElement quickSearchTextField;
	
	@FindBy(id="Header:lnkAdvancedSearch")
	private WebElement advancedSearchButton;
	
	@FindBy(id="btnQuickSearchStart")
	private WebElement goToQuickSearchingButton;
	
	@FindBy(id="Header:lnkBrowse")
	private WebElement browseItemsLink;
	
	public SearchComponent(WebDriver driver) {
		
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public AdvancedSearchPage navigateToAdvancedSearchPage() {
		
		advancedSearchButton.click();		
		return PageFactory.initElements(driver, AdvancedSearchPage.class);
	}
	
	public BrowseItemsPage callBrowseSection() {
		
		browseItemsLink.click();
		return PageFactory.initElements(driver, BrowseItemsPage.class);
	}
	
	public SearchQueryPage searchFor(String searchQueryKeyWord) {
		
		quickSearchTextField.clear();
		quickSearchTextField.sendKeys(searchQueryKeyWord);
		
		goToQuickSearchingButton.click();
		
		return PageFactory.initElements(driver, SearchQueryPage.class);
	}	
	
}
