package spot.pages.registered;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

public class EditItemsPage extends BasePage {

	@FindBy(css = ".selectMdButton")
	private WebElement metadataButton;
	
	@FindBy(xpath = "//input[contains(@id, 'editBatchForm')]")
	private WebElement keyBox;
	
	@FindBy(css = ".imj_mdInput")
	private WebElement valueBox;
	
	@FindBy(xpath = "//input[contains(@value, 'new value')]")
	private WebElement addValueAll;
	
	@FindBy(xpath = "//input[contains(@value, 'empty')]")
	private WebElement addValueIfEmpty;
	
	@FindBy(xpath = "//input[contains(@value, 'Overwrite')]")
	private WebElement overwriteAllValues;
	
	public EditItemsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	// IMJ-279, IMJ-228, IMJ-249
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
		wait.until(ExpectedConditions.elementToBeClickable(metadataButton));
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	// IMJ-280, IMJ-140, IMJ-251
	public EditItemsPage addValueIfEmpty(String key, String value) {
		addMetadata(key, value);
		addValueIfEmpty.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("lnkCollections")));
		try { Thread.sleep(2500); } catch (InterruptedException e) {}
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	// IMJ-281, IMJ-229, IMJ-259, IMJ-262
	public EditItemsPage overwriteAllValues(String key, String value) {
		addMetadata(key, value);
		overwriteAllValues.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("lnkCollections")));
		try { Thread.sleep(2500); } catch (InterruptedException e) {}
		
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
		try { Thread.sleep(2500); } catch (InterruptedException e) {}
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	// IMJ-279
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
		try { Thread.sleep(2500); } catch (InterruptedException e) {}
		
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	private void addMetadata(String key, String value) {
		addKey(key);
		WebElement valueBox1 = retryingElement(By.cssSelector(".imj_mdInput"));
		valueBox1.sendKeys(value);
		
		//If the type of the metadata is date, the datepicker hides some fields.
		String valueBoxType = valueBox1.getAttribute("type");		
		if (valueBoxType.equals("date")) {
			//Hide the datepicker
			((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility = 'hidden'", driver.findElement(By.id("ui-datepicker-div")));
		}
	}
	
	private void addKey(String key) {
		wait.until(ExpectedConditions.visibilityOf(metadataButton));
		metadataButton.click();
		keyBox.clear();
		keyBox.sendKeys(key);
		
		// Wait until no more candidates are loaded (by ajax) can only be approximated with waits:
		// 1) Wait until the full metadata name is typed in the keyBox
		wait.until(ExpectedConditions.textToBePresentInElementValue(keyBox, key));
		// 2) Wait until all names of all candidates contain the full name-key
		wait.until(textToBePresentInAllElements(By.xpath("//div[@id='selectStatementDialog']/descendant::a[contains(@id,'editBatchForm:select')]"), key));
		try {
			// 3) Wait until the searched metadata is visible (if not the statement is not available)
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(key)));
			// The metadata to click can still be stale (if there is another ajax reload). Therefore use retryFindClick to avoid a stale exception.
			retryingFindClick(By.linkText(key));
			return;
		}
		catch (TimeoutException exc) {
			throw new NoSuchElementException("Statement " + key + " is not available.");
		}
	}
	
	private static ExpectedCondition<Boolean> textToBePresentInAllElements(final By locator, final String text) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {					
					List<WebElement> elements = driver.findElements(locator);
					for (WebElement webElement : elements) {
						String elementText = webElement.getText();
						if (elementText != null) {
							if(!elementText.toLowerCase().contains(text.toLowerCase())) {
								return false;
							}
						}
					}
					return true;
				} catch (StaleElementReferenceException e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return String.format("text ('%s') to be the value of elements located by %s", text, locator);
			}
		};
	}
	
}
