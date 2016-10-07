package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class BrowseItemsPage extends BasePage {

	private List<WebElement> allItems;
	List<WebElement> imageList;
	
	public BrowseItemsPage(WebDriver driver) {
		super(driver);
		
		allItems = driver.findElements(By.className("imj_optionLabel"));
		imageList = driver.findElements(By.tagName("img"));
		
		PageFactory.initElements(driver, this);
	}
	
	public boolean isItemPresent(String itemTitle) {
		for(WebElement item : allItems) {
			String currentItemTitle = item.getAttribute("title");
			if (currentItemTitle.equals(itemTitle))
				return true;
		}
		return false;
	}
	
	public int imageCount() {
		return imageList.size();
	}
}
