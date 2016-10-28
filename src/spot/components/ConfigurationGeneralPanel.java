package spot.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.ConfigurationEditPage;

public class ConfigurationGeneralPanel extends BasePage {
	
	@FindBy(xpath = "/html/body/div[1]/div[5]/div[2]/div/div/form/div[1]/div[1]")
	private WebElement generalPanelReveal;
	
	@FindBy(css = "#configForm table td input")
	private WebElement privateModeNoRadioButton;
	
	@FindBy(css = "#configForm table td:nth-of-type(2) input")
	private WebElement privateModeYesRadioButton;
	
	@FindBy(css = ".imj_admindataSet:nth-of-type(10) table td input")
	private WebElement albumsNoRadioButton;
	
	@FindBy(css = ".imj_admindataSet:nth-of-type(10) table td:nth-of-type(2) input")
	private WebElement albumsYesRadioButton;
	
	@FindBy(css = "textarea:nth-of-type(2)")
	private WebElement maintenanceMessageBox;
	
	@FindBy(css = "textarea")
	private WebElement termsOfUseBox;
	
	@FindBy(css = ".imj_content .imj_submitButton")
	private WebElement saveGeneralButton;
	
	public ConfigurationGeneralPanel(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	private void openGeneralPanel() {
		generalPanelReveal.click();
	}
	
	public ConfigurationEditPage enablePrivateMode() {
		openGeneralPanel();
		
		if (!privateModeYesRadioButton.isSelected())
			privateModeYesRadioButton.click();
		
		return saveAllChanges();
	}
	
	public ConfigurationEditPage disablePrivateMode() {
		openGeneralPanel();
		
		if (!privateModeNoRadioButton.isSelected())
			privateModeNoRadioButton.click();
		
		return saveAllChanges();
	}
	
	public ConfigurationEditPage enableAlbums() {
		openGeneralPanel();
		
		if (!albumsYesRadioButton.isSelected())
			albumsYesRadioButton.click();
		
		return saveAllChanges();
	}
	
	public ConfigurationEditPage disableAlbums() {
		openGeneralPanel();
		
		if (!albumsNoRadioButton.isSelected())
			albumsNoRadioButton.click();
		
		return saveAllChanges();
	}
	
	public ConfigurationEditPage setMaintenanceMessage(String message) {
		openGeneralPanel();
		
		maintenanceMessageBox.clear();
		maintenanceMessageBox.sendKeys(message);
		return saveAllChanges();
	}
	
	public ConfigurationEditPage setTermsOfUse(String termsOfUse) {
		openGeneralPanel();
		
		termsOfUseBox.clear();
		termsOfUseBox.sendKeys(termsOfUse);
		return saveAllChanges();
	}
	
	private ConfigurationEditPage saveAllChanges() {
		saveGeneralButton.click();
		
		return PageFactory.initElements(driver, ConfigurationEditPage.class);
	}
}
