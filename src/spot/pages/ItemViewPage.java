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

import spot.components.SearchComponent.CategoryType;
import spot.components.ShareComponent;
import spot.pages.registered.EditItemPage;
import spot.pages.registered.KindOfSharePage;

public class ItemViewPage extends BasePage {

	private ShareComponent shareComponent;

	@FindBy(css = ".imj_siteContentHeadline h1")
	private WebElement fileName;

	@FindBy(id = "picWebResolutionInternalDigilib")
	private WebElement fileResolution;

	@FindBy(css = ".fa-download")
	private WebElement downloadMenu;

	@FindBy(css = ".imj_siteContentHeadline>h1>a")
	private WebElement collectionName;

	@FindBy(css = "#actionsMenuArea>div a[target='_blank']")
	private WebElement downloadOriginalFileButton;

	@FindBy(css = ".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(1)>.imj_metadataValue")
	private WebElement titleLabel;

	@FindBy(css = ".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(2)>.imj_metadataValue")
	private WebElement idLabel;

	@FindBy(css = ".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(3)>.imj_metadataValue")
	private WebElement authorFamilyNameLabel;

	@FindBy(css = ".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(4)>.imj_metadataValue")
	private WebElement publicationLinkLabel;

	@FindBy(css = ".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(5)>.imj_metadataValue")
	private WebElement dateLabel;

	@FindBy(css = ".fa-hand-o-right")
	private WebElement moveButton;

	@FindBy(css = "#actions>form>a")
	private WebElement editButton;

	@FindBy(css = "#actions .fa-trash")
	private WebElement deleteLink;

	@FindBy(id = "deleteItem")
	private WebElement deleteItemDialog;

	@FindBy(css = ".fa-ban")
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

	// IMJ-234, IMJ-235, IMJ-59
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
		WebElement globalMetadataSet = driver.findElement(By.className("imj_globalMetadataSet"));
		editButton.click();
		wait.until(ExpectedConditions.stalenessOf(globalMetadataSet));

		return PageFactory.initElements(driver, EditItemPage.class);
	}

	public KindOfSharePage shareItem() {
		return shareComponent.share(CategoryType.ITEM);
	}

	public CollectionEntryPage deleteItem() {
		deleteLink.click();
		wait.until(ExpectedConditions.visibilityOf(deleteItemDialog));
		deleteItemDialog.findElement(By.className("imj_submitButton")).click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
		}

		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}

	public CollectionEntryPage discardItem() {
		discardButton.click();
		wait.until(ExpectedConditions.visibilityOf(discardItemDialog));
		discardItemDialog.findElement(By.className("imj_dialogReasonText"))
				.sendKeys("Discarding for testing purposes.");
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

	public ItemViewPage deleteMetadata(String metaDataLabelName) {
		ItemViewPage itemViewPage = this.editItem().deleteFirstMetadata(metaDataLabelName);
		return itemViewPage;
	}
	
	public ItemViewPage deleteAllMetadata(String metaDataLabelName) {
			ItemViewPage itemViewPage = this.editItem().deleteAllMetadata(metaDataLabelName);
			return itemViewPage;
	}

	/**
	 * @param license
	 *            - select option value
	 */
	// IMJ-81
	public ItemViewPage addLicense(String license) {
		EditItemPage editItem = editItem();
		return editItem.selectLicense(license);
	}

	public boolean licensePresent(String link) {
		try {
			WebElement license = getValue("License").findElement(By.tagName("a"));
			return license.getAttribute("href").equals(link);
		} catch (NoSuchElementException exc) {
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
			} catch (NoSuchElementException exc) {
			}
		}

		throw new NoSuchElementException("Label '" + label  + "' is not present.");
	}

	public ItemViewPage moveItemToReleasedCollection(String collectionTitle) {
		moveButton.click();
		driver.findElement(By.linkText(collectionTitle)).click();
		WebElement moveDialog = driver.findElement(By.id("moveItem"));
		moveDialog.findElement(By.id("itemDialogs:move:form:j_idt404")).click();

		WebElement loaderWrapper = driver.findElement(By.cssSelector(".loaderWrapper"));
		wait.until(ExpectedConditions.invisibilityOf(loaderWrapper));

		return PageFactory.initElements(driver, ItemViewPage.class);
	}

	public ItemViewPage moveItemToPrivateCollection(String collectionTitle) {
		moveButton.click();
		driver.findElement(By.linkText(collectionTitle)).click();

		WebElement loaderWrapper = driver.findElement(By.cssSelector(".loaderWrapper"));
		wait.until(ExpectedConditions.invisibilityOf(loaderWrapper));

		return PageFactory.initElements(driver, ItemViewPage.class);
	}

	public CollectionEntryPage goToCollectionEntry() {
		back.click();

		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
}
