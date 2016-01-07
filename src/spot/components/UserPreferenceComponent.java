package spot.components;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class UserPreferenceComponent {

	private WebDriver driver;
	
	@FindBy(xpath=".//*[@id='Header:langForm']/div/div/div[1]")
	private WebElement currentLanguageComboEntry;
	
	@FindBy(css=".imj_metaMenuItemSelect")
	private WebElement languageSelect;
	
	@FindBy(xpath=".//*[@id='Header:langForm']/div/select")
	private WebElement languageComboBoxWebElement;	
	
	private Select languageComboBox;
	
	@FindBy(xpath="/html/body/div[1]/div[1]/div[1]/span/form[3]/div/div/div[2]")
	private WebElement buttonSortDescending;
	
	public UserPreferenceComponent(WebDriver driver) {
		this.driver = driver;
		
		PageFactory.initElements(driver, this);
	}
	
	public void selectLanguage(String language) {
		languageSelect.click();
		languageComboBox = new Select(languageComboBoxWebElement);
		
		languageComboBox.selectByVisibleText(language);

		try {
			languageComboBoxWebElement.submit();
		} catch (StaleElementReferenceException e) {
			languageComboBoxWebElement = driver.findElement(By.xpath(".//*[@id='Header:langForm']/div/select"));
			languageComboBoxWebElement.submit();
		}
	}

	public String getCurrentLanguage() {
		
		String text = currentLanguageComboEntry.getText();		
		return text;
	}
	
	
}
