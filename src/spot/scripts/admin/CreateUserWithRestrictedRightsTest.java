package spot.scripts.admin;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.admin.UserProfilePage;
import spot.pages.admin.UsersOverviewPage;

public class CreateUserWithRestrictedRightsTest extends BaseSelenium {
	
	private AdminHomePage adminHomePage;
	private AdministrationPage adminPage;
	
	private String newUserName;
	private UsersOverviewPage allUsersOverViewPage;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();		
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		
		adminPage = adminHomePage.goToAdminPage();
	
		newUserName = "edmond-test@mpdl.mpg.de";
	}

	@Test(priority = 1)
	public void createUserTest() {
		UserProfilePage userProfilePage = adminPage.createNewRestrictedUser(newUserName);
		allUsersOverViewPage = userProfilePage.goToAdminPage().viewAllUsers();
		boolean isUserPresent = allUsersOverViewPage.isEmailInUserList(newUserName);
		Assert.assertTrue(isUserPresent, "Email of new restricted user is not in user list.");
	}
	
	@Test(priority = 2)
	public void deleteRestrictedUser() {
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
		allUsersOverViewPage = adminHomePage.goToAdminPage().viewAllUsers();
		allUsersOverViewPage.deleteUserByEmail(newUserName);
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
		adminHomePage.logout();
	}

}
