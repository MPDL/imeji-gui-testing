package spot.components;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.AdvancedSearchPage;
import spot.pages.BrowseItemsPage;
import spot.pages.LoginPage;
import spot.pages.SearchResultsPage;

public class SearchComponent {
	
	public enum CategoryType {
		ITEM, COLLECTION
	}

	private WebDriver driver;
	
	@FindBy(id="simpleSearchInputText")
	private WebElement quickSearchTextField;
	
	@FindBy(id="btnSimpleSearch")
	private WebElement goToQuickSearchingButton;
	
	@FindBy(id="lnkAdvancedSearch")
	private WebElement advancedSearchButton;

	@FindBy(id="lnkItems")
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
		
		WebElement categoryMenu = driver.findElement(By.className("imj_bodyContextSearch"));
		switch(category) {
			case ITEM:
				categoryMenu.findElement(By.id("simpleSearchForItems")).click();
				break;
			case COLLECTION:
				categoryMenu.findElement(By.id("simpleSearchForCollections")).click();
				//driver.navigate().to("http://qa-imeji.mpdl.mpg.de/imeji/collections?q=" + searchQuery);
				break;
			default:
				categoryMenu.findElement(By.id("simpleSearchForItems")).click();
				break;
		}
		
		return PageFactory.initElements(driver, SearchResultsPage.class);
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
