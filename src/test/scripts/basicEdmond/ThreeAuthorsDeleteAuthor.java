package test.scripts.basicEdmond;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.components.SearchComponent.CategoryType;
import spot.pages.CollectionEntryPage;
import spot.pages.ItemViewPage;
import spot.pages.LoginPage;
import spot.pages.SearchResultsPage;
import spot.pages.StartPage;
import spot.pages.registered.EditCollectionPage;
import spot.pages.registered.EditItemsPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.pages.registered.SharePage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

/**
 * Testcase #6
 * 
 * @author helk
 *
 */
public class ThreeAuthorsDeleteAuthor extends BaseSelenium {
	
	private Homepage homepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " 3 authors public mode";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";

	/**
	 * IMJ-1
	 */
	@Test(priority = 1)
	public void loginUser1() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}

	/**
	 * IMJ-112, IMJ-113, IMJ-86
	 */
	@Test(priority = 2)
	public void createCollection3Authors() {
		NewCollectionPage newCollectionPage = homepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollection3Authors(collectionTitle, collectionDescription,
				null, getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
	}

	/**
	 * IMJ-123
	 */
	@Test(priority = 3, dependsOnMethods = { "createCollection3Authors" })
	public void editTitle() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		collectionTitle += " (revised)";
		editCollection.editTitle(collectionTitle);
		collectionEntry = editCollection.submitChanges();
		
		String pageTitle = collectionEntry.getTitle();
		Assert.assertEquals(pageTitle, collectionTitle, "Title was not changed.");
	}

	/**
	 * IMJ-243
	 */
	@Test(priority = 4, dependsOnMethods = { "createCollection3Authors" })
	public void deleteAuthor() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		editCollection.removeAuthor();
		
		homepage = new StartPage(driver).goToHomepage(homepage);
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.openDescription();
		String authors = collectionEntry.getAuthor();
		Assert.assertFalse(authors.contains("Thethird"), "Third author should not be present");
	}
	
	// IMJ-131, IMJ-56
	private void uploadItem(String title) {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.uploadFile(getFilepath(title));
		
		boolean uploadSuccessful = collectionEntry.findItem(title);
		Assert.assertTrue(uploadSuccessful, "Item not among uploads.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 5, dependsOnMethods = { "createCollection3Authors" })
	public void uploadPDF() {
		uploadItem("SamplePDFFile.pdf");
	}

	/**
	 * IMJ-279
	 */
	@Test(priority = 6, dependsOnMethods = { "createCollection3Authors" })
	public void metadataAllItems() {
		String key = "Description";
		String value = "Test collection";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueAll(key, value);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(key, value);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 7, dependsOnMethods = { "createCollection3Authors" })
	public void uploadTXT() {
		uploadItem("SampleTXTFile.txt");
	}

	/**
	 * IMJ-280
	 */
	@Test(priority = 8, dependsOnMethods = { "createCollection3Authors" })
	public void metadataIfEmpty() {
		String key = "Description";
		String value = "New value";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueIfEmpty(key, value);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayed = collectionEntry.metadataDisplayed("SampleTXTFile.txt", key, value);
		Assert.assertTrue(metadataDisplayed, "New metadata is not displayed on TXT item page.");
		
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		metadataDisplayed = collectionEntry.metadataDisplayed("SamplePDFFile.pdf", key, "Test collection");
		Assert.assertTrue(metadataDisplayed, "Old metadata is not displayed on PDF item page.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 9, dependsOnMethods = { "createCollection3Authors" })
	public void uploadXLSX() {
		uploadItem("SampleXLSXFile.xlsx");
	}

	/**
	 * IMJ-281
	 */
	@Test(priority = 10, dependsOnMethods = { "createCollection3Authors" })
	public void metadataOverwrite() {
		String key = "Description";
		String value = "Overwritten value";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.overwriteAllValues(key, value);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(key, value);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on all item pages.");
	}

	/**
	 * IMJ-79
	 */
	@Test(priority = 11, dependsOnMethods = { "createCollection3Authors" })
	public void assignPredefinedLicense() {
		for (int i = 0; i <= 1; i++) {
			collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
			ItemViewPage itemView = collectionEntry.openItem(i);
			itemView.editItem().selectLicense("CC0");
			itemView.hideMessages();
			
			collectionEntry = itemView.goToCollectionEntry();
			itemView = collectionEntry.openItem(i);
			boolean licensePresent = itemView.licensePresent("https://creativecommons.org/publicdomain/zero/1.0/");
			Assert.assertTrue(licensePresent, "License does not appear.");
		}
	}

	/**
	 * IMJ-80
	 */
	@Test(priority = 12, dependsOnMethods = { "createCollection3Authors" })
	public void assignLicenseURL() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem("SamplePDFFile.pdf");
		itemView.editItem().enterOwnLicense("CC0 1.0", "https://creativecommons.org/publicdomain/zero/1.0/");
		itemView.hideMessages();
		
		collectionEntry = itemView.goToCollectionEntry();
		itemView = collectionEntry.openItem("SamplePDFFile.pdf");
		boolean licensePresent = itemView.licensePresent("https://creativecommons.org/publicdomain/zero/1.0/");
		Assert.assertTrue(licensePresent, "License does not appear.");
	}

	/**
	 * IMJ-204
	 */
	@Test(priority = 13, dependsOnMethods = { "createCollection3Authors" })
	public void shareReadExternal() {
		String email = "nonexistentuser@mpdl.mpg.de";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		SharePage sharePage = collectionEntry.share();
		sharePage = sharePage.share(false, false, email, true, false, false);
		
		sharePage = sharePage.invite();
		boolean pendingInvitation = sharePage.isEmailPendingInvitation(email);
		Assert.assertTrue(pendingInvitation, "Email of external user is not in 'Pending invitations' list.");
	}

	/**
	 * IMJ-196
	 */
	@Test(priority = 14, dependsOnMethods = { "createCollection3Authors" })
	public void shareReadRU() {
		String user2Name = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		SharePage sharePage = collectionEntry.share();
		sharePage = sharePage.share(false, false, getPropertyAttribute(restrUsername), true, false, false);
		
		sharePage = sharePage.goToCollectionPage().openCollectionByTitle(collectionTitle).share();
		
		boolean user2InSharedList = sharePage.isSharedPersonInList(user2Name);
		Assert.assertTrue(user2InSharedList, "Second user is not present in shared list.");
		
		boolean grantsCorrect = sharePage.checkUserGrantSelections(false, user2Name, true, false, false);
		Assert.assertTrue(grantsCorrect, "User grants are not correct.");
	}

	/**
	 * IMJ-2
	 */
	@Test(priority = 15, dependsOnMethods = { "createCollection3Authors" })
	public void logout() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}

	/**
	 * IMJ-22
	 */
	@Test(priority = 16, dependsOnMethods = { "createCollection3Authors" })
	public void loginUser2() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(restrUsername), getPropertyAttribute(restrPassword));
	}

	/**
	 * IMJ-46
	 */
	@Test(priority = 17, dependsOnMethods = { "createCollection3Authors" })
	public void openCollection() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean shareVisible = collectionEntry.shareIconVisible();
		Assert.assertTrue(shareVisible, "Share icon is not displayed.");
	}

	/**
	 * IMJ-2
	 */
	@Test(priority = 18, dependsOnMethods = { "createCollection3Authors" })
	public void logoutUser2() {
		homepage.logout();
	}

	/**
	 * IMJ-98, IMJ-139, IMJ-45
	 */
	@Test(priority = 19, dependsOnMethods = { "createCollection3Authors" })
	public void releaseCollectionDefaultLicense() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.releaseCollection();
		
		boolean releaseDisplayed = collectionEntry.releasedIconVisible();
		Assert.assertTrue(releaseDisplayed, "Released icon is not displayed.");
	}

	/**
	 * IMJ-115
	 */
	@Test(priority = 20, dependsOnMethods = { "createCollection3Authors" })
	public void addCollectionDOI() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.setDOI();
		
		collectionEntry = collectionEntry.openDescription();
		String actualDOI = collectionEntry.getDOI();
		Assert.assertNotEquals(actualDOI, "", "DOIs do not match.");
	}

	/**
	 * IMJ-69
	 */
	@Test(priority = 21, dependsOnMethods = { "createCollection3Authors" })
	public void discardItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.selectItem("SampleTXTFile.txt");
		collectionEntry = collectionEntry.discardSelectedItems();
		
		collectionEntry.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean itemInList = collectionEntry.findItem("SampleTXTFile.txt");
		Assert.assertFalse(itemInList, "Discarded item should not be in item list.");
	}
	
	//TODO: The successful deletion of the third author should be tested above and not in this way! Maybe remove this test-method?
	/**
	 * Postcondition
	 */
	@Test(priority = 22, dependsOnMethods = { "createCollection3Authors" })	
	public void deletedAuthorTest() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.openDescription();
		String authors = collectionEntry.getAuthor();
		Assert.assertFalse(authors.contains("Thethird"), "Third author should not be present");
		
		navigateToStartPage();
		SearchResultsPage searchResults = new StartPage(driver).getSearchComponent().searchByCategory("Thethird", CategoryType.COLLECTION);
		int resultCount = searchResults.getResultCount();
		Assert.assertTrue(resultCount == 0, "Third author is still found in results.");
	}

	/**
	 * (IMJ-2), IMJ-59
	 */
	@Test(priority = 23, dependsOnMethods = { "createCollection3Authors" })
	public void downloadItemNRU() {
		homepage.logout();
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem(0);
		boolean downloadItemPossible = itemView.isDownloadPossible();
		Assert.assertTrue(downloadItemPossible, "Non-registered user cannot download item.");
	}

	/**
	 * IMJ-233
	 */
	@Test(priority = 24, dependsOnMethods = { "createCollection3Authors" })
	public void downloadAllNRU() {
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean downloadAllPossible = collectionEntry.downloadAllPossible();
		Assert.assertTrue(downloadAllPossible, "Non-registered user cannot download collection's items.");
	}

	/**
	 * IMJ-97
	 */
	@Test(priority = 25, dependsOnMethods = { "createCollection3Authors" })
	public void discardCollection() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.discardCollection();
	}

	/**
	 * IMJ-2
	 */
	@AfterClass
	public void afterClass() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}
	
}
