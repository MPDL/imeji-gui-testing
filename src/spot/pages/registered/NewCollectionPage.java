package spot.pages.registered;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;

public class NewCollectionPage extends BasePage {

	/** error occurred while filling in 'create new collection' form */
	private boolean errorOccurred;
	
	@FindBy(name="editContainer:mediaContainerForm:inputTitleText")
	private WebElement titleTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:inputDescription")
	private WebElement descriptionTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:inputFamilyNameText")
	private WebElement familyNameTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:inputGiveNameText")
	private WebElement givenNameTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:inputIdentifier")
	private WebElement identifierTextField;
	
	@FindBy(css="div.imj_organisation>div:nth-of-type(1)>div.imj_admindataValue>div.imj_admindataValueEntry>input")
	private WebElement organizationNameTextField;
	
	@FindBy(id="editContainer:mediaContainerForm:btn_saveCollection")
	private WebElement saveButton;
	
	@FindBy(css=".imj_cancelButton")
	private WebElement cancelButton;
	
	@FindBy(css="h4>.imj_inlineButtonGroup>a")
	private WebElement addAuthorButton;
	
	@FindBy(xpath="//a[contains(@id, 'editContainer:mediaContainerForm:persons:0:collectionAuthor:')]")
	private WebElement addOrganizationButton;
	
	@FindBy(id="editContainer:mediaContainerForm:notificationCheckBox")
	private WebElement emailOnDownload;
	
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
		identifierTextField.clear();
		
		organizationNameTextField.clear();
	}

	public CollectionEntryPage createCollection(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		fillForm(collectionTitle, collectionDescription, givenName, familyName, orgName);
		
		submitForm();
		
		if (errorOccurred)
			return null;
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionEntryPage createCollectionWithNotification(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		fillForm(collectionTitle, collectionDescription, givenName, familyName, orgName);
		if (!emailOnDownload.isSelected())
			emailOnDownload.click();
		
		submitForm();
		
		if (errorOccurred)
			return null;
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	public CollectionEntryPage createCollection3Authors(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		try {
			fillForm(collectionTitle, collectionDescription, givenName, familyName, orgName);
			addAuthor();
			WebElement author2Name = driver.findElement(By.id("editContainer:mediaContainerForm:persons:1:collectionAuthor:inputFamilyNameText"));
			author2Name.sendKeys("Thesecond");
			WebElement author2Org = driver.findElement(By.xpath("//input[contains(@id, 'mediaContainerForm:persons:1') and contains(@id, 'inputOrgaName1')]"));
			author2Org.sendKeys("MPDL");
			addAuthor();
			try { Thread.sleep(2000); } catch (InterruptedException e) { }
			WebElement author3Name = retryingElement(By.id("editContainer:mediaContainerForm:persons:1:collectionAuthor:inputFamilyNameText"));
//			wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(author3Name)));
			author3Name.sendKeys("Thethird");
			WebElement author3Org = retryingElement(By.xpath("//input[contains(@id, 'mediaContainerForm:persons:1') and contains(@id, 'inputOrgaName1')]"));
//			wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(author3Org)));
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
	
	public CollectionEntryPage createCollection3AuthorsNotification(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		try {
			fillForm(collectionTitle, collectionDescription, givenName, familyName, orgName);
			
			addAuthor();
			WebElement author2Name = driver.findElement(By.id("editContainer:mediaContainerForm:persons:1:collectionAuthor:inputFamilyNameText"));
			author2Name.sendKeys("AuthorTwo");
			WebElement author2Org = driver.findElement(By.xpath("//input[contains(@id, 'mediaContainerForm:persons:1') and contains(@id, 'inputOrgaName1')]"));
			author2Org.sendKeys(orgName);
			
			addAuthor();
			try { Thread.sleep(2000); } catch (InterruptedException e) { }
			WebElement author3Name = retryingElement(By.id("editContainer:mediaContainerForm:persons:1:collectionAuthor:inputFamilyNameText"));
			
//			try {
//				wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(author3Name)));
//			}
//			catch (TimeoutException exc) {}
			
			author3Name.sendKeys("AuthorThree");
			WebElement author3Org = retryingElement(By.xpath("//input[contains(@id, 'mediaContainerForm:persons:1') and contains(@id, 'inputOrgaName1')]"));
			
//			try {
//				wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(author3Org)));
//			}
//			catch (TimeoutException exc) {}
			
			author3Org.sendKeys(orgName);
			
			if (!emailOnDownload.isSelected())
				emailOnDownload.click();
			
			submitForm();
			
			if (errorOccurred)
				return null;
			
			return PageFactory.initElements(driver, CollectionEntryPage.class);
		}
		catch (StaleElementReferenceException exc) {
			CollectionsPage collectionsPage = this.goToCollectionPage();
			NewCollectionPage newCollection = collectionsPage.createCollection();
			return newCollection.createCollection3AuthorsNotification(collectionTitle, collectionDescription, givenName, familyName, orgName);
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
		
		// person related
		setTitle(collectionTitle);
		setDescription(collectionDescription);
		clearSomePrefilledTextFields(familyName, orgName);
		//confirmFamilyName(familyName);
		//confirmGivenName(givenName);
		//confirmIdentifier();
		
		// organization related
		//confirmOrganizationName(orgName);
		//confirmOrganizationIdentifier();	
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
		//saveButton.click();
		((JavascriptExecutor)driver).executeScript("arguments[0].click();", saveButton);
		try { Thread.sleep(2000); } catch (InterruptedException e) { }
	}

	private void setTitle(String title) {
		if (title.equals(""))
			errorOccurred = true;
		
		titleTextField.sendKeys(title);
	}
	
	private void setDescription(String description) {
		descriptionTextField.sendKeys(description);
	}

	private void confirmFamilyName(String familyName) {
		String preFilledFamilyName = familyNameTextField.getAttribute("value");
		
		if (!preFilledFamilyName.equals(familyName)) 
			errorOccurred = true;
	}
	
	private boolean confirmGivenName(String givenName) {
		String preFilledGivenName = givenNameTextField.getAttribute("value");
		
		if (!preFilledGivenName.equals(givenName))
			return false;
		return true;
	}
	
	private boolean confirmIdentifier() {
		String identifier = identifierTextField.getAttribute("value");
		
		if (identifier.length() <= 0)
			return false;
		return true;
	}
	
	private void confirmOrganizationName(String orgName) {
		
		String preFilledOrgName = organizationNameTextField.getAttribute("value");
		
		if (!preFilledOrgName.equals(orgName))
			errorOccurred = true;
	}
	
	private void checkNewMetaDataFromTemplate() {
		WebElement templateCheckbox = driver.findElement(By.xpath("//input[contains(@id, 'editContainer:mediaContainerForm:') and contains(@id, ':copyProfile')]"));
		if (!templateCheckbox.isSelected())
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", templateCheckbox);
	}

	public void addAuthor() {
		addAuthorButton.click();
	}

	public void addOrganization() {
		addOrganizationButton.click();
	}
	
}
