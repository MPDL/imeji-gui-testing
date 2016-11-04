package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.CategoryType;
import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;
import spot.components.ShareComponent;

public class AlbumEntryPage extends BasePage {

	@FindBy(id = "editMenu")
	private WebElement editMenu;
	
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
	
	public EditAlbumPage editAlbumInformation() {
		editMenu.click();
		editMenu.findElement(By.id("action:actionMenuEditAlbum")).click();
		
		return PageFactory.initElements(driver, EditAlbumPage.class);
	}
	
	public AlbumPage deleteAlbum() {
		getActionComponent().doAction(ActionType.DELETE);
		
		return PageFactory.initElements(driver, AlbumPage.class);
	}
	
	public DiscardedAlbumEntryPage discardAlbum() {
		//actionComponent.doAction(ActionType.DISCARD);
		
		WebElement actionMenu = driver.findElement(By.id("actionMenu"));
		actionMenu.click();
		actionMenu.findElement(By.id("action:actionMenuDiscard")).click();
		driver.findElement(By.xpath("//*[contains(@id, 'discardForm:discardComment')]")).sendKeys("Discarding for testing purposes.");
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(@id, 'discardForm:btnDiscardContainer')]")));
		driver.findElement(By.xpath("//*[contains(@id, 'discardForm:btnDiscardContainer')]")).click();
		
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
