package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.components.StateComponent;
import spot.components.StateComponent.StateOptions;
import spot.components.SortingComponent;

public class CollectionsPage extends BasePage {

	private List<WebElement> collectionList;
	
	private SortingComponent sortingComponent;
	
	private StateComponent stateComponent;
	
	@FindBy(css="#ajaxWrapper>div:nth-of-type(2)>form>a")
	private WebElement showAllCollectionsButton;
	
	public CollectionsPage(WebDriver driver) {
		super(driver);
		
		collectionList = driver.findElements(By.className("imj_bibliographicListItem"));
		
		stateComponent = new StateComponent(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public StateComponent getFilterComponent() {
		return stateComponent;
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
			WebElement collectionLink = largestCollection.findElement(By.tagName("img"));
			collectionLink.click();
		}
		
		return PageFactory.initElements(driver, CollectionContentPage.class);
	}
	
	/**
	 * @return The largest collection on the current page (measured by its number of items).
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
				// no valid number, do nothing
			}
		}
		
		return largestCollection;
	}

	public SortingComponent getSortingComponent() {
		return sortingComponent;
	}

	public CollectionContentPage openSomePublishedCollection() {
		WebElement stateDropdown = driver.findElement(By.className("fa-lock"));
		stateDropdown.click();
		stateComponent.filter(StateOptions.ONLY_PUBLISHED);
		
		CollectionsPage collectionsPage = PageFactory.initElements(driver, CollectionsPage.class);
		collectionsPage.getPageOfLargestCollection();
		return PageFactory.initElements(driver, CollectionContentPage.class);
	}

	
	public CollectionContentPage openCollectionByTitle(String collectionTitle) {
		WebElement collectionInQuestion = findCollectionByTitle(collectionTitle);		
		collectionInQuestion.findElement(By.className("imj_itemHeadline")).click();
		
		return PageFactory.initElements(driver, CollectionContentPage.class);
	}
	
	public void expandCollapseDescription(String collectionTitle) {
		WebElement collectionInQuestion = findCollectionByTitle(collectionTitle);
		try {
			collectionInQuestion.findElement(By.cssSelector(".imj_containerDescExpand")).click();
			collectionInQuestion.findElement(By.cssSelector(".imj_collapse")).click();
		}
		catch (NoSuchElementException exc) {
			throw new NoSuchElementException("Description is not long enough.");
		}
	}
	
	/**
	 * @throws NoSuchElementException if no collection's name matches collectionTitle
	 */
	private WebElement findCollectionByTitle(String collectionTitle) {
		
		WebElement collectionInQuestion = null;
		
		// since page could have been refreshed due to checkCollectionList()
		collectionList = driver.findElements(By.className("imj_bibliographicListItem"));
		
		for (WebElement collection : collectionList) {
			
			WebElement collBody = collection.findElement(By.className("imj_itemContent"));
			
			WebElement collHeadline = collBody.findElement(By.className("imj_itemHeadline"));
			
			String headline = collHeadline.getText();
			
			if (headline.equals(collectionTitle)) {
				collectionInQuestion = collection;
			}
		}
		
		if (collectionInQuestion == null)
			throw new NoSuchElementException("Collection with this title was not found.");
		
		return collectionInQuestion;
	}
	
	/**
	 * @throws NoSuchElementException if no collection on the page is published
	 */
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
		
		if (someNotPublishedCollection == null)
			throw new NoSuchElementException("No published collection was found on this page.");
		
		// open start page of the selected collection
		someNotPublishedCollection.findElement(By.tagName("img")).click();
		
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
