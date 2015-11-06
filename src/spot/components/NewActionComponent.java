package spot.components;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NewActionComponent {

	private WebDriver driver;
	
	@FindBy(xpath="html/body/div[1]/div[2]/div/div[1]")
	private WebElement newButton;
	
	@FindBy(xpath="/html/body/div[1]/div[2]/div/div[2]/ul/li[1]/a")
	private WebElement newCollectionDropBoxEntry;
	
	public NewActionComponent(WebDriver driver) {
		this.driver = driver;

		PageFactory.initElements(driver, this);
	}
	
	public void clickCreateNewCollection() {
		newButton.click();
		newCollectionDropBoxEntry.click();
	}
	
	public WebElement getNewButton() {
		return newButton;
	}
}
