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
	
	@FindBy(xpath = "/html/body/div[1]/div[6]/div[1]/div[2]/div[3]/div[2]/ul/li[1]/a")
	private WebElement mineFilter;
	
	@FindBy(xpath = "/html/body/div[1]/div[6]/div[1]/div[2]/div[3]/div[2]/ul/li[2]/a")
	private WebElement sharedWithMeFilter;
	
	@FindBy(xpath = "/html/body/div[1]/div[6]/div[1]/div[2]/div[3]/div[2]/ul/li[3]/a")
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
