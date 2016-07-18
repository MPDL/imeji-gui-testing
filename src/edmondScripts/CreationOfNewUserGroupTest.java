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
import spot.pages.admin.AdminHomePage;
import spot.util.TimeStamp;

import org.openqa.selenium.support.PageFactory;

public class CreationOfNewUserGroupTest extends BaseSelenium {
	
	private AdminHomePage adminHomePage;
	private AdministrationPage adminPage;
	
	private AllUserGroupsOverViewPage allUserGroupsOverViewPage;
	
	private String newUserGroupName;
	
	@BeforeClass
	public void beforeClass() {	
		super.setup();
		navigateToStartPage();		
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		
		adminPage = adminHomePage.goToAdminPage();
		
		newUserGroupName = "Test Group " + TimeStamp.getTimeStamp();
	}
	
	@Test
	public void createNewUserGroupTest() {
		allUserGroupsOverViewPage = adminPage.createNewUserGroup(newUserGroupName);
		
		boolean isNewUserGroupPresent = allUserGroupsOverViewPage.isNewUserGroupPresent(newUserGroupName);
		Assert.assertTrue(isNewUserGroupPresent, "New user group by the name '" + newUserGroupName +"' couldn't be created.");
		
		boolean isNewUserGroupOnTopOfList = allUserGroupsOverViewPage.isNewUserGroupListenOnTopOfList(newUserGroupName);
		Assert.assertTrue(isNewUserGroupOnTopOfList, "New user group is listed, but it is not on top of the list.");
	}
	
	@AfterClass
	public void afterClass() {
		
		allUserGroupsOverViewPage = PageFactory.initElements(driver, AllUserGroupsOverViewPage.class);
		allUserGroupsOverViewPage.deleteUserGroupByName(newUserGroupName);
		
		adminHomePage.logout();
	}
	
}
