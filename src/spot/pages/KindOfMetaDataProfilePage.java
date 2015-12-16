package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class KindOfMetaDataProfilePage extends BasePage {

	@FindBy(css="input[value='Create a new metadata profile']")
	private WebElement createNewMetaDataProfileButton;
	
	public KindOfMetaDataProfilePage(WebDriver driver) {
		super(driver);
	}
	
	public CreateIndividualMetaDataProfilePage selectNewIndividualMetaDataProfile() {
		createNewMetaDataProfileButton.click();
		
		return PageFactory.initElements(driver, CreateIndividualMetaDataProfilePage.class);
	}

}
