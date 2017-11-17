package spot.pages.admin;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;

public class BrowseFacetsPage extends BasePage {
	
	@FindBy(className="imj_itemContent")
	private List<WebElement> facets;

	public BrowseFacetsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public WebElement findFacet(String facetTitle) {
		for (WebElement facet : facets) {
			String currentName = facet.findElement(By.className("imj_admindataLabel")).getText();
			if (currentName.equals(facetTitle)) {
				return facet;
			}
		}
		throw new NoSuchElementException("Statement with this name is not available on the page.");
	}
	
	public boolean facetListed(String facetTitle) {
		try {
			findFacet(facetTitle);
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	public BrowseFacetsPage changeSystemFacetSelection(String facetTitle, String newSelection) {
		WebElement facet = findFacet(facetTitle);
		facet.findElement(By.partialLinkText("Edit")).click();
		EditFacetPage editFacet = PageFactory.initElements(driver, EditFacetPage.class);
		return editFacet.changeSystemFacetSelection(newSelection);
	}
	
	public BrowseFacetsPage changeMetadataFacetSelection(String facetTitle, String newSelection) {
		WebElement facet = findFacet(facetTitle);
		facet.findElement(By.partialLinkText("Edit")).click();
		EditFacetPage editFacet = PageFactory.initElements(driver, EditFacetPage.class);
		return editFacet.changeMetadataFacetSelection(newSelection);
	}
	
	public BrowseFacetsPage renameFacet(String facetTitle, String newFacetTitle) {
		WebElement facet = findFacet(facetTitle);
		facet.findElement(By.partialLinkText("Edit")).click();
		EditFacetPage editFacet = PageFactory.initElements(driver, EditFacetPage.class);
		return editFacet.renameFacet(newFacetTitle);
	}
	
	public BrowseFacetsPage deleteFacet(String facetTitle) {
		WebElement facet = findFacet(facetTitle);
		facet.findElement(By.linkText("Delete")).click();
		driver.findElement(By.cssSelector("#deleteFacet0>form>.imj_submitPanel>.imj_submitButton")).click();
		
		return PageFactory.initElements(driver, BrowseFacetsPage.class);
	}
	
	public EditFacetPage editFacet(String facetTitle) {
		WebElement facet = findFacet(facetTitle);
		facet.findElement(By.partialLinkText("Edit")).click();
		return PageFactory.initElements(driver, EditFacetPage.class);
	}
}
