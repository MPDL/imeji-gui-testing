package spot.pages.registered;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

public class EditItemsPage extends BasePage {

	@FindBy(xpath = "//input[contains(@id, 'editBatchForm')]")
	private WebElement keyBox;
	
	@FindBy(xpath = "//textarea[contains(@id, 'editBatchForm')]")
	private WebElement valueBox;
	
	@FindBy(css = "#editBatchForm input:nth-of-type(3)")
	private WebElement addValueAll;
	
	@FindBy(css = "#editBatchForm .imj_submitButton")
	private WebElement addValueIfEmpty;
	
	@FindBy(xpath = "//input[@value='Overwrite all values']")
	private WebElement overwriteAllValues;
	
	public EditItemsPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public EditItemsPage addValueAll(String key, String value) {
		addMetadata(key, value);
		addValueAll.click();
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	public EditItemsPage addValueIfEmpty(String key, String value) {
		addMetadata(key, value);
		addValueIfEmpty.click();
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	public EditItemsPage overwriteAllValues(String key, String value) {
		addMetadata(key, value);
		overwriteAllValues.click();
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	private void addMetadata(String key, String value) {
		keyBox.sendKeys(key);
		keyBox.sendKeys(Keys.TAB);
		wait.until(ExpectedConditions.visibilityOf(valueBox));
		valueBox.sendKeys(value);
	}
}
