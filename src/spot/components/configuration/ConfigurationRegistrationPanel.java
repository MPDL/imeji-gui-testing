package spot.components.configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.admin.ConfigurationPage;

public class ConfigurationRegistrationPanel extends BasePage {

	@FindBy(css = ".imj_adminPanel:nth-of-type(4) h2")
	private WebElement registrationPanelReveal;
	
	@FindBy(css = ".imj_adminPanel:nth-of-type(4)>.imj_admindataSet:nth-of-type(1)>input:nth-of-type(1)")
	private WebElement radioButtonNo;
	
	@FindBy(css = ".imj_adminPanel:nth-of-type(4)>.imj_admindataSet:nth-of-type(1)>input:nth-of-type(2)")
	private WebElement radioButtonYes;
	
	@FindBy(css = ".imj_adminPanel:nth-of-type(4)>imj_admindataSet:nth-of-type(3)>input")
	private WebElement allowedDomainsBox;

	@FindBy(css = ".imj_adminPanel:nth-of-type(4) .imj_submitButton")
	private WebElement saveRegistrationButton;
	
	public ConfigurationRegistrationPanel(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public ConfigurationPage enableRegistration() {
		return setRegistration(true);
	}
	
	public ConfigurationPage disableRegistration() {
		return setRegistration(false);
	}
	
	public ConfigurationPage restrictRegistrationDomains(String domains) {
		openRegistrationPanel();
		
		allowedDomainsBox.clear();
		allowedDomainsBox.sendKeys(domains);
		
		return saveAllChanges();
	}
	
	private void openRegistrationPanel() {
		registrationPanelReveal.click();
	}
	
	private ConfigurationPage saveAllChanges() {
		saveRegistrationButton.click();
		
		return PageFactory.initElements(driver, ConfigurationPage.class);
	}
	
	private ConfigurationPage setRegistration(boolean userCanRegister) {
		openRegistrationPanel();
		
		if (userCanRegister) {
			if (!radioButtonYes.isSelected())
				radioButtonYes.click();
		}
		
		else {
			if (!radioButtonNo.isSelected())
				radioButtonNo.click();
		}
		
		return saveAllChanges();
	}
}
