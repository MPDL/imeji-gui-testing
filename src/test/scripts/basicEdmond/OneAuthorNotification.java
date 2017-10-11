package test.scripts.basicEdmond;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.google.common.util.concurrent.SimpleTimeLimiter;

import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
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
import spot.util.MailAccountManager;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class OneAuthorNotification extends BaseSelenium {

	private Homepage homepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " 1 author notification on download";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";
	
	@Test(priority = 1)
	public void loginUser1() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}
	
	@Test(priority = 2)
	public void createDefaultCollection() {
		NewCollectionPage newCollectionPage = homepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollectionWithNotification(collectionTitle, collectionDescription, 
				getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
	}
	
	@Test(priority = 3)
	public void editDescription() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		collectionDescription += " (revised)";
		editCollection.editDescription(collectionDescription);
		collectionEntry = editCollection.submitChanges();
		
		String actual = collectionEntry.getDescription();
		Assert.assertEquals(actual, collectionDescription, "Description was not changed.");
	}
	
	private void uploadItem(String title) {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.uploadFile(getFilepath(title));
		
		boolean uploadSuccessful = collectionEntry.findItem(title);
		Assert.assertTrue(uploadSuccessful, "Item not among uploads.");
	}
	
	@Test(priority = 4)
	public void uploadPDF() {
		uploadItem("SamplePDFFile.pdf");
	}
	
	@Test(priority = 5)
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
	
	@Test(priority = 6)
	public void uploadTXT() {
		uploadItem("SampleTXTFile.txt");
	}
	
	@Test(priority = 7)
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
	
	@Test(priority = 8)
	public void uploadXLSX() {
		uploadItem("SampleXLSXFile.xlsx");
	}
	
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
	
	@Test(priority = 10)
	public void assignLicense() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditLicensePage editLicense = collectionEntry.editAllLicenses();
		collectionEntry = editLicense.setLicense("CC_BY");
		
		boolean licensePresent = collectionEntry.checkLicenseAll("https://creativecommons.org/licenses/by/4.0/");
		Assert.assertTrue(licensePresent, "License is not displayed.");
	}
	
	@Test(priority = 11)
	public void shareReadExternal() {
		String email = "nonexistentuser@mpdl.mpg.de";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		KindOfSharePage shareTransition = collectionEntry.share();
		SharePage sharePage = shareTransition.shareWithAUser();
		sharePage = sharePage.share(false, false, email, true, false, false);
		
		shareTransition = sharePage.invite();
		boolean pendingInvitation = shareTransition.isEmailPendingInvitation(email);
		Assert.assertTrue(pendingInvitation, "Email of external user is not in 'Pending invitations' list.");
	}
	
	@Test(priority = 12)
	public void shareReadRU() {
		String user2Name = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		KindOfSharePage shareTransition = collectionEntry.share();
		SharePage sharePage = shareTransition.shareWithAUser();
		sharePage = sharePage.share(false, false, getPropertyAttribute(restrUsername), true, false, false);
		
		collectionEntry = sharePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		shareTransition = collectionEntry.share();
		
		boolean user2InSharedList = shareTransition.isSharedPersonInList(user2Name);
		Assert.assertTrue(user2InSharedList, "Second user is not present in shared list.");
		
		boolean grantsCorrect = shareTransition.checkGrantSelections(false, user2Name, true, false, false);
		Assert.assertTrue(grantsCorrect, "User grants are not correct.");
	}
	
	@Test(priority = 13)
	public void logout() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}
	
	@Test(priority = 14)
	public void loginUser2() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(restrUsername), getPropertyAttribute(restrPassword));
	}
	
	@Test(priority = 15)
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
	
	@Test(priority = 16)
	public void downloadItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem("SamplePDFFile.pdf");
		boolean canDownload = itemView.isDownloadPossible();
		Assert.assertTrue(canDownload, "Item cannot be downloaded.");
		
		itemView.download();
		emailReceived();
	}
	
	@Test(priority = 17)
	public void downloadSelectedItems() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.selectItem("SamplePDFFile.pdf");
		collectionEntry.selectItem("SampleXLSXFile.xlsx");
		
		boolean downloadPossible = collectionEntry.downloadSelectedPossible();
		Assert.assertTrue(downloadPossible, "Download button is not enabled for selected items.");
		
		collectionEntry.downloadSelected();
		emailReceived();
	}
	
	@Test(priority = 18)
	public void downloadAllItems() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean canDownloadAll = collectionEntry.downloadAllPossible();
		Assert.assertTrue(canDownloadAll, "'Download All' button is not displayed or enabled.");
		
		collectionEntry.downloadAll();
		emailReceived();
	}
	
	@Test(priority = 19)
	public void logoutUser2() {
		homepage.logout();
	}
	
	@Test(priority = 20)
	public void releaseCollection() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.releaseCollection();
		
		boolean releaseDisplayed = collectionEntry.releasedIconVisible();
		Assert.assertTrue(releaseDisplayed, "Released icon is not displayed.");
	}
	
	@Test(priority = 21)
	public void addCollectionDOI() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.setDOI();
		
		collectionEntry = collectionEntry.openDescription();
		String actualDOI = collectionEntry.getDOI();
		Assert.assertNotEquals(actualDOI, "", "DOI should not be empty.");
	}
	
	@Test(priority = 22)
	public void discardItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem("SampleTXTFile.txt");
		collectionEntry = itemView.discardItem();
		
		boolean itemInList = collectionEntry.findItem("SampleTXTFile.txt");
		Assert.assertFalse(itemInList, "Discarded item should not be in item list.");
		
		// filter discarded, item should be in list
	}
	
	@Test(priority = 23)
	public void downloadItemNRU() {
		homepage.logout();
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem("SamplePDFFile.pdf");
		boolean downloadItemPossible = itemView.isDownloadPossible();
		Assert.assertTrue(downloadItemPossible, "Non-registered user cannot download item.");
		
		itemView.download();
		emailReceived();
	}
	
	@Test(priority = 24)
	public void downloadAllNRU() {
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean downloadAllPossible = collectionEntry.downloadAllPossible();
		Assert.assertTrue(downloadAllPossible, "Non-registered user cannot download collection's items.");
		
		collectionEntry.downloadAll();
		emailReceived();
	}
	
	@Test(priority = 25)
	public void checkDescription() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		collectionDescription += " (revised)";
		editCollection.editDescription(collectionDescription);
		collectionEntry = editCollection.submitChanges();
		
		String actual = collectionEntry.getDescription();
		Assert.assertEquals(actual, collectionDescription, "Description was not changed.");
	}
	
	@Test(priority = 26)
	public void discardCollection() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		DiscardedCollectionEntryPage discardedCollection = collectionEntry.discardCollection();
		
		boolean discardedIconDisplayed = discardedCollection.discardedIconDisplayed();
		Assert.assertTrue(discardedIconDisplayed, "Discard icon is not displayed.");
		
		boolean noItemsDisplayed = discardedCollection.noItemsDisplayed();
		Assert.assertTrue(noItemsDisplayed, "Items in a discarded collection are displayed.");
	}
	
	@AfterClass
	public void afterClass() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}
}
