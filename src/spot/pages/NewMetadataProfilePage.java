package spot.pages;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class NewMetadataProfilePage extends BasePage {

	@FindBy (xpath=".//*[@id='mdProfileType']")
	private List<WebElement> list;
	
	@FindBy (css="#profileForm\\:ajaxArea>div:nth-of-type(2) a[title='Add metadata']")	
	private WebElement addNewMetaDataBlock;
		
	private Select metaDataTypeDropBox;
	
	@FindBy(xpath=".//*[@id='mdProfileType']")
	private WebElement metaDataTypeDropBoxWebElement;
	
	@FindBy(xpath=".//*[@id='profileForm:profile:0:labels:0:inputLabel']")
	private WebElement firstLabelTexField;
	
	@FindBy(xpath=".//*[@id='profileForm']/div/div[2]/div[2]/input[2]")
	private WebElement saveButton;
	
	public NewMetadataProfilePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public MetaDataOverViewPage editProfile(Map<String, String> profileTypeMap) {

		metaDataTypeDropBox = new Select(metaDataTypeDropBoxWebElement);
		metaDataTypeDropBoxWebElement.click();
		
		// setting first meta data block
		// profile type "Text" already set by default
		// set label
		firstLabelTexField.sendKeys("This is a text meta data field");
		
		// check if all of the options are available
		List<WebElement> options = metaDataTypeDropBox.getOptions();
//		System.out.println("You have " + options.size() + " options!!!!");		
		
		MetaDataOverViewPage metaDataOverViewPage = createMetaDataBlocks(profileTypeMap);
		
		saveButton.click();
		
		return metaDataOverViewPage;
	}

	private MetaDataOverViewPage createMetaDataBlocks(Map<String, String> profileTypeMap) {

		// be aware: index shall remain 1  
		int index = 1;
		
		for (Map.Entry<String, String> entry : profileTypeMap.entrySet()) {
			
			// add a new meta data block
			addNewMetaDataBlock.click();
			
			// get div id of new meta data block
			String divIDofNewBlock = "div[id='profileForm:profile:"+index+":metadata']";
			
			// get webelement of div
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(divIDofNewBlock)));
			WebElement newMetaDataBlockWebElement = driver.findElement(By.cssSelector(divIDofNewBlock));
			
			String selectID = "mdProfileType";

//			WebElement dropBoxWebElement = newMetaDataBlockWebElement.findElement(By.id(selectID));
			WebElement dropBoxWebElement = driver.findElement(By.cssSelector(divIDofNewBlock+" #" + selectID));
			
			dropBoxWebElement = driver.findElement(By.cssSelector(divIDofNewBlock+" #" + selectID));
			try {
				dropBoxWebElement.click();
			} catch (StaleElementReferenceException e) {
				if (!retryingFindClick(By.cssSelector(divIDofNewBlock+" #" + selectID))) {
					dropBoxWebElement = driver.findElement(By.cssSelector(divIDofNewBlock+" #" + selectID));
					dropBoxWebElement.click();
				}
			}
			
			String profileType = entry.getKey();
		    String profileLabel = entry.getValue();
			
		    dropBoxWebElement = driver.findElement(By.cssSelector(divIDofNewBlock+" #" + selectID));
			dropBoxWebElement.sendKeys(profileType);

			dropBoxWebElement.click();			

			String textFieldIDofWebElement = "profileForm:profile:"+index+":labels:0:inputLabel";
						
			// make driver find the element once again, since page gets refreshed; otherwise StaleElementReferenceException is thrown
			newMetaDataBlockWebElement = driver.findElement(By.cssSelector(divIDofNewBlock));
			WebElement textField = newMetaDataBlockWebElement.findElement(By.id(textFieldIDofWebElement));
			
			textField.sendKeys(profileLabel);

		}
		

		
		return PageFactory.initElements(driver, MetaDataOverViewPage.class);
	}

}
