package spot.components;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.CategoryType;
import spot.pages.AdvancedSearchPage;
import spot.pages.SearchQueryPage;

public class SearchComponent {

	private WebDriver driver;
	
	@FindBy(name="Header:j_idt71:quickSearchString")
	private WebElement quickSearchTextField;
	
	@FindBy(name="Header:j_idt71:lnkAdvancedSearch")
	private WebElement advancedSearchButton;
	
	@FindBy(xpath=".//*[@id='Header:j_idt71:quickSearchRadioBox:0']")
	private WebElement itemRadioBox;
		
	@FindBy(xpath=".//*[@id='Header:j_idt71:quickSearchRadioBox:2']")
	private WebElement albumRadioBox;
	
	@FindBy(xpath=".//*[@id='Header:j_idt71:quickSearchRadioBox:1']")
	private WebElement collectionRadioBox;
	
	@FindBy(name="Header:j_idt71:btnQuickSearchStart")
	private WebElement goToQuickSearchingButton;
	
	public SearchComponent(WebDriver driver) {
		
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public AdvancedSearchPage navigateToAdvancedSearchPage() {
		
		advancedSearchButton.click();		
		return PageFactory.initElements(driver, AdvancedSearchPage.class);
	}
	
	public SearchQueryPage searchFor(CategoryType ct, String searchQueryKeyWord) {
		
		quickSearchTextField.clear();
		quickSearchTextField.sendKeys(searchQueryKeyWord);
		
		if (ct == CategoryType.ALBUM) {
			if (!albumRadioBox.isSelected())
				albumRadioBox.click();	
		} else if (ct == CategoryType.ITEM) {
			if (!itemRadioBox.isSelected())
				itemRadioBox.click();		
		} else if (ct == CategoryType.COLLECTION) {
			if (!collectionRadioBox.isSelected())
				collectionRadioBox.click();		
		}
		
		goToQuickSearchingButton.click();
		
		return PageFactory.initElements(driver, SearchQueryPage.class);
	}	
	
}
