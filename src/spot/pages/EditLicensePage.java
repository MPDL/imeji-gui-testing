package spot.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class EditLicensePage extends BasePage {

	@FindBy(xpath = "//select[contains(@id, 'licenseEditorForm')]")
	private WebElement licenseDropdown;
	
	@FindBy(className = "imj_submitButton")
	private WebElement submit;
	
	public EditLicensePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public CollectionEntryPage setLicense(String license) {
		Select licenseSelect = new Select(licenseDropdown);
		licenseSelect.selectByValue(license);
		
		retryingFindClick(By.className("imj_submitButton"));
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
}
