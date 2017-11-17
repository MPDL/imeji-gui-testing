package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.google.common.base.Predicate;

import spot.components.ShareComponent;
import test.base.CategoryType;

public class ItemViewPage extends BasePage {

	private ShareComponent shareComponent;

	@FindBy(css=".imj_siteContentHeadline h1")
	private WebElement fileName;
	
	@FindBy(id="picWebResolutionInternalDigilib")
	private WebElement fileResolution;
	
	@FindBy(css="#actionsMenuArea .fa-download")
	private WebElement downloadMenu;
	
	@FindBy(css=".imj_siteContentHeadline>h1>a")
	private WebElement collectionName;
	
	@FindBy(css="#actionsMenuArea>div a[target='_blank']")
	private WebElement downloadOriginalFileButton;
	
	@FindBy(css=".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(1)>.imj_metadataValue")
	private WebElement titleLabel;
	
	@FindBy(css=".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(2)>.imj_metadataValue")
	private WebElement idLabel;
	
	@FindBy(css=".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(3)>.imj_metadataValue")
	private WebElement authorFamilyNameLabel;
	
	@FindBy(css=".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(4)>.imj_metadataValue")
	private WebElement publicationLinkLabel;
	
	@FindBy(css=".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(5)>.imj_metadataValue")
	private WebElement dateLabel;
	
	@FindBy(css=".fa-hand-o-right")
	private WebElement moveButton;
	
	@FindBy(css="#actionsMenuArea>form>a")
	private WebElement editButton;
	
	@FindBy(css = "#actions .fa-trash")
	private WebElement deleteLink;
	
	@FindBy(id = "deleteItem")
	private WebElement deleteItemDialog;
	
	@FindBy(css = "#actionsMenuArea a:nth-of-type(2)")
	private WebElement discardButton;
	
	@FindBy(id = "withdrawItem")
	private WebElement discardItemDialog;
	
	@FindBy(css = ".imj_contentSubMenuItem:nth-of-type(6)")
	private WebElement activeAlbumButton;
	
	@FindBy(css = "#metadata .imj_metadataSet")
	private List<WebElement> metadata;
	
	@FindBy(css = ".imj_siteContentHeadline h1 a")
	private WebElement back;
	
	public ItemViewPage(WebDriver driver) {
		super(driver);
		
		shareComponent = new ShareComponent(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public String getCollectionTitle() {
		return collectionName.getText();
	}
	
	public String getFileTitle() {
		String fileTitle = fileName.getText();
		return fileTitle;
	}
	
	public boolean isDownloadPossible() {
		return downloadMenu.isDisplayed() && downloadMenu.isEnabled();
	}	
	
	public void download() {
		downloadMenu.click();
		new Actions(driver).sendKeys(Keys.ENTER);
	}
	
	public String getTitleLabel() {
		return titleLabel.getText();
	}

	public String getIDLabel() {
		return idLabel.getText();
	}
	
	public String getAuthorFamilyNameLabel() {
		return authorFamilyNameLabel.getText();
	}

	public String getPublicationLinkLabel() {
		return publicationLinkLabel.getText();
	}

	public String getDateLabel() {
		return dateLabel.getText();
	}

	public boolean isDetailedItemViewPageDisplayed() {
		try {
			return collectionName.isDisplayed() && fileResolution.isDisplayed();
		} catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	public EditItemPage editItem() {
		editButton.click();
		
		return PageFactory.initElements(driver, EditItemPage.class);
	}
	
	public KindOfSharePage shareItem() {
		return shareComponent.share(CategoryType.ITEM);
	}
	
	public CollectionEntryPage deleteItem() {
		deleteLink.click();
		wait.until(ExpectedConditions.visibilityOf(deleteItemDialog));
		deleteItemDialog.findElement(By.className("imj_submitButton")).click();
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionEntryPage discardItem() {
		discardButton.click();
		wait.until(ExpectedConditions.visibilityOf(discardItemDialog));
		discardItemDialog.findElement(By.className("imj_dialogReasonText")).sendKeys("Discarding for testing purposes.");
		discardItemDialog = driver.findElement(By.id("withdrawItem"));
		retryingFindClick(By.xpath("//input[contains(@id, 'btnDiscardContainer')]"));
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public ItemViewPage addToActiveAlbum() {
		activeAlbumButton.click();
		driver.findElement(By.xpath("//a[contains(@id, 'lnkPicFullResolution')]")).click();
		
		return PageFactory.initElements(driver, ItemViewPage.class);
	}
	
	public boolean shareIconVisible() {
		List<WebElement> shareIcons = driver.findElements(By.className("fa-users"));
		return shareIcons.size() > 1;
	}
	
	public boolean metadataPresent(String key, String value) {
		for (WebElement metadataSet : metadata) {
			List<WebElement> labels = metadataSet.findElements(By.className("imj_metadataLabel"));
			if (!labels.isEmpty()) {
				if (labels.get(0).getText().equals(key)) {
					String mValue = metadataSet.findElement(By.className("imj_metadataValue")).getText();
					if (mValue.equals(value))
						return true;
				}
			}
		}
		return false;
	}
	
	public boolean metadataPresent(String key) {
		for (WebElement metadataSet : metadata) {
			List<WebElement> labels = metadataSet.findElements(By.className("imj_metadataLabel"));
			if (!labels.isEmpty()) {
				String mLabel = metadataSet.findElement(By.className("imj_metadataLabel")).getText();
				if (mLabel.equals(key)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public ItemViewPage deleteMetadata(String key) {
		ItemViewPage itemView = this;
		
		while (metadataPresent(key)) {
			EditItemPage editItem = itemView.editItem();
			itemView = editItem.deleteFirstMetadata(key);
		}
		
		return itemView;
	}
	
	/**
	 * @param license - select option value
	 */
	public ItemViewPage addLicense(String license) {
		EditItemPage editItem = editItem();
		return editItem.selectLicense(license);
	}
	
	public boolean licensePresent(String link) {
		try {
			WebElement license = getValue("License").findElement(By.tagName("a"));
			return license.getAttribute("href").equals(link);
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	public WebElement getValue(String label) {
		List<WebElement> sets = driver.findElements(By.className("imj_metadataSet"));
		for (WebElement set : sets) {
			try {
				WebElement currentLabel = set.findElement(By.className("imj_metadataLabel"));
				if (label.equals(currentLabel.getText()))
					return set.findElement(By.className("imj_metadataValue"));
			}
			catch (NoSuchElementException exc) {}
		}
		
		throw new NoSuchElementException("Label is not present.");
	}
	
	public ItemViewPage moveItemToReleasedCollection(String collectionTitle) {
		moveButton.click();
		List<WebElement> collectionsList = driver.findElements(By.cssSelector("#actionItem\\:moveItemsComponent\\:moveItemsForm\\:list>p"));
		int collections = collectionsList.size();
		for (int i = 1; i <= collections; i++) {
			String selector = "#actionItem\\:moveItemsComponent\\:moveItemsForm\\:list>p:nth-of-type(" + i + ")>a";
			WebElement currentCollection = driver.findElement(By.cssSelector(selector));
			if (currentCollection.getText().equals(collectionTitle)) {
				currentCollection.click();
				WebElement moveDialog = driver.findElement(By.id("moveItemsDialog"));
				moveDialog.findElement(By.className("imj_submitButton")).click();
				return PageFactory.initElements(driver, ItemViewPage.class);
			}
		}
		
		throw new NoSuchElementException("Collection " + collectionTitle + " was not found in list.");
	}
	
	public ItemViewPage moveItemToPrivateCollection(String collectionTitle) {
		moveButton.click();
		List<WebElement> collectionsList = driver.findElements(By.cssSelector("#actionItem\\:moveItemsComponent\\:moveItemsForm\\:list>p"));
		int collections = collectionsList.size();
		for (int i = 1; i <= collections; i++) {
			String selector = "#actionItem\\:moveItemsComponent\\:moveItemsForm\\:list>p:nth-of-type(" + i + ")>a";
			WebElement currentCollection = driver.findElement(By.cssSelector(selector));
			if (currentCollection.getText().equals(collectionTitle)) {
				currentCollection.click();
				return PageFactory.initElements(driver, ItemViewPage.class);
			}
		}
		
		throw new NoSuchElementException("Collection " + collectionTitle + " was not found in list.");
	}
	
	public CollectionEntryPage goToCollectionEntry() {
		back.click();
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
}
