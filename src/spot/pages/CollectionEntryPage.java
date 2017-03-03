package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.components.ActionComponent;
import test.base.CategoryType;
import spot.components.ShareComponent;
import spot.components.UploadWindow;
import spot.pages.registered.EditItemsPage;

public class CollectionEntryPage extends BasePage {

	private ActionComponent actionComponent;
	private ShareComponent shareComponent;
	
	@FindBy(css = "#actionsMenuArea a:nth-of-type(2)")
	private WebElement share;
	
	@FindBy(css=".imj_contentMenu>ul>li:nth-of-type(4)")
	private WebElement uploadContentButton;
	
	@FindBy(id = "actionMenu")
	private WebElement actionMenu;
	
	@FindBy(id = "editMenu")
	private WebElement editButton;
	
	@FindBy(id = "upload")
	private WebElement uploadButton;
	
	@FindBy(id = "download")
	private WebElement downloadButton;
	
	@FindBy(id = "action:actionMenuEditCollection")
	private WebElement editInfoOption;
	
	@FindBy(css = ".imj_siteContentHeadline h1")
	private WebElement title;
	
	@FindBy(className = "imj_entryPagePreviewPicture")
	private WebElement logo;
	
	@FindBy(className = "imj_statusHeaderArea")
	private WebElement status;
	
	@FindBy(id = "description")
	private WebElement description;
	
	@FindBy(css = ".imj_infodataSet .imj_infodataValue a")
	private WebElement doiLink;
	
	@FindBy(css=".collectionAbout .fa-caret-down")
	private WebElement aboutLink;
	
	@FindBy(css = ".imj_menuButton .fa-pencil")
	private WebElement editCollection;
	
	@FindBy(css=".imj_tiledMediaList")
	private WebElement tiledMediaList;
	
	@FindBy(css="div.imj_overlayMenu.imj_menuHeader")
	private WebElement totalItemNumberWebElement;
	
	@FindBy(id="selPanel:preListForm:lblSelectedSize")
	private WebElement selectedItemCount;
	
	@FindBy(id = "dialogDeleteAll")
	private WebElement deleteAllDialog;
	
	private int totalItemNumber;
	
	public CollectionEntryPage(WebDriver driver) {
		super(driver);
		
		actionComponent = new ActionComponent(driver);
		shareComponent = new ShareComponent(driver);
		
		PageFactory.initElements(driver, this);
	}

	public ActionComponent getActionComponent() {
		return actionComponent;
	}
	
	public String getTitle() {
		return title.getText();
	}

	public MultipleUploadPage uploadContent() {
		uploadContentButton.click();
		
		return PageFactory.initElements(driver, MultipleUploadPage.class);
	}
	
	public EditCollectionPage editInformation() {
		editButton.click();
		wait.until(ExpectedConditions.visibilityOf(editInfoOption));
		editInfoOption.click();
		
		return PageFactory.initElements(driver, EditCollectionPage.class);
	}
	
	public KindOfSharePage goToSharePage() {
		return shareComponent.share(CategoryType.COLLECTION);
	}
	
	/* TODO: icons as private attributes */
	public boolean shareIconVisible() {
		try {
			driver.findElement(By.cssSelector(".imj_statusHeaderArea .fa-users"));
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	/* TODO: icons as private attributes */
	public boolean releasedIconVisible() {
		try {
			driver.findElement(By.className("fa-globe"));
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	public boolean labelDisplayed(String label) {
		List<WebElement> allSets = driver.findElements(By.className("imj_infodataSet"));
		for (WebElement set : allSets) {
			if (set.getText().contains(label))
				return true;
		}
		return false;
	}
	
	public void declareTotalItemNumber() {
		waitForInitationPageElements(10);
		
		String[] split = totalItemNumberWebElement.getText().split("\\s+");
		try {
			totalItemNumber = Integer.parseInt(split[1]);
		} catch (NumberFormatException nfe) {
			totalItemNumber = -1;
		}
	}

	public int getTotalItemNumber() {
		return totalItemNumber;
	}
	
	public boolean findItem(String title) {
		wait.until(ExpectedConditions.elementToBeClickable(aboutLink));
		List<WebElement> itemList = getItemList();
		for (WebElement item : itemList) {
			if (item.getAttribute("title").equals(title))
				return true;
		}
		return false;
	}
	
	public int getItemListSize() {
		List<WebElement> mediaList = getItemList();
		return mediaList.size();		
	}
	
	public List<WebElement> getItemList() {
		List<WebElement> itemList = driver.findElements(By.cssSelector(".imj_tileItem img"));
		return itemList;
	}
	
	public CollectionEntryPage releaseCollection() {
		return actionComponent.releaseCollection();
	}
	
	public CollectionsPage deleteCollection() {
		return actionComponent.deleteCollection();
	}
	
	public DiscardedCollectionEntryPage discardCollection() {
		return actionComponent.discardCollection();
	}
	
	public KindOfSharePage share() {
		share.click();
		
		return PageFactory.initElements(driver, KindOfSharePage.class);
	}

	public ItemViewPage downloadFirstItemInList() {
		WebElement firstItem = getItemList().get(0);
		firstItem.findElement(By.id("browseContent:pictureList:0:itemSelectForm:lnkPicDetailBrowse")).click();
		
		return PageFactory.initElements(driver, ItemViewPage.class);
	}
	
	public CollectionEntryPage deleteItem(int index) {
		ItemViewPage itemView = openItem(index);
		return itemView.deleteItem();
	}
	
	public CollectionEntryPage discardItem(int index) {
		ItemViewPage itemView = openItem(index);
		return itemView.discardItem();
	}
	
	public CollectionEntryPage deleteAllItems() {
		WebElement actionMenu = driver.findElement(By.id("actionMenu"));
		actionMenu.click();
		actionMenu.findElement(By.cssSelector("li:nth-of-type(3)")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dialogDeleteAll")));
		deleteAllDialog.findElement(By.className("imj_submitButton")).click();
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public ItemViewPage openItem(int index) {
		validateIndex(index);
		WebElement item = getItemList().get(index);
		item.click();
		
		return PageFactory.initElements(driver, ItemViewPage.class);
	}
	
	private void validateIndex(int index) {
		int itemCount = getItemListSize();
		if (index > itemCount)
			throw new IllegalArgumentException("No item with index " + index + ". Total count: " + itemCount);
	}
	
	public CollectionEntryPage openDescription() {
		((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility = 'visible';", aboutLink);
		((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", aboutLink);
		aboutLink.click();
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public boolean hasLogo() {
		openDescription();
		
		try {
			return logo.isDisplayed();
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	public CollectionEntryPage uploadFile(String filepath) {
		uploadButton.click();
		UploadWindow upload = new UploadWindow(driver);
		upload.uploadFile(filepath);
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionEntryPage setDOI(String doi) {
		return actionComponent.setDOI(doi);
	}
	
	public String getDOI() {
		openDescription();
		String doi = doiLink.getText();
		return doi.substring("http://dx.doi.org/".length());
	}
	
	public boolean downloadAllPossible() {
		return actionComponent.canDownloadItems();
	}
	
	public boolean downloadAllPossibleReadOnly() {
		WebElement downloadButton = driver.findElement(By.cssSelector("#actionsMenuArea a"));
		return downloadButton.isDisplayed() && downloadButton.isEnabled();
	}
	
	public boolean downloadSelectedPossible() {
		return downloadButton.isDisplayed() && downloadButton.isEnabled();
	}
	
	public EditItemsPage editAllItems() {
		return actionComponent.editAllItems();
	}
	
	public EditLicensePage editAllLicenses() {
		return actionComponent.editAllLicenses();
	}
	
	public boolean metadataDisplayed(int index, String key, String value) {
		ItemViewPage itemView = openItem(index);
		boolean metadataPresent = itemView.metadataPresent(key, value);
		itemView.goToCollectionEntry();
		return metadataPresent;
	}
	
	public boolean metadataDisplayedAll(String key, String value) {
		List<WebElement> itemList = getItemList();
		boolean metadataPresent = true;
		
		for (int i = 0; i < itemList.size(); i++) {
			metadataPresent = metadataPresent && metadataDisplayed(i, key, value);
		}
		
		return metadataPresent;
	}
	
	public boolean checkLicenseAll(String link) {
		List<WebElement> itemList = getItemList();
		boolean licensePresent = true;
		
		for (int i = 0; i < itemList.size(); i++) {
			ItemViewPage itemView = openItem(i);
			licensePresent = licensePresent && itemView.licensePresent(link);
			itemView.goToCollectionEntry();
		}
		
		return licensePresent;
	}
	
	public CollectionEntryPage selectItem(int index) {
		driver.findElement(By.id("browseContent:pictureList:" + index + ":itemSelectForm:pictureCheckbox")).click();
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
}
