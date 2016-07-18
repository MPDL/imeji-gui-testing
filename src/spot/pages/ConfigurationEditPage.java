package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import spot.components.ConfigurationAppearancePanel;
import spot.components.ConfigurationGeneralPanel;

public class ConfigurationEditPage extends BasePage {

	private ConfigurationGeneralPanel configurationGeneralPanel;
	
	private ConfigurationAppearancePanel configurationAppearancePanel;
	
	public ConfigurationEditPage(WebDriver driver) {
		super(driver);
		configurationGeneralPanel = new ConfigurationGeneralPanel(driver);
		configurationAppearancePanel = new ConfigurationAppearancePanel(driver);
		PageFactory.initElements(driver, this);
	}
	
	public ConfigurationEditPage enablePrivateMode() {
		return configurationGeneralPanel.enablePrivateMode();
	}
	
	public ConfigurationEditPage disablePrivateMode() {
		return configurationGeneralPanel.disablePrivateMode();
	}
	
	public ConfigurationEditPage enableAlbums() {
		return configurationGeneralPanel.enableAlbums();
	}
	
	public ConfigurationEditPage disableAlbums() {
		return configurationGeneralPanel.disableAlbums();
	}
	
	public ConfigurationEditPage browseDefaultViewThumbnails() {
		return configurationAppearancePanel.browseDefaultViewThumbnails();
	}
	
}
