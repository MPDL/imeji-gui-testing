package spot.components;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.registered.DiscardedCollectionEntryPage;
import spot.pages.registered.EditItemsPage;
import spot.pages.registered.EditLicensePage;
import spot.pages.registered.KindOfSharePage;
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
	
	@FindBy(css = ".dropdown>.content>a:nth-of-type(3)")
	private WebElement releaseCollection;
	
	@FindBy(linkText = "Delete")
	private WebElement deleteCollection;
	
	@FindBy(linkText = "Discard")
//	@FindBy(css = ".dropdown>.content>a:nth-of-type(5)")
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
		new Actions(driver).moveToElement(menuItems).moveToElement(editItems).click().build().perform();
		
		//Find the loaderWrapper with the style-attribute to get the actual, non-stale loaderWrapper
		WebElement loaderWrapper = driver.findElement(By.cssSelector(".loaderWrapper[style]"));
		wait.until(ExpectedConditions.invisibilityOf(loaderWrapper));
				
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	public MetadataTablePage editSelectedItems() {
		new Actions(driver).moveToElement(selectedMenu).moveToElement(editSelectedItems).click().build().perform();
		
		//Find the loaderWrapper with the specified parent to get the actual, non-stale loaderWrapper
		WebElement loaderWrapper = driver.findElement(By.cssSelector(".imj_allContentWrapper>.loaderWrapper[style]"));
		wait.until(ExpectedConditions.invisibilityOf(loaderWrapper));
		
		return PageFactory.initElements(driver, MetadataTablePage.class);
	}
	
	public EditLicensePage editAllLicenses() {
		new Actions(driver).moveToElement(menuItems).moveToElement(editLicenses).click().build().perform();

		return PageFactory.initElements(driver, EditLicensePage.class);
	}
	
	public CollectionEntryPage releaseCollection() {
		new Actions(driver).moveToElement(menuCollection).moveToElement(releaseCollection).click().build().perform();
		((JavascriptExecutor) driver).executeScript("document.querySelector('#releaseCollection .imj_submitPanel .imj_submitButton').click();");
		
		//Find the loaderWrapper with the style-attribute to get the actual, non-stale loaderWrapper
		WebElement loaderWrapper = driver.findElement(By.cssSelector(".loaderWrapper[style]"));
		wait.until(ExpectedConditions.invisibilityOf(loaderWrapper));
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionsPage deleteCollection() {
		new Actions(driver).moveToElement(menuCollection).moveToElement(deleteCollection).click().build().perform();
		((JavascriptExecutor) driver).executeScript("document.querySelector('#deleteCollection .imj_submitPanel .imj_submitButton').click();");
		
		return PageFactory.initElements(driver, CollectionsPage.class);
	}
	
	public CollectionsPage discardCollection() {
		new Actions(driver).moveToElement(menuCollection).moveToElement(discardCollection).click().build().perform();
		
		WebElement discardBox = driver.findElement(By.className("imj_dialogReasonText"));
		discardBox.sendKeys("Discarding for testing purposes.");
		try { Thread.sleep(2500); } catch (InterruptedException e) { }
		
		((JavascriptExecutor) driver).executeScript("document.querySelector('#withdrawCollection .imj_submitPanel .imj_submitButton').click();");
		return PageFactory.initElements(driver, CollectionsPage.class);
	}
	
	public CollectionEntryPage setDOI() {
		new Actions(driver).moveToElement(menuCollection).moveToElement(addDOI).click().build().perform();
		
		retryingFindClick(By.cssSelector("#getDOIDialog>form>.imj_submitPanel>.imj_submitButton"));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("lnkCollections")));
		try { Thread.sleep(5000); } catch (InterruptedException e) { }
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
}
