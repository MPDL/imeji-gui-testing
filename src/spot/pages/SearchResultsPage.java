package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SearchResultsPage extends BasePage {

	@FindBy(xpath="html/body/div[1]/div[4]/div[1]")
	private WebElement searchQueryDisplay;
	
	@FindBy(id="quickSearchString")
	private WebElement searchQueryKeyWord;
	
	@FindBy(xpath = "/html/body/div[1]/div[5]/div[1]/div[2]/form/div[1]")
	private WebElement resultCount;
	
	@FindBy(id = "j_idt346:filterInfoPanel")
	private WebElement filterInfoPanel;
	
	public SearchResultsPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public String getSearchQueryDisplayText() {
		return searchQueryKeyWord.getAttribute("value");
	}
	
	public int getResultCount() {
		String numResults = resultCount.getText().split(" ")[0];
		return Integer.parseInt(numResults);
	}
	
	public int getResultCountCategory() {
		String numResults = filterInfoPanel.getText().split(" ")[0];
		return Integer.parseInt(numResults);
	}

}
