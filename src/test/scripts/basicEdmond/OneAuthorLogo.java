package test.scripts.basicEdmond;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.ItemViewPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.EditCollectionPage;
import spot.pages.registered.EditItemsPage;
import spot.pages.registered.EditLicensePage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.pages.registered.SharePage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class OneAuthorLogo extends BaseSelenium {

	private Homepage homepage;
	private AdminHomepage adminHomepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " 1 author send note public mode";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";
	
	@Test(priority = 1)
	public void disablePrivateMode() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		adminHomepage.goToAdminPage().disablePrivateMode();
	}
	
	@Test(priority = 2)
	public void enableThumbnailView() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.goToAdminPage().enableThumbnailView();
	}
	
	@Test(priority = 3)
	public void forceThumbnailView() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openFirstCollection();
		collectionEntry = collectionEntry.enableThumbnailView();
	}
	
	@Test(priority = 4)
	public void logoutAdmin() {
		adminHomepage.logout();
	}
	
	/**
	 * IMJ-1
	 */
	@Test(priority = 5)
	public void loginUser1() {
		StartPage startPage = new StartPage(driver);
		startPage.hideMessages();
		LoginPage loginPage = startPage.openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		homepage.hideMessages();
	}

	/**
	 * IMJ-83
	 */
	@Test(priority = 6)
	public void createDefaultCollection() {
		NewCollectionPage newCollectionPage = homepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollection(collectionTitle, collectionDescription, 
				getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
	}

	/**
	 * IMJ-133
	 */
	@Test(priority = 7, dependsOnMethods = { "createDefaultCollection" })
	public void uploadLogo() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		editCollection.addLogo(getFilepath("SampleJPGFile.jpg"));
		collectionEntry = editCollection.submitChanges();
		
		boolean hasLogo = collectionEntry.hasLogo();
		Assert.assertTrue(hasLogo, "Logo is not displayed.");
	}

	/**
	 * IMJ-123
	 */
	@Test(priority = 8, dependsOnMethods = { "createDefaultCollection" })
	public void editTitle() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		collectionTitle += " (revised)";
		editCollection.editTitle(collectionTitle);
		collectionEntry = editCollection.submitChanges();
		collectionEntry.goToCollectionPage().openFirstCollection();
		
		String pageTitle = collectionEntry.getTitle();
		Assert.assertEquals(pageTitle, collectionTitle, "Title was not changed.");
	}
	
	private void uploadItem(String title) {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.uploadFile(getFilepath(title));
		collectionEntry = collectionEntry.goToCollectionPage().openCollectionByTitle(collectionTitle);
		
		boolean uploadSuccessful = collectionEntry.findItem(title);
		Assert.assertTrue(uploadSuccessful, "Item " + title + " not among uploads.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 9, dependsOnMethods = { "createDefaultCollection" })
	public void uploadPDF() {
		uploadItem("SamplePDFFile.pdf");
	}

	/**
	 * IMJ-279
	 */
	@Test(priority = 10, dependsOnMethods = { "createDefaultCollection" })
	public void metadataAllItems() {
		String key = "Description";
		String value = "Test collection";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueAll(key, value);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.hideMessages();
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(key, value);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 11, dependsOnMethods = { "createDefaultCollection" })
	public void uploadTXT() {
		uploadItem("SampleTXTFile.txt");
	}

	/**
	 * IMJ-280
	 */
	@Test(priority = 12, dependsOnMethods = { "createDefaultCollection" })
	public void metadataIfEmpty() {
		String key = "Description";
		String value = "New value";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueIfEmpty(key, value);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.hideMessages();
		boolean metadataDisplayed = collectionEntry.metadataDisplayed("SampleTXTFile.txt", key, value);
		Assert.assertTrue(metadataDisplayed, "New metadata is not displayed on TXT item page.");
		
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.hideMessages();
		metadataDisplayed = collectionEntry.metadataDisplayed("SamplePDFFile.pdf", key, "Test collection");
		Assert.assertTrue(metadataDisplayed, "Old metadata is not displayed on PDF item page.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 13, dependsOnMethods = { "createDefaultCollection" })
	public void uploadXLSX() {
		uploadItem("SampleXLSXFile.xlsx");
	}

	/**
	 * IMJ-281
	 */
	@Test(priority = 14, dependsOnMethods = { "createDefaultCollection" })
	public void metadataOverwrite() {
		String key = "Description";
		String value = "Overwritten value";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.overwriteAllValues(key, value);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.hideMessages();
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(key, value);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on all item pages.");
	}

	/**
	 * IMJ-67
	 */
	@Test(priority = 15, dependsOnMethods = { "createDefaultCollection" })
	public void deleteItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.deleteItem("SampleXLSXFile.xlsx");
		
		boolean itemPresent = collectionEntry.findItem("SampleXLSXFile.xlsx");
		Assert.assertFalse(itemPresent, "Item was not deleted.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 16, dependsOnMethods = { "createDefaultCollection" })
	public void uploadJPG() {
		uploadItem("SampleJPGFile2.jpg");
	}

	/**
	 * IMJ-134
	 */
	@Test(priority = 17, dependsOnMethods = { "createDefaultCollection" })
	public void assignLicense() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditLicensePage editLicense = collectionEntry.editAllLicenses();
		collectionEntry = editLicense.setLicense("CC_BY");
		collectionEntry.hideMessages();
		
		boolean licensePresent = collectionEntry.checkLicenseAll("https://creativecommons.org/licenses/by/4.0/");
		Assert.assertTrue(licensePresent, "License is not displayed.");
	}

	/**
	 * IMJ-204
	 */
	@Test(priority = 18, dependsOnMethods = { "createDefaultCollection" })
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
	@Test(priority = 19, dependsOnMethods = { "createDefaultCollection" })
	public void shareReadRU() {
		String user2Name = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		SharePage sharePage = collectionEntry.share();
		sharePage = sharePage.share(false, false, getPropertyAttribute(restrUsername), true, false, false);
		
		collectionEntry = sharePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		sharePage = collectionEntry.share();
		
		boolean user2InSharedList = sharePage.isSharedPersonInList(user2Name);
		Assert.assertTrue(user2InSharedList, "Second user is not present in shared list.");
		
		boolean grantsCorrect = sharePage.checkGrantSelections(false, user2Name, true, false, false);
		Assert.assertTrue(grantsCorrect, "User grants are not correct.");
	}

	/**
	 * IMJ-2
	 */
	@Test(priority = 20, dependsOnMethods = { "createDefaultCollection" })
	public void logout() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}

	/**
	 * IMJ-22
	 */
	@Test(priority = 21, dependsOnMethods = { "createDefaultCollection" })
	public void loginUser2() {
		StartPage startPage = new StartPage(driver);
		startPage.hideMessages();
		LoginPage loginPage = startPage.openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(restrUsername), getPropertyAttribute(restrPassword));
	}

	/**
	 * IMJ-46
	 */
	@Test(priority = 22, dependsOnMethods = { "createDefaultCollection" })
	public void openCollection() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean shareVisible = collectionEntry.shareIconVisible();
		Assert.assertTrue(shareVisible, "Share icon is not displayed.");
	}

	/**
	 * IMJ-234
	 */
	@Test(priority = 23, dependsOnMethods = { "createDefaultCollection" })
	public void downloadItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		// open PDF file
		ItemViewPage itemView = collectionEntry.openItem("SamplePDFFile.pdf");
		boolean canDownload = itemView.isDownloadPossible();
		Assert.assertTrue(canDownload, "Item cannot be downloaded.");
	}

	/**
	 * IMJ-236
	 */
	@Test(priority = 24, dependsOnMethods = { "createDefaultCollection" })
	public void downloadSelectedItems() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.selectItem("SamplePDFFile.pdf");
		collectionEntry.selectItem("SampleJPGFile2.jpg");
		
		boolean downloadPossible = collectionEntry.downloadSelectedPossible();
		Assert.assertTrue(downloadPossible, "Download button is not enabled for selected items.");
	}

	/**
	 * IMJ-232
	 */
	@Test(priority = 25, dependsOnMethods = { "createDefaultCollection" })
	public void downloadAllItems() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.deselectAllSelectedItems();
		boolean canDownloadAll = collectionEntry.downloadAllPossible();
		Assert.assertTrue(canDownloadAll, "'Download' button is not displayed or enabled.");
	}

	/**
	 * IMJ-2
	 */
	@Test(priority = 26, dependsOnMethods = { "createDefaultCollection" })
	public void logoutUser2() {
		homepage.logout();
	}

	/**
	 * IMJ-98, IMJ-139, IMJ-45
	 */
	@Test(priority = 27, dependsOnMethods = { "createDefaultCollection" })
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
	@Test(priority = 28, dependsOnMethods = { "createDefaultCollection" })
	public void addCollectionDOI() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.setDOI();
		collectionEntry.hideMessages();
		
		collectionEntry = collectionEntry.openDescription();
		String actualDOI = collectionEntry.getDOI();
		Assert.assertNotEquals(actualDOI, "", "DOI should not be empty.");
	}

	/**
	 * IMJ-69
	 */
	@Test(priority = 29, dependsOnMethods = { "createDefaultCollection" })
	public void discardItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.selectItem("SampleTXTFile.txt");
		collectionEntry = collectionEntry.discardSelectedItems();
		
		collectionEntry.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean itemInList = collectionEntry.findItem("SampleTXTFile.txt");
		Assert.assertFalse(itemInList, "Discarded item should not be in item list.");
	}

	/**
	 * IMJ-59
	 */
	@Test(priority = 30, dependsOnMethods = { "createDefaultCollection" })
	public void downloadItemNRU() {
		homepage.logout();
		StartPage startPage = new StartPage(driver);
		startPage.hideMessages();
		collectionEntry = startPage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem("SamplePDFFile.pdf");
		boolean downloadItemPossible = itemView.isDownloadPossible();
		Assert.assertTrue(downloadItemPossible, "Non-registered user cannot download item.");
	}

	/**
	 * IMJ-233
	 */
	@Test(priority = 31, dependsOnMethods = { "createDefaultCollection" })
	public void downloadAllNRU() {
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean downloadAllPossible = collectionEntry.downloadAllPossible();
		Assert.assertTrue(downloadAllPossible, "Non-registered user cannot download collection's items.");
	}

	/**
	 * IMJ-97
	 */
	@Test(priority = 32, dependsOnMethods = { "createDefaultCollection" })
	public void discardCollection() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		homepage.hideMessages();
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
