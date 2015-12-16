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
	private AdministrationPage adminPage;
	
	private AllUserGroupsOverViewPage allUserGroupsOverViewPage;
	
	private String newUserGroupName;
	
	@BeforeClass
	public void beforeClass() {		
		navigateToStartPage();		
	
		new StartPage(driver).selectLanguage(englishSetup);
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		
		adminPage = adminHomePage.goToAdminPage();
		
		newUserGroupName = "Test Group: " + TimeStamp.getTimeStamp();
		
		allUserGroupsOverViewPage = adminPage.createNewUserGroup(newUserGroupName);
	}
	
	@Test
	public void addNewUserToUserGroupTest() {
		
		String userEmail = getPropertyAttribute(spotRUUserName);
		
		UserGroupPage userGroupPage = allUserGroupsOverViewPage.viewUserGroupDetails(newUserGroupName);
		userGroupPage = userGroupPage.addNewUser(userEmail);
		
		boolean isNewlyAddedUserPresent = userGroupPage.isNewlyAddedUserPresent(userEmail);
		Assert.assertTrue(isNewlyAddedUserPresent, "User '" + userEmail + "' couldn't be added.");
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}
	
}
