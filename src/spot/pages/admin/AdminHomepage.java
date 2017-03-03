package spot.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.registered.Homepage;

/**
 * HomePage for logged-in admins.
 * 
 * @author kocar
 *
 */
public class AdminHomepage extends Homepage {

	@FindBy (xpath =".//*[@id='Header:j_idt59:lnkAdmin']")
	private WebElement goToAdminRightsOverviewButton;
	
	public AdminHomepage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public UserManagementOverviewPage goToUserPolicyManagementOverviewPage() {
		goToAdminRightsOverviewButton.click();
		return PageFactory.initElements(driver, UserManagementOverviewPage.class);
	}

}
