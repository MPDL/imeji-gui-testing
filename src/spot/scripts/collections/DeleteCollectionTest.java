package spot.scripts.collections;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;
import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;

public class DeleteCollectionTest extends BaseSelenium {

	private AdminHomePage adminHomePage;

	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
	}

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();

		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"),
				getPropertyAttribute("aSpotPassword"));

	}

	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}

	@Test(dependsOnGroups = "createCollectionSuccessfully")
	public void deleteCollectionTest() {
		String collectionTitle = "Sammlung zum LöschenTesten";

		CollectionEntryPage collectionEntryPage = createCollection(collectionTitle);

		ActionComponent actionComponent = collectionEntryPage.getActionComponent();
		CollectionsPage collectionsPage = (CollectionsPage) actionComponent.doAction(ActionType.DELETE);

		String actualInfoMessage = collectionsPage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Sammlung " + collectionTitle + " erfolgreich gelöscht";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage,
				"Collection deletion most probably unsuccessful, since delection confirmation info message is not displayed.");

	}

	private CollectionEntryPage createCollection(String collectionTitle) {
		CreateNewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();

		String collectionDescription = "Das ist eine Testbeschreibung für eine neue Sammlung.";

		CollectionEntryPage collectionEntryPage = createNewCollectionPage.fillForm(collectionTitle,
				collectionDescription, getPropertyAttribute("aGivenName"), getPropertyAttribute("aFamilyName"),
				getPropertyAttribute("aOrganizationName"));

		return collectionEntryPage;
	}
}
