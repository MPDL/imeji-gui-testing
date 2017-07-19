package spot.pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AdvancedSearchPage extends BasePage {

	@FindBy(css="#imj_searchQueryMessageArea")
	private WebElement searchQueryMessageArea;
	
	@FindBy(id="advancedSearchForm")
	private WebElement advancedSearchForm;
	
	@FindBy(name="advancedSearchForm:j_idt118")
	private WebElement advancedSearchBox;
	
	@FindBy(name="advancedSearchForm:j_idt120")
	private WebElement fulltextCheckbox;
	
	@FindBy(id="advancedSearchForm:submit")
	private WebElement submitButton;
	
	public AdvancedSearchPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public boolean doesSearchQueryMessageAreaExist() {
		try {
			searchQueryMessageArea.isEnabled();
			return true;
		} 
		catch(NoSuchElementException e) {
			return false;
		}
	}
	
	public boolean advancedSearchDisplayed() {
		try {
			advancedSearchForm.isEnabled();
			return true;
		} 
		catch(NoSuchElementException e) {
			return false;
		}
	}
	
	public SearchResultsPage advancedSearch(String term) {
		advancedSearchBox.sendKeys(term);
		if (!fulltextCheckbox.isSelected())
			fulltextCheckbox.click();
		submitButton.click();
		
		return PageFactory.initElements(driver, SearchResultsPage.class);
	}
}
