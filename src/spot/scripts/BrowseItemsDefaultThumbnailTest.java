package spot.scripts;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.BrowseItemsPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;

public class BrowseItemsDefaultThumbnailTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"), getPropertyAttribute("aSpotPassword"));
	}
	
	@Test(priority=1)
	public void setThumbNailsAsDefaultView() {
		adminHomePage.goToAdminPage().browseDefaultViewThumbnails();
	}
	
	@Test(priority=2)
	public void thumbnailsAsDefaultBrowseViewTest() {
		adminHomePage = (AdminHomePage) new StartPage(driver).goToHomePage(adminHomePage);
		adminHomePage.logout();
		StartPage startPage = new StartPage(driver);
		BrowseItemsPage browseItemsPage = startPage.navigateToItemPage();
		List<WebElement> imageList = driver.findElements(By.tagName("img"));
		Assert.assertTrue(imageList.size() >= 6, "Items are not shown as thumbnails.");
	}
	
}
