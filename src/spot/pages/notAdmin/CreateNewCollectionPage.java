package spot.pages.notAdmin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;

public class CreateNewCollectionPage extends BasePage {

	/** error occurred while filling in 'create new collection' form */
	private boolean errorOccurred;
	
	@FindBy(name="editContainer:mediaContainerForm:inputTitleText")
	private WebElement titleTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:inputDescription")
	private WebElement descriptionTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:inputFamilyNameText1")
	private WebElement familyNameTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:inputGiveNameText1")
	private WebElement givenNameTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:inputAlternativeName1")
	private WebElement alternativeNameTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:inputIdentifier1")
	private WebElement identifierTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:j_idt247:0:inputOrgaName1")
	private WebElement organizationNameTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:j_idt247:0:inputOrgaDescription1")
	private WebElement organizationDecriptionTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:j_idt247:0:inputOrgaIdentifier1")
	private WebElement organizationIdentifierTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:j_idt247:0:inputOrgaCity1")
	private WebElement organizationCityTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:j_idt247:0:inputOrgaCountry1")
	private WebElement organizationCountryTextField;
	
	@FindBy(name="editContainer:mediaContainerForm:j_idt299")
	private WebElement defineMetaDataProfileLaterRadioBox;
	
	@FindBy(name="editContainer:mediaContainerForm:j_idt344")
	private WebElement saveButton;
	
	@FindBy(xpath=".//*[@id='editContainer:mediaContainerForm:submitButtonPanel']/a")
	private WebElement cancelButton;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:j_idt214")
	private WebElement addAuthorButton;
	
	@FindBy(name="editContainer:mediaContainerForm:persons:0:collectionAuthor:j_idt247:0:j_idt276")
	private WebElement addOrganizationButton;
	
	public CreateNewCollectionPage(WebDriver driver) {
		super(driver);
		
		errorOccurred = false;
	}

	public void cancelCollectionCreation() {
		cancelButton.click();
	}
	
	public void clearForm() {
	
		clearPreFilledTextFields();
		submitForm();
	}
	
	private void clearPreFilledTextFields() {
		familyNameTextField.clear();
		givenNameTextField.clear();
		identifierTextField.clear();
		
		organizationNameTextField.clear();
		organizationIdentifierTextField.clear();
	}

	public CollectionEntryPage fillForm(String collectionTitle, String collectionDescription, String givenName, String familyName, String orgName) {
		
		// person related
		setTitle(collectionTitle);
		setDescription(collectionDescription);
		confirmFamilyName(familyName);
		confirmGivenName(givenName);
		setAlternativeName("testtest");
		confirmIdentifier();
		
		// organization related
		confirmOrganizationName(orgName);
		setOrganizationDescription("Das ist eine Testbeschreibung für die Organisation.");
		confirmOrganizationIdentifier();
		setCity("München");
		setCountry("Deutschland");
		
		selectRadioButtonDefineMetadataProfileLater();
		
		submitForm();
		
		if (errorOccurred)
			return null;
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	private void submitForm() {
		saveButton.click();
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
	
	private void setAlternativeName(String alternativeName) {
		alternativeNameTextField.sendKeys(alternativeName);
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
	
	private void setOrganizationDescription(String orgDescription) {
		organizationDecriptionTextField.sendKeys(orgDescription);
	}
	
	private boolean confirmOrganizationIdentifier() {
		String orgIdentifier = organizationIdentifierTextField.getAttribute("value");
		
		if (orgIdentifier.length() <= 0)
			return false;
		return true;
	}
	
	private void setCity(String city) {
		organizationCityTextField.sendKeys(city);
	}
	
	private void setCountry(String country) {
		organizationCountryTextField.sendKeys(country);
	}
	
	private void selectRadioButtonDefineMetadataProfileLater() {
		if (!defineMetaDataProfileLaterRadioBox.isSelected())
			defineMetaDataProfileLaterRadioBox.click();
	}

	public void addAuthor() {
		addAuthorButton.click();
	}

	public void addOrganization() {
		addOrganizationButton.click();
	}
	
}
