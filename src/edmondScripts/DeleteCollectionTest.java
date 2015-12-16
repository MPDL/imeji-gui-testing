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
import spot.pages.BasePage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;

public class DeleteCollectionTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
	private CollectionEntryPage collectionEntryPage;
	private String collectionTitle;

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();

		new StartPage(driver).selectLanguage(englishSetup);
		
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotRUUserName),
				getPropertyAttribute(spotRUPassWord));

		collectionTitle = "Collection doomed to be deleted";

		collectionEntryPage = createCollection(collectionTitle);
	}

	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}

	@Test
	public void deleteCollectionTest() {
		
		ActionComponent actionComponent = collectionEntryPage.getActionComponent();
		CollectionsPage collectionsPage = (CollectionsPage) actionComponent.doAction(ActionType.DELETE);

		String actualInfoMessage = collectionsPage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Collection " + collectionTitle + " deleted successfully";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage,
				"Collection deletion most probably unsuccessful, since deletion confirmation info message is not displayed.");

	}

	private CollectionEntryPage createCollection(String collectionTitle) {
		CreateNewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();

		String collectionDescription = "This collection is doomed to be deleted. For testing purposes.";

		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));

		return collectionEntryPage;
	}
}
