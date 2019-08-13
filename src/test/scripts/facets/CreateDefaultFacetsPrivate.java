package test.scripts.facets;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.BrowseFacetsPage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

/**
 * Testcase #23 (in Private mode)
 */
//TODO: Merge with CreateDefaultFacets
public class CreateDefaultFacetsPrivate extends BaseSelenium {

	private AdminHomepage adminHomepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " Outbox private mode";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";
	private String collectionFacet = "Collection facet: " + TimeStamp.getTimeStamp();
	private String authorsFacet = "Collections authors facet: " + TimeStamp.getTimeStamp();
	private String filetypeFacet = "Filetype facet: " + TimeStamp.getTimeStamp();
	private String organizationsFacet = "Organizations facet: " + TimeStamp.getTimeStamp();
	private String licenseFacet = "License facet: " + TimeStamp.getTimeStamp();
	
	@BeforeClass
	public void resetDriver() {
		// Restart the browser to prevent Firefox from crashing (see Ticket #40 'Firefox memory leak')
		restartDriver();
	}
	
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
	
	@Test(priority = 4)
	public void createCollection() {
		NewCollectionPage newCollectionPage = adminHomepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollection(collectionTitle, collectionDescription, 
				getPropertyAttribute(adminGivenName), getPropertyAttribute(adminFamilyName), getPropertyAttribute(adminOrganizationName));
	}
	
	private void uploadItem(String title) {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.uploadFile(getFilepath(title));
		
		boolean uploadSuccessful = collectionEntry.findItem(title);
		Assert.assertTrue(uploadSuccessful, "Item not among uploads.");
	}
	
	@Test(priority = 5)
	public void uploadItems() {
		String[] items = {"SampleJPGFile.jpg", "SampleJPGFile2.jpg",
				"SampleMP3File.mp3", "SamplePDFFile.pdf",
				"SamplePNGFile.png"};
		for (String item : items) {
			uploadItem(item);
		}
	}
	
	private void createSystemFacet(String facetTitle, String type) {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseFacetsPage browseFacets = adminHomepage.goToAdminPage().createItemFacet(facetTitle, type);
		
		boolean facetListed = browseFacets.facetListed(facetTitle);
		Assert.assertTrue(facetListed, "Facet " + facetTitle + " is not listed.");
	}
	
	private void changeSystemFacetSelectionTest(String facetTitle, String newSelection, String selectionAlias) {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		BrowseFacetsPage browseFacets = adminHomepage.goToAdminPage().changeFacetSelectionToItemFacet(facetTitle, newSelection);
		
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
	
	@Test(priority = 6)
	public void createSystemFacetCollection() {
		createSystemFacet(collectionFacet, "Collection");
	}
	
	@Test(priority = 7)
	public void changeSystemFacetSelectionCollection() {
		changeSystemFacetSelectionTest(collectionFacet, "Collection's authors", "collection.author");
		
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		Assert.assertTrue(collectionEntry.isTextFacetPresent(collectionFacet, getPropertyAttribute(adminFamilyName) + ", " + getPropertyAttribute(adminGivenName)));
	}
	
	@Test(priority = 8)
	public void renameSystemFacetCollection() {
		String oldCollectionFacet = collectionFacet;
		collectionFacet += " (revised)";
		renameFacetTest(oldCollectionFacet, collectionFacet);
		
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		Assert.assertTrue(collectionEntry.isTextFacetPresent(collectionFacet, getPropertyAttribute(adminFamilyName) + ", " + getPropertyAttribute(adminGivenName)));
		Assert.assertFalse(collectionEntry.isTextFacetPresent(oldCollectionFacet, getPropertyAttribute(adminFamilyName) + ", " + getPropertyAttribute(adminGivenName)));
	}
	
	@Test(priority = 9)
	public void deleteSystemFacetCollection() {
		deleteFacetTest(collectionFacet);
	}
	
	@Test(priority = 10)
	public void createSystemFacetAuthors() {
		createSystemFacet(authorsFacet, "Collection's authors");
	}
	
	@Test(priority = 11)
	public void changeSystemFacetSelectionAuthors() {
		changeSystemFacetSelectionTest(authorsFacet, "Filetype", "filetype");

		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		Assert.assertTrue(collectionEntry.isTextFacetPresent(authorsFacet, "Image"));
	}
	
	@Test(priority = 12)
	public void renameSystemFacetAuthors() {
		String oldAuthorsFacet = authorsFacet;
		authorsFacet += " (revised)";
		renameFacetTest(oldAuthorsFacet, authorsFacet);
		
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		Assert.assertTrue(collectionEntry.isTextFacetPresent(authorsFacet, "Image"));
		Assert.assertFalse(collectionEntry.isTextFacetPresent(oldAuthorsFacet, "Image"));
	}
	
	@Test(priority = 13)
	public void deleteSystemFacetAuthors() {
		deleteFacetTest(authorsFacet);
	}
	
	@Test(priority = 14)
	public void createSystemFacetFiletype() {
		createSystemFacet(filetypeFacet, "Filetype");
	}
	
	@Test(priority = 15)
	public void changeSystemFacetSelectionFiletype() {
		changeSystemFacetSelectionTest(filetypeFacet, "Collection's organizations", "collection.author.organization");
		
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		Assert.assertTrue(collectionEntry.isTextFacetPresent(filetypeFacet, getPropertyAttribute(adminOrganizationName)));
	}
	
	@Test(priority = 16)
	public void renameSystemFacetFiletype() {
		String oldFiletypeFacet = filetypeFacet;
		filetypeFacet += " (revised)";
		renameFacetTest(oldFiletypeFacet, filetypeFacet);
		
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		Assert.assertTrue(collectionEntry.isTextFacetPresent(filetypeFacet, getPropertyAttribute(adminOrganizationName)));
		Assert.assertFalse(collectionEntry.isTextFacetPresent(oldFiletypeFacet, getPropertyAttribute(adminOrganizationName)));
	}
	
	@Test(priority = 17)
	public void deleteSystemFacetFiletype() {
		deleteFacetTest(filetypeFacet);
	}
	
	@Test(priority = 18)
	public void createSystemFacetOrganizations() {
		createSystemFacet(organizationsFacet, "Collection's organizations");
	}
	
	@Test(priority = 19)
	public void changeSystemFacetSelectionOrganizations() {
		changeSystemFacetSelectionTest(organizationsFacet, "License", "license");

		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		Assert.assertTrue(collectionEntry.isTextFacetPresent(organizationsFacet, "None"));
	}
	
	@Test(priority = 20)
	public void renameSystemFacetOrganizations() {
		String oldOrganizationsFacet = organizationsFacet;
		organizationsFacet += " (revised)";
		renameFacetTest(oldOrganizationsFacet, organizationsFacet);
		
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		Assert.assertTrue(collectionEntry.isTextFacetPresent(organizationsFacet, "None"));
		Assert.assertFalse(collectionEntry.isTextFacetPresent(oldOrganizationsFacet, "None"));
	}
	
	@Test(priority = 21)
	public void deleteSystemFacetOrganizations() {
		deleteFacetTest(organizationsFacet);
	}
	
	@Test(priority = 22)
	public void createSystemFacetLicense() {
		createSystemFacet(licenseFacet, "License");
	}
	
	@Test(priority = 23)
	public void changeSystemFacetSelectionLicense() {
		changeSystemFacetSelectionTest(licenseFacet, "Collection", "collection");

		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		Assert.assertTrue(collectionEntry.isTextFacetPresent(licenseFacet, collectionTitle));
	}
	
	@Test(priority = 24)
	public void renameSystemFacetLicense() {
		String oldLicenseFacet = licenseFacet;
		licenseFacet += " (revised)";
		renameFacetTest(oldLicenseFacet, licenseFacet);
		
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		Assert.assertTrue(collectionEntry.isTextFacetPresent(licenseFacet, collectionTitle));
		Assert.assertFalse(collectionEntry.isTextFacetPresent(oldLicenseFacet, collectionTitle));
	}
	
	@Test(priority = 25)
	public void deleteSystemFacetLicense() {
		deleteFacetTest(licenseFacet);
	}
	
	@Test(priority = 26)
	public void deleteCollection() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.deleteCollection();
	}
	
	@AfterClass
	public void logoutAdmin() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.logout();
	}
}
