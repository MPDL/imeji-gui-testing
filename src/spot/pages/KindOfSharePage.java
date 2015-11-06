package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class KindOfSharePage extends BasePage {

	@FindBy(xpath=".//*[@id='shareButtons']/input")
	private WebElement shareWithAUserButton;
	
	@FindBy(xpath=".//*[@id='shareButtons']/a")
	private WebElement shareWithAUserGroup;
	
	public KindOfSharePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public SharePage shareWithAUser() {
		shareWithAUserButton.click();
		
		return PageFactory.initElements(driver, SharePage.class);
	}

}
