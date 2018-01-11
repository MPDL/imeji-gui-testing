package test.scripts.gluons;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.EditCollectionPage;
import spot.pages.ItemViewPage;
import spot.pages.KindOfSharePage;
import spot.pages.LoginPage;
import spot.pages.SearchResultsPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.EditItemsPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;
import test.base.CategoryType;

public class Default extends BaseSelenium {

	private Homepage homepage;
	private AdminHomepage adminHomepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " default 1 author private mode";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";
	
	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
	}

	/**
	 * IMJ-188
	 */
	@Test(priority = 1)
	public void switchPrivateMode() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		adminHomepage.goToAdminPage().enablePrivateMode().enableThumbnailView();
		adminHomepage.logout();
	}
	
	@Test(priority = 2)
	public void forceThumbnailView() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openFirstCollection();
		collectionEntry = collectionEntry.enableThumbnailView();
	}

	/**
	 * IMJ-19
	 */
	@Test(priority = 3)
	public void loginRU() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}

	/**
	 * IMJ-83
	 */
	@Test(priority = 4)
	public void createDefaultCollection() {
		NewCollectionPage newCollectionPage = homepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollection(collectionTitle, collectionDescription, 
				getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
		
		MessageType messageType = collectionEntry.getPageMessageType();
		Assert.assertNotEquals(messageType, MessageType.NONE, "No message was displayed.");
		Assert.assertEquals(messageType, MessageType.INFO, "Success message was not displayed.");
	}
	
	@Test(priority = 5)
	public void createExternalReference() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		editCollection.addInformation("Custom information", "https://mpdl.mpg.de/");
		collectionEntry = editCollection.submitChanges();
		
		collectionEntry = collectionEntry.openDescription();
		boolean labelDisplayed = collectionEntry.labelDisplayed("Custom information");
		Assert.assertTrue(labelDisplayed, "External reference is not displayed.");
	}

	/**
	 * IMJ-133
	 */
	@Test(priority = 5)
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
	@Test(priority = 6)
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
	@Test(priority = 7)
	public void uploadJPG() {
		uploadItem("SampleJPGFile.jpg");
	}

	/**
	 * IMJ-279
	 */
	@Test(priority = 8)
	public void metadataAllItems() {
		String key = "Title";
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
	@Test(priority = 9)
	public void uploadPDF() {
		uploadItem("SamplePDFFile.pdf");
	}

	/**
	 * IMJ-280
	 */
	@Test(priority = 10)
	public void metadataIfEmpty() {
		String key = "Title";
		String value = "New value";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueIfEmpty(key, value);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayed = collectionEntry.metadataDisplayed("SamplePDFFile.pdf", key, value);
		Assert.assertTrue(metadataDisplayed, "New metadata is not displayed on PDF item page.");
		
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		metadataDisplayed = collectionEntry.metadataDisplayed("SampleJPGFile.jpg", key, "Test collection");
		Assert.assertTrue(metadataDisplayed, "Old metadata is not displayed on JPG item page.");
	}

	/**
	 * IMJ-67
	 */
	@Test(priority = 11)
	public void deleteItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.deleteItem("SampleJPGFile.jpg");
		try { Thread.sleep(2000); } catch (InterruptedException e) { }
		
		MessageType messageType = collectionEntry.getPageMessageType();
		Assert.assertNotEquals(messageType, MessageType.NONE, "No message is displayed.");
		Assert.assertEquals(messageType, MessageType.INFO, "Information message is not displayed.");
		
		boolean itemPresent = collectionEntry.findItem("SampleJPGFile.jpg");
		Assert.assertFalse(itemPresent, "Item was not deleted.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 12)
	public void uploadJPG2() {
		uploadItem("SampleJPGFile2.jpg");
	}

	/**
	 * IMJ-195
	 */
	@Test(priority = 13)
	public void shareAdminRU() {
		String user2Name = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		SharePage sharePage = collectionEntry.share();
		sharePage = sharePage.share(false, false, getPropertyAttribute(restrUsername), true, true, true);
		
		collectionEntry = sharePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		sharePage = collectionEntry.share();
		
		boolean user2InSharedList = sharePage.isSharedPersonInList(user2Name);
		Assert.assertTrue(user2InSharedList, "Second user is not present in shared list.");
		
		boolean grantsCorrect = sharePage.checkGrantSelections(false, user2Name, true, true, true);
		Assert.assertTrue(grantsCorrect, "User grants are not correct.");
	}

	/**
	 * IMJ-2
	 */
	@Test(priority = 14)
	public void logoutRU() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}

	/**
	 * IMJ-22
	 */
	@Test(priority = 15)
	public void loginRestrictedUser() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(restrUsername), getPropertyAttribute(restrPassword));
	}

	/**
	 * IMJ-162
	 */
	@Test(priority = 16)
	public void searchCollection() {
		navigateToStartPage();
		SearchResultsPage searchResults = homepage.getSearchComponent().searchByCategory(collectionTitle, CategoryType.COLLECTION);
		Assert.assertNotEquals(searchResults.getResultCountCollection(), 0, "User cannot find collection " + collectionTitle);
	}

	/**
	 * IMJ-46
	 */
	@Test(priority = 17)
	public void shareIconDisplayed() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean shareVisible = collectionEntry.shareIconVisible();
		Assert.assertTrue(shareVisible, "Share icon is not displayed.");
	}

	/**
	 * IMJ-247
	 */
	@Test(priority = 18)
	public void addAuthor() {
		String newAuthor = "Addedauthor";
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		editCollection.addAuthor(newAuthor, getPropertyAttribute(ruOrganizationName));
		collectionEntry = editCollection.submitChanges();
		String authors = collectionEntry.getAuthor();
		
		Assert.assertTrue(authors.contains(newAuthor), "New author is not part of author list.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 19)
	public void uploadXLSX() {
		uploadItem("SampleXLSXFile.xlsx");
	}

	/**
	 * IMJ-281
	 */
	@Test(priority = 20)
	public void metadataOverwrite() {
		String key = "Date";
		String value = "2017-05-05";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.overwriteAllValues(key, value);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(key, value);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on all item pages.");
	}
	
	@Test(priority = 21)
	public void metadataAllItemsOwn() {
		String key = new Random().nextInt(1000) + "";
		String value = "Collection test";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addOwnMetadataAll(key, value);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(key, value);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
	}

	/**
	 * IMJ-234
	 */
	@Test(priority = 22)
	public void downloadItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem("SamplePDFFile.pdf");
		boolean canDownload = itemView.isDownloadPossible();
		Assert.assertTrue(canDownload, "Item cannot be downloaded.");
	}

	/**
	 * IMJ-236
	 */
	@Test(priority = 24)
	public void downloadSelectedItems() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.selectItem("SamplePDFFile.pdf");
		collectionEntry.selectItem("SampleXLSXFile.xlsx");
		
		boolean downloadPossible = collectionEntry.downloadSelectedPossible();
		Assert.assertTrue(downloadPossible, "Download button is not enabled for selected items.");
	}

	/**
	 * IMJ-232
	 */
	@Test(priority = 23)
	public void downloadAllItems() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean canDownloadAll = collectionEntry.downloadAllPossible();
		Assert.assertTrue(canDownloadAll, "'Download All' button is not displayed or enabled.");
	}

	/**
	 * IMJ-2
	 */
	@Test(priority = 25)
	public void logout() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}

	/**
	 * IMJ-19
	 */
	@Test(priority = 26)
	public void loginRU2() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}

	/**
	 * IMJ-96
	 */
	@Test(priority = 27)
	public void deleteCollection() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		CollectionsPage collectionsPage = collectionEntry.deleteCollection();
		
		boolean collectionPresent = collectionsPage.collectionPresent(collectionTitle);
		Assert.assertFalse(collectionPresent, "Collection was not deleted.");
	}

	/**
	 * IMJ-2
	 */
	@Test(priority = 28)
	public void logoutRU2() {
		homepage.logout();
	}
}
