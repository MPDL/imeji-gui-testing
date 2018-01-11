package spot.pages.registered;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
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
	
	@FindBy(css = "#editBatchForm .imj_metadataSet:nth-of-type(2) input:nth-of-type(1)")
	private WebElement addValueAll;
	
	@FindBy(css = "#editBatchForm .imj_metadataSet:nth-of-type(2) input:nth-of-type(2)")
	private WebElement addValueIfEmpty;
	
	//@FindBy(xpath = "//input[@value='Overwrite all values']")
	@FindBy(css = "#editBatchForm .imj_metadataSet:nth-of-type(2) input:nth-of-type(3)")
	private WebElement overwriteAllValues;
	
	@FindBy(id = "editBatchForm:select:statementList")
	private WebElement bestSuggestion;
	
	public EditItemsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public EditItemsPage addValueAll(String key, String value) {
		addMetadata(key, value);
		try {
			addValueAll.click();
		}
		catch (WebDriverException exc) {
			//autosuggestions prevent button from being clickable
			keyBox.sendKeys(Keys.ESCAPE);
			wait.until(ExpectedConditions.elementToBeClickable(addValueAll));
			addValueAll.click();
		}
		wait.until(ExpectedConditions.elementToBeClickable(By.id("lnkCollections")));
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	public EditItemsPage addValueIfEmpty(String key, String value) {
		addMetadata(key, value);
		addValueIfEmpty.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("lnkCollections")));
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	public EditItemsPage overwriteAllValues(String key, String value) {
		addMetadata(key, value);
		overwriteAllValues.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("lnkCollections")));
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	public EditItemsPage addOwnMetadataAll(String key, String value) {
		metadataButton.click();
		keyBox.sendKeys(key);
		try {
			Thread.sleep(5000);
		} 
		catch (InterruptedException e) {}
		
		driver.findElement(By.cssSelector("#editBatchForm\\:select\\:statementList .selectMdButton")).click();
		WebElement confirm = retryingElement(By.id("editBatchForm:select:dialog:btnCreateStatement"));
		confirm.click();
		
		WebElement valueBox1 = retryingElement(By.cssSelector(".imj_mdInput"));
		valueBox1.sendKeys(value);
		
		addValueAll.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("lnkCollections")));
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	public EditItemsPage addOwnMetadataAll(String key, String value, final List<String> predefinedValues) {
		metadataButton.click();
		keyBox.sendKeys(key);
		try {
			Thread.sleep(5000);
		} 
		catch (InterruptedException e) {}
		
		driver.findElement(By.cssSelector("#editBatchForm\\:select\\:statementList .selectMdButton")).click();
		WebElement confirm = retryingElement(By.id("editBatchForm:select:dialog:btnCreateStatement"));
		confirm.click();
		
		WebElement valueBox1 = retryingElement(By.cssSelector(".imj_mdInput"));
		valueBox1.sendKeys(value);
		
		int predefinedCount = predefinedValues.size();
		for (int i = 0; i < predefinedCount; i++) {
			driver.findElement(By.className("fa-plus-square-o")).click();
			
			By xpathNewInput = By.xpath("//input[contains(@id, '" + i + ":inputPredefined')]");
			wait.until(ExpectedConditions.visibilityOfElementLocated(xpathNewInput));
			driver.findElement(xpathNewInput).sendKeys(predefinedValues.get(i));
		}
		
		addValueAll.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("lnkCollections")));
		
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
			Thread.sleep(5000);
		} 
		catch (InterruptedException e) {}
		List<WebElement> results = driver.findElements(By.cssSelector("#editBatchForm\\:select\\:statementList>p>a"));
		for (WebElement result : results) {
			if (result.getText().equals(key)) {
				result.click();
				return;
			}
		}
		
		throw new NoSuchElementException("Statement " + key + " is not available.");
	}
}
