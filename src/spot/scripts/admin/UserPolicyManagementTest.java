package spot.scripts.admin;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.admin.UserManagementOverviewPage;
import spot.pages.admin.ViewEditUsersPage;

public class UserPolicyManagementTest extends BaseSelenium {

	private AdminHomePage adminHomePage;

	/**
	 * To carry out admin rights requires login as admin.
	 */
	@BeforeClass
	public void loginAsAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"), getPropertyAttribute("aSpotPassword"));
	}
	
	@Test
	public void deleteTestUserTest() {
		UserManagementOverviewPage userPolicyManagementOverviewPage = adminHomePage.goToUserPolicyManagementOverviewPage();
		ViewEditUsersPage viewEditUsersPage = userPolicyManagementOverviewPage.goToViewEditUsersButton();
		
		String toBeDeletedUsersEmailAddress = getPropertyAttribute("tuEmailAddress"); 
		
		viewEditUsersPage.deleteUser(toBeDeletedUsersEmailAddress);
		Assert.assertFalse(viewEditUsersPage.isUserPresent(toBeDeletedUsersEmailAddress), "User deletion failed: user still registered");
		
	}
	
	@AfterClass
	public void logout() {
		logout(PageFactory.initElements(driver, AdminHomePage.class));	
	}
}
