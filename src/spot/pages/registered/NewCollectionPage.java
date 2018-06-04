package spot.pages.registered;

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

public class NewCollectionPage extends BasePage {

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

	public CollectionEntryPage createCollection(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		fillForm(collectionTitle, collectionDescription, givenName, familyName, orgName);
		
		submitForm();
		
		if (errorOccurred)
			return null;
		
		wait.until(ExpectedConditions.elementToBeClickable(By.id("colForm:upload")));
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionEntryPage createCollection3Authors(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		try {
			fillForm(collectionTitle, collectionDescription, givenName, familyName, orgName);
			addAuthor();
			WebElement author2Name = driver.findElement(By.id("editContainer:form:persons:1:collectionAuthor:inputFamilyNameText"));
			author2Name.sendKeys("Thesecond");
			WebElement author2Org = driver.findElement(By.xpath("//input[contains(@id, 'form:persons:1') and contains(@id, 'inputOrgaName1')]"));
			author2Org.sendKeys("MPDL");
			addAuthor();
			try { Thread.sleep(2000); } catch (InterruptedException e) { }
			WebElement author3Name = retryingElement(By.id("editContainer:form:persons:1:collectionAuthor:inputFamilyNameText"));
			author3Name.sendKeys("Thethird");
			WebElement author3Org = retryingElement(By.xpath("//input[contains(@id, 'form:persons:1') and contains(@id, 'inputOrgaName1')]"));
			author3Org.sendKeys("Max Planck Society");
			
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
	
	public CollectionEntryPage createCollection1Author2OUs(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		fillForm(collectionTitle, collectionDescription, givenName, familyName, orgName);
		addOrganization();
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
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", saveButton);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("loaderWrapper")));
	}

	private void setTitle(String title) {
		if (title.equals(""))
			errorOccurred = true;
		
		titleTextField.sendKeys(title);
	}
	
	private void setDescription(String description) {
		descriptionTextField.sendKeys(description);
	}

	public void addAuthor() {
		addAuthorButton.click();
	}

	public void addOrganization() {
		addOrganizationButton.click();
	}
	
}
