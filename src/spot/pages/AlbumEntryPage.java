package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import spot.CategoryType;
import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;
import spot.components.ShareComponent;

public class AlbumEntryPage extends BasePage {

	private ActionComponent actionComponent;
	private ShareComponent shareComponent;
	
	public AlbumEntryPage(WebDriver driver) {
		super(driver);
		
		actionComponent = new ActionComponent(driver);
		shareComponent = new ShareComponent(driver);
		
		PageFactory.initElements(driver, this);
	}

	public ActionComponent getActionComponent() {
		return actionComponent;
	}

	public AlbumEntryPage publish() {
		actionComponent.doAction(ActionType.PUBLISH);
		
		return PageFactory.initElements(driver, AlbumEntryPage.class);
	}
	
	public AlbumPage deleteAlbum() {
		getActionComponent().doAction(ActionType.DELETE);
		
		return PageFactory.initElements(driver, AlbumPage.class);
	}
	
	public DiscardedAlbumEntryPage discardAlbum() {
		actionComponent.doAction(ActionType.DISCARD);
		
		return PageFactory.initElements(driver, DiscardedAlbumEntryPage.class);
	}
	
	public KindOfSharePage shareAlbum() {
		return shareComponent.share(CategoryType.ALBUM);
	}
	
	public int getItemCount() {
		List<WebElement> items = driver.findElements(By.className("imj_tileItem"));
		return items.size();
	}
}
