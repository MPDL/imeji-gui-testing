package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;

public class CollectionContentPage extends BasePage {

	@FindBy(css="#actionsMenuArea .imj_contentMenu .imj_contentMenuItem")
	private WebElement aboutLink;
	
	@FindBy(xpath="//a[contains(@title, 'Meta')]")
	private WebElement metaDataProfileLink;
	
	@FindBy(xpath="/html/body/div[1]/div[5]/div[1]/h2/a")
	private WebElement addNewMetaDataProfile;
	
	@FindBy(xpath=".//*[@id='j_idt324:j_idt328:extSelectTop']")
	private WebElement itemHitNumberPerView;
	
	@FindBy(css=".imj_tiledMediaList")
	private WebElement tiledMediaList;
	
	@FindBy(css="div.imj_overlayMenu.imj_menuHeader")
	private WebElement totalItemNumberWebElement;
	
	@FindBy(id="selPanel:preListForm:lblSelectedSize")
	private WebElement selectedItemCount;
	
	@FindBy(xpath="/html/body/div[1]/div[5]/div[1]/div[2]/form/div[2]/div[2]/ul/li[1]/a")
	private WebElement addToActiveAlbumButton;
	
	@FindBy()
	private WebElement addToPublishedAlbumButton;
	
	@FindBy(id = "dialogDeleteAll")
	private WebElement deleteAllDialog;
	
	@FindBy(css = "#actionsMenuArea .imj_overlayMenu:nth-of-type(2) li:nth-of-type(2) a")
	private WebElement metadataRDF;
	
	private ActionComponent actionComponent;
	
	private int totalItemNumber;
	
	public CollectionContentPage(WebDriver driver) {
		super(driver);
		
		actionComponent = new ActionComponent(driver);
		
		PageFactory.initElements(driver, this);
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

	public void changeItemHitNumberCollectionView(int itemHit) {
		waitForInitationPageElements(10);
		
		Select itemHitDropDown = new Select(itemHitNumberPerView);
		
		itemHitDropDown.selectByVisibleText(""+itemHit);		
		
		itemHitNumberPerView.submit();
	}
	
	public int getItemListSize() {
		List<WebElement> mediaList = getItemList();
		return mediaList.size();		
	}
	
	public List<WebElement> getItemList() {
		
		List<WebElement> itemList = driver.findElements(By.className("imj_tileItem"));
		return itemList;
	}
	
	public void publish() {
		actionComponent.doAction(ActionType.PUBLISH);
	}
	
	public KindOfSharePage share() {
		return (KindOfSharePage) actionComponent.doAction(ActionType.SHARE);
	}

	public void checkMetaDataOfItems(String title, String author, String id, String publicationLink, String date) {
		waitForInitationPageElements(20);
		
		List<WebElement> itemList = getItemList();
		
		
		for (int i=0; i<itemList.size(); i++) {
			
			WebElement item = itemList.get(i);
									
			WebElement itemThumbNail = item.findElement(By.className("browseContent:pictureList:" + i + ":itemSelectForm:lnkPicDetailBrowse"));
			itemThumbNail.click();
			driver.navigate().back();
		}
	}

	public DetailedItemViewPage downloadFirstItemInList() {
		WebElement firstItem = getItemList().get(0);
		firstItem.findElement(By.id("browseContent:pictureList:0:itemSelectForm:lnkPicDetailBrowse")).click();
		
		return PageFactory.initElements(driver, DetailedItemViewPage.class);
	}

	public void addFirstItemToAlbum() {
		addItemToAlbum(0);
	}
	
	/**
	 *  @param index: first item has index of 0
	 */
	public void addItemToAlbum(int index) {
		validateIndex(index);
		WebElement wantedItem = getItemList().get(index);
		wantedItem.findElement(By.cssSelector("span>input")).click();
		
		wait.until(ExpectedConditions.visibilityOf(selectedItemCount));
		selectedItemCount.click();
		wait.until(ExpectedConditions.visibilityOf(addToActiveAlbumButton));
		addToActiveAlbumButton.click();
	}

	public int addAllItemsToAlbum() {
		for (WebElement item : getItemList()) {
			item.findElement(By.cssSelector("span>input")).click();
		}

		wait.until(ExpectedConditions.visibilityOf(selectedItemCount));
		selectedItemCount.click();
		wait.until(ExpectedConditions.visibilityOf(addToActiveAlbumButton));
		addToActiveAlbumButton.click();
		
		return getItemListSize();
	}
	
	public CollectionContentPage deleteItem(int index) {
		DetailedItemViewPage itemView = openItem(index);
		return itemView.deleteItem();
	}
	
	public CollectionContentPage discardItem(int index) {
		DetailedItemViewPage itemView = openItem(index);
		return itemView.discardItem();
	}
	
	public CollectionContentPage deleteAllItems() {
		WebElement actionMenu = driver.findElement(By.id("actionMenu"));
		actionMenu.click();
		actionMenu.findElement(By.cssSelector("li:nth-of-type(3)")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dialogDeleteAll")));
		deleteAllDialog.findElement(By.className("imj_submitButton")).click();
		
		return PageFactory.initElements(driver, CollectionContentPage.class);
	}
	
	public DetailedItemViewPage openItem(int index) {
		validateIndex(index);
		WebElement item = getItemList().get(index).findElement(By.tagName("img"));
		item.click();
		
		return PageFactory.initElements(driver, DetailedItemViewPage.class);
	}
	
	private void validateIndex(int index) {
		int itemCount = getItemListSize();
		if (index > itemCount)
			throw new IllegalArgumentException("No item with index " + index + ". Total count: " + itemCount);
	}
	
	public DiscardedCollectionEntryPage discardCollection() {
		actionComponent.doAction(ActionType.DISCARD);
		
		return PageFactory.initElements(driver, DiscardedCollectionEntryPage.class);
	}
	
	public boolean isMetaDataProfileDefined() {
		metaDataProfileLink.click();
		try {
			driver.findElement(By.className("imj_metadataProfileItem"));
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	public MetadataOverviewPage openMetaDataProfile() {
		if (isMetaDataProfileDefined())
			return PageFactory.initElements(driver, MetadataOverviewPage.class);
		return null;
	}
	
	public CollectionEntryPage viewCollectionInformation() {
		aboutLink.click();
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public boolean downloadItemMetadata() {
		driver.findElement(By.cssSelector("#actionsMenuArea .imj_overlayMenu:nth-of-type(2)")).click();
		return metadataRDF.isDisplayed() && metadataRDF.isEnabled();
	}

}
