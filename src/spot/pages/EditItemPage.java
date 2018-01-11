package spot.pages;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

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
	
	public ItemViewPage selectLicense(String value) {
		Select licenseSelect = new Select(licenseDropdown);
		licenseSelect.selectByValue(value);
		
		wait.until(ExpectedConditions.visibilityOf(saveButton));
		saveButton.click();
		return PageFactory.initElements(driver, ItemViewPage.class);
	}
	
	public ItemViewPage deleteFirstMetadata(String name) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
		
		Iterator<WebElement> fieldIterator = metadataFields.iterator();
		while (fieldIterator.hasNext()) {
			WebElement field = fieldIterator.next();
			List<WebElement> labels = field.findElements(By.className("imj_metadataLabel"));
			if (labels.size() > 0) {
				String currentName = labels.get(0).getText();
				if (currentName.equals(name)) {
					field.findElement(By.className("fa-minus-square-o")).click();
					break;
				}
			}
		}

		PageFactory.initElements(driver, this);
		saveButton.click();
		return PageFactory.initElements(driver, ItemViewPage.class);
	}
	
	public ItemViewPage enterOwnLicense(String name, String link) {
		licenseEditor.click();
		wait.until(ExpectedConditions.visibilityOf(licenseName));
		licenseName.sendKeys(name);
		licenseLink.sendKeys(link);
		wait.until(ExpectedConditions.visibilityOf(saveButton));
		saveButton.click();
		try { Thread.sleep(2500); } catch (InterruptedException e) { }
		
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
						return PageFactory.initElements(driver, ItemViewPage.class);
					}
				}
			}
		}
		throw new NoSuchElementException("Metadata with this name was not found.");
	}
}
