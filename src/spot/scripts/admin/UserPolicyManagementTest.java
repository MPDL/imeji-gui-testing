package spot.scripts.admin;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
import spot.pages.AdministrationPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.admin.UserProfilePage;
import spot.pages.admin.UsersOverviewPage;

public class UserPolicyManagementTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
	private String emailOfNewUser;

	@BeforeClass
	public void setup() {
		super.setup();
		navigateToStartPage();
	}
	
	@Test(priority = 1)
	public void loginAsAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"), getPropertyAttribute("aSpotPassword"));
	}
	
	@Test(priority = 2)
	public void repeatedEmail() {
		AdministrationPage administrationPage = adminHomePage.goToAdminPage();
		UserProfilePage createUserPage = administrationPage.createNewUser(restrUserName);
		
		MessageType messageType = createUserPage.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertEquals(messageType, MessageType.ERROR, "No error message appeared after registration with repeated e-mail.");
		
		adminHomePage = (AdminHomePage) createUserPage.goToHomePage(adminHomePage);
	}
	
	@Test(priority = 2)
	public void missingEmail() {
		AdministrationPage administrationPage = adminHomePage.goToAdminPage();
		UserProfilePage createUserPage = administrationPage.createNewUser("");
		
		MessageType messageType = createUserPage.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertEquals(messageType, MessageType.ERROR, "No error message appeared after registration with missing e-mail.");
		
		adminHomePage = (AdminHomePage) createUserPage.goToHomePage(adminHomePage);
	}
	
	@Test(priority = 2)
	private void createNewUser() {
		emailOfNewUser = getPropertyAttribute("tuSpotUserName");
		
		AdministrationPage administrationPage = adminHomePage.goToAdminPage();
		administrationPage.createNewUser(emailOfNewUser);
		adminHomePage = (AdminHomePage) administrationPage.goToHomePage(adminHomePage);
	}
	
	@Test(priority = 3)
	public void deleteTestUserTest() {
		UsersOverviewPage allUsersOverViewPage = adminHomePage.goToAdminPage().viewAllUsers();
		allUsersOverViewPage.deleteUserByEmail(emailOfNewUser);
		allUsersOverViewPage = allUsersOverViewPage.goToAdminPage().viewAllUsers();
		
		Assert.assertFalse(allUsersOverViewPage.isEmailInUserList(emailOfNewUser), "User deletion failed: user still registered");
	}
	
	@AfterClass
	public void logout() {
		logout(PageFactory.initElements(driver, AdminHomePage.class));	
	}
}
