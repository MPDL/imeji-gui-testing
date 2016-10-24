package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.CategoryType;
import spot.components.ShareComponent;

public class DetailedItemViewPage extends BasePage {

	private ShareComponent shareComponent;

	@FindBy(id="editItem:txtUrl")
	private WebElement fileName;
	
	@FindBy(id="picWebResolutionInternalDigilib")
	private WebElement fileResolution;
	
	@FindBy(css="#actionsMenuArea .fa-download")
	private WebElement downloadMenu;
	
	@FindBy(css="#editItem .imj_metadataSet:nth-of-type(3) .imj_metadataValue a")
	private WebElement collectionName;
	
	@FindBy(css="#actionsMenuArea>div a[target='_blank']")
	private WebElement downloadOriginalFileButton;
	
	@FindBy(css=".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(1)>.imj_metadataValue")
	private WebElement titleLabel;
	
	@FindBy(css=".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(2)>.imj_metadataValue")
	private WebElement idLabel;
	
	@FindBy(css=".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(3)>.imj_metadataValue")
	private WebElement authorFamilyNameLabel;
	
	@FindBy(css=".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(4)>.imj_metadataValue")
	private WebElement publicationLinkLabel;
	
	@FindBy(css=".imj_metadataWrapper>.imj_metadataWrapper>div:nth-of-type(5)>.imj_metadataValue")
	private WebElement dateLabel;
	
	@FindBy(id = "deleteMenuItemDialog")
	private WebElement deleteItemDialog;
	
	@FindBy(id = "withdrawMenuItemDialog")
	private WebElement discardItemDialog;
	
	public DetailedItemViewPage(WebDriver driver) {
		super(driver);
		
		shareComponent = new ShareComponent(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public String getCollectionTitle() {
		return collectionName.getText();
	}
	
	public String getFileTitle() {
		
		String fileTitle = fileName.getText();
		return fileTitle;
	}
	
	public boolean isDownloadPossible() {
		downloadMenu.click();
		
		boolean isDownloadPossible = (downloadOriginalFileButton.isDisplayed() && downloadOriginalFileButton.isEnabled());
		
		return isDownloadPossible;
	}	
	
	public String getTitleLabel() {
		return titleLabel.getText();
	}

	public String getIDLabel() {
		return idLabel.getText();
	}
	
	public String getAuthorFamilyNameLabel() {
		return authorFamilyNameLabel.getText();
	}

	public String getPublicationLinkLabel() {
		return publicationLinkLabel.getText();
	}

	public String getDateLabel() {
		return dateLabel.getText();
	}

	public boolean isDetailedItemViewPageDisplayed() {
		try {
			return collectionName.isDisplayed();
		} catch (TimeoutException te) {
			wait.until(ExpectedConditions.visibilityOf(fileResolution));
			return false;
		}
	}
	
	public KindOfSharePage shareItem() {
		return shareComponent.share(CategoryType.ITEM);
	}
	
	public CollectionContentPage deleteItem() {
		WebElement actionMenu = driver.findElement(By.id("actionMenu"));
		actionMenu.click();
		actionMenu.findElement(By.id("action:actionMenuDeleteItem")).click();
		wait.until(ExpectedConditions.visibilityOf(deleteItemDialog));
		deleteItemDialog.findElement(By.className("imj_submitButton")).click();
		
		return PageFactory.initElements(driver, CollectionContentPage.class);
	}
	
	public CollectionContentPage discardItem() {
		WebElement actionMenu = driver.findElement(By.id("actionMenu"));
		actionMenu.click();
		actionMenu.findElement(By.id("action:actionMenuDiscardItem")).click();
		wait.until(ExpectedConditions.visibilityOf(discardItemDialog));
		discardItemDialog.findElement(By.className("imj_dialogReasonText")).sendKeys("Discarding for testing purposes.");
		wait.until(ExpectedConditions.elementToBeClickable(By.className("imj_submitButton")));
		discardItemDialog.findElement(By.className("imj_submitButton")).click();
		
		return PageFactory.initElements(driver, CollectionContentPage.class);
	}
	
	public boolean shareIconVisible() {
		List<WebElement> shareIcons = driver.findElements(By.className("fa-users"));
		return shareIcons.size() > 1;
	}
}
