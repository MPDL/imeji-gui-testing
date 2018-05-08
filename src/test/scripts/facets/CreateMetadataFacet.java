package test.scripts.facets;

import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import spot.components.MessageComponent.MessageType;
import spot.pages.BrowseItemsPage;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.admin.BrowseFacetsPage;
import spot.pages.admin.BrowseStatementsPage;
import spot.pages.admin.CreateFacetPage;
import spot.pages.registered.EditItemsPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;
import test.base.StatementType;

public class CreateMetadataFacet extends BaseSelenium {

	private AdminHomepage adminHomepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " Edmond public mode";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";
	
	private String statementText = "Text statement: " + TimeStamp.getTimeStamp();
	private String facetText = "Text facet: " + TimeStamp.getTimeStamp();
	private String textValue = "Some metadata value";
	private String statementURL = "URL statement: " + TimeStamp.getTimeStamp();
	private String facetURL = "URL facet: " + TimeStamp.getTimeStamp();
	private String URLValue = "https://www.mpdl.mpg.de/";
	private String statementPerson = "Person statement: " + TimeStamp.getTimeStamp();
	private String facetPerson = "Person facet: " + TimeStamp.getTimeStamp();
	private String personValue = "Testermann";
	private String statementDate = "Date statement: " + TimeStamp.getTimeStamp();
	private String facetDate = "Date facet: " + TimeStamp.getTimeStamp();
	private String dateValue = "2017-11-09";
	private String statementNumber = "Number statement: " + TimeStamp.getTimeStamp();
	private String facetNumber = "Number facet: " + TimeStamp.getTimeStamp();
	private String numberValue = "1000.0";
	
	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
	}
	
	@Test(priority = 1)
	public void switchOffPrivateMode() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		adminHomepage.goToAdminPage().disablePrivateMode();
	}

	/**
	 * IMJ-240
	 */
	@Test(priority = 2)
	public void enableThumbnailView() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.goToAdminPage().enableThumbnailView();
	}
	
	private void createStatement(String statementTitle, StatementType statementType) {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		AdministrationPage adminPage = adminHomepage.goToAdminPage();
		
		BrowseStatementsPage allStatements = adminPage.createStatement(statementTitle, statementType);
		allStatements = allStatements.makeDefault(statementTitle);
		
		boolean isDefault = allStatements.isDefault(statementTitle);
		Assert.assertTrue(isDefault, "Statement is present, but is not default.");
	}
	
	private boolean statementListedInFacets(String statementTitle) {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		AdministrationPage adminPage = adminHomepage.goToAdminPage();
		
		CreateFacetPage createFacet = adminPage.goToCreateFacetPage();
		return createFacet.metadataListed(statementTitle);
	}
	
	private void createStatementTest(String statementTitle, StatementType type) {
		createStatement(statementTitle, type);
		boolean metadataListed = statementListedInFacets(statementTitle);
		Assert.assertTrue(metadataListed, "Metadata " + statementTitle + " is not listed in page for creating facets.");
	}
	
	private void createFacetTest(String facetTitle, String metadata) {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseFacetsPage browseFacets = adminHomepage.goToAdminPage().createFacet(facetTitle, metadata);
		boolean facetListed = browseFacets.facetListed(facetTitle);
		Assert.assertTrue(facetListed, "Facet " + facetTitle + " is not listed.");
	}
	
	private void changeFacetSelectionTest(String facetTitle, String newSelection, String selectionAlias) {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseFacetsPage browseFacets = adminHomepage.goToAdminPage().changeMetadataFacetSelection(facetTitle, newSelection);
		
		String alias = browseFacets.editFacet(facetTitle).getTypeAlias();
		Assert.assertEquals(selectionAlias, alias, "Facet selection was not changed.");
	}
	
	private void renameFacetTest(String facetTitle, String newFacetTitle) {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseFacetsPage browseFacets = adminHomepage.goToAdminPage().renameFacet(facetTitle, newFacetTitle);

		Assert.assertFalse(browseFacets.facetListed(facetTitle), "Old title " + facetTitle + " is listed.");
		Assert.assertTrue(browseFacets.facetListed(newFacetTitle), "New title " + newFacetTitle + " is not listed.");
	}
	
	private void deleteFacetTest(String facetTitle) {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseFacetsPage browseFacets = adminHomepage.goToAdminPage().deleteFacet(facetTitle);
		
		Assert.assertFalse(browseFacets.facetListed(facetTitle), "Deleted facet is still in facet list.");
	}
	
	private void deleteStatementTest(String statementTitle) {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		AdministrationPage adminPage = adminHomepage.goToAdminPage();
		BrowseStatementsPage allStatements = adminPage.deleteStatement(statementTitle);
		
		boolean statementPresent = allStatements.isStatementPresent(statementTitle);
		Assert.assertFalse(statementPresent, "Statement " + statementTitle + " is still in list.");
	}
	
	@Test(priority = 3)
	public void createCollection() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCreateNewCollectionPage().createCollection(collectionTitle, collectionDescription,
				adminGivenName, adminFamilyName, adminOrganizationName);
		
		MessageType messageType = collectionEntry.getPageMessageType();
		Assert.assertNotEquals(messageType, MessageType.NONE, "No message was displayed.");
		Assert.assertEquals(messageType, MessageType.SUCCESS, "Success message was not displayed.");
	}
	
	private void uploadItem(String title) {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.uploadFile(getFilepath(title));
		
		boolean uploadSuccessful = collectionEntry.findItem(title);
		Assert.assertTrue(uploadSuccessful, "Item not among uploads.");
	}
	
	@Test(priority = 4)
	public void uploadItems() {
		String[] items = {"SampleJPGFile.jpg", "SampleJPGFile2.jpg",
				"SampleMP3File.mp3", "SamplePDFFile.pdf",
				"SamplePNGFile.png"};
		for (String item : items) {
			uploadItem(item);
		}
	}
	
	@Test(priority = 5)
	public void createStatementText() {
		createStatementTest(statementText, StatementType.TEXT);
	}
	
	@Test(priority = 6)
	public void createFacetText() {
		createFacetTest(facetText, statementText);
	}
	
	@Test(priority = 7)
	public void addValuesToTextStatement() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueAll(statementText, textValue);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(statementText, textValue);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
	}
	
	@Test(priority = 8)
	public void textFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isFacetPresent(facetText, textValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 9)
	public void textFacetDisplayedInCollection() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean facetDisplayed = collectionEntry.facetDisplayed(facetText, textValue);
		Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	@Test(priority = 10)
	public void changeFacetText() {
		String newSelection = "Title";
		String selectionAlias = "md.title.exact";
		changeFacetSelectionTest(facetText, newSelection, selectionAlias);
	}
	
	@Test(priority = 11)
	public void renameFacetText() {
		String oldFacetText = facetText;
		facetText += " (revised)";
		renameFacetTest(oldFacetText, facetText);
	}
	
	@Test(priority = 12)
	public void renamedTextFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isFacetPresent(facetText, textValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 13)
	public void renamedTextFacetDisplayedOnCollectionPage() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean facetDisplayed = collectionEntry.facetDisplayed(facetText, textValue);
		Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	@Test(priority = 14)
	public void createStatementURL() {
		createStatementTest(statementURL, StatementType.URL);
	}

	@Test(priority = 15)
	public void createFacetURL() {
		createFacetTest(facetURL, statementURL);
	}

	@Test(priority = 16)
	public void addValuesToURLStatement() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueAll(statementURL, URLValue);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(statementURL, URLValue);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
	}
	
	@Test(priority = 17)
	public void URLFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isFacetPresent(facetURL, URLValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 18)
	public void URLFacetDisplayedInCollection() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean facetDisplayed = collectionEntry.facetDisplayed(facetURL, URLValue);
		Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	@Test(priority = 19)
	public void changeFacetURL() {
		String newSelection = "Link";
		String selectionAlias = "md.link.exact";
		changeFacetSelectionTest(facetURL, newSelection, selectionAlias);
	}
	
	@Test(priority = 20)
	public void renameFacetURL() {
		String oldFacetURL = facetURL;
		facetURL += " (revised)";
		renameFacetTest(oldFacetURL, facetURL);
	}
	
	@Test(priority = 21)
	public void renamedURLFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isFacetPresent(facetURL, URLValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 22)
	public void renamedURLFacetDisplayedOnCollectionPage() {
			adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
			collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
			boolean facetDisplayed = collectionEntry.facetDisplayed(facetURL, URLValue);
			Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	@Test(priority = 23)
	public void createStatementPerson() {
		createStatementTest(statementPerson, StatementType.PERSON);
	}
	
	@Test(priority = 24)
	public void createFacetPerson() {
		createFacetTest(facetPerson, statementPerson);
	}
	@Test(priority = 25)
	public void addValuesToPersonStatement() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueAll(statementPerson, personValue);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(statementPerson, personValue);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
	}
	
	@Test(priority = 26)
	public void personFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isFacetPresent(facetPerson, personValue + ", ()");
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 27)
	public void personFacetDisplayedInCollection() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean facetDisplayed = collectionEntry.facetDisplayed(facetPerson, personValue + ", ()");
		Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	@Test(priority = 28)
	public void changeFacetPerson() {
		String newSelection = "Performer 1";
		String selectionAlias = "md.performer_1.exact";
		changeFacetSelectionTest(facetPerson, newSelection, selectionAlias);
	}
	
	@Test(priority = 29)
	public void renameFacetPerson() {
		String oldFacetPerson = facetPerson;
		facetPerson += " (revised)";
		renameFacetTest(oldFacetPerson, facetPerson);
	}
	
	@Test(priority = 30)
	public void renamedPersonFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isFacetPresent(facetPerson, personValue + ", ()");
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 31)
	public void renamedPersonFacetDisplayedOnCollectionPage() {
			adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
			collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
			boolean facetDisplayed = collectionEntry.facetDisplayed(facetPerson, personValue + ", ()");
			Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}

	@Test(priority = 32)
	public void createStatementDate() {
		createStatementTest(statementDate, StatementType.DATE);
	}
	
	@Test(priority = 33)
	public void createFacetDate() {
		createFacetTest(facetDate, statementDate);
	}
	
	@Test(priority = 34)
	public void addValuesToDateStatement() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueAll(statementDate, dateValue);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(statementDate, dateValue);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
	}
	
	@Test(priority = 35)
	public void dateFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isFacetPresent(facetDate, dateValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 36)
	public void dateFacetDisplayedInCollection() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean facetDisplayed = collectionEntry.facetDisplayed(facetDate, dateValue);
		Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	@Test(priority = 37)
	public void changeFacetDate() {
		String newSelection = "Date";
		String selectionAlias = "md.date.date";
		changeFacetSelectionTest(facetDate, newSelection, selectionAlias);
	}
	
	@Test(priority = 38)
	public void renameFacetDate() {
		String oldFacetDate = facetDate;
		facetDate += " (revised)";
		renameFacetTest(oldFacetDate, facetDate);
	}
	
	@Test(priority = 39)
	public void renamedDateFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isFacetPresent(facetDate, dateValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 40)
	public void renamedDateFacetDisplayedOnCollectionPage() {
			adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
			collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
			boolean facetDisplayed = collectionEntry.facetDisplayed(facetDate, dateValue);
			Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}

	@Test(priority = 41)
	public void createStatementNumber() {
		createStatementTest(statementNumber, StatementType.NUMBER);
	}

	@Test(priority = 42)
	public void createFacetNumber() {
		createFacetTest(facetNumber, statementNumber);
	}

	@Test(priority = 43)
	public void addValuesToNumberStatement() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueAll(statementNumber, numberValue);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(statementNumber, dateValue);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
	}
	
	@Test(priority = 44)
	public void numberFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isFacetPresent(facetNumber, numberValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 45)
	public void numberFacetDisplayedInCollection() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean facetDisplayed = collectionEntry.facetDisplayed(facetNumber, numberValue);
		Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	@Test(priority = 46)
	public void changeFacetNumber() {
		String newSelection = "Number";
		String selectionAlias = "md.number.number";
		changeFacetSelectionTest(facetNumber, newSelection, selectionAlias);
	}
	
	@Test(priority = 47)
	public void renameFacetNumber() {
		String oldFacetNumber = facetNumber;
		facetNumber += " (revised)";
		renameFacetTest(oldFacetNumber, facetNumber);
	}
	
	@Test(priority = 48)
	public void renamedNumberFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isFacetPresent(facetNumber, numberValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 49)
	public void renamedNumberFacetDisplayedOnCollectionPage() {
			adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
			collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
			boolean facetDisplayed = collectionEntry.facetDisplayed(facetNumber, numberValue);
			Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	@Test(priority = 50)
	public void deleteCollection() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.deleteCollection();
	}
	
	@Test(priority = 51)
	public void deleteStatementText() {
		deleteStatementTest(statementText);
	}
	
	@Test(priority = 52)
	public void deleteFacetText() {
		deleteFacetTest(facetText);
	}
	
	@Test(priority = 53)
	public void deleteStatementURL() {
		deleteStatementTest(statementURL);
	}
	
	@Test(priority = 54)
	public void deleteFacetURL() {
		deleteFacetTest(facetURL);
	}
	
	@Test(priority = 55)
	public void deleteStatementPerson() {
		deleteStatementTest(statementPerson);
	}
	
	@Test(priority = 56)
	public void deleteFacetPerson() {
		deleteFacetTest(facetPerson);
	}
	
	@Test(priority = 57)
	public void deleteStatementDate() {
		deleteStatementTest(statementDate);
	}
	
	@Test(priority = 58)
	public void deleteFacetDate() {
		deleteFacetTest(facetDate);
	}
	
	@Test(priority = 59)
	public void deleteStatementNumber() {
		deleteStatementTest(statementNumber);
	}
	
	@Test(priority = 60)
	public void deleteFacetNumber() {
		deleteFacetTest(facetNumber);
	}
	
	@AfterClass
	public void logoutAdmin() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.logout();
	}
}
