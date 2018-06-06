package spot.pages;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import spot.components.ActionComponent;
import spot.components.SearchComponent.CategoryType;
import spot.components.ShareComponent;
import spot.components.UploadWindow;
import spot.pages.registered.EditCollectionPage;
import spot.pages.registered.EditItemsPage;
import spot.pages.registered.EditLicensePage;
import spot.pages.registered.KindOfSharePage;
import spot.pages.registered.MetadataTablePage;
import spot.pages.registered.SharePage;

public class CollectionEntryPage extends BasePage {

	private ActionComponent actionComponent;
	private ShareComponent shareComponent;
	
	@FindBy(css = ".content a:nth-of-type(2)")
	private WebElement share;
	
	@FindBy(css=".imj_contentMenu>ul>li:nth-of-type(4)")
	private WebElement uploadContentButton;
	
	@FindBy(id = "actionMenu")
	private WebElement actionMenu;
	
	@FindBy(className = "fa-files-o")
	private WebElement selectedItemsMenu;
	
	@FindBy(css = "#colForm>.dropdown")
	private WebElement editButton;
	
	@FindBy(id = "colForm:upload")
	private WebElement uploadButton;
	
	@FindBy(className = "fa-download")
	private WebElement downloadButton;
	
	@FindBy(css = "#colForm>.dropdown>.content>a:nth-of-type(1)")
	private WebElement editInfoOption;
	
	@FindBy(css = "#pageTitle h1")
	private WebElement title;
	
	@FindBy(css = ".collectionAboutHidden>.imj_infodataSet:nth-of-type(3)>.imj_infodataValue")
	private WebElement author;
	
	@FindBy(css = "#description>pre")
	private WebElement description;
	
	@FindBy(className = "imj_entryPagePreviewPicture")
	private WebElement logo;
	
	@FindBy(className = "imj_statusHeaderArea")
	private WebElement status;
	
	@FindBy(css = ".imj_infodataSet .imj_infodataValue a")
	private WebElement doiLink;
	
	@FindBy(css=".moreCollection")
	private WebElement aboutLink;
	
	@FindBy(css = ".imj_menuButton .fa-pencil")
	private WebElement editCollection;
	
	@FindBy(css=".imj_tiledMediaList")
	private WebElement tiledMediaList;
	
	@FindBy(id="selPanel:preListForm:lblSelectedSize")
	private WebElement selectedItemCount;
	
	@FindBy(id = "dialogDeleteAll")
	private WebElement deleteAllDialog;
	
	@FindBy(id = "facetsArea")
	private WebElement facetsArea;
	
	@FindBy(css = "#imj_rangeSelector>select")
	private WebElement rangeSelector;
	
	@FindBy(css = ".listActionMenuTitle>.fa-cog")
	private WebElement displayMenu;
	
	@FindBy(css = ".listActionMenuTitle>span")
	private WebElement totalItemNumberWebElement;
	
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
	
	public String getAuthor() {
		return openDescription().getValue("Authors");
	}
	
	public String getDescription() {
		openDescription();
		return description.getText();
	}
	
	public EditCollectionPage editInformation() {
		new Actions(driver).moveToElement(editButton).moveToElement(editInfoOption).click().build().perform();
		
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
	
	public boolean hasThumbnailView() {
		return isElementPresent(By.id("imgFrame"));
	}
	
	public String getValue(String label) {
		openDescription();
		List<WebElement> labels = driver.findElements(By.className("imj_infodataLabel"));
		List<WebElement> values = driver.findElements(By.className("imj_infodataValue"));
		int numLabels = labels.size();
		for (int i = 0; i < numLabels; i++) {
			try {
				WebElement currentLabel = labels.get(i);
				if (label.equals(currentLabel.getText())) {
					WebElement value = values.get(i);
					return value.getText();
				}
			}
			catch (NoSuchElementException exc) {}
		}
		
		throw new NoSuchElementException("Label is not present.");
	}
	
	public int getTotalItemNumber() {
		waitForInitationPageElements(10);
		
		String stringCount = StringUtils.substringBefore(totalItemNumberWebElement.getText(), " Item");
		return Integer.parseInt(stringCount);
	}
	
	public boolean findItem(String title) {
		wait.until(ExpectedConditions.elementToBeClickable(aboutLink));
		List<WebElement> itemList = getItemList();
		if (thumbnailView()) {
			for (WebElement item : itemList) {
				WebElement itemTitleLabel = item.findElement(By.cssSelector("span>label"));
				if (itemTitleLabel.getText().equals(title))
					return true;
			}
		}
		else {
			for (WebElement item : itemList) {
				WebElement itemTitleLabel =  item.findElement(By.id("lnkPicDetailBrowse"));
				if (itemTitleLabel.getText().equals(title))
					return true;
			}
		}
		return false;
	}
	
	private int getItemIndex(String title) {
		//wait.until(ExpectedConditions.elementToBeClickable(aboutLink));
		List<WebElement> itemList = getItemList();
		int i = 0;
		if (thumbnailView()) {
			for (WebElement item : itemList) {
				if (item.findElement(By.tagName("label")).getText().equals(title)) {
						return i;
				}
				i++;
			}
		}
		else {
			for (WebElement item : itemList) {
				if (item.findElement(By.cssSelector(".order-table tr .imj_colFilename a")).getAttribute("title").equals(title)) {
						return i;
				}
				i++;
				}
				
			}
		throw new NoSuchElementException("Item with title " + title + " does not exist.");
	}
	
	public int getItemListSize() {
		return getItemList().size();		
	}
	
	public List<WebElement> getItemList() {
		// assumes collection is not empty. will create problems if we want to test if the collection is empty
		List<WebElement> itemList;
		if (thumbnailView()) {
			itemList = driver.findElements(By.className("thumb"));
		}
		else {
			// list view
			itemList = driver.findElements(By.className("imj_colFilename"));
		}
		return itemList;
	}
	
	/*
	 * Use only for non-empty collections.
	 */
	public boolean thumbnailView() {
		return isElementPresent(By.className("thumb"));
	}
	
	public CollectionEntryPage releaseCollection() {
		return actionComponent.releaseCollection();
	}
	
	public CollectionsPage deleteCollection() {
		return actionComponent.deleteCollection();
	}
	
	public CollectionsPage discardCollection() {
		return actionComponent.discardCollection();
	}
	
	public SharePage share() {
		new Actions(driver).moveToElement(editButton).moveToElement(share).click().build().perform();
		
		return PageFactory.initElements(driver, SharePage.class);
	}
	
	public ItemViewPage downloadFirstItemInList() {
		WebElement firstItem = getItemList().get(0);
		firstItem.findElement(By.id("browseContent:pictureList:0:itemSelectForm:lnkPicDetailBrowse")).click();
		
		return PageFactory.initElements(driver, ItemViewPage.class);
	}
	
	public CollectionEntryPage deleteItem(String title) {
		return deleteItem(getItemIndex(title));
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
		item.findElement(By.tagName("a")).click();
		
		return PageFactory.initElements(driver, ItemViewPage.class);
	}
	
	public ItemViewPage openItem(String title) {
		return openItem(getItemIndex(title));
	}
	
	private void validateIndex(int index) {
		int itemCount = getItemListSize();
		if (index >= itemCount)
			throw new IllegalArgumentException("No item with index " + index + ". Total count: " + itemCount);
	}
	
	public CollectionEntryPage openDescription() {
		((JavascriptExecutor) driver).executeScript("arguments[0].style.visibility = 'visible';", aboutLink);
		((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", aboutLink);
		try {
			aboutLink.click();
		}
		catch (ElementNotVisibleException exc) {
			// description is already open
		}
		
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
		wait.until(ExpectedConditions.elementToBeClickable(By.id("lnkCollections")));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("colForm:upload")));
		// avoid 'element not clickable' exceptions, lasts about 4 seconds
		
		//Find the loaderWrapper with the style-attribute to get the actual, non-stale loaderWrapper
		WebElement loaderWrapper = driver.findElement(By.cssSelector(".loaderWrapper[style]"));
		wait.until(ExpectedConditions.invisibilityOf(loaderWrapper));
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionEntryPage setDOI() {
		return actionComponent.setDOI();
	}
	
	public String getDOI() {
		String doi = getValue("DOI");
		return doi.substring("http://dx.doi.org/".length());
	}
	
	public boolean downloadAllPossible() {
		return downloadButton.isDisplayed() && downloadButton.isEnabled();
	}
	
	public void downloadAll() {
		downloadButton.click();
		new Actions(driver).sendKeys(Keys.ENTER);
	}
	
	public boolean downloadSelectedPossible() {
		selectedItemsMenu.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("fa-download")));
		WebElement downloadSelected = driver.findElement(By.className("fa-download"));
		return downloadSelected.isDisplayed() && downloadSelected.isEnabled();
	}
	
	public void downloadSelected() {
		downloadButton.click();
		new Actions(driver).sendKeys(Keys.ENTER);
	}
	
	public MetadataTablePage editSelectedItems() {
		return actionComponent.editSelectedItems();
	}
	
	public EditItemsPage editAllItems() {
		return actionComponent.editAllItems();
	}
	
	public EditLicensePage editAllLicenses() {
		return actionComponent.editAllLicenses();
	}
	
	public boolean metadataDisplayed(String title, String key, String value) {
		ItemViewPage itemView = openItem(title);
		boolean metadataPresent = itemView.metadataPresent(key, value);
		itemView.goToCollectionEntry();
		return metadataPresent;
	}
	
	public boolean metadataDisplayed(String title, String key) {
		ItemViewPage itemView = openItem(title);
		boolean metadataPresent = itemView.metadataPresent(key);
		itemView.goToCollectionEntry();
		return metadataPresent;
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
	
	public boolean facetDisplayed(String key, String value) {
		List<WebElement> facets = facetsArea.findElements(By.className("imj_facet"));
		
		for (WebElement facet : facets) {
			String currentKey = facet.findElement(By.className("imj_facetName")).getText();
			if (currentKey.equals(key)) {
				List<WebElement> values = facet.findElement(By.className("imj_facetValue")).findElements(By.tagName("a"));
				for (WebElement currentValue : values) {
					String currentValueText = currentValue.getText();
					if (currentValueText.equals(value)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean checkLicenseAll(String link) {
		List<WebElement> itemList = getItemList();
		boolean licensePresent = true;
		
		for (int i = 0; i < itemList.size(); i++) {
			ItemViewPage itemView = openItem(i);
			itemView.hideMessages();
			licensePresent = licensePresent && itemView.licensePresent(link);
			itemView.goToCollectionEntry();
		}
		
		return licensePresent;
	}
	
	public CollectionEntryPage selectItem(int index) {
		driver.findElement(By.id("th:f:i:" + index + ":sel")).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("th:f:i:0:sel")));
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionEntryPage selectItem(String title) {
		return selectItem(getItemIndex(title));
	}
	
	/**
	 * Deselect all selected items.
	 * Works only if the CollectionEntryPage is reloaded or visited again after the selection.
	 * @return the CollectionEntryPage
	 */
	public CollectionEntryPage deselectAllSelectedItems() {
		List<WebElement> selectedCheckboxes = driver.findElements(By.cssSelector("[class='imj_optionCheckbox'][id^='th:f:i:'][checked='checked']"));
		for (WebElement selectedCheckbox : selectedCheckboxes) {
			selectedCheckbox.click();
		}
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionEntryPage deleteSelectedItems() {
		driver.findElement(By.cssSelector("#selMenu\\:sf>.dropdown")).click();
		retryingFindClick(By.cssSelector("#selMenu\\:sf>.dropdown>.content>a:nth-of-type(5)"));
		((JavascriptExecutor) driver).executeScript("document.querySelector('#deleteSelectedItems .imj_submitPanel .imj_submitButton').click();");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("colForm:upload")));
		
		//Find the loaderWrapper with the style-attribute to get the actual, non-stale loaderWrapper
		WebElement loaderWrapper = driver.findElement(By.cssSelector(".loaderWrapper[style]"));
		wait.until(ExpectedConditions.invisibilityOf(loaderWrapper));
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionEntryPage discardSelectedItems() {
		driver.findElement(By.cssSelector("#selMenu\\:sf>.dropdown")).click();
		retryingFindClick(By.cssSelector("#selMenu\\:sf>.dropdown>.content>a:nth-of-type(4)"));
		driver.findElement(By.id("selDialogs:witdrawSel:f:discardComment")).sendKeys("Discarding for testing purposes.");
		((JavascriptExecutor) driver).executeScript("document.querySelector('#withdrawSelectedItems .imj_submitPanel .imj_submitButton').click();");
		wait.until(ExpectedConditions.elementToBeClickable(By.id("colForm:upload")));
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionEntryPage moveSelectedItemsToPrivateCollection(String collectionTitle) {
		retryingFindClick(By.id("menuItems"));
		retryingFindClick(By.cssSelector("#menuItems .imj_menuBody ul li:nth-of-type(3) a"));
		List<WebElement> collectionsList = driver.findElements(By.cssSelector("#actionSelectedItems\\:moveItemsComponent\\:moveItemsForm\\:list>p"));
		int collections = collectionsList.size();
		for (int i = 1; i <= collections; i++) {
			String selector = "#actionSelectedItems\\:moveItemsComponent\\:moveItemsForm\\:list>p:nth-of-type(" + i + ")>a";
			WebElement currentCollection = driver.findElement(By.cssSelector(selector));
			if (currentCollection.getText().equals(collectionTitle)) {
				currentCollection.click();
				return PageFactory.initElements(driver, CollectionEntryPage.class);
			}
		}
		
		throw new NoSuchElementException("Collection " + collectionTitle + " was not found in list.");
	}
	
	public CollectionEntryPage moveSelectedItemsToReleasedCollection(String collectionTitle) {
		retryingFindClick(By.id("menuItems"));
		retryingFindClick(By.cssSelector("#menuItems .imj_menuBody ul li:nth-of-type(3) a"));
		List<WebElement> collectionsList = driver.findElements(By.cssSelector("#actionSelectedItems\\:moveItemsComponent\\:moveItemsForm\\:list>p"));
		int collections = collectionsList.size();
		for (int i = 1; i <= collections; i++) {
			String selector = "#actionSelectedItems\\:moveItemsComponent\\:moveItemsForm\\:list>p:nth-of-type(" + i + ")>a";
			WebElement currentCollection = driver.findElement(By.cssSelector(selector));
			if (currentCollection.getText().equals(collectionTitle)) {
				currentCollection.click();
				WebElement moveDialog = driver.findElement(By.id("moveItemsDialog"));
				moveDialog.findElement(By.className("imj_submitButton")).click();
				return PageFactory.initElements(driver, CollectionEntryPage.class);
			}
		}
		
		throw new NoSuchElementException("Collection " + collectionTitle + " was not found in list.");
	}
	
	/**
	 * Sets number of items per page to the maximum: useful for tests with big collections
	 * The maximum varies from browser to browser
	 */
	public CollectionEntryPage displayMaximalItems() {
		Select rangeSelect = new Select(rangeSelector);
		int optionCount = rangeSelect.getOptions().size();
		rangeSelect.selectByIndex(optionCount - 1);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.className("imj_itemsAreaContent")));
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionEntryPage enableThumbnailView() {
		displayMenu.click();
		List<WebElement> thumbnailViewButtons = driver.findElements(By.xpath("//a[contains(@id, 'switchListView')]"));
		if (!thumbnailViewButtons.isEmpty()) {
			new Actions(driver).moveToElement(displayMenu).moveToElement(thumbnailViewButtons.get(0)).click().build().perform();
		}
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionEntryPage enableListView() {
		displayMenu.click();
		List<WebElement> listViewButtons = driver.findElements(By.xpath("//a[title='detail list']"));
		if (!listViewButtons.isEmpty()) {
			new Actions(driver).moveToElement(displayMenu).moveToElement(listViewButtons.get(0)).click().build().perform();
		}
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
}
