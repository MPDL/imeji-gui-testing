package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.Select;

public class CollectionContentPage extends BasePage {

//	@FindBy(xpath=".//*[@id='j_idt330:j_idt334:extSelectTop']")
	@FindBy(xpath=".//*[@id='j_idt324:j_idt328:extSelectTop']")
	private WebElement itemHitNumberPerView;
	
	@FindBy(className="imj_tiledMediaList")
	private WebElement tiledMediaList;
	
	@FindBy(css="div.imj_overlayMenu.imj_menuHeader")
	private WebElement totalItemNumberWebElement;
	
	private int totalItemNumber;
	
	public CollectionContentPage(WebDriver driver) {
		super(driver);
		
		waitForInitationPageElements();
	}

	public void declareTotalItemNumber() {
		waitForInitationPageElements();
		
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
		waitForInitationPageElements();
		
		Select itemHitDropDown = new Select(itemHitNumberPerView);
		
		itemHitDropDown.selectByVisibleText(""+itemHit);		
		
		itemHitNumberPerView.submit();
	}
	
	public int getMediaListSize() {
		
		List<WebElement> mediaList = tiledMediaList.findElements(By.className("imj_tileItem"));
		
		return mediaList.size();		
	}
}
