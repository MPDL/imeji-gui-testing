package test.scripts.collections;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.NewCollectionPage;
import test.base.BaseSelenium;

public class CreateCollectionWithMissingCollectionNameTest extends BaseSelenium {

	private AdminHomepage adminHomePage;

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}

	@Test
	public void createCollectionWithMissingTitleTest() {
		NewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();

		String collectionDescription = "Some collection description";

		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollection(
				"", collectionDescription, getPropertyAttribute("aGivenName"), getPropertyAttribute("aFamilyName"), 
				getPropertyAttribute(ruOrganizationName));

		Assert.assertTrue(collectionEntryPage == null, "Collection shouldn't have been created since collection title was missing.");

		MessageType messageType = createNewCollectionPage.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertTrue(messageType == MessageType.ERROR, "No error message was displayed.");

	}

	@AfterClass
	public void afterClass() {
		adminHomePage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomePage);
		adminHomePage.logout();
	}

}
