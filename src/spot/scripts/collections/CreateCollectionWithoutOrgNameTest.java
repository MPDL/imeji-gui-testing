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

public class CreateCollectionWithoutOrgNameTest extends BaseSelenium {

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
	public void createCollectionWithoutOrgNameTest() {
		CreateNewCollectionPage createNewCollectionPage = adminHomePage
				.goToCreateNewCollectionPage();

		String collectionTitle = "Dienstag";
		String collectionDescription = "Testtext f�r Dienstag.";
		CollectionEntryPage collectionEntryPage = createNewCollectionPage
				.createCollectionWithoutStandardMetaDataProfile(collectionTitle, collectionDescription, getPropertyAttribute("aGivenName"),
						getPropertyAttribute("aFamilyName"), "");

		Assert.assertTrue(
				collectionEntryPage == null,
				"creation of collection shouldn't have succeeded since organization name was missing");

		String errorMessage = createNewCollectionPage.getMessageComponent()
				.getErrorMessage();
		Assert.assertEquals(errorMessage,
				"Ein Organisation ben�tigt einen Namen",
				"Default error essage for missing organisation is not displayed");

	}
}
