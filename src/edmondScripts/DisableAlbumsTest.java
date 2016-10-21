package edmondScripts;

import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.HomePage;

public class DisableAlbumsTest extends BaseSelenium {

	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
	}
	
	@Test(priority = 1)
	public void disableAlbums() {
		loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName),getPropertyAttribute(spotAdminPassWord));
		adminHomePage.goToAdminPage().disableAlbums();
		adminHomePage.logout();
	}
	
	@Test(priority = 2, expectedExceptions = NoSuchElementException.class)
	public void RUCannotViewAlbums() {
		loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
		homePage.goToAlbumPage();
	}
	
	@Test(priority = 3, expectedExceptions = NoSuchElementException.class)
	public void RUCannotCreateAlbums() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.goToCreateNewAlbumPage();
	}
	
	@Test(priority = 4)
	public void enableAlbums() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
		loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName),getPropertyAttribute(spotAdminPassWord));
		adminHomePage.goToAdminPage().enableAlbums();
		adminHomePage.logout();
	}
}
