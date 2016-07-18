package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;

public class CollectionEntryPage extends BasePage {

	private ActionComponent actionComponent;
	
	@FindBy(css=".imj_mainContentWrapper .imj_collectionEntryPage .imj_entryPagePreviewTiledList .imj_headline a")
	private WebElement uploadContentButton;
	
	@FindBy(css=".imj_collectionEntryPage>h2>a")
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
