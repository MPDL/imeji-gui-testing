package spot.components.configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.ConfigurationPage;

public class ConfigurationAppearancePanel extends BasePage {

	@FindBy(css = ".imj_adminPanel:nth-of-type(2)")
	private WebElement appearancePanelReveal;
	
	@FindBy(xpath = "//input[@value='THUMBNAIL']")
	private WebElement defaultViewThumbnailRadioButton;
	
	@FindBy(xpath = "//input[@value='LIST']")
	private WebElement defaultViewListRadioButton;
	
	@FindBy(css = ".imj_adminPanel:nth-of-type(2) .imj_submitButton")
	private WebElement saveAppearanceButton;
	
	public ConfigurationAppearancePanel(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	private void openAppearancePanel() {
		appearancePanelReveal.click();
	}
	
	public ConfigurationPage browseDefaultViewThumbnails() {
		openAppearancePanel();
		
		if (!defaultViewThumbnailRadioButton.isSelected())
			defaultViewThumbnailRadioButton.click();
		
		return saveAllChanges();
	}
	
	public ConfigurationPage browseDefaultViewList() {
		openAppearancePanel();
		
		if (!defaultViewThumbnailRadioButton.isSelected())
			defaultViewThumbnailRadioButton.click();
		
		return saveAllChanges();
	}
	
	private ConfigurationPage saveAllChanges() {
		saveAppearanceButton.click();
		
		return PageFactory.initElements(driver,  ConfigurationPage.class);
	}
}
