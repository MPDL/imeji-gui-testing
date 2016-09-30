package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.admin.UserGroupsOverviewPage;
import spot.util.TimeStamp;

public class DeleteUserGroupTest extends BaseSelenium {

	AdminHomePage adminHomePage;
	AdministrationPage adminPage;
	UserGroupsOverviewPage allUserGroupsOverViewPage;
	
	private String newUserGroupName;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		loginAsAdmin();
		createNewGroup();
	}
	
	private void loginAsAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		adminPage = adminHomePage.goToAdminPage();
	}
	
	private void createNewGroup() {
		newUserGroupName = "Group for testing purposes " + TimeStamp.getTimeStamp();
		allUserGroupsOverViewPage = adminPage.createNewUserGroup(newUserGroupName);
		adminHomePage = (AdminHomePage) allUserGroupsOverViewPage.goToHomePage(adminHomePage);
	}
	
	@Test
	public void deleteGroupTest() {
		allUserGroupsOverViewPage = adminHomePage.goToAdminPage().viewAllUserGroups();
		allUserGroupsOverViewPage = allUserGroupsOverViewPage.deleteUserGroupByName(newUserGroupName);
		
		adminHomePage = (AdminHomePage) (allUserGroupsOverViewPage.goToHomePage(adminHomePage));
		allUserGroupsOverViewPage = adminHomePage.goToAdminPage().viewAllUserGroups();
		Assert.assertFalse(allUserGroupsOverViewPage.isNewUserGroupPresent(newUserGroupName), "User group should have been"
				+ "deleted, but is still present.");
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}
}
