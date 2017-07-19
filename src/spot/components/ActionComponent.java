package spot.components;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

public class ActionComponent extends BasePage {

	@FindBy(id = "menuItems")
	private WebElement menuItems;
	
	@FindBy(id = "menuCollection")
	private WebElement menuCollection;
	
	@FindBy(css = ".imj_menuBody ul form li:nth-of-type(1) a")
	private WebElement editItems;
	
	@FindBy(css = ".imj_menuBody ul form li:nth-of-type(2) a")
	private WebElement editLicenses;
	
	@FindBy(css = "#menuCollection>.imj_menuBody>ul>li:nth-of-type(3)")
	private WebElement releaseCollection;
	
	@FindBy(linkText = "Delete")
	private WebElement deleteCollection;
	
	@FindBy(css = "#menuCollection>.imj_menuBody>ul>li:nth-of-type(4)>a")
	private WebElement discardCollection;
	
	@FindBy(css = "#menuCollection>.imj_menuBody>ul>li:nth-of-type(3)")
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
	
	public EditLicensePage editAllLicenses() {
		openMenuItems();
		editLicenses.click();
		return PageFactory.initElements(driver, EditLicensePage.class);
	}
	
	public CollectionEntryPage releaseCollection() {
		openMenuCollection();
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
		retryingFindClick(By.cssSelector("#menuCollection>.imj_menuBody>ul>li:nth-of-type(4)>a"));
		
		WebElement discardBox = driver.findElement(By.className("imj_dialogReasonText"));
		discardBox.sendKeys("Discarding for testing purposes.");
		
		retryingFindClick(By.xpath("//input[contains(@id, 'discardForm:btnDiscardContainer')]"));
		return PageFactory.initElements(driver, DiscardedCollectionEntryPage.class);
	}
	
	public CollectionEntryPage setDOI() {
		openMenuCollection();
		addDOI.click();
		retryingFindClick(By.cssSelector("#getDOIDialog .imj_submitButton"));
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	private void openMenuCollection() {
		menuCollection.click();
	}
	
	private void openMenuItems() {
		menuItems.click();
	}
	
}
