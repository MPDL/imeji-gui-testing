package spot.pages.registered;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;
import test.base.SeleniumWrapper;

//TODO: Merge with NewCollectionPage!?
public class EditCollectionPage extends BasePage {
	
	private static final Logger log4j = LogManager.getLogger(EditCollectionPage.class.getName());

	@FindBy(id = "editContainer:form:inputTitleText")
	private WebElement titleBox;
	
	@FindBy(id = "editContainer:form:inputDescription")
	private WebElement descriptionBox;
	
	@FindBy(id = "editContainer:form:persons:0:collectionAuthor:inputFamilyNameText")
	private WebElement familyNameBox;
	
	@FindBy(id = "editContainer:form:persons:0:collectionAuthor:inputGiveNameText")
	private WebElement givenNameBox;
	
	@FindBy(xpath = "//a[contains(@id, 'editContainer:form:persons:0') and contains(@class, 'fa-plus-square-o')]")
	private WebElement addAuthorButton;
	
	@FindBy(id = "editContainer:form:persons:1:collectionAuthor:inputFamilyNameText")
	private WebElement author2FamilyName;
	
	@FindBy(xpath = "//input[contains(@id, 'editContainer:form:persons:1:collectionAuthor') and contains(@id, 'OrgaName')]")
	private WebElement organisation2Name;
	
	@FindBy(id = "editContainer:form:additionalInfos")
	private WebElement additionalInfo;
	
	@FindBy(xpath = "//a[contains(@id, 'editContainer:form:persons:1') and contains(@class, 'fa-minus-square-o')]")
	private WebElement removeAuthor;
	
	@FindBy(css = "#editContainer\\:form\\:additionalInfos .fa-minus-square-o")
	private WebElement removeInfo;
	
	@FindBy(id = "editContainer:form:list:0:inputInfoLabel")
	private WebElement infoLabelBox;
	
	@FindBy(id = "editContainer:form:list:0:inputInfoText")
	private WebElement infoTextBox;
	
	@FindBy(id = "editContainer:form:list:0:inputInfoUrl")
	private WebElement infoUrlBox;
	
	@FindBy(id = "pickfiles")
	private WebElement logoButton;
	
	@FindBy(id = "container")
	private WebElement logoContainer;
	
	@FindBy(id="editContainer:form:save")
	private WebElement saveButton;
	
	public EditCollectionPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	// IMJ-123
	public void editTitle(String newTitle) {
		titleBox.clear();
		titleBox.sendKeys(newTitle);
	}
	
	// IMJ-246
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
	
	public void selectStudyTypes(List<String> studyTypes) {
		for (String studyType : studyTypes) {
			selectStudyType(studyType);
		}
	}
	
	public void deselectStudyTypes(List<String> studyTypes) {
		for (String studyType : studyTypes) {
			deselectStudyType(studyType);
		}
	}
	
	public void selectStudyType(String studyType) {
		WebElement studyTypeCheckbox = driver.findElement(By.xpath("//input[@value='"+ studyType +"' and @type='checkbox']"));
		
		if(!studyTypeCheckbox.isSelected()) {
			studyTypeCheckbox.click();
		}else {
			log4j.warn("Study Type '" + studyType + "' allready selected!");
		}
	}
	
	public void deselectStudyType(String studyType) {
		WebElement studyTypeCheckbox = driver.findElement(By.xpath("//input[@value='"+ studyType +"' and @type='checkbox']"));
		
		if(studyTypeCheckbox.isSelected()) {
			studyTypeCheckbox.click();
		}else {
			log4j.warn("Study Type '" + studyType + "' is not selected!");
		}
	}
	
	public void editCollectionMetadata(String key, String value) {
		WebElement metadataValueInputField = driver.findElement(By.xpath("//span[contains(@id,'additionalInfos')]//span[text()='"+ key +"']/following-sibling::textarea"));
		metadataValueInputField.clear();
		metadataValueInputField.sendKeys(value);
	}
	
	public void addAutoSuggestedStudyContext(String key, String value) {
		WebElement emptyMetadataKeyInputField = driver.findElement(By.xpath("//span[contains(@id,'additionalInfos')]//input[contains(@id,'inputInfoLabel') and not(@value)]"));
		emptyMetadataKeyInputField.clear();
		emptyMetadataKeyInputField.sendKeys(key);
		
		WebElement autosuggestedMetadataKey = driver.findElement(By.xpath("//li/a[text()='"+ key +"']"));
		autosuggestedMetadataKey.click();
		
		WebElement metadataValueInputField = emptyMetadataKeyInputField.findElement(By.xpath("./following::textarea[contains(@id,'inputInfoText')]"));
		metadataValueInputField.sendKeys(value);
		
		//click the + button to add anothor empty Study Context field for possible additional added collection metadata. 
		additionalInfo.findElement(By.className("fa-plus-square-o")).click();
	}
	
	// IMJ-127
	// The additional informations field does not exist anymore. Use addStudyContext() instead of addInformation().
	@Deprecated
	public void addInformation(String label, String link) {
		additionalInfo.findElement(By.className("fa-plus-square-o")).click();
		
		infoLabelBox.sendKeys(label);
		infoTextBox.sendKeys("This is a test collection.");
		infoUrlBox.sendKeys(link);
	}
	
	//FIXME: Only works if no other Study Context is defined.
	public void addOwnStudyContext(String key, String value) {
		additionalInfo.findElement(By.className("fa-plus-square-o")).click();
		
		infoLabelBox.sendKeys(key);
		infoTextBox.sendKeys(value);
	}
	
	// IMJ-243
	public void removeAuthor() {
		removeAuthor.click();
		wait.until(ExpectedConditions.elementToBeClickable(saveButton));
		submitChanges();
	}
	
	public void removeLabel() {
		removeInfo.click();
		wait.until(ExpectedConditions.elementToBeClickable(saveButton));
	}
	
	// IMJ-133
	public void addLogo(String filepath) {
		WebElement inputDiv = logoContainer.findElement(By.className("moxie-shim-html5"));
		WebElement inputFile = inputDiv.findElement(By.tagName("input"));
				
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		//Set the visibility of the upload element so that the sendKeys() method works
		jse.executeScript("arguments[0].style.visibility = 'visible';", inputFile);
		//Wait for the visibility of the upload element
		wait.until(ExpectedConditions.attributeToBe(inputFile, "visibility", "visible"));
		
		inputFile.sendKeys(filepath);
		
		// Wait for the upload to complete
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='uploader']//span[text()='100%']")));
		// Waiting for the '100%' upload-status to show up is sometimes not enough
		try {Thread.sleep(2000);} catch (Exception exc) {}
	}
	
	public CollectionEntryPage submitChanges() {
		WebElement staleElement = driver.findElement(By.id("uploader"));
		
		// Clicking the saveButton throws no Exceptions but the click does not work correctly (is not recognized)
		// Scrolling the saveButton into view solves this problem
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView();", saveButton);
		saveButton.click();
		
		SeleniumWrapper.waitForPageLoad(wait, staleElement);
		this.hideMessages();
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public List<String> getOrganisations() {
		List<String> ous = new LinkedList<>();
		List<WebElement> ouBoxes = driver.findElements(By.xpath("//input[contains(@id, 'OrgaName')]"));
		for (WebElement box : ouBoxes) {
			ous.add(box.getAttribute("value"));
		}
		
		return ous;
	}
}
