package spot.pages;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;

public class NewMetadataProfilePage extends BasePage {

	@FindBy (xpath=".//*[@id='mdProfileType']")
	private List<WebElement> list;
	
	@FindBy (css="#profileForm\\:ajaxArea>div:nth-of-type(2) a[title='Add metadata']")	
	private WebElement addNewMetadataBlock;
	
	@FindBy(xpath=".//*[@id='mdProfileType']")
	private WebElement metadataTypeDropBoxWebElement;
	
	@FindBy(xpath=".//*[@id='profileForm:profile:0:metadata']")
	private WebElement firstMetadataField;
	
	@FindBy(xpath=".//*[@id='profileForm']/div/div[2]/div[2]/input[2]")
	private WebElement saveButton;
	
	public NewMetadataProfilePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public MetadataTransitionPage editProfile(Map<String, String> profileTypeMap, Map<String, String[]> predefinedValues, Map<String, String> vocabulary) {
		editFirstBlock();
		createMetaDataBlocks(profileTypeMap, predefinedValues, vocabulary);
		
		saveButton.click();
		return PageFactory.initElements(driver, MetadataTransitionPage.class);
	}
	
	public MetadataTransitionPage editProfile(Map<String, String> profileTypeMap) {
		editFirstBlock();
		createMetaDataBlocks(profileTypeMap);
		
		saveButton.click();
		return PageFactory.initElements(driver, MetadataTransitionPage.class);
	}
	
	private void editFirstBlock() {
		// first metadata block type is "Text" by default
		WebElement firstMetadataText = firstMetadataField.findElement(By.id("profileForm:profile:0:labels:0:inputLabel"));
		firstMetadataText.sendKeys("A text field");
		// fill in predefined values
		String[] values = {"yes", "no"};
		fillInAllowedValues(values, 0);
	}

	private void createMetaDataBlocks(Map<String, String> profileTypeMap, Map<String, String[]> predefinedValues, Map<String, String> vocabulary) {
		int index = 1;
		
		for (Map.Entry<String, String> entry : profileTypeMap.entrySet()) {
			String blockType = entry.getKey();
			String label = entry.getValue();
			if (predefinedValues.containsKey(blockType))
				createBlockAllowedValues(blockType, label, index, predefinedValues.get(blockType));
			else
				if (vocabulary.containsKey(blockType))
					createBlockVocabulary(blockType, label, index, vocabulary.get(blockType));
				else
					createBlock(blockType, label, index);
			index++;
		}
	}
	
	private void createMetaDataBlocks(Map<String, String> profileTypeMap) {
		int index = 1;
		
		for (Map.Entry<String, String> entry : profileTypeMap.entrySet()) {
			String blockType = entry.getKey();
			String label = entry.getValue();
			createBlock(blockType, label, index);
			index++;
		}
	}
	
	private void createBlock(String type, String label, int metadataIndex) {
		driver.findElement(By.id("profileForm:profile:" + (metadataIndex - 1) + ":j_idt329")).click();
		// text is always the default type
		WebElement profile = driver.findElement(By.id("profileForm:profile:" + metadataIndex + ":metadata"));
		
		selectBlockType(profile, type);
		fillInLabel(profile, label, metadataIndex);
	}
	
	private void createBlockAllowedValues(String type, String label, int metadataIndex, String[] predefinedValues) {
		createBlock(type, label, metadataIndex);
		fillInAllowedValues(predefinedValues, metadataIndex);
	}
	
	private void createBlockVocabulary(String type, String label, int metadataIndex, String vocabulary) {
		createBlock(type, label, metadataIndex);
		chooseVocabulary(vocabulary, metadataIndex);
	}
	
	// TODO
	public void addChildBlock(WebElement profile) {
		profile.findElement(By.className("imj_submitButton")).click();
	}
	
	private void selectBlockType(WebElement profile, String type) {
		WebElement metadataDropdown = profile.findElement(By.id("mdProfileType"));
		metadataDropdown.click();
		Select metadata = new Select(metadataDropdown);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return metadata.getOptions().size() == 8;
			}
		});
		metadata.selectByVisibleText(type);
	}
	
	private void fillInLabel(WebElement profile, String label, int metadataIndex) {
		String labelBoxID = "profileForm:profile:" + metadataIndex + ":labels:0:inputLabel";
		WebElement labelBox = profile.findElement(By.id(labelBoxID));
		labelBox.sendKeys(label);
	}
	
	/**
	 * @param metadataIndex: position of current metadata block, starting from 0
	 * @param predefinedValues: array of possible values for this metadata block
	 */
	private void fillInAllowedValues(String[] predefinedValues, int metadataIndex) {
		// TODO don't rely on generated IDs
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		String profileNr = "profileForm:profile:" + metadataIndex + ":";
		jse.executeScript("document.getElementById('" + profileNr + "j_idt366').click();");
		int valuesCount = predefinedValues.length;
		for (int currentValue = 0; currentValue < valuesCount; currentValue++) {
			WebElement valueBox = driver.findElement(By.name(profileNr + "constraints:" + currentValue + ":j_idt370"));
			valueBox.sendKeys(predefinedValues[currentValue]);
			if (currentValue != valuesCount - 1) {
				jse.executeScript("document.getElementById('" + profileNr + "constraints:" + currentValue + ":j_idt372').click();");
			}
		}
	}
	
	private void chooseVocabulary(String vocabulary, int metadataIndex) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		String profileNr = "profileForm:profile:" + metadataIndex + ":";
		jse.executeScript("document.getElementById('" + profileNr + "j_idt355').click();");
		WebElement dropdown = driver.findElement(By.id("profileForm:profile:" + metadataIndex + ":selectVocabulary"));
		Select select = new Select(dropdown);
		select.selectByVisibleText(vocabulary);
	}

}
