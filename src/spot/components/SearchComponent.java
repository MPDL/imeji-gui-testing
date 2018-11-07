package spot.components;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import spot.pages.AdvancedSearchPage;
import spot.pages.BrowseItemsPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.SearchResultsPage;
import spot.pages.admin.BrowseUsersPage;

public class SearchComponent {
	
	public enum CategoryType {
		ITEM, COLLECTION, USER
	}

	private WebDriver driver;
	private WebDriverWait wait; 
	
	@FindBy(id = "simpleSearchInputText")
	private WebElement quickSearchTextField;
	
	@FindBy(id = "btnSimpleSearch")
	private WebElement goToQuickSearchingButton;
	
	@FindBy(id = "simpleSearchForItems")
	private WebElement searchItems;
	
	@FindBy(id = "simpleSearchForCollections")
	private WebElement searchCollections;
	
	@FindBy(id = "simpleSearchForUsers")
	private WebElement searchUsers;
	
	@FindBy(id = "lnkAdvancedSearch")
	private WebElement advancedSearchButton;

	@FindBy(id = "lnkItems")
	private WebElement browseItemsLink;
	
	public SearchComponent(WebDriver driver) {
		this.driver = driver;
		
		//TODO: Merge this wait with wait in BasePage..
		wait = new WebDriverWait(this.driver, 20);
		
		PageFactory.initElements(driver, this);
	}
	
	// IMJ-219
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
	
	//TODO: Refactor the search methods: Merge the searchByCategory-method with the different searchFor...-methods
	// IMJ-162
	public SearchResultsPage searchByCategory(String searchQuery, CategoryType category) {
		quickSearchTextField.clear();
		quickSearchTextField.sendKeys(searchQuery);
		
		switch(category) {
			case ITEM:
				searchItems.click();
				break;
			case COLLECTION:
				searchCollections.click();
				break;
			case USER:
				searchUsers.click();
				break;
			default:
				searchItems.click();
				break;
		}
		
		return PageFactory.initElements(driver, SearchResultsPage.class);
	}
	
	public BrowseItemsPage searchForItemsByExactTitle(String searchQuery) {
		WebElement staleElement = driver.findElement(By.id("btnSimpleSearch"));
		
		quickSearchTextField.clear();
		String exactSearchQeuery = "\"" + searchQuery + "\"";
		quickSearchTextField.sendKeys(exactSearchQeuery);
		searchItems.click();
		
		wait.until(ExpectedConditions.stalenessOf(staleElement));
		
		return PageFactory.initElements(driver, BrowseItemsPage.class);
	}
	
	public CollectionsPage searchForCollectionsByExactTitle(String searchQuery) {
		WebElement staleElement = driver.findElement(By.id("btnSimpleSearch"));
		
		quickSearchTextField.clear();
		String exactSearchQeuery = "\"" + searchQuery + "\"";
		quickSearchTextField.sendKeys(exactSearchQeuery);
		searchCollections.click();
		
		wait.until(ExpectedConditions.stalenessOf(staleElement));
		
		return PageFactory.initElements(driver, CollectionsPage.class);
	}
	
	public BrowseUsersPage searchForUsersByExactTitle(String searchQuery) {
		WebElement staleElement = driver.findElement(By.id("btnSimpleSearch"));
		
		quickSearchTextField.clear();
		String exactSearchQeuery = "\"" + searchQuery + "\"";
		quickSearchTextField.sendKeys(exactSearchQeuery);
		searchUsers.click();
		
		wait.until(ExpectedConditions.stalenessOf(staleElement));
		
		return PageFactory.initElements(driver, BrowseUsersPage.class);
	}
	
	public boolean advancedSearchAccessible() {
		try {
			return advancedSearchButton.isDisplayed() && advancedSearchButton.isEnabled();
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	public boolean advancedSearchUnaccessible() {
		try {
			advancedSearchButton.click();
			LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
			return loginPage.loginFormIsOpen();
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	public boolean browseAccessible() {
		try {
			return browseItemsLink.isDisplayed() && browseItemsLink.isEnabled();
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
}
