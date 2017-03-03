package spot.pages;

import java.awt.AWTException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import spot.util.DefaultMetaDataProfile;

public class SingleUploadPage extends BasePage {
	
	@FindBy(css="input[type='file']")
	private WebElement uploadButton;
	
	@FindBy(id="singleUpload:collections")
	private WebElement selectCollectionToUploadDropBox;
	
	@FindBy(id="singleUpload:submitBottom")
	private WebElement saveButton;
	
	@FindBy(id="singleUpload:metadata:0:editMd:inputText")
	private WebElement metaDataTitleTextField;
	
	@FindBy(id="singleUpload:metadata:2:editMd:inputPerson:inputIdentifier1")
	private WebElement metaDataIDTextField;
	
	@FindBy(id="singleUpload:metadata:1:editMd:inputPerson:inputFamilyNameText1")
	private WebElement metaDataAuthorFamilyNameTextField;
	
	@FindBy(xpath="//input[contains(@id, 'inputOrgaName')]")
	private WebElement metaDataOrganizationTextField;
	
	@FindBy(id="singleUpload:metadata:3:editMd:inputPublicationURI")
	private WebElement metaDataPublicationTextField;
	
	@FindBy(id="singleUpload:metadata:2:editMd:inputDate")
	private WebElement metaDataDateTextField;
	
	@FindBy(id="singleUpload:metadata:3:editMd:inputLocationName")
	private WebElement metaDataGeolocNameTextField;
	
	@FindBy(id="singleUpload:metadata:3:editMd:inputLatitude")
	private WebElement metaDataGeolocLatitudeField;
	
	@FindBy(id="singleUpload:metadata:3:editMd:inputLongitude")
	private WebElement metaDataGeolocLongitudeField;
	
	@FindBy(id = "singleUpload:licenseEditorContainer")
	private WebElement licenseDropdown;
	
	@FindBy(id="uploader")
	private WebElement inputFileTagContainer;
	
	@FindBy(id="container")
	private WebElement singleUploadForm;
	
	public SingleUploadPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public ItemViewPage upload(String pathToFile, String collectionTitle) throws AWTException, TimeoutException {	

		JavascriptExecutor jse = (JavascriptExecutor)driver;		
						
		jse.executeScript("arguments[0].style.visibility = 'visible';", singleUploadForm);
		jse.executeScript("arguments[0].style.display = 'block';", singleUploadForm);
		jse.executeScript("arguments[0].style.opacity = '1';", singleUploadForm);
		jse.executeScript("arguments[0].style.height = '1px';", singleUploadForm);
		jse.executeScript("arguments[0].style.width = '1px';", singleUploadForm);
		uploadButton = singleUploadForm.findElement(By.tagName("input"));
		uploadButton.sendKeys(pathToFile);		
		
		wait.withTimeout(3, TimeUnit.SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.id("singleUpload:collections")));
		Select selectDropBox = new Select(selectCollectionToUploadDropBox);
		selectDropBox.selectByVisibleText(collectionTitle);
		
		fillLicense();
				
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("singleUpload:submitBottom")));
		saveButton.click();
		
		return PageFactory.initElements(driver, ItemViewPage.class);
	}
	
	public void jqClick(String selector, JavascriptExecutor driver, String path) {
	    driver.executeScript("$('" + selector + "').sendKeys('" + path + "')");
	}
	
	public ItemViewPage uploadAndFillMetaData(String filePath, String collectionTitle) throws AWTException, TimeoutException {
		
		JavascriptExecutor jse = (JavascriptExecutor)driver;		
		
		jse.executeScript("arguments[0].style.visibility = 'visible';", singleUploadForm);
		jse.executeScript("arguments[0].style.display = 'block';", singleUploadForm);
		jse.executeScript("arguments[0].style.opacity = '1';", singleUploadForm);
		jse.executeScript("arguments[0].style.height = '1px';", singleUploadForm);
		jse.executeScript("arguments[0].style.width = '1px';", singleUploadForm);
		uploadButton = singleUploadForm.findElement(By.tagName("input"));
		uploadButton.sendKeys(filePath);
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#singleUpload\\:collections")));
		Select selectDropBox = new Select(selectCollectionToUploadDropBox);
		selectDropBox.selectByVisibleText(collectionTitle);
		
		fillMetaData();
		fillLicense();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#singleUpload\\:submitBottom")));
		saveButton.click();
		
		return PageFactory.initElements(driver, ItemViewPage.class);
	}

	private void fillMetaData() {
		
		DefaultMetaDataProfile defaultMetaDataProfile = DefaultMetaDataProfile.getDefaultMetaDataProfileInstance();
				
		//setting title
		wait.until(ExpectedConditions.visibilityOf(metaDataTitleTextField));
		metaDataTitleTextField.sendKeys(defaultMetaDataProfile.getTitle());

		//setting author family name
		wait.until(ExpectedConditions.visibilityOf(metaDataAuthorFamilyNameTextField));
		metaDataAuthorFamilyNameTextField.sendKeys(defaultMetaDataProfile.getAuthor());
		
		//setting organization
		wait.until(ExpectedConditions.visibilityOf(metaDataOrganizationTextField));
		metaDataOrganizationTextField.sendKeys(defaultMetaDataProfile.getOrganization());
		
		//setting date
		wait.until(ExpectedConditions.visibilityOf(metaDataDateTextField));
		metaDataDateTextField.sendKeys(defaultMetaDataProfile.getDate());
		
		//setting geolocation
		wait.until(ExpectedConditions.visibilityOf(metaDataGeolocNameTextField));
		metaDataGeolocNameTextField.sendKeys(defaultMetaDataProfile.getGeolocName());
		metaDataGeolocLatitudeField.sendKeys(defaultMetaDataProfile.getLatitude());
		metaDataGeolocLongitudeField.sendKeys(defaultMetaDataProfile.getLongitude());
		
		//setting publication link
		//wait.until(ExpectedConditions.visibilityOf(metaDataPublicationTextField));
		//metaDataPublicationTextField.sendKeys(defaultMetaDataProfile.getPublicationLink());
	}
	
	/**
	 * Released collections require licenses for all items. Chooses ODbL v1.0 by default.
	 */
	private void fillLicense() {
		try {
			PageFactory.initElements(driver, this);
			WebElement licenseDropbox = wait.withTimeout(3, TimeUnit.SECONDS).until(ExpectedConditions.presenceOfNestedElementLocatedBy(licenseDropdown, By.tagName("select")));
			Select licenseSelect = new Select(licenseDropbox);
			licenseSelect.selectByValue("ODC_ODbL");
		}
		catch (TimeoutException exc) {}
	}
	
}
