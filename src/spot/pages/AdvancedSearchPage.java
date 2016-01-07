package spot.pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AdvancedSearchPage extends BasePage {

	@FindBy(css="#imj_searchQueryMessageArea")
	private WebElement searchQueryMessageArea;
	
	public AdvancedSearchPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public boolean doesSearchQueryMessageAreaExist() {
		
		try {
			searchQueryMessageArea.isEnabled();
		} catch(NoSuchElementException e) {
			return false;
		}
		
		return true;
		
	}

	
}
