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

	@Test
	public void createUserTest() {
		UserProfilePage userProfilePage = adminPage.createNewRestrictedUser(newUserName);
		allUsersOverViewPage = userProfilePage.goToAdminPage().viewAllUsers();
		
		String actualMessage = allUsersOverViewPage.getMessageComponent().getInfoMessage();	
		String expectedMessage = "User created successfully.";
		Assert.assertEquals(actualMessage, expectedMessage, "User most probably couldn't be created");
		allUsersOverViewPage.goToHomePage(adminHomePage).logout();
	}
	
	@AfterClass
	public void afterClass() {
		
		allUsersOverViewPage.deleteUserByEmail(newUserName);
		
		adminHomePage.logout();
	}

}
