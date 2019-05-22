package spot.pages.registered;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.StartPage;
import test.base.SeleniumWrapper;

//TODO: Merge with EditCollectionPage!?
public class NewCollectionPage extends BasePage {

	private static final Logger log4j = LogManager.getLogger(NewCollectionPage.class.getName());
	
	// error occurred while filling in 'create new collection' form
	private boolean errorOccurred;
	
	@FindBy(name="editContainer:form:inputTitleText")
	private WebElement titleTextField;
	
	@FindBy(name="editContainer:form:inputDescription")
	private WebElement descriptionTextField;
	
	@FindBy(name="editContainer:form:persons:0:collectionAuthor:inputFamilyNameText")
	private WebElement familyNameTextField;
	
	@FindBy(name="editContainer:form:persons:0:collectionAuthor:inputGiveNameText")
	private WebElement givenNameTextField;
	
	@FindBy(xpath="//input[contains(@id, '0:inputOrgaName1')]")
	private WebElement organizationNameTextField;
	
	@FindBy(id="editContainer:form:save")
	private WebElement saveButton;
	
	@FindBy(css=".imj_cancelButton")
	private WebElement cancelButton;
	
	@FindBy(css="h4>.imj_inlineButtonGroup>a")
	private WebElement addAuthorButton;
	
	@FindBy(xpath="//a[contains(@id, 'editContainer:form:persons:0:collectionAuthor:')]")
	private WebElement addOrganizationButton;
	
	public NewCollectionPage(WebDriver driver) {
		super(driver);
		
		errorOccurred = false;
		
		PageFactory.initElements(driver, this);
	}

	public void cancelCollectionCreation() {
		cancelButton.click();
	}
	
	public void submitEmptyForm() {
		clearPreFilledTextFields();
		submitForm();
	}
	
	private void clearPreFilledTextFields() {
		familyNameTextField.clear();
		givenNameTextField.clear();
		
		organizationNameTextField.clear();
	}

	// IMJ-83
	public CollectionEntryPage createCollection(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		fillForm(collectionTitle, collectionDescription, givenName, familyName, orgName);
		
		submitForm();
		
		if (errorOccurred)
			return null;
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("colForm:upload")));
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionEntryPage createCollection(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName, List<String> studyTypes) {
		selectStudyTypes(studyTypes);
		
		return createCollection(collectionTitle, collectionDescription, givenName, familyName, orgName);
	}
	
	// IMJ-86
	public CollectionEntryPage createCollection3Authors(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		try {
			fillForm(collectionTitle, collectionDescription, givenName, familyName, orgName);
			this.addAuthor("Thesecond", "MPDL");
			this.addAuthor("Thethird", "Max Planck Society");
			
			submitForm();
			
			if (errorOccurred)
				return null;
			
			return PageFactory.initElements(driver, CollectionEntryPage.class);
		}
		catch (StaleElementReferenceException exc) {
			CollectionsPage collectionsPage = this.goToCollectionPage();
			NewCollectionPage newCollection = collectionsPage.createCollection();
			return newCollection.createCollection3Authors(collectionTitle, collectionDescription, givenName, familyName, orgName);
		}
	}
	
	// IMJ-245
	public CollectionEntryPage createCollection1Author2OUs(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		fillForm(collectionTitle, collectionDescription, givenName, familyName, orgName);
		addOrganizationToFirstAuthor();
		WebElement org2 = driver.findElement(By.xpath("//input[contains(@name, '1:inputOrgaName1')]"));
		org2.sendKeys("MPDL");
		
		submitForm();
		
		if (errorOccurred)
			return null;
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}

	private void fillForm(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		setTitle(collectionTitle);
		setDescription(collectionDescription);
		clearSomePrefilledTextFields(familyName, orgName);
	}
	
	//FIXME: familyName and orgName is not set, only the fields are cleared
	private void clearSomePrefilledTextFields(String familyName, String orgName) {
		if (familyName.equals("")) {
			familyNameTextField.clear();
			errorOccurred = true;
		}
		if (orgName.equals("")) {
			organizationNameTextField.clear();
			errorOccurred = true;
		}
	}
	
	private void submitForm() {
		WebElement saveButton = driver.findElement(By.id("editContainer:form:save"));
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", saveButton);
		
		SeleniumWrapper.waitForPageLoad(wait, saveButton);
	}

	private void setTitle(String title) {
		if (title.equals(""))
			errorOccurred = true;
		
		titleTextField.sendKeys(title);
	}
	
	private void setDescription(String description) {
		descriptionTextField.sendKeys(description);
	}
	
	public void addAuthor(String familyName, String organisationName) {
		addAuthor();
		// addAuthor() always adds authors behind the first author: Therefore selecting the elements containing '1' as counter in the selection-expression is correct
		WebElement additionalAuthorName = driver.findElement(By.xpath("//input[contains(@id, 'form:persons:1') and contains(@id, 'inputFamilyNameText')]"));
		additionalAuthorName.sendKeys(familyName);
		WebElement additionalAuthorOrganisation = driver.findElement(By.xpath("//input[contains(@id, 'form:persons:1') and contains(@id, 'inputOrgaName')]"));
		additionalAuthorOrganisation.sendKeys(organisationName);
	}

	public void addAuthor() {
		int autherElementCound = driver.findElements(By.className("imj_authorMetadataSet")).size();
		
		// Click the first AddAuthor-Button
		addAuthorButton.click();
		
		wait.until(ExpectedConditions.numberOfElementsToBe(By.className("imj_authorMetadataSet"), autherElementCound+1));
	}

	public void addOrganizationToFirstAuthor() {
		int organisationInputElementCound = driver.findElements(By.xpath("//input[contains(@id,'inputOrgaName')]")).size();
		
		addOrganizationButton.click();
		
		wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//input[contains(@id,'inputOrgaName')]"), organisationInputElementCound+1));
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
	
}
