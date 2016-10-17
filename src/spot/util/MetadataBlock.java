package spot.util;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * A metadata block for building a metadata profile. Can contain other metadata blocks.
 * @author apetrova
 *
 */
public class MetadataBlock {

	private WebElement profile;
	private WebDriver driver;
	private WebDriverWait wait = new WebDriverWait(driver, 50);
	
	private int metadataIndex;
	private String label;
	private List<MetadataBlock> children;
	
	public MetadataBlock(WebDriver driver, int metadataIndex) {
		this.driver = driver;
		this.metadataIndex = metadataIndex;
		profile = driver.findElement(By.id("profileForm:profile:" + metadataIndex + ":metadata"));
	}
	
	public void selectBlockType(String type) {
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
	
	public void fillInLabel(String label) {
		this.label = label;
		String labelBoxID = "profileForm:profile:" + metadataIndex + ":labels:0:inputLabel";
		WebElement labelBox = profile.findElement(By.id(labelBoxID));
		labelBox.sendKeys(this.label);
	}
	
	/**
	 * @param metadataIndex: position of current metadata block, starting from 0
	 * @param predefinedValues: array of possible values for this metadata block
	 */
	public void fillInAllowedValues(String[] predefinedValues) {
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
	
	public void chooseVocabulary(String vocabulary) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		String profileNr = "profileForm:profile:" + metadataIndex + ":";
		jse.executeScript("document.getElementById('" + profileNr + "j_idt355').click();");
		WebElement dropdown = driver.findElement(By.id("profileForm:profile:" + metadataIndex + ":selectVocabulary"));
		Select select = new Select(dropdown);
		select.selectByVisibleText(vocabulary);
	}
}
