package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.components.StateComponent;

public class AlbumPage extends BasePage {

	private List<WebElement> albumList;
	
	@FindBy(css = "#ajaxWrapper>div:nth-of-type(2)>form>a")
	private WebElement showAllAlbumsButton;
	
	private StateComponent stateComponent;
	
	public AlbumPage(WebDriver driver) {
		super(driver);
		
		albumList = driver.findElements(By.className("imj_bibliographicListItem"));
		
		stateComponent = new StateComponent(driver);
		
		PageFactory.initElements(driver,  this);
	}
	
	public AlbumEntryPage openAlbumByTitle(String albumTitle) {
		WebElement albumInQuestion = findAlbum(albumTitle);
		albumInQuestion.findElement(By.tagName("a")).click();
		
		return PageFactory.initElements(driver, AlbumEntryPage.class);
	}
	
	private WebElement findAlbum(String albumTitle) {
		WebElement albumInQuestion = null;
		
		albumList = driver.findElements(By.className("imj_bibliographicListItem"));
		for (WebElement album : albumList) {
			WebElement collBody = album.findElement(By.className("imj_itemContent"));
			
			WebElement collHeadline = collBody.findElement(By.className("imj_itemHeadline"));
			
			String headline = collHeadline.getText();
			
			if (headline.equals(albumTitle)) {
				albumInQuestion = album;
			}
		}
		
		if (albumInQuestion == null)
			throw new NoSuchElementException("Album " + albumTitle + " was not found.");
		
		return albumInQuestion;
	}
	
	public boolean isAlbumPresent(String albumTitle) {
		try {
			findAlbum(albumTitle);
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	public AlbumPage makeAlbumActive(String albumTitle) {
		WebElement albumInQuestion = findAlbum(albumTitle);
		albumInQuestion.findElement(By.xpath("//input[contains(@id, 'btnActivate')]")).click();
		
		return PageFactory.initElements(driver, AlbumPage.class);
	}
	
	public AlbumPage makeAlbumInactive(String albumTitle) {
		WebElement albumInQuestion = findAlbum(albumTitle);
		albumInQuestion.findElement(By.xpath("//input[contains(@id, 'btnDeactivate')]")).click();
		
		return PageFactory.initElements(driver, AlbumPage.class);
	}
	
	public StateComponent getStateComponent() {
		return stateComponent;
	}
	

}
