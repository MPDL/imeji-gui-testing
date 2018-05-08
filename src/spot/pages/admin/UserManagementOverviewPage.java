package spot.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;

public class UserManagementOverviewPage extends BasePage {
	
	public UserManagementOverviewPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
}
