package spot.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditCollectionPage extends BasePage {

	@FindBy(id = "editContainer:mediaContainerForm:additionalInfos")
	private WebElement additionalInfo;
	
	@FindBy(id = "editContainer:mediaContainerForm:list:0:inputInfoLabel")
	private WebElement infoLabelBox;
	
	@FindBy(id = "editContainer:mediaContainerForm:list:0:inputInfoText")
	private WebElement infoTextBox;
	
	@FindBy(id = "editContainer:mediaContainerForm:list:0:inputInfoUrl")
	private WebElement infoUrlBox;
	
	@FindBy(id = "container")
	private WebElement logoContainer;
	
	@FindBy(css=".imj_submitButton")
	private WebElement saveButton;
	
	public EditCollectionPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public void addInformation(String label) {
		additionalInfo.findElement(By.className("fa-plus-square-o")).click();
		
		infoLabelBox.sendKeys(label);
		infoTextBox.sendKeys("This is a test collection.");
		infoUrlBox.sendKeys("http://imeji.org/");
	}
	
	public void addLogo(String filepath) {
		WebElement input = logoContainer.findElement(By.tagName("input"));
		input.sendKeys(filepath);
	}
	
	public CollectionEntryPage submitChanges() {
		saveButton.click();
		
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}
}
