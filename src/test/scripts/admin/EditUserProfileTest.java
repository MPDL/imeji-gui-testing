package test.scripts.admin;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.admin.BrowseUsersPage;
import spot.pages.admin.EditUserProfilePage;
import spot.pages.admin.UserProfilePage;
import test.base.BaseSelenium;

public class EditUserProfileTest extends BaseSelenium {

	private AdminHomepage adminHomePage;
	private AdministrationPage adminPage;
	private BrowseUsersPage allUsersOverViewPage;
	
	private String newUserName = "edmond-test@mpdl.mpg.de";
	
	@BeforeClass
	public void beforeClass() {		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		adminPage = adminHomePage.goToAdminPage();
		UserProfilePage userProfilePage = adminPage.createNewUser(newUserName);
		allUsersOverViewPage = userProfilePage.goToAdminPage().browseAllUsers();
	}
	
	@Test(priority = 1)
	public void changeNameOfUserTest() {
		UserProfilePage userProfilePage = allUsersOverViewPage.viewDetailsOfUser(newUserName);
		EditUserProfilePage editUserProfilePage = userProfilePage.editProfile();
		
		String newFamilyName = "TestMan";
		userProfilePage = editUserProfilePage.changeUserFamilyName(newFamilyName);		
		
		boolean isEditedSuccessfully = userProfilePage.checkFamilyName("TestMan");
		
		Assert.assertTrue(isEditedSuccessfully, "Edit Profile Test: Name of User couldn't be changed.");
	}
	
	@Test(priority = 2)
	public void changePasswordOfUserTest() {
		UserProfilePage userProfilePage = allUsersOverViewPage.viewDetailsOfUser(newUserName);
		userProfilePage.changePassword();
		
		String actualInfoMessage = userProfilePage.getMessageComponent().getSuccessMessage();
		String expectedInfoMessage = "Password changed successfully";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Edit Profile Test: Password couldn't be changed.");
	}
	
	@Test(priority = 3)
	public void removeCollectionRightsTest() {
		UserProfilePage userProfilePage = allUsersOverViewPage.viewDetailsOfUser(newUserName);
		userProfilePage = userProfilePage.editProfile().removeCollectionRights();
		
		/*String actualInfoMessage = userProfilePage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Password changed successfully";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Edit Profile Test: Password couldn't be changed.");*/
	}
	
	@Test(priority = 4)
	public void giveAdminRights() {
		UserProfilePage userProfilePage = allUsersOverViewPage.viewDetailsOfUser(newUserName);
		userProfilePage = userProfilePage.giveAdminRights();
		Assert.assertTrue(userProfilePage.isAdmin(), "User is not given administrator rights.");
	}
	
	@Test(priority = 5)
	public void withdrawAdminRights() {
		UserProfilePage userProfilePage = allUsersOverViewPage.viewDetailsOfUser(newUserName);
		userProfilePage = userProfilePage.withdrawAdminRights();
		Assert.assertFalse(userProfilePage.isAdmin(), "User should not have administrator rights.");
	}
	
	@Test(priority = 6)
	public void deleteUser() {
		allUsersOverViewPage.deleteUser(newUserName);
	}
	
	@AfterMethod
	public void afterMethod() {
		adminHomePage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomePage);
		allUsersOverViewPage = adminHomePage.goToAdminPage().browseAllUsers();
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomePage);
		adminHomePage.logout();
	}
}
