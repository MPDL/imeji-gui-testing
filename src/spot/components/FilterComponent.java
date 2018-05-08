package spot.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

public class FilterComponent extends BasePage {

	public enum FilterOptions {
		MINE, SHARED_WITH_ME, MINE_OR_SHARED
	};
	
	public FilterComponent (WebDriver driver) {
		super(driver);

		PageFactory.initElements(driver, this);
	}
	
	// link text is not a persistent find method, but the menu structure is not fixed between releases for now
	@FindBy(linkText = "Created by me")
	private WebElement mineFilter;
	
	@FindBy(linkText = "Shared with me")
	private WebElement sharedWithMeFilter;
	
	@FindBy(linkText = "Created by me or shared with me")
	private WebElement mineOrSharedFilter;
	
	public void filter(FilterOptions filter) {
		switch (filter) {
			case MINE:
				wait.until(ExpectedConditions.visibilityOf(mineFilter));
				mineFilter.click();
				break;
			case SHARED_WITH_ME:
				wait.until(ExpectedConditions.visibilityOf(sharedWithMeFilter));
				sharedWithMeFilter.click();
				break;
			case MINE_OR_SHARED:
				wait.until(ExpectedConditions.visibilityOf(mineOrSharedFilter));
				mineOrSharedFilter.click();
				break;
		}
	}
}
