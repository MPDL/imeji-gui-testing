package spot.scripts.collections;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import spot.BaseSelenium;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;

public class CreateCollectionWithMissingAuthorNameTest extends BaseSelenium {
  
	private AdminHomePage adminHomePage;

	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
	}

	@AfterMethod
	public void afterMethod() {
	}

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();

		adminHomePage = loginPage.loginAsAdmin(
				getPropertyAttribute("aSpotUserName"),
				getPropertyAttribute("aSpotPassword"));
	}

	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}

	@Test
	public void createCollectionWithMissingTitleTest() {
		CreateNewCollectionPage createNewCollectionPage = adminHomePage
				.goToCreateNewCollectionPage();

		String collectionTitle = "";
		String collectionDescription = "Some collection description";

		CollectionEntryPage collectionEntryPage = createNewCollectionPage
				.createCollectionWithoutStandardMetaDataProfile(collectionTitle, collectionDescription,
						"",
						"", "");

		Assert.assertTrue(
				collectionEntryPage == null,
				"creation of collection shouldn't have succeeded since author's family name is missing");

		String errorMessage = createNewCollectionPage.getMessageComponent()
				.getErrorMessage();
		Assert.assertEquals(errorMessage,
				"Ein Autor benötigt mindestens einen Familiennamen.",
				"Default error essage for missing author name is not displayed");

	}
}
