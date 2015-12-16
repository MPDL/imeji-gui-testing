package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.AllUsersOverViewPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;

public class CreateUserWithRestrictedRightsTest extends BaseSelenium {
	
	private AdminHomePage adminHomePage;
	private AdministrationPage adminPage;
	
	private String newUserName;
	private AllUsersOverViewPage allUsersOverViewPage;
	
	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();		
	
		new StartPage(driver).selectLanguage(englishSetup);
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		
		adminPage = adminHomePage.goToAdminPage();
	
		newUserName = "edmond-test@mpdl.mpg.de";
	}

	@Test
	public void createUserTest() {
				
		allUsersOverViewPage = adminPage.createNewUser(newUserName);
		
		String actualMessage = allUsersOverViewPage.getMessageComponent().getInfoMessage();	
		String expectedMessage = "User created successfully.";
		Assert.assertEquals(actualMessage, expectedMessage, "User most probably couldn't be created");	
	}
	
	@AfterClass
	public void afterClass() {
		
		// delete the user again
		allUsersOverViewPage.deleteUserByEmail(newUserName);
		
		adminHomePage.logout();
	}

//	@Override
//	public void setTestLinkTestCaseID() {
//		testLinkTestCaseID = "IM-8";
//	}
}
