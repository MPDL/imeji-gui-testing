package spot.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import spot.components.SortingComponent.Order;

/**
 * default meta data profile
 * 
 * @author kocar
 *
 */
public class CollectiveEditOfDefaultMetaDataPage extends BasePage {

	public enum CitationStyles {
		APA, AJP, JUS
	};

	public enum MetaDataIdentifier {
		TITLE, ID, AUTHOR, PUBLICATION, DATE
	};

	private HashMap<MetaDataIdentifier, String> metaDataIdentifiers;

	@FindBy(xpath = ".//*[@id='batchEditForm:selStatement']")
	private WebElement metaDataElementsDropBoxWebElement;

	private Select metaDataElementsDrobBox;

	/** Meta data: Title **/
	@FindBy(xpath = ".//*[@id='batchEditForm:metadata:0:j_idt135:inputText']")
	private WebElement titleTextField;

	/** Meta data: ID **/
	@FindBy(xpath = ".//*[@id='batchEditForm:editor']/div[1]/div/div[2]/div[1]/div/input")
	private WebElement idTextField;

	/** Meta data: Author **/
	@FindBy(xpath = ".//*[@id='batchEditForm:metadata:2:j_idt135:j_idt218:inputFamilyNameText1']")
	private WebElement authorFamilyNameTextField;

	/** Meta data: Publication **/
	@FindBy(xpath = ".//*[@id='batchEditForm:metadata:3:j_idt135:inputPublicationURI']")
	private WebElement publicationLinkTextField;

	@FindBy(xpath = ".//*[@id='batchEditForm:metadata:3:j_idt135:exportFormatSelection']")
	private WebElement publicationCitationStyleDropBoxWebElement;

	private Select publicationCitationStyleDropBox;

	/** Meta data: Date **/	
//	@FindBy(id = "dp1447408776934")
	@FindBy(css = "#dp1447408776934")
	private WebElement dateTextField;

	/** radio buttons **/
	@FindBy(xpath = ".//*[@id='batchEditForm:j_idt300:0']")
	private WebElement addOnlyIfValueIsEmptyRadioButton;

	@FindBy(xpath = ".//*[@id='batchEditForm:j_idt300:1']")
	private WebElement overWriteAllValuesRadioButton;

	/** action buttons **/
	@FindBy(xpath = ".//*[@id='batchEditForm:j_idt304']")
	private WebElement addToAllButton;

	@FindBy(xpath = ".//*[@id='batchEditForm:j_idt492']")
	private WebElement clearAllButton;

	@FindBy(xpath = ".//*[@id='batchEditForm:j_idt493']")
	private WebElement resetChangesButton;

	@FindBy(xpath = ".//*[@id='batchEditForm:j_idt495']")
	private WebElement cancelButton;

	@FindBy(xpath = ".//*[@id='batchEditForm:j_idt496']")
	private WebElement saveButton;

	@FindBy(xpath = ".//*[@id='batchEditForm:j_idt497']")
	private WebElement saveAndReturnToViewButton;

	public CollectiveEditOfDefaultMetaDataPage(WebDriver driver) {
		super(driver);

		metaDataIdentifiers = new HashMap<MetaDataIdentifier, String>();
		metaDataIdentifiers.put(MetaDataIdentifier.TITLE, "Title");
		metaDataIdentifiers.put(MetaDataIdentifier.ID, "ID");
		metaDataIdentifiers.put(MetaDataIdentifier.AUTHOR, "Author");
		metaDataIdentifiers.put(MetaDataIdentifier.PUBLICATION, "publication");
		metaDataIdentifiers.put(MetaDataIdentifier.DATE, "Date");

		PageFactory.initElements(driver, this);
	}

	public void clickAddOnlyIfValueIsEmpty() {
		if (!addOnlyIfValueIsEmptyRadioButton.isSelected())
			addOnlyIfValueIsEmptyRadioButton.click();
	}

	public void clickOverWriteAllValues() {
		if (!overWriteAllValuesRadioButton.isSelected())
			overWriteAllValuesRadioButton.click();
	}

	public void setTitle(String title) {
		titleTextField.sendKeys(title);
	}

	public void setID(String id) {
		idTextField.sendKeys(id);
	}

	public void setAuthorFamilyName(String authorFamilyName) {
		authorFamilyNameTextField.sendKeys(authorFamilyName);
	}

	public void setPublicationLink(String publicationLink) {
		publicationLinkTextField.sendKeys(publicationLink);
	}

	public void setDate(String date) {
		waitForInitationPageElements(10);
		System.out.println();
//		dateTextField.sendKeys(date);
	}

	public CollectionContentPage editMetaData(String title, String authorFamilyName, String id, String publicationLink, String date) {
		
		waitForInitationPageElements(10);
		
		metaDataElementsDrobBox = new Select(metaDataElementsDropBoxWebElement);
		
//		WebElement editglobalAssignment = driver.findElement(By.className("imj_editGlobalAssignment"));
//		WebElement findElement = editglobalAssignment.findElement(By.className("imj_submitButton"));
		
//		setTitle(title);
//		addToAllButton.click();

		
		for (Map.Entry<MetaDataIdentifier, String> metaDataIdentifierEntry : metaDataIdentifiers.entrySet()) {
			waitForInitationPageElements(50);
			
			String metaDataIdentifierVisibleText = metaDataIdentifierEntry.getValue();
			metaDataElementsDrobBox.selectByVisibleText(metaDataIdentifierVisibleText);

			waitForInitationPageElements(10);

			MetaDataIdentifier metaDataIdentifier = metaDataIdentifierEntry.getKey();

			switch (metaDataIdentifier) {
			case TITLE:
				setTitle(title);
				break;
			case AUTHOR:
				setAuthorFamilyName(authorFamilyName);
				break;
			case DATE:
				setDate(date);
				break;
			case ID:
				setID(id);
				break;
			case PUBLICATION:
				setPublicationLink(publicationLink);
				break;
			}
			
			waitForInitationPageElements(100);
			addToAllButton.click();
//			try {
//				findElement.click();
//			} catch(NoSuchElementException nse) {
//				System.out.println("no such element");
//			}
		}
		
		
		saveAndReturnToViewButton.click();
		
		return PageFactory.initElements(driver, CollectionContentPage.class);
	}

}
