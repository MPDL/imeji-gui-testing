package test.scripts.highVolume;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.admin.BrowseUsersPage;
import spot.pages.admin.UserProfilePage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class CreateMultipleUsers extends BaseSelenium{

	private AdminHomepage adminHomepage;
	private AdministrationPage adminPage;
	private BrowseUsersPage allUsersPage;
	
	private String genericUserName = TimeStamp.getTimeStamp(); 
	
	private final int numberOfUsers = 10;
	
	/**
	 * IMJ-21
	 */
	@Test(priority = 1)
	public void loginAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
	}
	
	@Test(priority = 2)
	public void createUsers() {
		adminPage = adminHomepage.goToAdminPage();
		
		int oldUserCount = this.getUserCount();
		
		for(int i=0; i<numberOfUsers; i++) {
			String newUserEmailAddress = "Temp_Test_User_" + i + "@seleniumtest.imeji.org";
			String newUserFamilyName = "Temp_Test_User";
			String newUserGivenName = "Temp_Test_User_" + i + " " + genericUserName;
			createUser(newUserEmailAddress, newUserFamilyName, newUserGivenName);
		}
		
		int newUserCount = this.getUserCount();
		Assert.assertEquals(newUserCount-oldUserCount, numberOfUsers, "Not all users have been created.");
	}
	
	public int getUserCount() {
		BrowseUsersPage browseUsersPage = adminPage.browseAllUsers();
		int userCount = browseUsersPage.userCount();
		adminPage = browseUsersPage.goToAdminPage();
		
		return userCount;
	}
	
	public void createUser(String emailAddress, String familyName, String givenName) {
		UserProfilePage userPage = adminPage.createNewUser(emailAddress, familyName, givenName);
		adminPage = userPage.goToAdminPage();
	}
	
	@Test(priority = 3)
	public void deleteUsers() {
		allUsersPage = adminPage.browseAllUsers();
		
		for(int i=0; i<numberOfUsers; i++) {
			String userEmailAddress = "Temp_Test_User_" + i + "@seleniumtest.imeji.org";
			deleteUser(userEmailAddress);
		}
	}
	
	public void deleteUser(String newUserEmailAddress) {
		allUsersPage = allUsersPage.deleteUser(newUserEmailAddress);
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
