package spot.scripts;

import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.LoginPage;
import spot.pages.admin.AdminHomePage;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class NewActionTest extends BaseSelenium {

	private AdminHomePage adminHomePage;

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
		LoginPage loginPage = getStartPage().openLoginForm();
		
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"), getPropertyAttribute("aSpotPassword"));
			
	}

	@AfterClass
	public void afterClass() {
		logout(PageFactory.initElements(getDriver(), AdminHomePage.class));
	}

	@Test
	public void createCollectionTest() {
		adminHomePage.createNewCollection();
		Assert.assertEquals(true, false);
	}

	@Test
	public void createAlbumTest() {
		adminHomePage.createNewAlbum();
		Assert.assertEquals(true, false);
	}
}
