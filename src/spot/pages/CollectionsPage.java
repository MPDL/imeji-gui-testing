package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.components.FilterComponent;
import spot.components.SortingComponent;

public class CollectionsPage extends BasePage {

	private List<WebElement> collectionList;
	
	private SortingComponent sortingComponent;
	
	private FilterComponent filterComponent;
	
	@FindBy(css="#ajaxWrapper>div:nth-of-type(2)>form>a")
	private WebElement showAllCollectionsButton;
	
	public CollectionsPage(WebDriver driver) {
		super(driver);
		
		collectionList = driver.findElements(By.className("imj_bibliographicListItem"));
		
		filterComponent = new FilterComponent(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public FilterComponent getFilterComponent() {
		return filterComponent;
	}

	private WebElement getFirstCollectionInList() {
		return collectionList.get(0);
	}
	
	public void goToDetailedViewOfRandomCollection() {
		WebElement goToDetailedViewButton = getFirstCollectionInList().findElement(By.name("j_idt256:j_idt333:list:0:j_idt337:lnkViewMetadataDetail"));
	}
	
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

	public SortingComponent getSortingComponent() {
		return sortingComponent;
	}

	public CollectionContentPage openSomePublishedCollection() {
		
		checkCollectionList();
		
		WebElement somePublishedCollection = null;
		
		// since page could have been refreshed due to checkCollectionList()
		collectionList = driver.findElements(By.className("imj_bibliographicListItem"));
		
		for (WebElement collection : collectionList) {
			
			WebElement status = collection.findElement(By.className("imj_statusArea"));
			
			if (status.findElements(By.xpath(".//*")).size()==0) {
				System.out.println("This collection is published");
				
				somePublishedCollection = collection;				
				break;
			} 
		}
		
		// open start page of the selected collection
		somePublishedCollection.findElement(By.cssSelector(".imj_itemActionArea li:nth-of-type(2) a")).click();
		
		return PageFactory.initElements(driver, CollectionContentPage.class);
	}

	public CollectionContentPage openSomeNotPublishedCollection() {
		
		checkCollectionList();
		
		WebElement someNotPublishedCollection = null;
		
		// any need for finding again? since page could have been refreshed due to checkCollectionList()
		try {
			collectionList.get(0).findElement(By.className("imj_statusArea"));		
		} catch (StaleElementReferenceException e) {
			collectionList = driver.findElements(By.className("imj_bibliographicListItem"));	
		}
				
		
		for (WebElement collection : collectionList) {
			
			WebElement status = collection.findElement(By.className("imj_statusArea"));
			
			if (status.findElements(By.xpath(".//*")).size()>0) {
				
				System.out.println("This collection is not yet published");
				
				// it also needs to have files, at least one; does it?
				WebElement collItemCount= collection.findElement(By.cssSelector(".imj_itemCount"));
				String[] split = collItemCount.getText().split("\\s+");
				
				try {
					String itemCountString = split[0];
					int itemCount = Integer.parseInt(itemCountString);
				
					if (itemCount>0) {
						someNotPublishedCollection = collection;				
						break;				
					}
				} catch (NumberFormatException nfe) {
					// no valid number
					// do nothing
				}				
			} 
		}
		
		// open start page of the selected collection
		someNotPublishedCollection.findElement(By.cssSelector(".imj_itemActionArea li:nth-of-type(2) a")).click();
		
		return PageFactory.initElements(driver, CollectionContentPage.class);
	}

	private void checkCollectionList() {		
		
		if (collectionList.size() == 0) {
			showAllCollectionsButton.click();
		} else if (collectionList.size() == 1) {
			WebElement emptyList = collectionList.get(0);
			String emptyListInnerHTML = emptyList.getText();
			if (emptyListInnerHTML.equals("No results found")) {
				showAllCollectionsButton.click();
			}
		}
	}

	public void deleteCollections() {
		for (WebElement collection : collectionList) {
			collection.findElement(By.cssSelector(".imj_itemActionArea li:nth-of-type(2) a")).click();
		}
	}


}
