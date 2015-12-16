package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;

public class AlbumEntryPage extends BasePage {

	private ActionComponent actionComponent;
	
	public AlbumEntryPage(WebDriver driver) {
		super(driver);
		
		actionComponent = new ActionComponent(driver);
		
		PageFactory.initElements(driver, this);
	}

	public ActionComponent getActionComponent() {
		return actionComponent;
	}

	public void publish() {
		actionComponent.doAction(ActionType.PUBLISH);
	}
}
