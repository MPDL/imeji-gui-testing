package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;

public class CollectionEntryPage extends BasePage {

	private ActionComponent actionComponent;
	
	@FindBy(css=".imj_contentMenu>ul>li:nth-of-type(4)")
	private WebElement uploadContentButton;
	
	@FindBy(css="#metadata>a")
	private WebElement addMetaDataProfileButton;	 
	
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

	public KindOfMetaDataProfilePage addMetaDataProfile() {
		addMetaDataProfileButton.click();
		
		return PageFactory.initElements(driver, KindOfMetaDataProfilePage.class);
	}
	
	public DiscardedCollectionEntryPage discardCollection() {
		getActionComponent().doAction(ActionType.DISCARD);
		
		return PageFactory.initElements(driver, DiscardedCollectionEntryPage.class);
	}
	
	public CollectionsPage deleteCollection() {
		getActionComponent().doAction(ActionType.DELETE);
		
		return PageFactory.initElements(driver, CollectionsPage.class);
	}
}
