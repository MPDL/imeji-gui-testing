package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.AllUserGroupsOverViewPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.UserGroupPage;
import spot.pages.admin.AdminHomePage;
import spot.util.TimeStamp;

public class AddingNewUserToUserGroupTest extends BaseSelenium {
	
	private AdminHomePage adminHomePage;
	private AllUserGroupsOverViewPage allUserGroupsOverViewPage;
	
	private String newUserGroupName;
	
	@BeforeClass
	public void beforeClass() {	
		super.setup();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
	}
	
	@Test(priority = 1)
	public void createNewUserGroup() {
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		
		newUserGroupName = "Test Group: " + TimeStamp.getTimeStamp();
		
		allUserGroupsOverViewPage = adminPage.createNewUserGroup(newUserGroupName);
	}
	
	@Test(priority = 2)
	public void addNewUserToUserGroupTest() {
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
		allUserGroupsOverViewPage = adminHomePage.goToAdminPage().viewAllUserGroups();
		
		String userEmail = getPropertyAttribute(spotRUUserName);
		
		UserGroupPage userGroupPage = allUserGroupsOverViewPage.viewUserGroupDetails(newUserGroupName);
		userGroupPage = userGroupPage.addNewUser(userEmail);
		
		boolean isNewlyAddedUserPresent = userGroupPage.isNewlyAddedUserPresent(userEmail);
		Assert.assertTrue(isNewlyAddedUserPresent, "User '" + userEmail + "' couldn't be added.");
	}
	
	@Test(priority = 3)
	public void deleteUserGroup() {
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
		allUserGroupsOverViewPage = adminHomePage.goToAdminPage().viewAllUserGroups();
		allUserGroupsOverViewPage.deleteUserGroupByName(newUserGroupName);
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
		adminHomePage.logout();
	}
	
}
