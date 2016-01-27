package spot.pages;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;


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
	@FindBy(xpath = ".//*[@id='batchEditForm:metadata:0:editBatchMd:inputText']")	
	private WebElement titleTextField;

	/** Meta data: ID **/
	@FindBy(xpath=".//*[@id='batchEditForm:metadata:1:editBatchMd:inputNumber']")
	private WebElement idTextField;

	/** Meta data: Author **/
	@FindBy(name = "batchEditForm:metadata:2:editBatchMd:inputPerson:inputFamilyNameText1")	
	private WebElement authorFamilyNameTextField;

	/** Meta data: Publication **/
	@FindBy(xpath = ".//*[@id='batchEditForm:metadata:3:j_idt135:inputPublicationURI']")
	private WebElement publicationLinkTextField;

	@FindBy(xpath = ".//*[@id='batchEditForm:metadata:3:j_idt135:exportFormatSelection']")
	private WebElement publicationCitationStyleDropBoxWebElement;

	/** Meta data: Date **/	
	@FindBy(css = "#dp1448611126646")
	private WebElement dateTextField;

	/** radio buttons **/
	@FindBy(xpath = ".//*[@id='batchEditForm:j_idt300:0']")
	private WebElement addOnlyIfValueIsEmptyRadioButton;

	@FindBy(xpath = ".//*[@id='batchEditForm:j_idt300:1']")
	private WebElement overWriteAllValuesRadioButton;

	/** action buttons **/
	@FindBy(css = ".imj_editGlobalAssignment .imj_submitPanel>input")
	private WebElement addToAllButton;

	@FindBy(css = ".imj_listFooter .imj_submitPanel input:nth-of-type(1)")
	private WebElement clearAllButton;

	@FindBy(css = ".imj_listFooter .imj_submitPanel input:nth-of-type(2)")
	private WebElement resetChangesButton;

	@FindBy(css = ".imj_listFooter .imj_submitPanel input:nth-of-type(3)")
	private WebElement cancelButton;

	@FindBy(css = ".imj_listFooter .imj_submitPanel input:nth-of-type(4)")
	private WebElement saveButton;

	@FindBy(css = ".imj_listFooter .imj_submitPanel input:nth-of-type(5)")
	private WebElement saveAndReturnToViewButton;

	public CollectiveEditOfDefaultMetaDataPage(WebDriver driver) {
		super(driver);

		metaDataIdentifiers = new HashMap<MetaDataIdentifier, String>();
		metaDataIdentifiers.put(MetaDataIdentifier.TITLE, "Title");
//		metaDataIdentifiers.put(MetaDataIdentifier.ID, "ID");
//		metaDataIdentifiers.put(MetaDataIdentifier.AUTHOR, "Author");
//		metaDataIdentifiers.put(MetaDataIdentifier.PUBLICATION, "publication");
//		metaDataIdentifiers.put(MetaDataIdentifier.DATE, "Date");

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
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='batchEditForm:metadata:0:editBatchMd:inputText']")));
		
		titleTextField.sendKeys(title);
	}

	public void setID(String id) {
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='batchEditForm:metadata:1:editBatchMd:inputNumber']")));
		
		idTextField.sendKeys(id);
		
	}

	public void setAuthorFamilyName(String authorFamilyName) {
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("batchEditForm:metadata:2:editBatchMd:inputPerson:inputFamilyNameText1")));
		authorFamilyNameTextField.sendKeys(authorFamilyName);
	}

	public void setPublicationLink(String publicationLink) {
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='batchEditForm:metadata:3:j_idt135:inputPublicationURI']")));
		publicationLinkTextField.sendKeys(publicationLink);
	}

	public void setDate(String date) {
		
		
		WebElement editGlobalAssignment = driver.findElement(By.className("imj_editGlobalAssignment"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("imj_editGlobalAssignment")));
		
		WebElement singleStatementEdit = editGlobalAssignment.findElement(By.className("imj_singleStatementEdit"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("imj_singleStatementEdit")));
		
		WebElement metadataValue = singleStatementEdit.findElement(By.cssSelector("input[type='text']"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='text']")));
		metadataValue.sendKeys(date);
	}

	public CollectionContentPage editMetaData(String title, String authorFamilyName, String id, String publicationLink, String date) {
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='batchEditForm:selStatement']")));
		
		metaDataElementsDrobBox = new Select(metaDataElementsDropBoxWebElement);
		
		for (Map.Entry<MetaDataIdentifier, String> metaDataIdentifierEntry : metaDataIdentifiers.entrySet()) {

			String metaDataIdentifierVisibleText = metaDataIdentifierEntry.getValue();
			
			try {
				metaDataElementsDrobBox.selectByVisibleText(metaDataIdentifierVisibleText);
			} catch (StaleElementReferenceException e) {
				
				if (retryingFindClick(By.xpath(".//*[@id='batchEditForm:selStatement']")))
					metaDataElementsDrobBox.selectByVisibleText(metaDataIdentifierVisibleText);
			}

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
			
			
			addToAllButton.click();			
			
		}
		
		
		saveAndReturnToViewButton.click();
		
		return PageFactory.initElements(driver, CollectionContentPage.class);
	}

}
