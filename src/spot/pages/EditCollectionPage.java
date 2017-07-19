package spot.pages;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class EditCollectionPage extends BasePage {

	@FindBy(id = "editContainer:mediaContainerForm:inputTitleText")
	private WebElement titleBox;
	
	@FindBy(id = "editContainer:mediaContainerForm:inputDescription")
	private WebElement descriptionBox;
	
	@FindBy(id = "editContainer:mediaContainerForm:persons:0:collectionAuthor:inputFamilyNameText")
	private WebElement familyNameBox;
	
	@FindBy(id = "editContainer:mediaContainerForm:persons:0:collectionAuthor:inputGiveNameText")
	private WebElement givenNameBox;
	
	@FindBy(xpath = "//a[contains(@id, 'editContainer:mediaContainerForm:persons:0') and contains(@class, 'fa-plus-square-o')]")
	private WebElement addAuthorButton;
	
	@FindBy(id = "editContainer:mediaContainerForm:persons:1:collectionAuthor:inputFamilyNameText")
	private WebElement author2FamilyName;
	
	@FindBy(xpath = "//input[contains(@id, 'editContainer:mediaContainerForm:persons:1:collectionAuthor') and contains(@id, 'inputOrgaName')]")
	private WebElement organisation2Name;
	
	@FindBy(id = "editContainer:mediaContainerForm:additionalInfos")
	private WebElement additionalInfo;
	
	@FindBy(xpath = "//a[contains(@id, 'editContainer:mediaContainerForm:persons:1') and contains(@class, 'fa-minus-square-o')]")
	private WebElement removeAuthor;
	
	@FindBy(css = "#editContainer\\:mediaContainerForm\\:additionalInfos .fa-minus-square-o")
	private WebElement removeInfo;
	
	@FindBy(id = "editContainer:mediaContainerForm:list:0:inputInfoLabel")
	private WebElement infoLabelBox;
	
	@FindBy(id = "editContainer:mediaContainerForm:list:0:inputInfoText")
	private WebElement infoTextBox;
	
	@FindBy(id = "editContainer:mediaContainerForm:list:0:inputInfoUrl")
	private WebElement infoUrlBox;
	
	@FindBy(id = "container")
	private WebElement logoContainer;
	
	@FindBy(id = "editContainer:mediaContainerForm:notificationCheckBox")
	private WebElement notificationBox;
	
	@FindBy(id="editContainer:mediaContainerForm:btn_saveCollection")
	private WebElement saveButton;
	
	public EditCollectionPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public void editTitle(String newTitle) {
		titleBox.clear();
		titleBox.sendKeys(newTitle);
	}
	
	public void editDescription(String newDescription) {
		descriptionBox.clear();
		descriptionBox.sendKeys(newDescription);
	}
	
	public void editAuthor(String newFamilyName, String newGivenName) {
		familyNameBox.clear();
		familyNameBox.sendKeys(newFamilyName);
		givenNameBox.clear();
		givenNameBox.sendKeys(newGivenName);
	}
	
	public void addAuthor(String familyName, String organisation) {
		addAuthorButton.click();
		author2FamilyName.sendKeys(familyName);
		organisation2Name.sendKeys(organisation);
	}
	
	public void addInformation(String label, String link) {
		additionalInfo.findElement(By.className("fa-plus-square-o")).click();
		
		infoLabelBox.sendKeys(label);
		infoTextBox.sendKeys("This is a test collection.");
		infoUrlBox.sendKeys(link);
	}
	
	public void removeAuthor() {
		removeAuthor.click();
		wait.until(ExpectedConditions.elementToBeClickable(saveButton));
		submitChanges();
	}
	
	public void removeLabel() {
		removeInfo.click();
		wait.until(ExpectedConditions.elementToBeClickable(saveButton));
	}
	
	public void addLogo(String filepath) {
		WebElement input = logoContainer.findElement(By.tagName("input"));
		input.sendKeys(filepath);
	}
	
	public CollectionEntryPage submitChanges() {
		saveButton.click();
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public List<String> getOrganisations() {
		List<String> ous = new LinkedList<>();
		List<WebElement> ouBoxes = driver.findElements(By.xpath("//input[contains(@id, 'inputOrgaName1')]"));
		for (WebElement box : ouBoxes) {
			ous.add(box.getAttribute("value"));
		}
		
		return ous;
	}
	
	public void enableNotifications() {
		if (!notificationBox.isSelected())
			notificationBox.click();
	}
}
