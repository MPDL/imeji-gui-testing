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
	
	@FindBy(className = "fa-cog")
	private WebElement stateMenu;
	
	@FindBy(linkText = "Private")
	private WebElement onlyPrivateFilter;
	
	@FindBy(linkText = "Published")
	private WebElement onlyPublishedFilter;
	
	@FindBy(linkText = "Discarded")
	private WebElement onlyDiscardedFilter;

	public StateComponent (WebDriver driver) {
		super(driver);

		PageFactory.initElements(driver, this);
	}
	
	public void filter(StateOptions filter) {
		stateMenu.click();
		
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
