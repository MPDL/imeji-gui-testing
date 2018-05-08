package spot.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import spot.components.configuration.ConfigurationAppearancePanel;
import spot.components.configuration.ConfigurationAutosuggestionPanel;
import spot.components.configuration.ConfigurationGeneralPanel;
import spot.components.configuration.ConfigurationRegistrationPanel;
import spot.pages.BasePage;

public class ConfigurationPage extends BasePage {

	private ConfigurationGeneralPanel generalPanel;
	private ConfigurationAppearancePanel appearancePanel;
	private ConfigurationRegistrationPanel registrationPanel;
	private ConfigurationAutosuggestionPanel autosuggestionPanel;
	
	public ConfigurationPage(WebDriver driver) {
		super(driver);
		generalPanel = new ConfigurationGeneralPanel(driver);
		appearancePanel = new ConfigurationAppearancePanel(driver);
		registrationPanel = new ConfigurationRegistrationPanel(driver);
		autosuggestionPanel = new ConfigurationAutosuggestionPanel(driver);
		PageFactory.initElements(driver, this);
	}
	
	public ConfigurationPage enablePrivateMode() {
		return generalPanel.enablePrivateMode();
	}
	
	public ConfigurationPage disablePrivateMode() {
		return generalPanel.disablePrivateMode();
	}
	
	public ConfigurationPage enableRegistration() {
		return registrationPanel.enableRegistration();
	}
	
	public ConfigurationPage disableRegistration() {
		return registrationPanel.disableRegistration();
	}
	
	public ConfigurationPage restrictRegistrationDomains(String domains) {
		return registrationPanel.restrictRegistrationDomains(domains);
	}
	
	public ConfigurationPage setMaintenanceMessage(String message) {
		return generalPanel.setMaintenanceMessage(message);
	}
	
	public ConfigurationPage setTermsOfUse(String termsOfUse) {
		return generalPanel.setTermsOfUse(termsOfUse);
	}
	
	public ConfigurationPage setLicense(String license) {
		return generalPanel.setLicense(license);
	}
	
	public ConfigurationPage enableThumbnailView() {
		return appearancePanel.enableThumbnailView();
	}
	
	public ConfigurationPage enableListView() {
		return appearancePanel.enableListView();
	}
	
	public ConfigurationPage setAutosuggestionMP() {
		return autosuggestionPanel.setAutosuggestionMaxPlanck();
	}
	
}
