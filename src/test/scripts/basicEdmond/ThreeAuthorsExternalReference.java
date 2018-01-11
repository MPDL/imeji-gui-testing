package test.scripts.basicEdmond;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.DiscardedCollectionEntryPage;
import spot.pages.EditCollectionPage;
import spot.pages.EditLicensePage;
import spot.pages.ItemViewPage;
import spot.pages.KindOfSharePage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.registered.EditItemsPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class ThreeAuthorsExternalReference extends BaseSelenium {

	private Homepage homepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " 3 authors public mode";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";
	
	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
	}

	/**
	 * IMJ-1
	 */
	@Test(priority = 1)
	public void loginUser1() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}

	/**
	 * IMJ-86
	 */
	@Test(priority = 2)
	public void createCollection3Authors() {
		NewCollectionPage newCollectionPage = homepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollection3Authors(collectionTitle, collectionDescription,
				null, getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
	}

	/**
	 * IMJ-127
	 */
	@Test(priority = 3)
	public void createExternalReference() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		editCollection.addInformation("Custom information", "https://mpdl.mpg.de/");
		collectionEntry = editCollection.submitChanges();
		
		collectionEntry = collectionEntry.openDescription();
		boolean labelDisplayed = collectionEntry.labelDisplayed("Custom information");
		Assert.assertTrue(labelDisplayed, "Label is not displayed.");
	}

	/**
	 * IMJ-123
	 */
	@Test(priority = 4)
	public void editTitle() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		collectionTitle += " (revised)";
		editCollection.editTitle(collectionTitle);
		collectionEntry = editCollection.submitChanges();
		
		String pageTitle = collectionEntry.getTitle();
		Assert.assertEquals(pageTitle, collectionTitle, "Title was not changed.");
	}
	
	private void uploadItem(String title) {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.uploadFile(getFilepath(title));
		
		boolean uploadSuccessful = collectionEntry.findItem(title);
		Assert.assertTrue(uploadSuccessful, "Item not among uploads.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 5)
	public void uploadPDF() {
		uploadItem("SamplePDFFile.pdf");
	}

	/**
	 * IMJ-279
	 */
	@Test(priority = 6)
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
	@Test(priority = 7)
	public void uploadTXT() {
		uploadItem("SampleTXTFile.txt");
	}

	/**
	 * IMJ-280
	 */
	@Test(priority = 8)
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
	@Test(priority = 8)
	public void uploadXLSX() {
		uploadItem("SampleXLSXFile.xlsx");
	}

	/**
	 * IMJ-281
	 */
	@Test(priority = 9)
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
	 * IMJ-134
	 */
	@Test(priority = 10)
	public void assignLicense() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditLicensePage editLicense = collectionEntry.editAllLicenses();
		collectionEntry = editLicense.setLicense("CC_BY");
		
		boolean licensePresent = collectionEntry.checkLicenseAll("https://creativecommons.org/licenses/by/4.0/");
		Assert.assertTrue(licensePresent, "License is not displayed.");
	}

	/**
	 * IMJ-81
	 */
	@Test(priority = 11)
	public void changeItemLicense() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem(0);
		itemView = itemView.addLicense("CC0");
		Assert.assertTrue(itemView.licensePresent("https://creativecommons.org/publicdomain/zero/1.0/"), "License is not displayed.");
	}

	/**
	 * IMJ-204
	 */
	@Test(priority = 12)
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
	@Test(priority = 13)
	public void shareReadRU() {
		String user2Name = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		SharePage sharePage = collectionEntry.share();
		sharePage = sharePage.share(false, false, getPropertyAttribute(restrUsername), true, false, false);
		
		sharePage = sharePage.goToCollectionPage().openCollectionByTitle(collectionTitle).share();
		
		boolean user2InSharedList = sharePage.isSharedPersonInList(user2Name);
		Assert.assertTrue(user2InSharedList, "Second user is not present in shared list.");
		
		boolean grantsCorrect = sharePage.checkGrantSelections(false, user2Name, true, false, false);
		Assert.assertTrue(grantsCorrect, "User grants are not correct.");
	}

	/**
	 * IMJ-2
	 */
	@Test(priority = 14)
	public void logout() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}

	/**
	 * IMJ-22
	 */
	@Test(priority = 15)
	public void loginUser2() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(restrUsername), getPropertyAttribute(restrPassword));
	}

	/**
	 * IMJ-46
	 */
	@Test(priority = 16)
	public void openCollection() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean shareVisible = collectionEntry.shareIconVisible();
		Assert.assertTrue(shareVisible, "Share icon is not displayed.");
	}

	/**
	 * IMJ-2
	 */
	@Test(priority = 17)
	public void logoutUser2() {
		homepage.logout();
	}

	/**
	 * IMJ-98, IMJ-139, IMJ-45
	 */
	@Test(priority = 18)
	public void releaseCollection() {
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
	@Test(priority = 19)
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
	@Test(priority = 20)
	public void discardItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem(0);
		
		String itemTitle = itemView.getFileTitle();
		collectionEntry = itemView.discardItem();
		
		boolean itemInList = collectionEntry.findItem(itemTitle);
		Assert.assertFalse(itemInList, "Discarded item should not be in item list.");
	}
	
	@Test(priority = 21)
	public void checkExternalReference() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.openDescription();
		boolean labelDisplayed = collectionEntry.labelDisplayed("Custom information");
		Assert.assertTrue(labelDisplayed, "Label is not displayed.");
	}

	/**
	 * IMJ-59
	 */
	@Test(priority = 22)
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
	@Test(priority = 23)
	public void downloadAllNRU() {
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean downloadAllPossible = collectionEntry.downloadAllPossible();
		Assert.assertTrue(downloadAllPossible, "Non-registered user cannot download collection's items.");
	}

	/**
	 * IMJ-97
	 */
	@Test(priority = 24)
	public void discardCollection() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		DiscardedCollectionEntryPage discardedCollection = collectionEntry.discardCollection();
		
		boolean discardedIconDisplayed = discardedCollection.discardedIconDisplayed();
		Assert.assertTrue(discardedIconDisplayed, "Discard icon is not displayed.");
		
		boolean noItemsDisplayed = discardedCollection.noItemsDisplayed();
		Assert.assertTrue(noItemsDisplayed, "Items in a discarded collection are displayed.");

		CollectionsPage collections = collectionEntry.goToCollectionPage().filterDiscarded();
		Assert.assertTrue(collections.collectionPresent(collectionTitle), "Collection is not among discarded collection list.");
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
