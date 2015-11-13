package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.components.ActionComponent;

public class CollectionEntryPage extends BasePage {

	private ActionComponent actionComponent;
	
	@FindBy(xpath="html/body/div[1]/div[5]/div[2]/div[1]/h2/a")
	private WebElement uploadContentButton;
	
	public CollectionEntryPage(WebDriver driver) {
		super(driver);
		
		actionComponent = new ActionComponent(driver);
		
		PageFactory.initElements(driver, this);
	}

	public ActionComponent getActionComponent() {
		return actionComponent;
	}

	public MultipleUploadPage uploadContent() {
		uploadContentButton.click();
		
		return PageFactory.initElements(driver, MultipleUploadPage.class);
	}
}
