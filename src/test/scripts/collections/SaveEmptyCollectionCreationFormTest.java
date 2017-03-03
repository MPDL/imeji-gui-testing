package test.scripts.collections;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import spot.components.MessageComponent.MessageType;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.NewCollectionPage;
import test.base.BaseSelenium;

public class SaveEmptyCollectionCreationFormTest extends BaseSelenium {
  
	private AdminHomepage adminHomePage;

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();

		adminHomePage = loginPage.loginAsAdmin(
				getPropertyAttribute("aSpotUserName"),
				getPropertyAttribute("aSpotPassword"));
	}
	
	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
	}

	@Test
	public void submitEmptyForm() {
		NewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();
		createNewCollectionPage.submitEmptyForm();
		
		MessageType messageType = createNewCollectionPage.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertTrue(messageType == MessageType.ERROR, "Error message for missing collection information is not displayed.");
	}

	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}

}
