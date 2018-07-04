package test.scripts.admin;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.components.MessageComponent.MessageType;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.admin.UserProfilePage;
import spot.pages.admin.UsersOverviewPage;
import test.base.BaseSelenium;

public class UserPolicyManagementTest extends BaseSelenium {

	private AdminHomepage adminHomePage;
	private String emailOfNewUser;
	
	@Test(priority = 1)
	public void loginAsAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
	}
	
	@Test(priority = 2)
	public void repeatedEmail() {
		AdministrationPage administrationPage = adminHomePage.goToAdminPage();
		UserProfilePage createUserPage = administrationPage.createNewUser(getPropertyAttribute(restrUsername));
		
		MessageType messageType = createUserPage.getPageMessageType();
		Assert.assertEquals(messageType, MessageType.ERROR, "No error message appeared after registration with repeated e-mail.");
		
		adminHomePage = (AdminHomepage) createUserPage.goToHomepage(adminHomePage);
	}
	
	@Test(priority = 3)
	public void missingEmail() {
		AdministrationPage administrationPage = adminHomePage.goToAdminPage();
		UserProfilePage createUserPage = administrationPage.createNewUser("");
		
		MessageType messageType = createUserPage.getPageMessageType();
		Assert.assertEquals(messageType, MessageType.ERROR, "No error message appeared after registration with missing e-mail.");
		
		adminHomePage = (AdminHomepage) createUserPage.goToHomepage(adminHomePage);
	}
	
	@Test(priority = 4)
	private void createNewUser() {
		emailOfNewUser = getPropertyAttribute("tuSpotUserName");
		
		AdministrationPage administrationPage = adminHomePage.goToAdminPage();
		administrationPage.createNewUser(emailOfNewUser);
		adminHomePage = (AdminHomepage) administrationPage.goToHomepage(adminHomePage);
	}
	
	@Test(priority = 5)
	public void deleteTestUserTest() {
		UsersOverviewPage allUsersOverViewPage = adminHomePage.goToAdminPage().viewAllUsers();
		allUsersOverViewPage.deleteUserByEmail(emailOfNewUser);
		allUsersOverViewPage = allUsersOverViewPage.goToAdminPage().viewAllUsers();
		
		Assert.assertFalse(allUsersOverViewPage.isEmailInUserList(emailOfNewUser), "User deletion failed: user still registered");
	}
	
	@AfterClass
	public void logout() {
		logout(PageFactory.initElements(driver, AdminHomepage.class));	
	}
}
