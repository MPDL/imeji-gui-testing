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

public class CreateCollectionWithoutOrgNameTest extends BaseSelenium {

	private AdminHomepage adminHomePage;

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(ruUsername),getPropertyAttribute(ruPassword));
	}
	
	@Test
	public void createCollectionWithoutOrgNameTest() {
		NewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();

		String collectionTitle = "Collection without organisation name";
		String collectionDescription = "Test collection";
		
		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollection(
				collectionTitle, collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName), "");
		Assert.assertTrue(collectionEntryPage == null, "Collection shouldn't have been created since organization name was missing");

		MessageType messageType = createNewCollectionPage.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertTrue(messageType == MessageType.ERROR, "Error message for missing organisation is not displayed.");

	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomePage);
		adminHomePage.logout();
	}
}
