package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
	
	@FindBy(id = "actionMenu")
	private WebElement actionMenu;
	
	@FindBy(id = "editMenu")
	private WebElement editButton;
	
	@FindBy(id = "action:actionMenuEditCollection")
	private WebElement editInfoOption;
	
	@FindBy(className = "imj_statusHeaderArea")
	private WebElement status;
	
	@FindBy(id = "description")
	private WebElement description;
	
	@FindBy(id = "metadata")
	private WebElement metadata;
	
	public CollectionEntryPage(WebDriver driver) {
		super(driver);
		
		actionComponent = new ActionComponent(driver);
		shareComponent = new ShareComponent(driver);
		
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
		actionMenu.click();
		driver.findElement(By.id("action:actionMenuRelease")).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("releaseMenuItemDialog")));
		driver.findElement(By.className("imj_submitButton")).click();
		
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
	
	public boolean shareIconVisible() {
		try {
			driver.findElement(By.cssSelector(".fa-users.fa-size-3"));
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}
	
	public boolean descriptionMetadataDisplayed() {
		return description.isDisplayed() && metadata.isDisplayed();
	}
	
	public boolean labelDisplayed(String label) {
		List<WebElement> allSets = driver.findElements(By.className("imj_infodataSet"));
		for (WebElement set : allSets) {
			if (set.getText().contains("Label"))
				return set.getText().contains(label);
		}
		return false;
	}
}
