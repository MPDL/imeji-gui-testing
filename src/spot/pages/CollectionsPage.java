package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class CollectionsPage extends BasePage {

	private List<WebElement> collectionList;
	
	public CollectionsPage(WebDriver driver) {
		super(driver);
		
		collectionList = driver.findElements(By.className("imj_bibliographicListItem"));
	}

//	public void createNewCollection
	
	public CollectionContentPage getPageOfLargestCollection() {
	
		WebElement largestCollection = getLargestCollection();
		
		if (largestCollection != null) {
			WebElement collectionActionWE = largestCollection.findElement(By.className("imj_itemActionArea"));
			List<WebElement> findElements = collectionActionWE.findElements(By.tagName("li"));
			
			WebElement showContentTag = findElements.get(1);
			
			WebElement showContentLink = showContentTag.findElement(By.tagName("a"));
			
			showContentLink.click();
		}
		
		//TODO what if no largest collection received
		
		return PageFactory.initElements(driver, CollectionContentPage.class);
	}
	
	/**
	 * Get the largest collection (measured by its number of items). 
	 * Attention: The largest collection on the actual page number. 
	 * @return
	 */
	private WebElement getLargestCollection() {
		
		WebElement largestCollection = null;
		int maxItemCount = 0; 
		
		for (WebElement collection : collectionList) {
			WebElement collItemCount= collection.findElement(By.className("imj_itemCount"));
			
			String[] split = collItemCount.getText().split("\\s+");
			
			try {
				String itemCountString = split[0];
				int itemCount = Integer.parseInt(itemCountString);
				if (itemCount > maxItemCount) { 
					maxItemCount = itemCount;
					largestCollection = collection;
				}
			} catch (NumberFormatException nfe) {
				// no valid number
				// do nothing
			}
		}
		
		return largestCollection;
	}

}
