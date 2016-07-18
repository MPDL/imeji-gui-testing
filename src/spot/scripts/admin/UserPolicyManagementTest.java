package spot.scripts.admin;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.AllUsersOverViewPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;

public class UserPolicyManagementTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
	private String emailOfNewUser;

	/**
	 * To carry out admin rights requires login as admin.
	 */
	@BeforeClass
	public void setup() {
		super.setup();
		navigateToStartPage();
		loginAsAdmin();
		createNewUser();
	}
	
	private void loginAsAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"), getPropertyAttribute("aSpotPassword"));
	}
	
	private void createNewUser() {
		emailOfNewUser = getPropertyAttribute("tuSpotUserName");
		
		AdministrationPage administrationPage = adminHomePage.goToAdminPage();
		administrationPage.createNewUser(emailOfNewUser);
		adminHomePage = (AdminHomePage) administrationPage.goToHomePage(adminHomePage);
	}
	
	@Test
	public void deleteTestUserTest() {
		AllUsersOverViewPage allUsersOverViewPage = adminHomePage.goToAdminPage().viewAllUsers();
		allUsersOverViewPage.deleteUserByEmail(emailOfNewUser);
		allUsersOverViewPage = allUsersOverViewPage.goToAdminPage().viewAllUsers();
		
		Assert.assertFalse(allUsersOverViewPage.isEmailInUserList(emailOfNewUser), "User deletion failed: user still registered");
	}
	
	@AfterClass
	public void logout() {
		logout(PageFactory.initElements(driver, AdminHomePage.class));	
	}
}
