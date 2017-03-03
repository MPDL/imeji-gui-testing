package spot.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.AdvancedSearchPage;
import spot.pages.BrowseItemsPage;
import spot.pages.SearchResultsPage;
import test.base.CategoryType;

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
	
	public SearchResultsPage searchFor(String searchQuery) {
		
		quickSearchTextField.clear();
		quickSearchTextField.sendKeys(searchQuery);
		
		goToQuickSearchingButton.click();
		
		return PageFactory.initElements(driver, SearchResultsPage.class);
	}	
	
	public SearchResultsPage searchByCategory(String searchQuery, CategoryType category) {
		quickSearchTextField.clear();
		quickSearchTextField.sendKeys(searchQuery);
		
		WebElement categoryMenu = driver.findElement(By.className("imj_menuSimpleSearch"));
		switch(category) {
			case ITEM:
				categoryMenu.findElement(By.id("simpleSearchForItems")).click();
				break;
			case ALBUM:
				categoryMenu.findElement(By.id("simpleSearchForAlbums")).click();
				break;
			case COLLECTION:
				categoryMenu.findElement(By.id("simpleSearchForCollections")).click();
				break;
			default:
				categoryMenu.findElement(By.id("simpleSearchForItems")).click();
				break;
		}
		
		return PageFactory.initElements(driver, SearchResultsPage.class);
	}
	
}
