package spot.components.configuration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;
import spot.pages.admin.ConfigurationPage;

public class ConfigurationAutosuggestionPanel extends BasePage {
	
	@FindBy(xpath = "//div[@class='imj_adminPanel']/descendant::h2[contains(text(),'Automatic suggestion')]")
	private WebElement autosuggestionPanelReveal;
	
	@FindBy(xpath = "//h2[contains(text(),'Automatic suggestion')]/../following-sibling::div[@class='imj_content']")
	private WebElement autosuggestionPanelContent;
	
	@FindBy(xpath = "//h2[contains(text(),'Automatic suggestion')]/../following-sibling::div[@class='imj_content']//input[@value='Save all']")
	private WebElement saveGeneralButton;
	
	public ConfigurationAutosuggestionPanel(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public ConfigurationPage setAutosuggestionMaxPlanck() {
		openAutosuggestionPanel();
		
		// Div-Elements can not be selected by their inner text() with xpath
		// => Select 'Enable suggestion for Users' element by its position:
		WebElement enableSuggestionForUsersDataSet = autosuggestionPanelContent.findElement(By.xpath(".//div[3]"));
		WebElement radioButtonMPAuthors = enableSuggestionForUsersDataSet.findElement(By.xpath(".//label[contains(text(),'Max Planck authors')]/preceding-sibling::input"));
		
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
