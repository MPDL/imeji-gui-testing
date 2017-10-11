package test.scripts.cesar;

import java.util.Random;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
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

public class CesarDefault extends BaseSelenium {

	private Homepage homepage;
	private AdminHomepage adminHomepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " 1 author private mode by admin";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";
	
	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
	}
	
	@Test(priority = 1)
	public void switchPrivateMode() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		adminHomepage.goToAdminPage().enablePrivateMode();
	}
	
	@Test(priority = 2)
	public void enableThumbnailView() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.goToAdminPage().enableThumbnailView();
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		
		boolean thumbnailView = adminHomepage.goToCollectionPage().getPageOfLargestCollection().isElementPresent(By.id("imgFrame"));
		Assert.assertTrue(thumbnailView, "Collections should be in thumbnail view.");
	}
	
	@Test(priority = 3)
	public void createDefaultCollection() {
		NewCollectionPage newCollectionPage = adminHomepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollection(collectionTitle, collectionDescription, 
				getPropertyAttribute(adminGivenName), getPropertyAttribute(adminFamilyName), getPropertyAttribute(adminOrganizationName));
		
		MessageType messageType = collectionEntry.getPageMessageType();
		Assert.assertNotEquals(messageType, MessageType.NONE, "No message was displayed.");
		Assert.assertEquals(messageType, MessageType.INFO, "Success message was not displayed.");
	}
	
	@Test(priority = 4)
	public void createExternalReference() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		editCollection.addInformation("Custom information", "https://mpdl.mpg.de/");
		collectionEntry = editCollection.submitChanges();
		
		collectionEntry = collectionEntry.openDescription();
		boolean labelDisplayed = collectionEntry.labelDisplayed("Custom information");
		Assert.assertTrue(labelDisplayed, "External reference is not displayed.");
	}
	
	@Test(priority = 5)
	public void uploadLogo() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		editCollection.addLogo(getFilepath("SampleJPGFile.jpg"));
		collectionEntry = editCollection.submitChanges();
		
		boolean hasLogo = collectionEntry.hasLogo();
		Assert.assertTrue(hasLogo, "Logo is not displayed.");
	}
	
	@Test(priority = 6)
	public void editTitle() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		collectionTitle += " (revised)";
		editCollection.editTitle(collectionTitle);
		collectionEntry = editCollection.submitChanges();
		
		String pageTitle = collectionEntry.getTitle();
		Assert.assertEquals(pageTitle, collectionTitle, "Title was not changed.");
	}
	
	private void uploadItem(String title, Homepage roleHomepage) {
		collectionEntry = roleHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.uploadFile(getFilepath(title));
		
		boolean uploadSuccessful = collectionEntry.findItem(title);
		Assert.assertTrue(uploadSuccessful, "Item not among uploads.");
	}
	
	@Test(priority = 7)
	public void uploadJPG() {
		uploadItem("SampleJPGFile.jpg", adminHomepage);
	}
	
	@Test(priority = 8)
	public void metadataAllItems() {
		String key = "Title";
		String value = "Test collection";
		
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueAll(key, value);
		
		MessageType messageType = editItems.getPageMessageType();
		Assert.assertNotEquals(messageType, MessageType.NONE, "No message is displayed.");
		Assert.assertEquals(messageType, MessageType.INFO, "Information message is not displayed.");
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(key, value);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
	}
	
	@Test(priority = 9)
	public void uploadPDF() {
		uploadItem("SamplePDFFile.pdf", adminHomepage);
	}
	
	@Test(priority = 10)
	public void metadataIfEmpty() {
		String key = "Title";
		String value = "New value";
		
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueIfEmpty(key, value);
		
		MessageType messageType = editItems.getPageMessageType();
		Assert.assertNotEquals(messageType, MessageType.NONE, "No message is displayed.");
		Assert.assertEquals(messageType, MessageType.INFO, "Information message is not displayed.");
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayed = collectionEntry.metadataDisplayed("SamplePDFFile.pdf", key, value);
		Assert.assertTrue(metadataDisplayed, "New metadata is not displayed on PDF item page.");
		
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		metadataDisplayed = collectionEntry.metadataDisplayed("SampleJPGFile.jpg", key, "Test collection");
		Assert.assertTrue(metadataDisplayed, "Old metadata is not displayed on JPG item page.");
	}
	
	@Test(priority = 11)
	public void deleteItem() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.deleteItem("SampleJPGFile.jpg");
		
		MessageType messageType = collectionEntry.getPageMessageType();
		Assert.assertNotEquals(messageType, MessageType.NONE, "No message is displayed.");
		Assert.assertEquals(messageType, MessageType.INFO, "Information message is not displayed.");
		
		boolean itemPresent = collectionEntry.findItem("SampleJPGFile.jpg");
		Assert.assertFalse(itemPresent, "Item was not deleted.");
	}
	
	@Test(priority = 12)
	public void uploadJPG2() {
		uploadItem("SampleJPGFile2.jpg", adminHomepage);
	}
	
	@Test(priority = 13)
	public void shareEditRU() {
		String user2Name = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		KindOfSharePage shareTransition = collectionEntry.share();
		SharePage sharePage = shareTransition.shareWithAUser();
		sharePage = sharePage.share(false, false, getPropertyAttribute(restrUsername), true, true, false);
		
		collectionEntry = sharePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		shareTransition = collectionEntry.share();
		
		boolean user2InSharedList = shareTransition.isSharedPersonInList(user2Name);
		Assert.assertTrue(user2InSharedList, "Second user is not present in shared list.");
		
		boolean grantsCorrect = shareTransition.checkGrantSelections(false, user2Name, true, true, false);
		Assert.assertTrue(grantsCorrect, "User grants are not correct.");
	}
	
	@Test(priority = 14)
	public void logoutAdmin() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.logout();
	}
	
	@Test(priority = 15)
	public void loginRestrictedUser() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(restrUsername), getPropertyAttribute(restrPassword));
	}
	
	@Test(priority = 16)
	public void searchCollection() {
		SearchResultsPage searchResults = homepage.getSearchComponent().searchByCategory(collectionTitle, CategoryType.COLLECTION);
		Assert.assertNotEquals(searchResults.getResultCountCollection(), 0, "User cannot find collection " + collectionTitle);
	}
	
	@Test(priority = 17)
	public void shareIconDisplayed() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean shareVisible = collectionEntry.shareIconVisible();
		Assert.assertTrue(shareVisible, "Share icon is not displayed.");
	}
	
	@Test(priority = 18)
	public void uploadXLSX() {
		uploadItem("SampleXLSXFile.xlsx", homepage);
	}
	
	@Test(priority = 19)
	public void metadataOverwrite() {
		String key = "Date";
		String value = "2017-05-05";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.overwriteAllValues(key, value);
		
		MessageType messageType = editItems.getPageMessageType();
		Assert.assertNotEquals(messageType, MessageType.NONE, "No message is displayed.");
		Assert.assertEquals(messageType, MessageType.INFO, "Information message is not displayed.");
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(key, value);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on all item pages.");
	}
	
	@Test(priority = 20)
	public void metadataAllItemsOwn() {
		String key = new Random().nextInt(1000) + "";
		String value = "Collection test";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addOwnMetadataAll(key, value);
		
		MessageType messageType = editItems.getPageMessageType();
		Assert.assertNotEquals(messageType, MessageType.NONE, "No message is displayed.");
		Assert.assertEquals(messageType, MessageType.INFO, "Information message is not displayed.");
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(key, value);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
	}
	
	@Test(priority = 21)
	public void downloadItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem("SamplePDFFile.pdf");
		boolean canDownload = itemView.isDownloadPossible();
		Assert.assertTrue(canDownload, "Item cannot be downloaded.");
	}
	
	@Test(priority = 22)
	public void downloadSelectedItems() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.selectItem("SamplePDFFile.pdf");
		collectionEntry.selectItem("SampleJPGFile2.jpg");
		
		boolean downloadPossible = collectionEntry.downloadSelectedPossible();
		Assert.assertTrue(downloadPossible, "Download button is not enabled for selected items.");
	}
	
	@Test(priority = 23)
	public void downloadAllItems() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean canDownloadAll = collectionEntry.downloadAllPossible();
		Assert.assertTrue(canDownloadAll, "'Download All' button is not displayed or enabled.");
	}
	
	@Test(priority = 24)
	public void logout() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}
	
	@Test(priority = 25)
	public void loginAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
	}
	
	@Test(priority = 26)
	public void deleteCollection() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		CollectionsPage collectionsPage = collectionEntry.deleteCollection();
		
		boolean collectionPresent = collectionsPage.collectionPresent(collectionTitle);
		Assert.assertFalse(collectionPresent, "Collection was not deleted.");
	}
	
	@AfterClass
	public void logoutAdmin2() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.logout();
	}
	
}
