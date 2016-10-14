package edmondScriptsPrivateMode;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.AdministrationPage;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.HomePage;

public class UserDeletesPrivateCollectionPMTest extends BaseSelenium {
	
	private HomePage homePage;
	
	private String collectionTitle;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		switchToPrivateMode(true);
	}
	
	private void switchToPrivateMode(boolean switchOnPrivateMode) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName), getPropertyAttribute(spotAdminPassWord));
		
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		if (switchOnPrivateMode)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		adminHomePage = (AdminHomePage) adminPage.goToHomePage(adminHomePage);
		adminHomePage.logout();
	}
	
	@Test
	public void deletePrivateCollection() {
		collectionTitle = getPropertyAttribute(collectionPMKey);
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionsPage collectionsPage = homePage.goToCollectionPage();
		CollectionContentPage collectionContentPage = collectionsPage.openCollectionByTitle(collectionTitle);
		collectionsPage = collectionContentPage.viewCollectionInformation().deleteCollection();
		homePage = collectionsPage.goToHomePage(homePage);
		
		collectionsPage = homePage.goToCollectionPage();
		boolean collectionSuccessfullyDeleted = false;
		try {
			collectionsPage.openCollectionByTitle(collectionTitle);
		}
		catch (NoSuchElementException | NullPointerException exc) {
			collectionSuccessfullyDeleted = true;
		}
		
		Assert.assertTrue(collectionSuccessfullyDeleted, "Private collection was not successfully deleted");
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
		switchToPrivateMode(false);
	}
	
}
