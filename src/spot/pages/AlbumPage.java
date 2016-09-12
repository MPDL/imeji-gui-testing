package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
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
		
		albumInQuestion.click();
		
		return PageFactory.initElements(driver, AlbumEntryPage.class);
	}
	
	public boolean isAlbumPresent(String albumTitle) {
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
			return false;
		else
			return true;
	}
	
	public StateComponent getStateComponent() {
		return stateComponent;
	}
	

}
