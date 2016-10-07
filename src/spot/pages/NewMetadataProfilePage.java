package spot.pages;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
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
	
	public MetadataOverviewPage editProfile(Map<String, String> profileTypeMap, Map<String, String[]> predefinedValues, Map<String, String> vocabulary) {
		editFirstBlock();
		createMetaDataBlocks(profileTypeMap, predefinedValues, vocabulary);
		
		saveButton.click();
		return PageFactory.initElements(driver, MetadataOverviewPage.class);
	}
	
	public MetadataOverviewPage editProfile(Map<String, String> profileTypeMap) {
		editFirstBlock();
		createMetaDataBlocks(profileTypeMap);
		
		saveButton.click();
		return PageFactory.initElements(driver, MetadataOverviewPage.class);
	}
	
	private void editFirstBlock() {
		// first metadata block type is "Text" by default
		WebElement firstMetadataText = firstMetadataField.findElement(By.id("profileForm:profile:0:labels:0:inputLabel"));
		firstMetadataText.sendKeys("A text field");
		// fill in predefined values
		String[] values = {"yes", "no"};
		fillInAllowedValues(firstMetadataText, values, 0);
	}

	private void createMetaDataBlocks(Map<String, String> profileTypeMap, Map<String, String[]> predefinedValues, Map<String, String> vocabulary) {
		int index = 1;
		
		for (Map.Entry<String, String> entry : profileTypeMap.entrySet()) {
			String blockType = entry.getKey();
			String label = entry.getValue();
			boolean allowedValues = predefinedValues.containsKey(blockType);
			if (allowedValues)
				createBlockAllowedValues(blockType, label, index, predefinedValues.get(blockType));
			else
				createBlockVocabulary(blockType, label, index, vocabulary.get(blockType));
			index++;
		}
	}
	
	private void createMetaDataBlocks(Map<String, String> profileTypeMap) {
		int index = 1;
		
		for (Map.Entry<String, String> entry : profileTypeMap.entrySet()) {
			String blockType = entry.getKey();
			String label = entry.getValue();
			WebElement profile = driver.findElement(By.id("profileForm:profile:" + index + ":metadata"));
			fillInLabel(profile, label, index);
		}
	}
	
	private void createBlockAllowedValues(String type, String label, int metadataIndex, String[] predefinedValues) {
		addNewMetadataBlock.click();
		// text is always the default type
		WebElement profile = driver.findElement(By.id("profileForm:profile:" + metadataIndex + ":metadata"));
		
		selectBlockType(profile, type);
		fillInLabel(profile, label, metadataIndex);
		fillInAllowedValues(profile, predefinedValues, metadataIndex);
	}
	
	private void createBlockVocabulary(String type, String label, int metadataIndex, String vocabulary) {
		addNewMetadataBlock.click();
		// text is always the default type
		WebElement profile = driver.findElement(By.id("profileForm:profile:" + metadataIndex + ":metadata"));
		
		selectBlockType(profile, type);
		fillInLabel(profile, label, metadataIndex);
		chooseVocabulary(profile, vocabulary, metadataIndex);
	}
	
	// TODO
	public void addChildBlock(WebElement profile) {
		profile.findElement(By.className("imj_submitButton")).click();
	}
	
	private void selectBlockType(WebElement profile, String type) {
		WebElement metadataDropdown = profile.findElement(By.id("mdProfileType"));
		Select metadata = new Select(metadataDropdown);
		metadata.selectByVisibleText(type);
	}
	
	private void fillInLabel(WebElement profile, String label, int metadataIndex) {
		String labelBoxID = "profileForm:profile:" + metadataIndex + ":labels:0:inputLabel";
		WebElement labelBox = profile.findElement(By.id(labelBoxID));
		labelBox.sendKeys(label);
	}
	
	/**
	 * @param metadataIndex: position of current metadata block, starting from 0
	 */
	private void fillInAllowedValues(WebElement profile, String[] predefinedValues, int metadataIndex) {
		//String addValuesID = "profileForm:profile:" + metadataIndex + ":j_idt369";
		String addValuesID = ".imj_metadataSetGroup .fa-plus-square-o";
		profile.findElement(By.cssSelector(addValuesID)).click();
		int valuesCount = predefinedValues.length;
		for (int currentValue = 0; currentValue < valuesCount; currentValue++) {
			String valueID = "profileForm:profile:" + metadataIndex + ":constraints:" + currentValue + ":j_idt373";
			profile.findElement(By.id(valueID)).sendKeys(predefinedValues[currentValue]);
			String newValueID = "profileForm:profile:" + metadataIndex + ":constraints:" + currentValue + ":j_idt375";
			if (currentValue != valuesCount - 1)
				profile.findElement(By.id(newValueID)).click();
		}
	}
	
	private void chooseVocabulary(WebElement profile, String vocabulary, int metadataIndex) {
		String vocabularyID = "profileForm:profile:" + metadataIndex + ":j_idt358";
		profile.findElement(By.id(vocabularyID)).click();
		WebElement dropdown = profile.findElement(By.id("profileForm:profile:" + metadataIndex + ":selectVocabulary"));
		Select select = new Select(dropdown);
		select.selectByVisibleText(vocabulary);
	}

}
