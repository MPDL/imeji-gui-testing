package spot.components.configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.admin.ConfigurationPage;

public class ConfigurationAutosuggestionPanel extends BasePage {
	
	@FindBy(css = ".imj_adminPanel:nth-of-type(11) h2")
	private WebElement autosuggestionPanelReveal;
	
	@FindBy(css = ".imj_adminPanel:nth-of-type(11) .imj_admindataSet:nth-of-type(3) td:nth-of-type(3) input")
	private WebElement radioButtonMPAuthors;
	
	@FindBy(css = ".imj_adminPanel:nth-of-type(11) .imj_submitButton")
	private WebElement saveGeneralButton;
	
	public ConfigurationAutosuggestionPanel(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public ConfigurationPage setAutosuggestionMaxPlanck() {
		openAutosuggestionPanel();
		
		if (!radioButtonMPAuthors.isSelected())
			radioButtonMPAuthors.click();
		
		return saveAllChanges();
	}
	
	private void openAutosuggestionPanel() {
		autosuggestionPanelReveal.click();
	}
	
	private ConfigurationPage saveAllChanges() {
		saveGeneralButton.click();
		
		return PageFactory.initElements(driver, ConfigurationPage.class);
	}
}
