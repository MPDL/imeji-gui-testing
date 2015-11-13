package spot.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CreateMetaDataPage extends BasePage {

	@FindBy (xpath=".//*[@id='mdProfileType']")
	private List<WebElement> list;
	
	public CreateMetaDataPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public void createMetaDataProfile() {
		
	}

}
