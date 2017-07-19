package spot.pages.registered;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

public class EditItemsPage extends BasePage {

	@FindBy(css = ".selectMdButton")
	private WebElement metadataButton;
	
	@FindBy(xpath = "//input[contains(@id, 'editBatchForm')]")
	private WebElement keyBox;
	
	@FindBy(css = ".imj_mdInput")
	private WebElement valueBox;
	
	@FindBy(css = "#editBatchForm .imj_metadataSet input:nth-of-type(1)")
	private WebElement addValueAll;
	
	@FindBy(css = "#editBatchForm .imj_metadataSet input:nth-of-type(2)")
	private WebElement addValueIfEmpty;
	
	//@FindBy(xpath = "//input[@value='Overwrite all values']")
	@FindBy(css = "#editBatchForm .imj_metadataSet input:nth-of-type(3)")
	private WebElement overwriteAllValues;
	
	@FindBy(id = "editBatchForm:select:statementList")
	private WebElement bestSuggestion;
	
	public EditItemsPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public EditItemsPage addValueAll(String key, String value) {
		addMetadata(key, value);
		addValueAll.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("Header:mainMenu:lnkCollections")));
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	public EditItemsPage addValueIfEmpty(String key, String value) {
		addMetadata(key, value);
		addValueIfEmpty.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("Header:mainMenu:lnkCollections")));
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	public EditItemsPage overwriteAllValues(String key, String value) {
		addMetadata(key, value);
		overwriteAllValues.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("Header:mainMenu:lnkCollections")));
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	public EditItemsPage addOwnMetadataAll(String key, String value) {
		metadataButton.click();
		keyBox.sendKeys(key);
		try {
			Thread.sleep(2000);
		} 
		catch (InterruptedException e) {}
		
		driver.findElement(By.cssSelector("#editBatchForm\\:select\\:statementList .selectMdButton")).click();
		WebElement confirm = retryingElement(By.id("editBatchForm:select:dialog:btnCreateStatement"));
		confirm.click();
		
		WebElement valueBox1 = retryingElement(By.cssSelector(".imj_mdInput"));
		valueBox1.sendKeys(value);
		addValueAll.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("Header:mainMenu:lnkCollections")));
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	private void addMetadata(String key, String value) {
		addKey(key);
		WebElement valueBox1 = retryingElement(By.cssSelector(".imj_mdInput"));
		valueBox1.sendKeys(value);
	}
	
	private void addKey(String key) {
		metadataButton.click();
		keyBox.sendKeys(key);
		try {
			Thread.sleep(2000);
		} 
		catch (InterruptedException e) {}
		driver.findElement(By.cssSelector("#editBatchForm\\:select\\:statementList a")).click();
	}
}
