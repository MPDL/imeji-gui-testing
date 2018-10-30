package test.scripts.highVolume;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.admin.UserGroupPage;
import spot.pages.admin.UserGroupsOverviewPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class CreateMultipleUserGroups extends BaseSelenium{

	private AdminHomepage adminHomepage;
	private AdministrationPage adminPage;
	private UserGroupsOverviewPage browseUserGroupsPage;
	
	private String genericUserGroupName = TimeStamp.getTimeStamp() + "_userGroup_"; 
	
	private final int numberOfUserGroups = 2;
	
	/**
	 * IMJ-21
	 */
	@Test(priority = 1)
	public void loginAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
	}
	
	@Test(priority = 2)
	public void createUserGroups() {
		adminPage = adminHomepage.goToAdminPage();
		
		for(int i=0; i<numberOfUserGroups; i++) {
			this.createUserGroup(genericUserGroupName + i);
		}
	}
	
	public void createUserGroup(String newUserGroupName) {
		UserGroupPage newUserGroupPage = adminPage.createNewUserGroup(newUserGroupName);
		adminPage = newUserGroupPage.goToAdminPage();
	}
	
	@Test(priority = 2)
	public void deleteUserGroups() {
		browseUserGroupsPage = adminPage.viewAllUserGroups();
		
		for(int i=0; i<numberOfUserGroups; i++) {
			this.deleteUserGroup(genericUserGroupName + i);
		}
	}
	
	public void deleteUserGroup(String userGroupName) {
		browseUserGroupsPage = browseUserGroupsPage.deleteUserGroupByName(userGroupName);
	}
	
	/**
	 * IMJ-2
	 */
	@AfterClass
	public void afterClass() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.logout();
	}
	
}
