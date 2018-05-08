package spot.components.configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import spot.pages.BasePage;
import spot.pages.admin.ConfigurationPage;

public class ConfigurationGeneralPanel extends BasePage {
	
	//@FindBy(xpath = "/html/body/div[1]/div[5]/div[2]/div/div/form/div[1]/div[1]")
	@FindBy(css = ".imj_adminPanel:nth-of-type(1)")
	private WebElement generalPanelReveal;
	
	@FindBy(css = "#configForm table td input")
	private WebElement privateModeNoRadioButton;
	
	@FindBy(css = "#configForm table td:nth-of-type(2) input")
	private WebElement privateModeYesRadioButton;
	
	@FindBy(css = ".imj_admindataSet:nth-of-type(10) table td input")
	private WebElement albumsNoRadioButton;
	
	@FindBy(css = ".imj_admindataSet:nth-of-type(10) table td:nth-of-type(2) input")
	private WebElement albumsYesRadioButton;
	
	@FindBy(css = ".imj_adminPanel .imj_admindataSet:nth-of-type(11) textarea")
	private WebElement maintenanceMessageBox;
	
	@FindBy(css = ".imj_adminPanel .imj_admindataSet:nth-of-type(8) textarea")
	private WebElement termsOfUseBox;
	
	@FindBy(tagName = "select")
	private WebElement licenseDropdown;
	
	@FindBy(css = ".imj_content .imj_submitButton")
	private WebElement saveGeneralButton;
	
	public ConfigurationGeneralPanel(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	private void openGeneralPanel() {
		generalPanelReveal.click();
	}
	
	public ConfigurationPage enablePrivateMode() {
		openGeneralPanel();
		
		if (!privateModeYesRadioButton.isSelected())
			privateModeYesRadioButton.click();
		
		return saveAllChanges();
	}
	
	public ConfigurationPage disablePrivateMode() {
		openGeneralPanel();
		
		if (!privateModeNoRadioButton.isSelected())
			privateModeNoRadioButton.click();
		
		return saveAllChanges();
	}
	
	public ConfigurationPage enableAlbums() {
		openGeneralPanel();
		
		if (!albumsYesRadioButton.isSelected())
			albumsYesRadioButton.click();
		
		return saveAllChanges();
	}
	
	public ConfigurationPage disableAlbums() {
		openGeneralPanel();
		
		if (!albumsNoRadioButton.isSelected())
			albumsNoRadioButton.click();
		
		return saveAllChanges();
	}
	
	public ConfigurationPage setMaintenanceMessage(String message) {
		openGeneralPanel();
		
		maintenanceMessageBox.clear();
		maintenanceMessageBox.sendKeys(message);
		return saveAllChanges();
	}
	
	public ConfigurationPage setTermsOfUse(String termsOfUse) {
		openGeneralPanel();
		
		termsOfUseBox.clear();
		termsOfUseBox.sendKeys(termsOfUse);
		return saveAllChanges();
	}
	
	public ConfigurationPage setLicense(String license) {
		openGeneralPanel();
		Select licenseSelect = new Select(licenseDropdown);
		licenseSelect.selectByValue(license);
		
		return saveAllChanges();
	}
	
	private ConfigurationPage saveAllChanges() {
		saveGeneralButton.click();
		
		return PageFactory.initElements(driver, ConfigurationPage.class);
	}
}
