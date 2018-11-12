package spot.pages.registered;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import spot.pages.BasePage;
import spot.pages.ItemViewPage;

public class EditItemPage extends BasePage {

	@FindBy(css = ".imj_metadataValue select")
	private WebElement licenseDropdown;
	
	@FindBy(linkText = "Enter your own license")
	private WebElement licenseEditor;
	
	@FindBy(css = "#editor\\:editItem\\:licenseEditor>.imj_metadataValue>.imj_metadataSet>input")
	private WebElement licenseName;
	
	@FindBy(css = "#editor\\:editItem\\:licenseEditor>.imj_metadataValue>.imj_metadataSet:nth-of-type(2)>input")
	private WebElement licenseLink;
	
	@FindBy(css = ".selectMdButton")
	private WebElement addMetadata;
	
	@FindBy(css = ".imj_submitPanel>a:nth-of-type(2)")
	private WebElement saveButton;
	
	@FindBy(className = "imj_metadataSet")
	private List<WebElement> metadataFields;
	
	public EditItemPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	// IMJ-79
	public ItemViewPage selectLicense(String value) {
		Select licenseSelect = new Select(licenseDropdown);
		licenseSelect.selectByValue(value);
		
		wait.until(ExpectedConditions.visibilityOf(saveButton));
		saveButton.click();
		try { Thread.sleep(2000); } catch (InterruptedException e) {}
		
		return PageFactory.initElements(driver, ItemViewPage.class);
	}
	
	public ItemViewPage deleteFirstMetadata(String metaDataLabelName) {
		this.deleteMetadata(metaDataLabelName);
		
		WebElement itemMetaData = driver.findElement(By.id("itemMetadata"));
		saveButton.click();
		wait.until(ExpectedConditions.stalenessOf(itemMetaData));
		
		return PageFactory.initElements(driver, ItemViewPage.class);
	}
	
	public ItemViewPage deleteAllMetadata(String metaDataLabelName) {		
		int numberOfMetadata = this.numberOfMetadata(metaDataLabelName);
		for(int i=0; i<numberOfMetadata;  i++) {
			this.deleteMetadata(metaDataLabelName);
		}
		
		WebElement itemMetaData = driver.findElement(By.id("itemMetadata"));
		saveButton.click();
		wait.until(ExpectedConditions.stalenessOf(itemMetaData));
		
		return PageFactory.initElements(driver, ItemViewPage.class);
	}
	
	private void deleteMetadata(String metaDataLabelName) {
		List<WebElement> metaDataLabels = driver.findElements(By.xpath("//div[@class='imj_metadataLabel' and contains(text(),'" + metaDataLabelName + "')]"));
		for (WebElement metaDataLabel : metaDataLabels) {
			String currentName = metaDataLabel.getText();
			if (currentName.equals(metaDataLabelName)) {
				WebElement minusButton = metaDataLabel.findElement(By.xpath("./following-sibling::div/a[contains(@class,'fa-minus-square-o')]"));
				// The Selenium click method 'minusButton.click();' does not always work. Using the JavascriptExecutor.executeScript to click the minusButton instead:
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", minusButton);
				wait.until(ExpectedConditions.stalenessOf(metaDataLabel));
				break;
			}
		}
		PageFactory.initElements(driver, this);		
	}
	
	private int numberOfMetadata(String metaDataLabelName) {
		int numberOfMetadata = 0;
		List<WebElement> metaDataLabels = driver.findElements(By.xpath("//div[@class='imj_metadataLabel' and contains(text(),'" + metaDataLabelName + "')]"));
		for (WebElement metaDataLabel : metaDataLabels) {
			String currentName = metaDataLabel.getText();
			if (currentName.equals(metaDataLabelName)) {
				numberOfMetadata++;
			}
		}
		return numberOfMetadata;
	}
	
	// IMJ-80
	public ItemViewPage enterOwnLicense(String name, String link) {
		licenseEditor.click();
		wait.until(ExpectedConditions.visibilityOf(licenseName));
		licenseName.sendKeys(name);
		licenseLink.sendKeys(link);
		
		// FIXME: Replace the Thread.sleep with an explicit wait
		// Problem: Clicking on another field after changing licenseName and licenseLink results in a short loading screen (loaderWrapper)
		// But nothing is loaded, therefore an explicit wait is not possible for now, because there is nothing (race condition safe) to wait for!
		try { Thread.sleep(2000); } catch (InterruptedException e) {}
		
		saveButton.click();
		
		return PageFactory.initElements(driver, ItemViewPage.class);
	}
	
	public ItemViewPage addMetadata(String name, String value) {
		addMetadata.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("selectStatementDialog")));
		List<WebElement> metadataList = driver.findElement(By.id("selectStatementDialog")).findElements(By.tagName("a"));
		for (WebElement metadata : metadataList) {
			if (metadata.getText().equals(name)) {
				metadata.click();
				break;
			}
		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
		
		metadataFields = driver.findElements(By.cssSelector("#editor\\:editItem>.imj_metadataSet"));
		for (WebElement field : metadataFields) {
			List<WebElement> labels = field.findElements(By.className("imj_metadataLabel"));
			if (labels.size() > 0) {
				String currentName = labels.get(0).getText();
				if (currentName.equals(name)) {
					WebElement valueBox = field.findElement(By.cssSelector(".imj_metadataValue>.imj_mdInput"));
					if (valueBox.getAttribute("value").equals("")) {
						valueBox.sendKeys(value);
						saveButton.click();
						try { Thread.sleep(2500); } catch (InterruptedException e) { }
						return PageFactory.initElements(driver, ItemViewPage.class);
					}
				}
			}
		}
		throw new NoSuchElementException("Metadata with this name was not found.");
	}
}
