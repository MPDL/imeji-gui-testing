package spot.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

public class FilterComponent extends BasePage {

	public enum FilterOptions {
		ALL, MY, ONLY_PRIVATE, ONLY_PUBLISHED, ONLY_DISCARDED
	};
	
	@FindBy (css = "#actionsMenuArea form ul li:nth-of-type(1) a")
	private WebElement allFilter;
	
	@FindBy (css = "#actionsMenuArea form ul li:nth-of-type(2) a")
	private WebElement myFilter;
	
	@FindBy (css = "#actionsMenuArea form ul li:nth-of-type(3) a")
	private WebElement onlyPrivateFilter;
	
	@FindBy (css = "#actionsMenuArea form ul li:nth-of-type(4) a")
	private WebElement onlyPublishedFilter;
	
	@FindBy (css = "#actionsMenuArea form ul li:nth-of-type(5) a")
	private WebElement onlyDiscardedFilter;

	public FilterComponent (WebDriver driver) {
		super(driver);

		PageFactory.initElements(driver, this);
	}
	
	public void filter(FilterOptions filter) {
		
		switch (filter) {
		case ALL:
			wait.until(ExpectedConditions.visibilityOf(allFilter));
			allFilter.click();				
			break;
		case MY:
			wait.until(ExpectedConditions.visibilityOf(myFilter));
			myFilter.click();
			break;
		case ONLY_PRIVATE:
			wait.until(ExpectedConditions.visibilityOf(onlyPrivateFilter));
			onlyPrivateFilter.click();
			break;
		case ONLY_PUBLISHED:
			wait.until(ExpectedConditions.visibilityOf(onlyPublishedFilter));
			onlyPublishedFilter.click();
			break;
		case ONLY_DISCARDED:
			wait.until(ExpectedConditions.visibilityOf(onlyDiscardedFilter));
			onlyDiscardedFilter.click();
			break;
		}
	}
}
