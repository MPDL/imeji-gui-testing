package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.CategoryType;
import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;
import spot.components.ShareComponent;

public class CollectionEntryPage extends BasePage {

	private ActionComponent actionComponent;
	private ShareComponent shareComponent;
	
	@FindBy(css=".imj_contentMenu>ul>li:nth-of-type(4)")
	private WebElement uploadContentButton;
	
	@FindBy(css="#metadata>a")
	private WebElement addMetaDataProfileButton;	
	
	@FindBy(id = "editMenu")
	private WebElement editButton;
	
	@FindBy(id = "action:actionMenuEditCollection")
	private WebElement editInfoOption;
	
	@FindBy(className = "imj_statusHeaderArea")
	private WebElement status;
	
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

	public MetadataTransitionPage addMetaDataProfile() {
		addMetaDataProfileButton.click();
		
		return PageFactory.initElements(driver, MetadataTransitionPage.class);
	}
	
	public DiscardedCollectionEntryPage discardCollection() {
		getActionComponent().doAction(ActionType.DISCARD);
		
		return PageFactory.initElements(driver, DiscardedCollectionEntryPage.class);
	}
	
	public CollectionsPage deleteCollection() {
		getActionComponent().doAction(ActionType.DELETE);
		
		return PageFactory.initElements(driver, CollectionsPage.class);
	}
	
	public CollectionEntryPage publishCollection() {
		getActionComponent().doAction(ActionType.PUBLISH);
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public EditCollectionPage editInformation() {
		editButton.click();
		wait.until(ExpectedConditions.visibilityOf(editInfoOption));
		editInfoOption.click();
		
		return PageFactory.initElements(driver, EditCollectionPage.class);
	}
	
	public KindOfSharePage goToSharePage() {
		return shareComponent.share(CategoryType.COLLECTION);
	}
}
