package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import spot.components.FacetsComponent;

public class BrowseItemsPage extends BasePage {
	
	private FacetsComponent facetsComponent;
	
	private List<WebElement> allItems;
	List<WebElement> imageList;
	
	public BrowseItemsPage(WebDriver driver) {
		super(driver);
		
		facetsComponent = new FacetsComponent(driver);
		
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
			driver.findElement(By.className("itemsArea"));
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	public boolean isTextFacetPresent(String facetName, String facetValue) {
		return this.facetsComponent.isTextFacetPresent(facetName, facetValue);
	}

	public boolean isNumberFacetPresent(String facetName, String facetValue) {
		return this.facetsComponent.isNumberFacetPresent(facetName, facetValue);
	}

	public boolean isDateFacetPresent(String facetName, String facetValue) {
		return this.facetsComponent.isDateFacetPresent(facetName, facetValue);
	}

	public boolean isPersonFacetPresent(String facetName, String facetValue) {
		return this.facetsComponent.isPersonFacetPresent(facetName, facetValue);
	}

	public boolean isUrlFacetPresent(String facetName, String facetValue) {
		return this.facetsComponent.isUrlFacetPresent(facetName, facetValue);
	}
}
