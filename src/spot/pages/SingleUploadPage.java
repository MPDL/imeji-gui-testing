package spot.pages;

import java.awt.AWTException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

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
	
	@FindBy(id="singleUpload:metadata:2:editMd:inputPerson:inputFamilyNameText1")
	private WebElement metaDataAuthorFamilyNameTextField;
	
	@FindBy(id="singleUpload:metadata:2:editMd:inputPerson:j_idt488:0:inputOrgaName")
	private WebElement metaDataOrganizationTextField;
	
	@FindBy(id="singleUpload:metadata:3:editMd:inputPublicationURI")
	private WebElement metaDataPublicationTextField;
	
	@FindBy(id="singleUpload:metadata:4:editMd:inputDate")
	private WebElement metaDataDateTextField;
	
	@FindBy(id="singleUpload:metadata:5:editMd:inputLocationName")
	private WebElement metaDataGeolocNameTextField;
	
	@FindBy(id="singleUpload:metadata:5:editMd:inputLatitude")
	private WebElement metaDataGeolocLatitudeField;
	
	@FindBy(id="singleUpload:metadata:5:editMd:inputLongitude")
	private WebElement metaDataGeolocLongitudeField;
	
	@FindBy(id="uploader")
	private WebElement inputFileTagContainer;
	
	@FindBy(id="container")
	private WebElement singleUploadForm;
	
	public SingleUploadPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public DetailedItemViewPage upload(String pathToFile, String collectionTitle) throws AWTException, TimeoutException {	

		JavascriptExecutor jse = (JavascriptExecutor)driver;		
						
		jse.executeScript("arguments[0].style.visibility = 'visible';", singleUploadForm);
		//jse.executeScript("arguments[0].style.visibility = 'visible';", inputTag);
		jse.executeScript("arguments[0].style.display = 'block';", singleUploadForm);
		jse.executeScript("arguments[0].style.opacity = '1';", singleUploadForm);
		jse.executeScript("arguments[0].style.height = '1px';", singleUploadForm);
		jse.executeScript("arguments[0].style.width = '1px';", singleUploadForm);
		uploadButton = singleUploadForm.findElement(By.tagName("input"));
		uploadButton.sendKeys(pathToFile);		
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("singleUpload:collections")));
		Select selectDropBox = new Select(selectCollectionToUploadDropBox);
		selectDropBox.selectByVisibleText(collectionTitle);
				
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("singleUpload:submitBottom")));
		saveButton.click();
		
		return PageFactory.initElements(driver, DetailedItemViewPage.class);
	}
	
	public void jqClick(String selector, JavascriptExecutor driver, String path) {
	    driver.executeScript("$('" + selector + "').sendKeys('" + path + "')");
	}
	
	public DetailedItemViewPage uploadAndFillMetaData(String filePath, String collectionTitle) throws AWTException, TimeoutException {
		
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
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#singleUpload\\:submitBottom")));
		saveButton.click();
		
		return PageFactory.initElements(driver, DetailedItemViewPage.class);
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
		
		//setting publication link
		wait.until(ExpectedConditions.visibilityOf(metaDataPublicationTextField));
		metaDataPublicationTextField.sendKeys(defaultMetaDataProfile.getPublicationLink());
		
		//setting date
		wait.until(ExpectedConditions.visibilityOf(metaDataDateTextField));
		metaDataDateTextField.sendKeys(defaultMetaDataProfile.getDate());
		
		//setting geolocation
		wait.until(ExpectedConditions.visibilityOf(metaDataGeolocNameTextField));
		metaDataGeolocNameTextField.sendKeys(defaultMetaDataProfile.getGeolocName());
		metaDataGeolocLatitudeField.sendKeys(defaultMetaDataProfile.getLatitude());
		metaDataGeolocLongitudeField.sendKeys(defaultMetaDataProfile.getLongitude());
	}
	
}
