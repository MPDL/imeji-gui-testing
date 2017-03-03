package test.scripts;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.BrowseItemsPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import test.base.BaseSelenium;

public class BrowseItemsDefaultThumbnailTest extends BaseSelenium {

	private AdminHomepage adminHomePage;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
	}
	
	@Test(priority=1)
	public void setThumbNailsAsDefaultView() {
		adminHomePage.goToAdminPage().browseDefaultViewThumbnails();
	}
	
	@Test(priority=2)
	public void thumbnailsAsDefaultBrowseViewTest() {
		adminHomePage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomePage);
		adminHomePage.logout();
		BrowseItemsPage browseItemsPage = new StartPage(driver).navigateToItemPage();
		int imageCount = browseItemsPage.imageCount();
		Assert.assertTrue(imageCount >= 6, "Items are not shown as thumbnails.");
	}
	
}
