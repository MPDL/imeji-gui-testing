package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BrowseItemsPage extends BasePage {

	private List<WebElement> allItems;
	List<WebElement> imageList;
	
	@FindBy(id = "facetsArea")
	private WebElement facetsArea;
	
	public BrowseItemsPage(WebDriver driver) {
		super(driver);
		
		allItems = driver.findElements(By.className("imj_tileItem"));
		imageList = driver.findElements(By.tagName("img"));
		
		PageFactory.initElements(driver, this);
	}
	
	public boolean isItemPresent(String itemTitle) {
		for (WebElement item : allItems) {
			WebElement titleElement = item.findElement(By.className("imj_optionLabel"));
			String currentItemTitle = titleElement.getAttribute("title");
			if (currentItemTitle.equals(itemTitle)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isFacetPresent(String key, String value) {
		List<WebElement> facets = facetsArea.findElements(By.className("imj_facet"));
		
		for (WebElement facet : facets) {
			String currentKey = facet.findElement(By.className("imj_facetName")).getText();
			if (currentKey.equals(key)) {
				List<WebElement> values = facet.findElement(By.className("imj_facetValue")).findElements(By.tagName("a"));
				for (WebElement currentValue : values) {
					String currentValueText = currentValue.getText();
					if (currentValueText.equals(value)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public int imageCount() {
		return imageList.size();
	}
	
	/**
	 * @throws NoSuchElementException if no item title on the page matches itemTitle
	 */
	public ItemViewPage openItemByTitle(String itemTitle) {
		for(WebElement item : allItems) {
			WebElement titleElement = item.findElement(By.className("imj_optionLabel"));
			String currentItemTitle = titleElement.getAttribute("title");
			if (currentItemTitle.equals(itemTitle)) {
				item.findElement(By.tagName("img")).click();
				return PageFactory.initElements(driver, ItemViewPage.class);
			}
		}
		throw new NoSuchElementException("No item with this name was found.");
	}
	
	public boolean isItemAreaDisplayed() {
		try {
			driver.findElement(By.id("itemsArea"));
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
}
