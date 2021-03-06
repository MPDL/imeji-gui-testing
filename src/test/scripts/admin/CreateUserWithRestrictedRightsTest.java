package test.scripts.admin;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.admin.BrowseUsersPage;
import spot.pages.admin.UserProfilePage;
import test.base.BaseSelenium;

public class CreateUserWithRestrictedRightsTest extends BaseSelenium {
	
	private AdminHomepage adminHomePage;
	private AdministrationPage adminPage;
	
	private String newUserName;
	private BrowseUsersPage allUsersOverViewPage;
	
	@BeforeClass
	public void beforeClass() {		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		
		adminPage = adminHomePage.goToAdminPage();
	
		newUserName = "edmond-test@mpdl.mpg.de";
	}

	@Test(priority = 1)
	public void createUserTest() {
		UserProfilePage userProfilePage = adminPage.createNewRestrictedUser(newUserName);
		allUsersOverViewPage = userProfilePage.goToAdminPage().browseAllUsers();
		boolean isUserPresent = allUsersOverViewPage.isEmailInUserList(newUserName);
		Assert.assertTrue(isUserPresent, "Email of new restricted user is not in user list.");
	}
	
	@Test(priority = 2)
	public void deleteRestrictedUser() {
		adminHomePage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomePage);
		allUsersOverViewPage = adminHomePage.goToAdminPage().browseAllUsers();
		allUsersOverViewPage.deleteUser(newUserName);
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomePage);
		adminHomePage.logout();
	}

}
