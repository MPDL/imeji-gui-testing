package spot.pages.notAdmin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;

public class CreateNewCollectionPage extends BasePage {

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
	
	public CreateNewCollectionPage(WebDriver driver) {
		super(driver);
	}

	public CollectionEntryPage fillForm(String collectionTitle, String givenName, String familyName, String orgName) {
		// person related
		setTitle(collectionTitle);
		setDescription("Das ist eine Testbeschreibung für eine neue Sammlung.");
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
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
	
	private void submitForm() {
		saveButton.click();
	}

	private void setTitle(String title) {
		titleTextField.sendKeys(title);
	}
	
	private void setDescription(String description) {
		descriptionTextField.sendKeys(description);
	}

	private boolean confirmFamilyName(String familyName) {
		String preFilledFamilyName = familyNameTextField.getAttribute("value");
		
		if (!preFilledFamilyName.equals(familyName)) 
			return false;
		return true;
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
	
	private boolean confirmOrganizationName(String orgName) {
		String preFilledOrgName = organizationNameTextField.getAttribute("value");
		
		if (!preFilledOrgName.equals(orgName))
			return false;
		return true;
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
	
}
