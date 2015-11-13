package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;

public class DiscardCollectionTest extends BaseSelenium {
 
	private LoginPage loginPage;
	private AdminHomePage adminHomePage;
	
	@BeforeMethod
	public void beforeMethod() {
	}

	@AfterMethod
	public void afterMethod() {
	}
	
	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
		
		loginPage = new StartPage(driver).openLoginForm();
		
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"),
				getPropertyAttribute("aSpotPassword"));

		
		//TODO create collection
	}
	
	@AfterClass
	public void afterClass() {
	}
	
	@Test (dependsOnGroups="dataUploadWithoutMetaDataProfile")
	public void discardCollectionTest() {
		
		new ActionComponent(driver).doAction(ActionType.DISCARD);
		
		Assert.assertTrue(false);
	}
}
