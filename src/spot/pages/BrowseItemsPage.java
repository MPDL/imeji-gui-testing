package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class BrowseItemsPage extends BasePage {

	private List<WebElement> allItems;
	List<WebElement> imageList;
	
	public BrowseItemsPage(WebDriver driver) {
		super(driver);
		
		allItems = driver.findElements(By.className("imj_tileItem"));
		imageList = driver.findElements(By.tagName("img"));
		
		PageFactory.initElements(driver, this);
	}
	
	public boolean isItemPresent(String itemTitle) {
		for(WebElement item : allItems) {
			WebElement titleElement = item.findElement(By.className("imj_optionLabel"));
			String currentItemTitle = titleElement.getAttribute("title");
			if (currentItemTitle.equals(itemTitle)) {
				item.findElement(By.tagName("img")).click();
				return true;
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
	public DetailedItemViewPage openItemByTitle(String itemTitle) {
		for(WebElement item : allItems) {
			WebElement titleElement = item.findElement(By.className("imj_optionLabel"));
			String currentItemTitle = titleElement.getAttribute("title");
			if (currentItemTitle.equals(itemTitle)) {
				item.findElement(By.tagName("img")).click();
				return PageFactory.initElements(driver, DetailedItemViewPage.class);
			}
		}
		throw new NoSuchElementException("No item with this name was found.");
	}
}
