package test.scripts.collections;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.NewCollectionPage;
import test.base.BaseSelenium;

public class CreateCollectionWithMissingAuthorNameTest extends BaseSelenium {
  
	private AdminHomepage adminHomePage;

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(ruUsername),getPropertyAttribute(ruPassword));
	}
	
	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
	}

	@Test
	public void createCollectionWithMissingAuthorTest() {
		NewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();

		String collectionTitle = "Collection without an author's name";
		String collectionDescription = "Some collection description";

		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollection(
				collectionTitle, collectionDescription, "", "", getPropertyAttribute(ruOrganizationName));

		Assert.assertTrue(collectionEntryPage == null,
				"Collection shouldn't have been created since author's family name is missing");

		MessageType messageType = createNewCollectionPage.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertTrue(messageType == MessageType.ERROR, "Error message for missing author name is not displayed");
	}
	

	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}
}
