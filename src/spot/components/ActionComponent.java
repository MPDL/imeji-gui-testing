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

	@FindBy(id = "actionCollectionMore")
	private WebElement menu;
	
	@FindBy(css = "#actionsMenuArea .imj_overlayMenuList li a")
	private WebElement editItems;
	
	@FindBy(css = "#actionsMenuArea .imj_overlayMenuList li:nth-of-type(2) a")
	private WebElement editLicenses;
	
	@FindBy(css = "#actionsMenuArea .imj_overlayMenuList li:nth-of-type(3) a")
	private WebElement releaseCollection;
	
	@FindBy(css = "#actionsMenuArea .imj_overlayMenuList li:nth-of-type(4) a")
	private WebElement deleteCollection;
	
	@FindBy(css = "#actionsMenuArea .imj_overlayMenuList li:nth-of-type(3) a")
	private WebElement discardCollection;
	
	@FindBy(css = "#actionsMenuArea .imj_overlayMenuList li:nth-of-type(4) a")
	private WebElement addDOI;
	
	@FindBy(css = "#actionsMenuArea .imj_overlayMenuList li:nth-of-type(5) a")
	private WebElement downloadAllItems;
	
	public ActionComponent (WebDriver driver) {
		super(driver);

		PageFactory.initElements(driver, this);
	}
	
	public EditItemsPage editAllItems() {
		openMenu();
		editItems.click();
		return PageFactory.initElements(driver, EditItemsPage.class);
	}
	
	public EditLicensePage editAllLicenses() {
		openMenu();
		editLicenses.click();
		return PageFactory.initElements(driver, EditLicensePage.class);
	}
	
	public CollectionEntryPage releaseCollection() {
		openMenu();
		releaseCollection.click();
		retryingFindClick(By.className("imj_submitButton"));
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionsPage deleteCollection() {
		openMenu();
		deleteCollection.click();
		return PageFactory.initElements(driver, CollectionsPage.class);
	}
	
	public DiscardedCollectionEntryPage discardCollection() {
		openMenu();
		discardCollection.click();
		
		WebElement discardBox = driver.findElement(By.className("imj_dialogReasonText"));
		discardBox.sendKeys("Discarding for testing purposes.");
		
		retryingFindClick(By.xpath("//input[contains(@id, 'discardForm:btnDiscardContainer')]"));
		return PageFactory.initElements(driver, DiscardedCollectionEntryPage.class);
	}
	
	public CollectionEntryPage setDOI(String doi) {
		openMenu();
		addDOI.click();
		
		WebElement doiBox = driver.findElement(By.xpath("//input[contains(@id, 'discardForm:discardComment')]"));
		doiBox.clear();
		doiBox.sendKeys(doi);
		
		retryingFindClick(By.xpath("//input[@value='Save']"));
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public boolean canDownloadItems() {
		openMenu();
		return downloadAllItems.isDisplayed() && downloadAllItems.isEnabled();
	}
	
	private void openMenu() {
		menu.click();
	}
	
}
