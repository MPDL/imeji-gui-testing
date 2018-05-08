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

public class EditFacetPage extends BasePage {

	@FindBy(xpath = "//input[contains(@id, 'inputFacetName')]")
	private WebElement facetTitleBox;
	
	@FindBy(linkText = "Reset")
	private WebElement resetButton;
	
	@FindBy(className = "imj_submitButton")
	private WebElement submitButton;
	
	public EditFacetPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public BrowseFacetsPage changeSystemFacetSelection(String newSelection) {
		try {
			resetButton.click();
			try { Thread.sleep(2000); } catch (InterruptedException e) {}
			PageFactory.initElements(driver, this);
			
			driver.findElement(By.linkText(newSelection)).click();
			submitButton.click();
			
			return PageFactory.initElements(driver, BrowseFacetsPage.class);
		}
		catch (NoSuchElementException exc) {
			throw new NoSuchElementException("System facet type " + newSelection + " was not found: it either doesn't exist, or is already used by another system facet.");
		}
		catch (StaleElementReferenceException exc) {
			return goToAdminPage().changeSystemFacetSelection(facetTitleBox.getText(), newSelection);
		}
	}
	
	public BrowseFacetsPage changeMetadataFacetSelection(String newSelection) {
		try {
			resetButton.click();
			PageFactory.initElements(driver, this);
			
			WebElement metadataLink = findMetadata(newSelection);
			metadataLink.click();
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			submitButton.click();
			
			return PageFactory.initElements(driver, BrowseFacetsPage.class);
		}
		catch (StaleElementReferenceException exc) {
			return goToAdminPage().changeMetadataFacetSelection(facetTitleBox.getText(), newSelection);
		}
	}
	
	public BrowseFacetsPage renameFacet(String newTitle) {
		facetTitleBox.clear();
		facetTitleBox.sendKeys(newTitle);
		submitButton.click();
		
		return PageFactory.initElements(driver, BrowseFacetsPage.class);
	}
	
	public String getTypeAlias() {
		List<WebElement> metadataSets = driver.findElements(By.className("imj_metadataSet"));
		for (WebElement metadataSet : metadataSets) {
			String label = metadataSet.findElement(By.className("imj_metadataLabel")).getText();
			if (label.equals("Index")) {
				String value = metadataSet.findElement(By.className("imj_metadataValue")).getText();
				return value;
			}
		}
		
		throw new NoSuchElementException("No index name was found.");
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
}
