package test.scripts.edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.AdministrationPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.UserGroupPage;
import spot.pages.admin.UserGroupsOverviewPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class UserGroupTest extends BaseSelenium {
	
	private AdminHomepage adminHomePage;
	private UserGroupsOverviewPage userGroupsOverview;
	private UserGroupPage userGroupPage;
	
	private String nameNewGroup;
	private String newTitle;
	private String userEmail;
	
	@BeforeClass
	public void beforeClass() {	
		super.setup();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
	}
	
	@Test(priority = 1)
	public void createNewUserGroup() {
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		
		nameNewGroup = "Test Group: " + TimeStamp.getTimeStamp();
		
		userGroupsOverview = adminPage.createNewUserGroup(nameNewGroup);
	}
	
	@Test(priority = 2)
	public void addNewUserToUserGroupTest() {
		userEmail = getPropertyAttribute(ruUsername);
		
		openGroupPage();
		userGroupPage = userGroupPage.addNewUser(userEmail);
		
		boolean isNewlyAddedUserPresent = userGroupPage.isUserPresent(userEmail);
		Assert.assertTrue(isNewlyAddedUserPresent, "User '" + userEmail + "' couldn't be added.");
	}
	
	@Test(priority = 3)
	public void deleteUserFromGroup() {
		openGroupPage();
		userGroupPage.deleteUser(userEmail);
		
		boolean isNewlyAddedUserPresent = userGroupPage.isUserPresent(userEmail);
		Assert.assertFalse(isNewlyAddedUserPresent, "User '" + userEmail + "' was not deleted.");
	}
	
	@Test(priority = 4)
	public void editUserGroupTitle() {
		openGroupPage();
		
		newTitle = "New title of user group";
		userGroupPage = userGroupPage.changeTitle(newTitle);
		String actualTitle = userGroupPage.getUserGroupTitle();
		Assert.assertEquals(newTitle, actualTitle, "Title was not successfully changed.");
		
	}
	
	@Test(priority = 5)
	public void deleteUserGroup() {
		adminHomePage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomePage);
		userGroupsOverview = adminHomePage.goToAdminPage().viewAllUserGroups();
		userGroupsOverview.deleteUserGroupByName(newTitle);
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomePage);
		adminHomePage.logout();
	}
	
	private void openGroupPage() {
		adminHomePage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomePage);
		userGroupsOverview = adminHomePage.goToAdminPage().viewAllUserGroups();
		userGroupPage = userGroupsOverview.viewUserGroupDetails(nameNewGroup);
	}
	
}
