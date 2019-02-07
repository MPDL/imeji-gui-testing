package spot.components;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;
import spot.pages.BrowseItemsPage;
import spot.pages.CollectionEntryPage;

/**
 * Component for the Facets-Area. <br>
 * The Facets-Area is visible on the right side of the {@link CollectionEntryPage} and the {@link BrowseItemsPage}.
 * 
 * @author helk
 *
 */
public class FacetsComponent extends BasePage {

	@FindBy(className = "imj_facet")
	private List<WebElement> facets;

	public FacetsComponent(WebDriver driver) {
		super(driver);

		PageFactory.initElements(driver, this);
	}

	private WebElement showMoreFacetValues(WebElement facet) {
		String currentFacetName = facet.findElement(By.className("imj_facetName")).getText();
		
		List<WebElement> moreElements = facet
				.findElements(By.xpath(".//descendant::div/a[contains(text(),'More') and not(@title)]"));
		if (moreElements.size() == 1) {
			// If the facet has more values show them by clicking 'more'.
			moreElements.get(0).click();
			// Wait after the more-click -> until more statement values are loaded 
			// (then the old facet element is stale)
			wait.until(ExpectedConditions.stalenessOf(facet));
			
			//Find and return the reloaded facet element (because the old facet element has become stale)
			WebElement reloadedFacet = driver.findElement(By.xpath("//div[@class='imj_facet' and div[@class='imj_facetName' and text()='" + currentFacetName + "']]"));
			return reloadedFacet;
		}
		
		return facet;
	}

	private boolean isFacetPresent(String facetName, String facetValue) {
		for (WebElement facet : facets) {
			String currentFacetName = facet.findElement(By.className("imj_facetName")).getText();
			if (currentFacetName.equals(facetName)) {
				facet = this.showMoreFacetValues(facet);
				List<WebElement> values = facet.findElement(By.className("imj_facetValue"))
						.findElements(By.tagName("a"));
				for (WebElement currentValue : values) {
					String currentValueText = currentValue.getText();
					if (currentValueText.equals(facetValue)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isRangeFacetPresent(String facetName, String facetValue) {
		for (WebElement facet : facets) {
			String currentFacetName = facet.findElement(By.className("imj_facetName")).getText();
			if (currentFacetName.equals(facetName)) {
				// Note: Only works when the facet has only one value (fromValue = toValue)
				String fromValue = facet.findElement(By.xpath(".//input[@placeholder='from']")).getAttribute("value");
				String toValue = facet.findElement(By.xpath(".//input[@placeholder='to']")).getAttribute("value");
				if (fromValue.equals(facetValue) && toValue.equals(facetValue)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isTextFacetPresent(String facetName, String facetValue) {
		return this.isFacetPresent(facetName, facetValue);
	}

	public boolean isNumberFacetPresent(String facetName, String facetValue) {
		return this.isRangeFacetPresent(facetName, facetValue);
	}

	public boolean isDateFacetPresent(String facetName, String facetValue) {
		return this.isRangeFacetPresent(facetName, facetValue);
	}

	public boolean isPersonFacetPresent(String facetName, String facetValue) {
		return this.isFacetPresent(facetName, facetValue);
	}

	public boolean isUrlFacetPresent(String facetName, String facetValue) {
		return this.isFacetPresent(facetName, facetValue);
	}

}
