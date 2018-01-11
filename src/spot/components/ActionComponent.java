package spot.components;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.DiscardedCollectionEntryPage;
import spot.pages.EditLicensePage;
import spot.pages.KindOfSharePage;
import spot.pages.registered.EditItemsPage;
import spot.pages.registered.MetadataTablePage;

public class ActionComponent extends BasePage {

	@FindBy(css = ".dropdown:nth-of-type(2)>a")
	private WebElement menuItems;
	
	@FindBy(className = "fa-folder-o")
	private WebElement menuCollection;
	
	@FindBy(css = ".dropdown:nth-of-type(2)>.content>a:nth-of-type(1)")
	private WebElement editItems;
	
	@FindBy(css = "#menuItems .imj_menuBody ul li:nth-of-type(1) a")
	private WebElement editSelectedItems;
	
	@FindBy(css = ".dropdown:nth-of-type(2)>.content>a:nth-of-type(2)")
	private WebElement editLicenses;
	
	@FindBy(css = ".dropdown>.content>a:nth-of-type(3)")
	private WebElement releaseCollection;
	
	@FindBy(linkText = "Delete")
	private WebElement deleteCollection;
	
	@FindBy(css = "#menuCollection>.imj_menuBody>ul>li:nth-of-type(4)>a")
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
		openMenuItems();
		editItems.click();
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	public MetadataTablePage editSelectedItems() {
		openMenuItems();
		editSelectedItems.click();
		return PageFactory.initElements(driver, MetadataTablePage.class);
	}
	
	public EditLicensePage editAllLicenses() {
		openMenuItems();
		editLicenses.click();
		return PageFactory.initElements(driver, EditLicensePage.class);
	}
	
	public CollectionEntryPage releaseCollection() {
		openMenuCollection();
		wait.until(ExpectedConditions.visibilityOf(releaseCollection));
		releaseCollection.click();
		((JavascriptExecutor) driver).executeScript("document.querySelector('#releaseCollection .imj_submitPanel .imj_submitButton').click();");

		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionsPage deleteCollection() {
		openMenuCollection();
		deleteCollection.click();
		((JavascriptExecutor) driver).executeScript("document.querySelector('#deleteCollection .imj_submitPanel .imj_submitButton').click();");
		
		return PageFactory.initElements(driver, CollectionsPage.class);
	}
	
	public DiscardedCollectionEntryPage discardCollection() {
		openMenuCollection();
		retryingFindClick(By.cssSelector(".dropdown>.content>a:nth-of-type(4)"));
		
		WebElement discardBox = driver.findElement(By.className("imj_dialogReasonText"));
		discardBox.sendKeys("Discarding for testing purposes.");
		try { Thread.sleep(2500); } catch (InterruptedException e) { }
		
		((JavascriptExecutor) driver).executeScript("document.querySelector('#withdrawCollection .imj_submitPanel .imj_submitButton').click();");
		return PageFactory.initElements(driver, DiscardedCollectionEntryPage.class);
	}
	
	public CollectionEntryPage setDOI() {
		openMenuCollection();
		addDOI.click();
		retryingFindClick(By.cssSelector("#getDOIDialog>form>.imj_submitPanel>.imj_submitButton"));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("lnkCollections")));
		try { Thread.sleep(2500); } catch (InterruptedException e) { }
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	private void openMenuCollection() {
		menuCollection.click();
	}
	
	private void openMenuItems() {
		menuItems.click();
	}
	
}
