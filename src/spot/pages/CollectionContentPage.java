package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;

public class CollectionContentPage extends BasePage {

//	@FindBy(xpath=".//*[@id='j_idt330:j_idt334:extSelectTop']")
	@FindBy(xpath=".//*[@id='j_idt324:j_idt328:extSelectTop']")
	private WebElement itemHitNumberPerView;
	
	@FindBy(css=".imj_tiledMediaList")
	private WebElement tiledMediaList;
	
	@FindBy(css="div.imj_overlayMenu.imj_menuHeader")
	private WebElement totalItemNumberWebElement;
	
	@FindBy(css="#selPanel\\:preListForm\\:lblSelectedSize")
	private WebElement selectedItemCount;
	
	@FindBy(css="#selPanel\\:selectionInfoPanel .imj_overlayMenuListItem:nth-of-type(1)>a")
	private WebElement addToActiveAlbumButton;
	
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
		
		List<WebElement> itemList = null;
				
		try {
			tiledMediaList.isDisplayed();
			itemList = tiledMediaList.findElements(By.className("imj_tileItem"));
		} catch (NoSuchElementException e) {
			
			retryingFinding(By.cssSelector(".imj_tiledMediaList"));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".imj_tiledMediaList")));
			tiledMediaList = driver.findElement(By.cssSelector(".imj_tiledMediaList"));
			
			itemList = tiledMediaList.findElements(By.className("imj_tileItem"));				
		}
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
									
			WebElement itemThumbNail = item.findElement(By.className("browseContent:pictureList:"+i+":itemSelectForm:lnkPicDetailBrowse"));
			itemThumbNail.click();
			driver.navigate().back();
		}
		
//		for (WebElement item : itemList) {
//
//			
//			WebElement tooltip = item.findElement(By.className("imj_tooltip"));
//			
//			List<WebElement> toolTipValues = tooltip.findElements(By.className("imj_metadataValue"));
//			
//			for (WebElement toolTipValue : toolTipValues) {
//				String text = toolTipValue.getText();
//				System.out.println("Tooltip value text: " + text);
//			}
//		}
	}

	public DetailedItemViewPage downloadFirstItemInList() {
		WebElement firstItem = getItemList().get(0);
		firstItem.findElement(By.id("browseContent:pictureList:0:itemSelectForm:lnkPicDetailBrowse")).click();
		
		return PageFactory.initElements(driver, DetailedItemViewPage.class);
	}

	public void addFirstItemToAlbum() {
		WebElement firstItem = getItemList().get(0);
		firstItem.findElement(By.cssSelector("span>input")).click();
		
		try {
			selectedItemCount.click();
		} catch (NoSuchElementException e) {
			retryingFindClick(By.cssSelector("#selPanel\\:preListForm\\:lblSelectedSize"));
		}
		wait.until(ExpectedConditions.visibilityOf(addToActiveAlbumButton));
		addToActiveAlbumButton.click();
	}

	public int addAllItemsToAlbum() {
		for (WebElement item : getItemList()) {
			item.findElement(By.cssSelector("span>input")).click();
		}
		
		try {
			selectedItemCount.click();
		} catch (StaleElementReferenceException e) {
			retryingFindClick(By.cssSelector("#selPanel\\:preListForm\\:lblSelectedSize"));
		}
		
		wait.until(ExpectedConditions.visibilityOf(addToActiveAlbumButton));
		addToActiveAlbumButton.click();
		
		return getItemListSize();
	}

}
