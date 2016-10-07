package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class KindOfSharePage extends BasePage {

	@FindBy(id="share:userShare")
	private WebElement shareWithAUserButton;
	
	@FindBy(id="share:userGroupShare")
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
