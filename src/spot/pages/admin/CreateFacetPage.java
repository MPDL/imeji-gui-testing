package spot.pages.admin;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;
import test.base.SeleniumWrapper;

public class CreateFacetPage extends BasePage {

	@FindBy(xpath = "//input[contains(@id, 'inputFacetName')]")
	private WebElement facetTitleBox;
	
	@FindBy(className = "imj_submitButton")
	private WebElement submitButton;
	
	public CreateFacetPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public WebElement findMetadata(String metadata) {
		driver.findElement(By.partialLinkText("Select metadata")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("selectStatementDialog")));
		WebElement statementDialog = driver.findElement(By.id("selectStatementDialog"));
		
		List<WebElement> unusedFacets = statementDialog.findElements(By.tagName("a"));
		for (WebElement facetLink : unusedFacets) {
			if (facetLink.getText().equals(metadata)) {
//				driver.findElement(By.className("imj_cancelButton")).click();
				return facetLink;
			}
		}
		driver.findElement(By.className("imj_cancelButton")).click();
		throw new NoSuchElementException("Metadata with the name " + metadata + " was not found.");
	}
	
	public boolean metadataListed(String metadata) {
		try {
			findMetadata(metadata);
			driver.findElement(By.className("imj_cancelButton")).click();
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	public BrowseFacetsPage createItemFacet(String facetTitle, String facetIndexType) {
		String facetObjectType = "item";		
		return this.createSystemFacet(facetTitle, facetIndexType, facetObjectType);
	}
	
	public BrowseFacetsPage createCollectionFacet(String facetTitle, String facetIndexType) {
		String facetObjectType = "collection";		
		return this.createSystemFacet(facetTitle, facetIndexType, facetObjectType);
	}
	
	private BrowseFacetsPage createSystemFacet(String facetTitle, String facetIndexType, String facetObjectType) {
		try {
			facetTitleBox.sendKeys(facetTitle);
			String facetIndexLinkText = facetIndexType + " " + "(" + facetObjectType + ")";
			driver.findElement(By.linkText(facetIndexLinkText)).click();
			
			WebElement indexLabel = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[@class='imj_metadataLabel' and text()=' Index']")));
			wait.until(ExpectedConditions.elementToBeClickable(submitButton));
			submitButton.click();
			
			SeleniumWrapper.waitForPageLoad(wait, indexLabel);
			
			return PageFactory.initElements(driver, BrowseFacetsPage.class);
		}
		catch (NoSuchElementException exc) {
			throw new NoSuchElementException("System facet type " + facetIndexType + " was not found: it either doesn't exist, or is already used by another system facet.");
		}
	}
	
	public BrowseFacetsPage createMetadataFacet(String facetTitle, String metadata) {
		facetTitleBox.sendKeys(facetTitle);
		
		WebElement metadataLink = findMetadata(metadata);
		metadataLink.click();
		
		WebElement indexLabel = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[@class='imj_metadataLabel' and text()=' Index']")));
		wait.until(ExpectedConditions.elementToBeClickable(submitButton));
		submitButton.click();
		
		SeleniumWrapper.waitForPageLoad(wait, indexLabel);
		
		return PageFactory.initElements(driver, BrowseFacetsPage.class);
	}
}
