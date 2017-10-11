package test.scripts.basicEdmond;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class OneAuthorTwoOUs extends BaseSelenium {

	private Homepage homepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " 1 author 2 OUs public mode";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";
	
	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
	}
	
	@Test(priority = 1)
	public void loginUser1() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}
	
	@Test(priority = 2)
	public void createCollection() {
		NewCollectionPage newCollectionPage = homepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollection1Author2OUs(collectionTitle, collectionDescription,
				null, getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
	}
	
	@Test(priority = 3)
	public void editDescription() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		collectionDescription += " edited";
		editCollection.editDescription(collectionDescription);
		collectionEntry = editCollection.submitChanges();
		String actual = collectionEntry.getDescription();
		Assert.assertEquals(actual, collectionDescription, "Description probably was not changed.");
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
	public void metadataIfEmpty2() {
		String key = "Description";
		String value = "Rewritten value";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueIfEmpty(key, value);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayed = collectionEntry.metadataDisplayed("SampleXLSXFile.xlsx", key, value);
		Assert.assertTrue(metadataDisplayed, "New metadata is not displayed on XLSX item page.");
	}
	
	@Test(priority = 10)
	public void deleteItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.deleteItem("SampleXLSXFile.xlsx");
		
		boolean itemPresent = collectionEntry.findItem("SampleXLSXFile.xlsx");
		Assert.assertFalse(itemPresent, "Item was not deleted.");
	}
	
	@Test(priority = 12)
	public void uploadJPG() {
		uploadItem("SampleJPGFile.jpg");
	}
	
	@Test(priority = 13)
	public void assignLicense() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditLicensePage editLicense = collectionEntry.editAllLicenses();
		collectionEntry = editLicense.setLicense("CC_BY");
		
		boolean licensePresent = collectionEntry.checkLicenseAll("https://creativecommons.org/licenses/by/4.0/");
		Assert.assertTrue(licensePresent, "License is not displayed.");
	}
	
	@Test(priority = 14)
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
	
	@Test(priority = 15)
	public void shareReadRU() {
		String user2Name = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		KindOfSharePage shareTransition = collectionEntry.share();
		SharePage sharePage = shareTransition.shareWithAUser();
		sharePage = sharePage.share(false, false, getPropertyAttribute(restrUsername), true, false, false);
		
		shareTransition = sharePage.goToCollectionPage().openCollectionByTitle(collectionTitle).share();
		
		boolean user2InSharedList = shareTransition.isSharedPersonInList(user2Name);
		Assert.assertTrue(user2InSharedList, "Second user is not present in shared list.");
		
		boolean grantsCorrect = shareTransition.checkGrantSelections(false, user2Name, true, false, false);
		Assert.assertTrue(grantsCorrect, "User grants are not correct.");
	}
	
	@Test(priority = 16)
	public void logout() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}
	
	@Test(priority = 17)
	public void loginUser2() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(restrUsername), getPropertyAttribute(restrPassword));
	}
	
	@Test(priority = 18)
	public void openCollection() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean shareVisible = collectionEntry.shareIconVisible();
		Assert.assertTrue(shareVisible, "Share icon is not displayed.");
	}
	
	@Test(priority = 19)
	public void logoutUser2() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}
	
	@Test(priority = 20)
	public void releaseCollection() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.releaseCollection();
	}
	
	@Test(priority = 21)
	public void addCollectionDOI() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.setDOI();
		
		collectionEntry = collectionEntry.openDescription();
		String actualDOI = collectionEntry.getDOI();
		Assert.assertNotEquals(actualDOI, "", "DOIs do not match.");
	}
	
	@Test(priority = 22)
	public void discardItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem(0);
		
		String itemTitle = itemView.getFileTitle();
		collectionEntry = itemView.discardItem();
		
		boolean itemInList = collectionEntry.findItem(itemTitle);
		Assert.assertFalse(itemInList, "Discarded item should not be in item list.");
		
		// filter discarded, item should be in list
	}
	
	@Test(priority = 23)
	public void downloadItemNRU() {
		homepage.logout();
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem(0);
		boolean downloadItemPossible = itemView.isDownloadPossible();
		Assert.assertTrue(downloadItemPossible, "Non-registered user cannot download item.");
	}
	
	@Test(priority = 24)
	public void downloadAllNRU() {
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean downloadAllPossible = collectionEntry.downloadAllPossible();
		Assert.assertTrue(downloadAllPossible, "Non-registered user cannot download collection's items.");
	}
	
	@Test(priority = 25)
	public void checkOU2Correctness() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		
		EditCollectionPage editCollection = collectionEntry.editInformation();
		List<String> organisations = editCollection.getOrganisations();
		Assert.assertEquals(organisations.size(), 2, "An organisation is probably missing");
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
