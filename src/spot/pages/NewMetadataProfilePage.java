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
	
	private void createBlockAllowedValues(String type, String label, int metadataIndex, String[] predefinedValues) {
		createBlock(type, label, metadataIndex);
		fillInAllowedValues(predefinedValues, metadataIndex);
	}
	
	private void createBlockVocabulary(String type, String label, int metadataIndex, String vocabulary) {
		createBlock(type, label, metadataIndex);
		chooseVocabulary(vocabulary, metadataIndex);
	}
	
	private void createBlock(String type, String label, int metadataIndex) {
		WebElement profile;
		
		if (label.contains("child")) {
			profile = createChildBlock(type, label, metadataIndex);
		}
		else {
			profile = createUsualBlock(type, label, metadataIndex);
		}
		
		selectBlockType(profile, type);
		fillInLabel(profile, label, metadataIndex);
	}
	
	private WebElement createUsualBlock(String type, String label, int metadataIndex) {
		driver.findElement(By.id("profileForm:profile:" + (metadataIndex - 1) + ":metadata")).findElement(By.className("imj_itemHeader")).findElement(By.className("fa-plus-square-o")).click();
		// text is always the default type
		WebElement profile = driver.findElement(By.id("profileForm:profile:" + metadataIndex + ":metadata"));
		return profile;
	}
	
	private WebElement createChildBlock(String type, String label, int metadataIndex) {
		WebElement parentProfile = driver.findElement(By.id("profileForm:profile:" + (metadataIndex - 1) + ":metadata"));
		parentProfile.findElement(By.id("profileForm:profile:" + (metadataIndex - 1) + ":addChildMetadata")).click();
		
		WebElement childProfile = driver.findElement(By.id("profileForm:profile:" + metadataIndex + ":metadata"));
		return childProfile;
	}
	
	private void selectBlockType(WebElement profile, String type) {
		WebElement metadataDropdown = profile.findElement(By.id("mdProfileType"));
		metadataDropdown.click();
		Select metadata = new Select(metadataDropdown);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return metadata.getOptions().size() == 7;
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
		WebElement vocabularyConstraintsArea = driver.findElement(By.id("profileForm:profile:" + metadataIndex + ":vocabularyAndConstraintsArea"));
		WebElement addAllowedValues = vocabularyConstraintsArea.findElement(By.cssSelector(".imj_metadataSet:nth-of-type(2)>.imj_metadataValue>a"));
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].click();", addAllowedValues);
		String profileNr = "profileForm:profile:" + metadataIndex + ":";
		int valuesCount = predefinedValues.length;
		for (int currentValue = 0; currentValue < valuesCount; currentValue++) {
			WebElement valueBox = driver.findElement(By.xpath("//input[contains(@name, '" + profileNr + "constraints:" + currentValue + "')]"));
			valueBox.sendKeys(predefinedValues[currentValue]);
			if (currentValue != valuesCount - 1) {
				WebElement addValue = driver.findElement(By.cssSelector("#profileForm\\:profile\\:" + metadataIndex + "\\:vocabularyAndConstraintsArea .imj_metadataValue .imj_metadataSet:nth-of-type(" + (currentValue + 1) + ") .imj_inlineButtonGroup a"));
				jse.executeScript("arguments[0].click();", addValue);
			}
		}
	}
	
	private void chooseVocabulary(String vocabulary, int metadataIndex) {
		WebElement vocabularyConstraintsArea = driver.findElement(By.id("profileForm:profile:" + metadataIndex + ":vocabularyAndConstraintsArea"));
		WebElement addVocabulary = vocabularyConstraintsArea.findElement(By.cssSelector(".imj_metadataSet>.imj_metadataValue a"));
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].click();", addVocabulary);
		String profileNr = "profileForm:profile:" + metadataIndex + ":";
		WebElement dropdown = driver.findElement(By.id(profileNr + "selectVocabulary"));
		Select select = new Select(dropdown);
		select.selectByVisibleText(vocabulary);
	}

}
