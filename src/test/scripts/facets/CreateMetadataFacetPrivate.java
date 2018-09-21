package test.scripts.facets;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

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

/**
 * Testcases #24, #25, #26, #27, #28, #29 (in Private mode)
 */
//TODO: Merge with CreateMetadataFacet
public class CreateMetadataFacetPrivate extends BaseSelenium {


	private AdminHomepage adminHomepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " Edmond public mode";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";
	
	private String facetText = "Text facet: " + TimeStamp.getTimeStamp();
	private String statementText = "Text statement: " + TimeStamp.getTimeStamp();	
	private String statementTextValue = "Some metadata value";
	private String statementTitle = "Title statement: " + TimeStamp.getTimeStamp();
	private String statementTitleValue = "Title value";
	
	private String facetURL = "URL facet: " + TimeStamp.getTimeStamp();
	private String statementURL = "URL statement: " + TimeStamp.getTimeStamp();	
	private String statementURLValue = "https://www.mpdl.mpg.de/";
	private String statementLink = "Link statement: " + TimeStamp.getTimeStamp();
	private String statementLinkValue = "Link value";
	
	private String facetPerson = "Person facet: " + TimeStamp.getTimeStamp();
	private String statementPerson = "Person statement: " + TimeStamp.getTimeStamp();	
	private String personValue = "Testermann";
	private String statementPerformer = "Performer 1 statement: " + TimeStamp.getTimeStamp();
	private String statementPerformerValue = "Performer 1 value";
	
	private String facetDate = "Date facet: " + TimeStamp.getTimeStamp();
	private String statementDate = "Date statement: " + TimeStamp.getTimeStamp();	
	private String dateValue = "2017-11-09";
	private String statementNewDate = "New Date statement: " + TimeStamp.getTimeStamp();
	private String statementNewDateValue = "2222-02-02";
	
	private String facetNumber = "Number facet: " + TimeStamp.getTimeStamp();
	private String statementNumber = "Number statement: " + TimeStamp.getTimeStamp();	
	private String numberValue = "1000.0";
	private String statementNewNumber = "New Number statement: " + TimeStamp.getTimeStamp();
	private String statementNewNumberValue = "2000.0";
	
	@Test(priority = 1)
	public void switchPrivateMode() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		adminHomepage.goToAdminPage().enablePrivateMode();
	}

	/**
	 * IMJ-191
	 */
	@Test(priority = 2)
	public void enableAutoSuggestions() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.goToAdminPage().setAutosuggestionMP();
	}

	/**
	 * IMJ-240
	 */
	@Test(priority = 3)
	public void enableListView() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.goToAdminPage().enableListView();
	}
	
	private void createStatement(String statementTitle, StatementType statementType) {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		AdministrationPage adminPage = adminHomepage.goToAdminPage();
		
		BrowseStatementsPage allStatements = adminPage.createStatement(statementTitle, statementType);
		allStatements.hideMessages();
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
	
	// IMJ-282
	private void createFacetTest(String facetTitle, String metadata) {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseFacetsPage browseFacets = adminHomepage.goToAdminPage().createFacet(facetTitle, metadata);
		boolean facetListed = browseFacets.facetListed(facetTitle);
		Assert.assertTrue(facetListed, "Facet " + facetTitle + " is not listed.");
	}
	
	private String convertStatementName(String string) {
		string = string.toLowerCase().replaceAll(" ", "_").replaceAll(":", "").replaceAll("\\-", "").replaceAll("\\.", "");
		return string;
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
		allStatements.hideMessages();
		
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
	
	/**
	 * Create statement (Type Text), IMJ-226
	 */
	@Test(priority = 5)
	public void createStatementText() {
		createStatementTest(statementText, StatementType.TEXT);
		createStatementTest(statementTitle, StatementType.TEXT);
	}
	
	/**
	 * IMJ-282 (Type Text)
	 */
	@Test(priority = 6)
	public void createFacetText() {
		createFacetTest(facetText, statementText);
	}
	
	/**
	 * IMJ-279
	 */
	@Test(priority = 7)
	public void addValuesToTextStatement() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueAll(statementText, statementTextValue);
		editItems = editItems.addValueAll(statementTitle, statementTitleValue);
		
		editItems.hideMessages();
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(statementText, statementTextValue);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
		boolean titleDisplayedAll = collectionEntry.metadataDisplayedAll(statementTitle, statementTitleValue);
		Assert.assertTrue(titleDisplayedAll, "Title is not displayed on item page.");
	}
	
	@Test(priority = 8)
	public void textFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isTextFacetPresent(facetText, statementTextValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 9)
	public void textFacetDisplayedInCollection() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean facetDisplayed = collectionEntry.isTextFacetPresent(facetText, statementTextValue);
		Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	/**
	 * IMJ-285
	 */
	@Test(priority = 10)
	public void changeFacetText() {
		String newSelection = statementTitle;
		String newSelectionAlias = "md." + this.convertStatementName(statementTitle) + ".exact";
		changeFacetSelectionTest(facetText, newSelection, newSelectionAlias);
	}
	
	/**
	 * IMJ-284
	 */
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
		boolean facetDisplayedInItems = browseItems.isTextFacetPresent(facetText, statementTitleValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 13)
	public void renamedTextFacetDisplayedOnCollectionPage() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean facetDisplayed = collectionEntry.isTextFacetPresent(facetText, statementTitleValue);
		Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	/**
	 * Create statement (Type URL)
	 */
	@Test(priority = 14)
	public void createStatementURL() {
		createStatementTest(statementURL, StatementType.URL);
		createStatementTest(statementLink, StatementType.URL);
	}

	/**
	 * IMJ-282 (Type URL)
	 */
	@Test(priority = 15)
	public void createFacetURL() {
		createFacetTest(facetURL, statementURL);
	}

	/**
	 * IMJ-279
	 */
	@Test(priority = 16)
	public void addValuesToURLStatement() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueAll(statementURL, statementURLValue);
		editItems = editItems.addValueAll(statementLink, statementLinkValue);
		
		editItems.hideMessages();
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(statementURL, statementURLValue);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
		boolean linkDisplayedAll = collectionEntry.metadataDisplayedAll(statementLink, statementLinkValue);
		Assert.assertTrue(linkDisplayedAll, "Link is not displayed on item page.");
	}
	
	@Test(priority = 17)
	public void URLFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isUrlFacetPresent(facetURL, statementURLValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 18)
	public void URLFacetDisplayedInCollection() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean facetDisplayed = collectionEntry.isUrlFacetPresent(facetURL, statementURLValue);
		Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	/**
	 * IMJ-285
	 */
	@Test(priority = 19)
	public void changeFacetURL() {
		String newSelection = statementLink;
		String newSelectionAlias = "md." + this.convertStatementName(statementLink) + ".exact";
		changeFacetSelectionTest(facetURL, newSelection, newSelectionAlias);
	}
	
	/**
	 * IMJ-284
	 */
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
		boolean facetDisplayedInItems = browseItems.isUrlFacetPresent(facetURL, statementLinkValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 22)
	public void renamedURLFacetDisplayedOnCollectionPage() {
			adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
			collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
			boolean facetDisplayed = collectionEntry.isUrlFacetPresent(facetURL, statementLinkValue);
			Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	/**
	 * Create statement (Type Person)
	 */
	@Test(priority = 23)
	public void createStatementPerson() {
		createStatementTest(statementPerson, StatementType.PERSON);
		createStatementTest(statementPerformer, StatementType.PERSON);
	}
	
	/**
	 * IMJ-282 (Type Person)
	 */
	@Test(priority = 24)
	public void createFacetPerson() {
		createFacetTest(facetPerson, statementPerson);
	}
	
	/**
	 * IMJ-279
	 */
	@Test(priority = 25)
	public void addValuesToPersonStatement() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueAll(statementPerson, personValue);
		editItems = editItems.addValueAll(statementPerformer, statementPerformerValue);
		
		editItems.hideMessages();
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(statementPerson, personValue + ", ()");
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
		boolean performerDisplayedAll = collectionEntry.metadataDisplayedAll(statementPerformer, statementPerformerValue + ", ()");
		Assert.assertTrue(performerDisplayedAll, "Performer is not displayed on item page.");
	}
	
	@Test(priority = 26)
	public void personFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isPersonFacetPresent(facetPerson, personValue + ",");
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 27)
	public void personFacetDisplayedInCollection() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean facetDisplayed = collectionEntry.isPersonFacetPresent(facetPerson, personValue + ",");
		Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	/**
	 * IMJ-285
	 */
	@Test(priority = 28)
	public void changeFacetPerson() {
		String newSelection = statementPerformer;
		String newSelectionAlias = "md." + this.convertStatementName(statementPerformer) + ".exact";
		changeFacetSelectionTest(facetPerson, newSelection, newSelectionAlias);
	}
	
	/**
	 * IMJ-284
	 */
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
		boolean facetDisplayedInItems = browseItems.isPersonFacetPresent(facetPerson, statementPerformerValue + ",");
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 31)
	public void renamedPersonFacetDisplayedOnCollectionPage() {
			adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
			collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
			boolean facetDisplayed = collectionEntry.isPersonFacetPresent(facetPerson, statementPerformerValue + ",");
			Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}

	/**
	 * Create statement (Type Date)
	 */
	@Test(priority = 32)
	public void createStatementDate() {
		createStatementTest(statementDate, StatementType.DATE);
		createStatementTest(statementNewDate, StatementType.DATE);
	}
	
	/**
	 * IMJ-282 (Type Date)
	 */
	@Test(priority = 33)
	public void createFacetDate() {
		createFacetTest(facetDate, statementDate);
	}
	
	/**
	 * IMJ-279
	 */
	@Test(priority = 34)
	public void addValuesToDateStatement() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueAll(statementDate, dateValue);
		editItems = editItems.addValueAll(statementNewDate, statementNewDateValue);
		
		editItems.hideMessages();
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(statementDate, dateValue);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
		boolean newDateDisplayedAll = collectionEntry.metadataDisplayedAll(statementNewDate, statementNewDateValue);
		Assert.assertTrue(newDateDisplayedAll, "New Date is not displayed on item page.");
	}
	
	@Test(priority = 35)
	public void dateFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isDateFacetPresent(facetDate, dateValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 36)
	public void dateFacetDisplayedInCollection() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean facetDisplayed = collectionEntry.isDateFacetPresent(facetDate, dateValue);
		Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	/**
	 * IMJ-285
	 */
	@Test(priority = 37)
	public void changeFacetDate() {
		String newSelection = statementNewDate;
		String newSelectionAlias = "md." + this.convertStatementName(statementNewDate) + ".date";
		changeFacetSelectionTest(facetDate, newSelection, newSelectionAlias);
	}
	
	/**
	 * IMJ-284
	 */
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
		boolean facetDisplayedInItems = browseItems.isDateFacetPresent(facetDate, statementNewDateValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 40)
	public void renamedDateFacetDisplayedOnCollectionPage() {
			adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
			collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
			boolean facetDisplayed = collectionEntry.isDateFacetPresent(facetDate, statementNewDateValue);
			Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}

	/**
	 * Create statement (Type Number)
	 */
	@Test(priority = 41)
	public void createStatementNumber() {
		createStatementTest(statementNumber, StatementType.NUMBER);
		createStatementTest(statementNewNumber, StatementType.NUMBER);
	}

	/**
	 * IMJ-282 (Type Number)
	 */
	@Test(priority = 42)
	public void createFacetNumber() {
		createFacetTest(facetNumber, statementNumber);
	}

	/**
	 * IMJ-279
	 */
	@Test(priority = 43)
	public void addValuesToNumberStatement() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueAll(statementNumber, numberValue);
		editItems = editItems.addValueAll(statementNewNumber, statementNewNumberValue);
		
		editItems.hideMessages();
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(statementNumber, numberValue);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
		boolean newNumberDisplayedAll = collectionEntry.metadataDisplayedAll(statementNewNumber, statementNewNumberValue);
		Assert.assertTrue(newNumberDisplayedAll, "New Number is not displayed on item page.");
	}
	
	@Test(priority = 44)
	public void numberFacetDisplayedInItems() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseItemsPage browseItems = adminHomepage.navigateToItemPage();
		boolean facetDisplayedInItems = browseItems.isNumberFacetPresent(facetNumber, numberValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 45)
	public void numberFacetDisplayedInCollection() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean facetDisplayed = collectionEntry.isNumberFacetPresent(facetNumber, numberValue);
		Assert.assertTrue(facetDisplayed, "Facet is not displayed on collection page");
	}
	
	/**
	 * IMJ-285
	 */
	@Test(priority = 46)
	public void changeFacetNumber() {
		String newSelection = statementNewNumber;
		String newSelectionAlias = "md." + this.convertStatementName(statementNewNumber) + ".number";
		changeFacetSelectionTest(facetNumber, newSelection, newSelectionAlias);
	}
	
	/**
	 * IMJ-284
	 */
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
		boolean facetDisplayedInItems = browseItems.isNumberFacetPresent(facetNumber, statementNewNumberValue);
		Assert.assertTrue(facetDisplayedInItems, "Facet is not displayed while browsing items");
	}
	
	@Test(priority = 49)
	public void renamedNumberFacetDisplayedOnCollectionPage() {
			adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
			collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
			boolean facetDisplayed = collectionEntry.isNumberFacetPresent(facetNumber, statementNewNumberValue);
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
		deleteStatementTest(statementTitle);
	}
	
	/**
	 * IMJ-291
	 */
	@Test(priority = 52)
	public void deleteFacetText() {
		deleteFacetTest(facetText);
	}
	
	@Test(priority = 53)
	public void deleteStatementURL() {
		deleteStatementTest(statementURL);
		deleteStatementTest(statementLink);
	}
	
	/**
	 * IMJ-291
	 */
	@Test(priority = 54)
	public void deleteFacetURL() {
		deleteFacetTest(facetURL);
	}
	
	@Test(priority = 55)
	public void deleteStatementPerson() {
		deleteStatementTest(statementPerson);
		deleteStatementTest(statementPerformer);
	}
	
	/**
	 * IMJ-291
	 */
	@Test(priority = 56)
	public void deleteFacetPerson() {
		deleteFacetTest(facetPerson);
	}
	
	@Test(priority = 57)
	public void deleteStatementDate() {
		deleteStatementTest(statementDate);
		deleteStatementTest(statementNewDate);
	}
	
	/**
	 * IMJ-291
	 */
	@Test(priority = 58)
	public void deleteFacetDate() {
		deleteFacetTest(facetDate);
	}
	
	@Test(priority = 59)
	public void deleteStatementNumber() {
		deleteStatementTest(statementNumber);
		deleteStatementTest(statementNewNumber);
	}
	
	/**
	 * IMJ-291
	 */
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
