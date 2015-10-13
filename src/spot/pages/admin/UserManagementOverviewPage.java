package spot.pages.admin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;

public class UserManagementOverviewPage extends BasePage {

	@FindBy (xpath ="html/body/div[1]/div[4]/div[2]/div/div[1]/div[2]/ul/li[2]/a")
	private WebElement viewAllUsersButton;
	
	public UserManagementOverviewPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public ViewEditUsersPage goToViewEditUsersButton() {
		viewAllUsersButton.click();
		return PageFactory.initElements(driver, ViewEditUsersPage.class);
	}

}
