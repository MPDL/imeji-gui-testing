package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.util.TimeStamp;

public class UserGroupGrantTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
	
	private String groupName = "Test Group: " + TimeStamp.getTimeStamp();
	private String collectionTitle = "Collection granted to a user group: " + TimeStamp.getTimeStamp();
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
	}
	
	@Test(priority = 1)
	public void createUserGroup() {
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		adminPage.createNewUserGroup(groupName);
	}
	
	@Test(priority = 2)
	public void createCollection() {
		NewCollectionPage newCollectionPage = adminHomePage.goToCreateNewCollectionPage();
		newCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle, "", getPropertyAttribute(adminGivenName),
				getPropertyAttribute(adminFamilyName), getPropertyAttribute(adminOrganizationName));
	}
	
	@Test(priority = 3)
	public void shareCollection() {
		CollectionEntryPage collectionEntry = adminHomePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		collectionEntry.goToSharePage().shareWithUserGroup().shareWithGroup(groupName, false, false, true, true, true, true, true, true, true);
	}
	
	@Test(priority = 4)
	public void checkGrants() {
		CollectionEntryPage collectionEntry = adminHomePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		SharePage sharePage = collectionEntry.goToSharePage().shareWithUserGroup();
		
		boolean grantsCorrect = sharePage.checkGrantSelections(false, groupName, true, true, true, true, true, true, true);
		Assert.assertTrue(grantsCorrect, "Share grants are not correct.");
		
		boolean canRevoke = sharePage.revokeDisplayed(groupName);
		Assert.assertTrue(canRevoke, "Grants cannot be revoked: button not displayed.");
	}
	
	@Test(priority = 5)
	public void deleteCollection() {
		CollectionEntryPage collectionEntry = adminHomePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		collectionEntry.deleteCollection();
	}
	
	@Test(priority = 6)
	public void deleteUserGroup() {
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		adminPage.viewAllUserGroups().deleteUserGroupByName(groupName);
	}
	
	@AfterMethod
	public void refreshHomePage() {
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
	}
	
	@AfterClass
	public void logout() {
		adminHomePage.logout();
	}
}
