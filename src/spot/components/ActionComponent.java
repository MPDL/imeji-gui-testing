package spot.components;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.registered.EditItemsPage;
import spot.pages.registered.EditLicensePage;
import spot.pages.registered.MetadataTablePage;

public class ActionComponent extends BasePage {

	@FindBy(css = "#colForm>.dropdown:nth-of-type(2)")
	private WebElement menuItems;
	
	@FindBy(className = "fa-folder-o")
	private WebElement menuCollection;
	
	@FindBy(css = "#colForm>.dropdown:nth-of-type(2)>.content>a:nth-of-type(1)")
	private WebElement editItems;
	
	@FindBy(css = "#selMenu\\:sf>.dropdown")
	private WebElement selectedMenu;
	
	@FindBy(css = "#selMenu\\:sf>.dropdown>.content>a")
	private WebElement editSelectedItems;
	
	@FindBy(css = "#colForm>.dropdown:nth-of-type(2)>.content>a:nth-of-type(2)")
	private WebElement editLicenses;
	
	@FindBy(linkText = "Publish")
	private WebElement releaseCollection;
	
	@FindBy(linkText = "Delete")
	private WebElement deleteCollection;
	
	@FindBy(linkText = "Discard")
	private WebElement discardCollection;
	
	@FindBy(id = "lnkCreateDOI")
	private WebElement addDOI;
	
	@FindBy(css = "#actionsMenuArea .imj_overlayMenuList li:nth-of-type(5) a")
	private WebElement downloadAllItems;
	
	@FindBy(css = "#actionsMenuArea .imj_menuButton")
	private WebElement downloadAllNRU;
	
	public ActionComponent (WebDriver driver) {
		super(driver);

		PageFactory.initElements(driver, this);
	}
	
	public EditItemsPage editAllItems() {
		new Actions(driver).moveToElement(menuItems).perform();
		editItems.click();
		
		//Find the loaderWrapper with the style-attribute to get the actual, non-stale loaderWrapper
		WebElement loaderWrapper = driver.findElement(By.cssSelector(".loaderWrapper[style]"));
		wait.until(ExpectedConditions.invisibilityOf(loaderWrapper));
				
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	public MetadataTablePage editSelectedItems() {
		new Actions(driver).moveToElement(selectedMenu).perform();
		editSelectedItems.click();
		
		//Find the loaderWrapper with the specified parent to get the actual, non-stale loaderWrapper
		WebElement loaderWrapper = driver.findElement(By.cssSelector(".imj_allContentWrapper>.loaderWrapper[style]"));
		wait.until(ExpectedConditions.invisibilityOf(loaderWrapper));
		
		return PageFactory.initElements(driver, MetadataTablePage.class);
	}
	
	public EditLicensePage editAllLicenses() {
		new Actions(driver).moveToElement(menuItems).perform();
		editLicenses.click();

		return PageFactory.initElements(driver, EditLicensePage.class);
	}
	
	public CollectionEntryPage releaseCollection() {
		new Actions(driver).moveToElement(menuCollection).perform();
		releaseCollection.click();
		
		((JavascriptExecutor) driver).executeScript("document.querySelector('#releaseCollection .imj_submitPanel .imj_submitButton').click();");
		
		//Find the loaderWrapper with the style-attribute to get the actual, non-stale loaderWrapper
		WebElement loaderWrapper = driver.findElement(By.cssSelector(".loaderWrapper[style]"));
		wait.until(ExpectedConditions.invisibilityOf(loaderWrapper));
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionsPage deleteCollection() {
		new Actions(driver).moveToElement(menuCollection).perform();
		deleteCollection.click();
		
		((JavascriptExecutor) driver).executeScript("document.querySelector('#deleteCollection .imj_submitPanel .imj_submitButton').click();");
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text()[2],'New collection')]")));
		
		return PageFactory.initElements(driver, CollectionsPage.class);
	}
	
	public CollectionsPage discardCollection() {
		new Actions(driver).moveToElement(menuCollection).perform();
		discardCollection.click();
		
		WebElement discardBox = driver.findElement(By.className("imj_dialogReasonText"));
		discardBox.sendKeys("Discarding for testing purposes.");
		try { Thread.sleep(2500); } catch (InterruptedException e) { }
		
		((JavascriptExecutor) driver).executeScript("document.querySelector('#withdrawCollection .imj_submitPanel .imj_submitButton').click();");
		
		wait.until(ExpectedConditions.stalenessOf(discardBox));
		
		return PageFactory.initElements(driver, CollectionsPage.class);
	}
	
	public CollectionEntryPage setDOI() {
		WebElement staleElement = driver.findElement(By.id("pageTitle"));
		
		new Actions(driver).moveToElement(menuCollection).perform();
		addDOI.click();
		
		retryingFindClick(By.cssSelector("#getDOIDialog>form>.imj_submitPanel>.imj_submitButton"));
		
		//Waiting for the staleness doesn't seem to be enough. Sometimes the loading-screen (loaderWrapper) is still visible , which leads to errors.
		//Why is the waiting for the staleness, in this case, not sufficient?
		wait.until(ExpectedConditions.stalenessOf(staleElement));
		//=> Added an additional wait
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pageTitle")));
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
}
