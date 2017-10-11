package test.scripts.edmondScriptsPrivateMode;

import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.registered.Homepage;
import test.base.BaseSelenium;

public class UserDeletesPrivateCollectionPMTest extends BaseSelenium {
	
	private Homepage homePage;
	
	private String collectionTitle;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		switchToPrivateMode(true);
	}
	
	private void switchToPrivateMode(boolean switchOnPrivateMode) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomepage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		
		AdministrationPage adminPage = adminHomePage.goToAdminPage();
		if (switchOnPrivateMode)
			adminPage.enablePrivateMode();
		else
			adminPage.disablePrivateMode();
		
		adminHomePage = (AdminHomepage) adminPage.goToHomepage(adminHomePage);
		adminHomePage.logout();
	}
	
	@Test
	public void deletePrivateCollection() {
		collectionTitle = getPropertyAttribute(collectionPMKey);
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		CollectionsPage collectionsPage = homePage.goToCollectionPage();
		CollectionEntryPage collectionContentPage = collectionsPage.openCollectionByTitle(collectionTitle);
		collectionsPage = collectionContentPage.openDescription().deleteCollection();
		homePage = collectionsPage.goToHomepage(homePage);
		
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
		homePage = new StartPage(driver).goToHomepage(homePage);
		homePage.logout();
		switchToPrivateMode(false);
	}
	
}
