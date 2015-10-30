package spot.components;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NewComponent {

	private WebDriver driver;
	
	@FindBy(xpath="html/body/div[1]/div[2]/div/div[1]")
	protected WebElement createNewButton;
	
	public NewComponent(WebDriver driver) {
		this.driver = driver;

		PageFactory.initElements(driver, this);
	}
		
	public boolean isCreateNewButtonExistent() {
		
		try {
			createNewButton.isEnabled();
			return true;
		} catch(NoSuchElementException e) {
			return false;
		}		
	}
}
