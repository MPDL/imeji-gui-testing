package spot.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;

/**
 * Page Object Pattern for Admin -> Configuration -> Collections Page.
 * 
 * @author helk
 *
 */
public class CollectionsConfigurationPage extends BasePage{
	
	@FindBy(className = "imj_submitButton")
	private WebElement saveAllButton;

	@FindBy(xpath = "(//textarea[@class='imj_admindataEdit'])[1]")
	private WebElement studyTypesInputField;
	
	@FindBy(xpath = "(//textarea[@class='imj_admindataEdit'])[2]")
	private WebElement metadataFieldsInputField;
	
	@FindBy(xpath = "(//textarea[@class='imj_admindataEdit'])[3]")
	private WebElement metadataAutosuggestInputField;
	
	public CollectionsConfigurationPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * Set a list of study types. <br>
	 * Each study type within the String must be separated by a new line (\n).
	 * 
	 * @param studyTypes All study types separated by a new line (\n).
	 * @return The CollectionsConfigurationPage
	 */
	public CollectionsConfigurationPage setStudyTypes(String studyTypes) {
		studyTypesInputField.clear();
		studyTypesInputField.sendKeys(studyTypes);
		
		saveAllButton.click();
		
		return PageFactory.initElements(driver, CollectionsConfigurationPage.class);
	}
	
	/**
	 * Set a list of metadata fields. <br>
	 * Each metadata field within the String must be separated by a new line (\n).
	 * 
	 * @param metadataFields All metadata field separated by a new line (\n).
	 * @return The CollectionsConfigurationPage
	 */
	public CollectionsConfigurationPage setMetadataFields(String metadataFields) {
		metadataFieldsInputField.clear();
		metadataFieldsInputField.sendKeys(metadataFields);
		
		saveAllButton.click();
		
		return PageFactory.initElements(driver, CollectionsConfigurationPage.class);
	}
	
	/**
	 * Set a list of metadata autosuggests. <br>
	 * Each metadata autosuggest within the String must be separated by a new line (\n).
	 * 
	 * @param metadataAutosuggests All metadata autosuggests separated by a new line (\n).
	 * @return The CollectionsConfigurationPage
	 */
	public CollectionsConfigurationPage setMetadataAutosuggests(String metadataAutosuggests) {
		metadataAutosuggestInputField.clear();
		metadataAutosuggestInputField.sendKeys(metadataAutosuggests);
		
		saveAllButton.click();
		
		return PageFactory.initElements(driver, CollectionsConfigurationPage.class);
	}

	public CollectionsConfigurationPage clearStudyTypes() {
		studyTypesInputField.clear();
		saveAllButton.click();
		
		return PageFactory.initElements(driver, CollectionsConfigurationPage.class);
	}
	
	public CollectionsConfigurationPage clearMetadataFields() {
		metadataFieldsInputField.clear();
		saveAllButton.click();
		
		return PageFactory.initElements(driver, CollectionsConfigurationPage.class);
	}
	
	public CollectionsConfigurationPage clearMetadataAutosuggests() {
		metadataAutosuggestInputField.clear();
		saveAllButton.click();
		
		return PageFactory.initElements(driver, CollectionsConfigurationPage.class);
	}
	
}
