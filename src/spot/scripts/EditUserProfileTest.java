package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.AllUsersOverViewPage;
import spot.pages.EditUserProfilePage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.UserProfilePage;
import spot.pages.admin.AdminHomePage;

public class EditUserProfileTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
	private AdministrationPage adminPage;
	
	private String newUserName;
	private AllUsersOverViewPage allUsersOverViewPage;
	
	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();		
	
//		new StartPage(driver).selectLanguage(englishSetup);
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		
		adminPage = adminHomePage.goToAdminPage();
	
		newUserName = "edmond-test@mpdl.mpg.de";
		
		allUsersOverViewPage = adminPage.createNewUser(newUserName);
	}

	@AfterMethod
	public void afterMethod() {
		String allUsersOverViewLink = "http://qa-edmond.mpdl.mpg.de/imeji/users";
		driver.navigate().to(allUsersOverViewLink);
	}
	
	
	@Test
	public void changeNameOfUserTest() {
		UserProfilePage userProfilePage = allUsersOverViewPage.viewDetails(newUserName);
		EditUserProfilePage editUserProfilePage = userProfilePage.editProfile();
		
		String newFamilyName = "TestMan";
		userProfilePage = editUserProfilePage.changeUserFamilyName(newFamilyName);		
		
		boolean isEditedSuccessfully = userProfilePage.checkFamilyName("TestMan");
		
		Assert.assertTrue(isEditedSuccessfully, "Edit Profile Test: Name of User couldn't be changed.");
	}
	
	@Test
	public void changePasswordOfUserTest() {
		UserProfilePage userProfilePage = allUsersOverViewPage.viewDetails(newUserName);
		userProfilePage.changePassword();
		
		String actualInfoMessage = userProfilePage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Password changed successfully";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Edit Profile Test: Password couldn't be changed.");
	}
	
	@AfterClass
	public void afterClass() {
		
		// delete the user again
		allUsersOverViewPage.deleteUserByEmail(newUserName);
		
		adminHomePage.logout();
	}
}
