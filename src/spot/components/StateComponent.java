package spot.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

public class StateComponent extends BasePage {

	public enum StateOptions {
		ONLY_PRIVATE, ONLY_PUBLISHED, ONLY_DISCARDED
	};
	
	@FindBy (xpath = "/html/body/div[1]/div[1]/div[5]/div/div[2]/div[2]/div[2]/ul/li[1]/a")
	private WebElement onlyPrivateFilter;
	
	@FindBy (xpath = "/html/body/div[1]/div[1]/div[5]/div/div[2]/div[2]/div[2]/ul/li[2]/a")
	private WebElement onlyPublishedFilter;
	
	@FindBy (xpath = "/html/body/div[1]/div[1]/div[5]/div/div[2]/div[2]/div[2]/ul/li[3]/a")
	private WebElement onlyDiscardedFilter;

	public StateComponent (WebDriver driver) {
		super(driver);

		PageFactory.initElements(driver, this);
	}
	
	public void filter(StateOptions filter) {
		switch (filter) {
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
