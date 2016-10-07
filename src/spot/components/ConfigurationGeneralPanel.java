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
	
	@FindBy(id = "configForm:j_idt356:0")
	private WebElement privateModeNoRadioButton;
	
	@FindBy(id = "configForm:j_idt356:1")
	private WebElement privateModeYesRadioButton;
	
	@FindBy(id = "configForm:j_idt362:0")
	private WebElement albumsNoRadioButton;
	
	@FindBy(id = "configForm:j_idt362:1")
	private WebElement albumsYesRadioButton;
	
	@FindBy(name = "configForm:j_idt320")
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
	
	private ConfigurationEditPage saveAllChanges() {
		saveGeneralButton.click();
		
		return PageFactory.initElements(driver, ConfigurationEditPage.class);
	}
}
