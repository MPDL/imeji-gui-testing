package test.scripts.collections;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.NewCollectionPage;
import test.base.BaseSelenium;

public class CreateCollectionWithMissingInformationTest extends BaseSelenium {
  
	private AdminHomepage adminHomepage;

	@BeforeClass
	public void beforeClass() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(ruUsername),getPropertyAttribute(ruPassword));
	}
	
	@BeforeMethod
	public void beforeMethod() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
	}

	@Test
	public void createCollectionWithMissingAuthorTest() {
		NewCollectionPage createNewCollectionPage = adminHomepage.goToCreateNewCollectionPage();

		String collectionTitle = "Collection without an author's name";
		String collectionDescription = "Some collection description";

		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollection(
				collectionTitle, collectionDescription, "", "", getPropertyAttribute(ruOrganizationName));

		Assert.assertTrue(collectionEntryPage == null,
				"Collection shouldn't have been created since author's family name is missing");

		MessageType messageType = createNewCollectionPage.getPageMessageType();
		Assert.assertTrue(messageType == MessageType.ERROR, "Error message for missing author name is not displayed");
	}
	
	@Test
	public void createCollectionWithMissingTitleTest() {
		NewCollectionPage createNewCollectionPage = adminHomepage.goToCreateNewCollectionPage();

		String collectionDescription = "Some collection description";

		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollection(
				"", collectionDescription, getPropertyAttribute(adminGivenName), getPropertyAttribute(adminFamilyName), 
				getPropertyAttribute(ruOrganizationName));

		Assert.assertTrue(collectionEntryPage == null, "Collection shouldn't have been created since collection title was missing.");

		MessageType messageType = createNewCollectionPage.getPageMessageType();
		Assert.assertTrue(messageType == MessageType.ERROR, "No error message was displayed.");

	}
	
	@Test
	public void createCollectionWithoutOrgNameTest() {
		NewCollectionPage createNewCollectionPage = adminHomepage.goToCreateNewCollectionPage();

		String collectionTitle = "Collection without organisation name";
		String collectionDescription = "Test collection";
		
		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollection(
				collectionTitle, collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName), "");
		Assert.assertTrue(collectionEntryPage == null, "Collection shouldn't have been created since organization name was missing");

		MessageType messageType = createNewCollectionPage.getPageMessageType();
		Assert.assertTrue(messageType == MessageType.ERROR, "Error message for missing organisation is not displayed.");

	}
	
	@Test
	public void submitEmptyForm() {
		NewCollectionPage createNewCollectionPage = adminHomepage.goToCreateNewCollectionPage();
		createNewCollectionPage.submitEmptyForm();
		
		MessageType messageType = createNewCollectionPage.getPageMessageType();
		Assert.assertTrue(messageType == MessageType.ERROR, "Error message for missing collection information is not displayed.");
	}
	

	@AfterClass
	public void afterClass() {
		adminHomepage.logout();
	}
}
