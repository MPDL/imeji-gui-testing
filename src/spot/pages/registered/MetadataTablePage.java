package spot.pages.registered;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;
import spot.pages.ItemViewPage;

public class MetadataTablePage extends BasePage {

	@FindBy(css = ".selectMdButton")
	private WebElement addColumn;
	
	@FindBy(css = ".edit_selected_table>tbody>tr")
	private List<WebElement> items;
	
	@FindBy(css = ".edit_selected_table>thead>tr>th")
	private List<WebElement> headEntries;
	
	@FindBy(css = "#saveSelected .imj_submitButton")
	private WebElement submitButton;
	
	public MetadataTablePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		// exclude table header
		items.remove(0);
	}
	
	public MetadataTablePage addColumn(String columnName) {
		wait.until(ExpectedConditions.elementToBeClickable(addColumn));
		addColumn.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("selectStatementDialog")));
		try {
			driver.findElement(By.linkText(columnName)).click();
			
			WebElement loaderWrapper = driver.findElement(By.cssSelector(".loaderWrapper"));
			wait.until(ExpectedConditions.invisibilityOf(loaderWrapper));
			
			return PageFactory.initElements(driver, MetadataTablePage.class);
		}
		catch (NoSuchElementException exc) {
			throw new NoSuchElementException("Metadata '" + columnName + "' does not exist.");
		}
	}
	
	public CollectionEntryPage editEntry(final List<String[]> arguments) {
		for (String[] args : arguments) {
			String itemTitle = args[0];
			String key = args[1];
			String value = args[2];
			editColumn(itemTitle, key, value);
		}
		return submit();
	}
	
	private void editColumn(String itemTitle, String key, String value) {
		for (WebElement item : items) {
			String currentTitle = item.findElement(By.tagName("th")).getText();
			if (currentTitle.equals(itemTitle)) {
				editEntry(item, key, value);
				return;
			}
		}
		
		throw new NoSuchElementException("Item with name " + itemTitle + " was not found");
	}
	
	// TODO break into different methods for all metadata types
	private void editEntry(WebElement item, String key, String value) {
		List<WebElement> metadataEntries = item.findElements(By.tagName("td"));
		int metadataCount = metadataEntries.size();
		for (int i = 0; i < metadataCount; i++) {
			String currentKey = headEntries.get(i + 1).getText().trim();
			if (currentKey.equals(key)) {
				WebElement correctEntry = metadataEntries.get(i);
				correctEntry.click();
				correctEntry = refreshCell(item, i);
				List<WebElement> valueBoxes = correctEntry.findElements(By.className("imj_mdInput"));
				WebElement valueBox = valueBoxes.get(0);
				String valueBoxTag = valueBox.getTagName();
				String valueBoxType = valueBox.getAttribute("type");
				if (valueBoxTag.equals("textarea") || valueBoxTag.equals("input")) {
					for (WebElement box : valueBoxes) {
						box.clear();
						box.sendKeys(value);
						// date picker hides the next fields
						if (valueBoxType.equals("date")) {
							((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility = 'hidden'", driver.findElement(By.id("ui-datepicker-div")));
						}
					}
				}
				else if (valueBox.getTagName().equals("select")) {
					// metadata has predefined values
					Select valueSelect = new Select(valueBox);
					valueSelect.selectByVisibleText(value);
				}
				return;
			}
		}
		
		throw new NoSuchElementException("Metadata with name " + key + " was not found.");
	}
	
	private CollectionEntryPage submit() {
		submitButton.click();
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	private WebElement refreshCell(WebElement row, int colIndex) {
		int rowIndex = items.indexOf(row);
		// we assume items are not removed from the table
		List<WebElement> rows = driver.findElements(By.cssSelector(".edit_selected_table>tbody>tr"));
		WebElement refreshedRow = rows.get(rowIndex);
		List<WebElement> cols = refreshedRow.findElements(By.tagName("td"));
		return cols.get(colIndex);
	}
}
