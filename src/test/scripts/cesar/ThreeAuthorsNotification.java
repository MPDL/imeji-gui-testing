package test.scripts.cesar;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.util.concurrent.SimpleTimeLimiter;

import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.EditCollectionPage;
import spot.pages.EditLicensePage;
import spot.pages.ItemViewPage;
import spot.pages.KindOfSharePage;
import spot.pages.LoginPage;
import spot.pages.SharePage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.EditItemsPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.MailAccountManager;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class ThreeAuthorsNotification extends BaseSelenium {

	private AdminHomepage adminHomepage;
	private Homepage homepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " 3 authors send note private mode";
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
		adminHomepage.goToAdminPage().enablePrivateMode();
	}

	/**
	 * IMJ-239
	 */
	@Test(priority = 2)
	public void createCollection() {
		NewCollectionPage newCollectionPage = adminHomepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollection3AuthorsNotification(collectionTitle, collectionDescription,
				getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
		
		MessageType messageType = collectionEntry.getPageMessageType();
		Assert.assertNotEquals(messageType, MessageType.NONE, "No message was displayed.");
		Assert.assertEquals(messageType, MessageType.INFO, "Success message was not displayed.");
	}

	/**
	 * IMJ-133
	 */
	@Test(priority = 3)
	public void uploadLogo() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		editCollection.addLogo(getFilepath("SampleJPGFile.jpg"));
		collectionEntry = editCollection.submitChanges();
		
		boolean hasLogo = collectionEntry.hasLogo();
		Assert.assertTrue(hasLogo, "Logo is not displayed.");
	}

	/**
	 * IMJ-246
	 */
	@Test(priority = 4)
	public void editDescription() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		collectionDescription += " (revised)";
		editCollection.editDescription(collectionDescription);
		collectionEntry = editCollection.submitChanges();
		
		String actual = collectionEntry.getDescription();
		Assert.assertEquals(actual, collectionDescription, "Description was not changed.");
	}

	/**
	 * IMJ-244
	 */
	@Test(priority = 5)
	public void editAuthor() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		String newFamilyName = "NewAuthor";
		String newGivenName = "Some";
		editCollection.editAuthor(newFamilyName, newGivenName);
		collectionEntry = editCollection.submitChanges();
		
		String actual = collectionEntry.getValue("Authors");
		String authorName = newFamilyName + ", " + newGivenName;
		Assert.assertTrue(actual.contains(authorName), "Author name was not changed: " + actual);
	}
	
	private void uploadItem(String title) {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.uploadFile(getFilepath(title));
		
		boolean uploadSuccessful = collectionEntry.findItem(title);
		Assert.assertTrue(uploadSuccessful, "Item not among uploads.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 6)
	public void uploadPDF() {
		uploadItem("SamplePDFFile.pdf");
	}

	/**
	 * IMJ-279
	 */
	@Test(priority = 7)
	public void metadataAllItems() {
		String key = "Description";
		String value = "Test collection";
		
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueAll(key, value);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(key, value);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 8)
	public void uploadTXT() {
		uploadItem("SampleTXTFile.txt");
	}

	/**
	 * IMJ-280
	 */
	@Test(priority = 9)
	public void metadataIfEmpty() {
		String key = "Description";
		String value = "New value";
		
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
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
	@Test(priority = 10)
	public void uploadXLSX() {
		uploadItem("SampleXLSXFile.xlsx");
	}

	/**
	 * IMJ-281
	 */
	@Test(priority = 11)
	public void metadataOverwrite() {
		String key = "Description";
		String value = "Overwritten value";
		
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.overwriteAllValues(key, value);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(key, value);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on all item pages.");
	}

	/**
	 * IMJ-134
	 */
	@Test(priority = 12)
	public void assignLicense() {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditLicensePage editLicense = collectionEntry.editAllLicenses();
		collectionEntry = editLicense.setLicense("CC_BY");
		
		boolean licensePresent = collectionEntry.checkLicenseAll("https://creativecommons.org/licenses/by/4.0/");
		Assert.assertTrue(licensePresent, "License is not displayed.");
	}

	/**
	 * IMJ-195
	 */
	@Test(priority = 14)
	public void shareAdminRU() {
		String user2Name = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
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
	@Test(priority = 15)
	public void logout() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.logout();
	}

	/**
	 * IMJ-22
	 */
	@Test(priority = 16)
	public void loginUser2() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(restrUsername), getPropertyAttribute(restrPassword));
	}

	/**
	 * IMJ-46
	 */
	@Test(priority = 17)
	public void openCollection() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean shareVisible = collectionEntry.shareIconVisible();
		Assert.assertTrue(shareVisible, "Share icon is not displayed.");
	}
	
	private synchronized void emailReceived() {
		MailAccountManager mailAccountMngr = new MailAccountManager(getProperties());
	    mailAccountMngr.accessInboxFolder();
	      
	    SimpleTimeLimiter timeLimiter = new SimpleTimeLimiter();

	    String notificationMail = "";
		int predefinedTimeOutInSeconds = 30;
		try {
			notificationMail = timeLimiter.callWithTimeout(new Callable<String>() {
				public String call() {
					return mailAccountMngr.checkForNewMessage();
				}
			}, predefinedTimeOutInSeconds, TimeUnit.SECONDS, false);
		} catch (Exception e) {
			Assert.assertTrue(!notificationMail.equals(""), "Time out after " + predefinedTimeOutInSeconds + " seconds. System didn't send notification mail.");			
		} 
			
		Assert.assertTrue(notificationMail.contains("downloaded"), "Email does not contain information about download.");
	}

	/**
	 * IMJ-234, IMJ-125
	 */
	@Test(priority = 18)
	public void downloadItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem("SamplePDFFile.pdf");
		boolean canDownload = itemView.isDownloadPossible();
		Assert.assertTrue(canDownload, "Item cannot be downloaded.");
		
		itemView.download();
		emailReceived();
	}

	/**
	 * IMJ-236, IMJ-125
	 */
	@Test(priority = 20)
	public void downloadSelectedItems() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.selectItem("SampleXLSXFile.xlsx");
		collectionEntry.selectItem("SampleTXTFile.txt");
		
		boolean downloadPossible = collectionEntry.downloadSelectedPossible();
		Assert.assertTrue(downloadPossible, "Download button is not enabled for selected items.");
		
		collectionEntry.downloadSelected();
		emailReceived();
	}

	/**
	 * IMJ-232, IMJ-125
	 */
	@Test(priority = 19)
	public void downloadAllItems() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean canDownloadAll = collectionEntry.downloadAllPossible();
		Assert.assertTrue(canDownloadAll, "'Download All' button is not displayed or enabled.");
		
		collectionEntry.downloadAll();
		emailReceived();
	}

	/**
	 * IMJ-96
	 */
	@Test(priority = 21)
	public void deleteCollection() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		CollectionsPage collectionsPage = collectionEntry.deleteCollection();
		
		boolean collectionPresent = collectionsPage.collectionPresent(collectionTitle);
		Assert.assertFalse(collectionPresent, "Collection was not deleted.");
	}

	/**
	 * IMJ-2
	 */
	@Test(priority = 22)
	public void logoutUser2() {
		homepage.logout();
	}
}
