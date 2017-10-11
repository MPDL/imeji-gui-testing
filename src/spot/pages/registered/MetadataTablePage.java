package spot.pages.registered;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;

public class MetadataTablePage extends BasePage {

	@FindBy(css = ".edit_selected_table>tbody>tr")
	private List<WebElement> items;
	
	@FindBy(css = ".edit_selected_table>thead>tr>th")
	private List<WebElement> headEntries;
	
	@FindBy(css = ".editSelectedHeader .imj_submitButton")
	private WebElement submitButton;
	
	public MetadataTablePage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		items.remove(0);
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
			if (item.findElement(By.tagName("th")).getText().equals(itemTitle)) {
				editEntry(item, key, value);
				return;
			}
		}
		
		throw new NoSuchElementException("Item with name " + itemTitle + " was not found");
	}
	
	private void editEntry(WebElement item, String key, String value) {
		List<WebElement> metadataEntries = item.findElements(By.tagName("td"));
		int metadataCount = metadataEntries.size();
		for (int i = 1; i < metadataCount; i++) {
			if (headEntries.get(i).getText().trim().equals(key)) {
				WebElement valueBox = metadataEntries.get(i - 1).findElement(By.className("imj_mdInput"));
				valueBox.clear();
				valueBox.sendKeys(value);
				return;
			}
		}
		
		throw new NoSuchElementException("Metadata with name " + key + " was not found.");
	}
	
	private CollectionEntryPage submit() {
		submitButton.click();
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
}
