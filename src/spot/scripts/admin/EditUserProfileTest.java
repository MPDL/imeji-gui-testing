package spot.scripts.admin;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.EditUserProfilePage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.admin.UserProfilePage;
import spot.pages.admin.UsersOverviewPage;

public class EditUserProfileTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
	private AdministrationPage adminPage;
	private UsersOverviewPage allUsersOverViewPage;
	
	private String newUserName = "edmond-test@mpdl.mpg.de";
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		adminPage = adminHomePage.goToAdminPage();
		UserProfilePage userProfilePage = adminPage.createNewUser(newUserName);
		allUsersOverViewPage = userProfilePage.goToAdminPage().viewAllUsers();
	}
	
	@Test(priority = 1)
	public void changeNameOfUserTest() {
		UserProfilePage userProfilePage = allUsersOverViewPage.viewDetails(newUserName);
		EditUserProfilePage editUserProfilePage = userProfilePage.editProfile();
		
		String newFamilyName = "TestMan";
		userProfilePage = editUserProfilePage.changeUserFamilyName(newFamilyName);		
		
		boolean isEditedSuccessfully = userProfilePage.checkFamilyName("TestMan");
		
		Assert.assertTrue(isEditedSuccessfully, "Edit Profile Test: Name of User couldn't be changed.");
	}
	
	@Test(priority = 2)
	public void changePasswordOfUserTest() {
		UserProfilePage userProfilePage = allUsersOverViewPage.viewDetails(newUserName);
		userProfilePage.changePassword();
		
		String actualInfoMessage = userProfilePage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Password changed successfully";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Edit Profile Test: Password couldn't be changed.");
	}
	
	@Test(priority = 3)
	public void removeCollectionRightsTest() {
		UserProfilePage userProfilePage = allUsersOverViewPage.viewDetails(newUserName);
		userProfilePage = userProfilePage.editProfile().removeCollectionRights();
		
		/*String actualInfoMessage = userProfilePage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Password changed successfully";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Edit Profile Test: Password couldn't be changed.");*/
	}
	
	@Test(priority = 4)
	public void giveAdminRights() {
		UserProfilePage userProfilePage = allUsersOverViewPage.viewDetails(newUserName);
		userProfilePage = userProfilePage.giveAdminRights();
		Assert.assertTrue(userProfilePage.isAdmin(), "User is not given administrator rights.");
	}
	
	@Test(priority = 5)
	public void withdrawAdminRights() {
		UserProfilePage userProfilePage = allUsersOverViewPage.viewDetails(newUserName);
		userProfilePage = userProfilePage.withdrawAdminRights();
		Assert.assertFalse(userProfilePage.isAdmin(), "User should not have administrator rights.");
	}
	
	@Test(priority = 6)
	public void deleteUser() {
		allUsersOverViewPage.deleteUserByEmail(newUserName);
	}
	
	@AfterMethod
	public void afterMethod() {
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
		allUsersOverViewPage = adminHomePage.goToAdminPage().viewAllUsers();
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
		adminHomePage.logout();
	}
}
