package spot.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.ConfigurationEditPage;

public class ConfigurationAppearancePanel extends BasePage {

	@FindBy(xpath = "/html/body/div[1]/div[5]/div[2]/div/div/form/div[2]/div[1]")
	private WebElement appearancePanelReveal;
	
	@FindBy(id = "configForm:j_idt381:1")
	private WebElement defaultViewThumbnailRadioButton;
	
	@FindBy(name = "configForm:j_idt377")
	private WebElement saveAppearanceButton;
	
	public ConfigurationAppearancePanel(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	private void openAppearancePanel() {
		appearancePanelReveal.click();
	}
	
	public ConfigurationEditPage browseDefaultViewThumbnails() {
		openAppearancePanel();
		
		if (!defaultViewThumbnailRadioButton.isSelected())
			defaultViewThumbnailRadioButton.click();
		
		return saveAllChanges();
	}
	
	private ConfigurationEditPage saveAllChanges() {
		saveAppearanceButton.click();
		
		return PageFactory.initElements(driver,  ConfigurationEditPage.class);
	}
}
