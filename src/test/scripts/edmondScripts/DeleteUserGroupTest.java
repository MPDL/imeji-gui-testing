package test.scripts.edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.AdministrationPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.UserGroupsOverviewPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class DeleteUserGroupTest extends BaseSelenium {

	AdminHomepage adminHomePage;
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
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		adminPage = adminHomePage.goToAdminPage();
	}
	
	private void createNewGroup() {
		newUserGroupName = "Group for testing purposes " + TimeStamp.getTimeStamp();
		allUserGroupsOverViewPage = adminPage.createNewUserGroup(newUserGroupName);
		adminHomePage = (AdminHomepage) allUserGroupsOverViewPage.goToHomepage(adminHomePage);
	}
	
	@Test
	public void deleteGroupTest() {
		allUserGroupsOverViewPage = adminHomePage.goToAdminPage().viewAllUserGroups();
		allUserGroupsOverViewPage = allUserGroupsOverViewPage.deleteUserGroupByName(newUserGroupName);
		
		adminHomePage = (AdminHomepage) (allUserGroupsOverViewPage.goToHomepage(adminHomePage));
		allUserGroupsOverViewPage = adminHomePage.goToAdminPage().viewAllUserGroups();
		Assert.assertFalse(allUserGroupsOverViewPage.isNewUserGroupPresent(newUserGroupName), "User group should have been"
				+ "deleted, but is still present.");
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}
}
