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
	
	@FindBy(className = "imj_backPanel")
	private WebElement backLink;
	
	public KindOfSharePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	public SharePage shareWithAUser() {
		shareWithAUserButton.click();
		
		return PageFactory.initElements(driver, SharePage.class);
	}
	
	public SharePage shareWithUserGroup() {
		shareWithAUserGroup.click();
		
		return PageFactory.initElements(driver, SharePage.class);
	}
	
	public CollectionEntryPage goBackToCollection() {
		backLink.click();
		return PageFactory.initElements(driver, CollectionEntryPage.class);
	}

}
