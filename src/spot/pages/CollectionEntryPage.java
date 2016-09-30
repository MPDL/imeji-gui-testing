package spot.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;
import spot.pages.notAdmin.NewCollectionPage;

public class CollectionEntryPage extends BasePage {

	private ActionComponent actionComponent;
	
	@FindBy(css=".imj_contentMenu>ul>li:nth-of-type(4)")
	private WebElement uploadContentButton;
	
	@FindBy(css="#metadata>a")
	private WebElement addMetaDataProfileButton;	
	
	@FindBy(id = "editMenu")
	private WebElement editButton;
	
	@FindBy(id = "action:actionMenuEditCollection")
	private WebElement editInfoOption;
	
	@FindBy(id = "sharingMenu")
	private WebElement shareButton;
	
	@FindBy(id = "action:actionMenuShareCollection")
	private WebElement shareOption;
	
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
	
	public SharePage goToSharePage() {
		shareButton.click();
		wait.until(ExpectedConditions.visibilityOf(shareOption));
		shareOption.click();
		
		return PageFactory.initElements(driver, SharePage.class);
	}
}
