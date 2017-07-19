package spot.pages;

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
	
	@FindBy(css = ".imj_submitPanel>a:nth-of-type(2)")
	private WebElement saveButton;
	
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
	
	public ItemViewPage enterOwnLicense(String name, String link) {
		licenseEditor.click();
		wait.until(ExpectedConditions.visibilityOf(licenseName));
		licenseName.sendKeys(name);
		licenseLink.sendKeys(link);
		wait.until(ExpectedConditions.visibilityOf(saveButton));
		saveButton.click();
		
		return PageFactory.initElements(driver, ItemViewPage.class);
	}
}
