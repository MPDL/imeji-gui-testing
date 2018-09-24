package test.scripts.basicEdmond;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.ItemViewPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.registered.EditCollectionPage;
import spot.pages.registered.EditItemsPage;
import spot.pages.registered.EditLicensePage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.pages.registered.SharePage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

/**
 * Testcase #8
 * 
 * @author helk
 *
 */
public class OneAuthorTwoOUs extends BaseSelenium {

	private Homepage homepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " 1 author 2 OUs public mode";
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
	 * IMJ-112, IMJ-113, IMJ-245
	 */
	@Test(priority = 2)
	public void createCollection() {
		NewCollectionPage newCollectionPage = homepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollection1Author2OUs(collectionTitle, collectionDescription,
				null, getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
	}

	/**
	 * IMJ-246
	 */
	@Test(priority = 3, dependsOnMethods = { "createCollection" })
	public void editDescription() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		collectionDescription += " edited";
		editCollection.editDescription(collectionDescription);
		collectionEntry = editCollection.submitChanges();
		String actual = collectionEntry.getDescription();
		Assert.assertEquals(actual, collectionDescription, "Description probably was not changed.");
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
	@Test(priority = 4, dependsOnMethods = { "createCollection" })
	public void uploadPDF() {
		uploadItem("SamplePDFFile.pdf");
	}

	/**
	 * IMJ-279
	 */
	@Test(priority = 5, dependsOnMethods = { "createCollection" })
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
	@Test(priority = 6, dependsOnMethods = { "createCollection" })
	public void uploadTXT() {
		uploadItem("SampleTXTFile.txt");
	}

	/**
	 * IMJ-280
	 */
	@Test(priority = 7, dependsOnMethods = { "createCollection" })
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
	@Test(priority = 8, dependsOnMethods = { "createCollection" })
	public void uploadXLSX() {
		uploadItem("SampleXLSXFile.xlsx");
	}

	/**
	 * IMJ-280
	 */
	@Test(priority = 9, dependsOnMethods = { "createCollection" })
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

	/**
	 * IMJ-67
	 */
	@Test(priority = 10, dependsOnMethods = { "createCollection" })
	public void deleteItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.deleteItem("SampleXLSXFile.xlsx");
		
		boolean itemPresent = collectionEntry.findItem("SampleXLSXFile.xlsx");
		Assert.assertFalse(itemPresent, "Item was not deleted.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 12, dependsOnMethods = { "createCollection" })
	public void uploadJPG() {
		uploadItem("SampleJPGFile.jpg");
	}

	/**
	 * IMJ-134
	 */
	@Test(priority = 13, dependsOnMethods = { "createCollection" })
	public void assignLicense() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditLicensePage editLicense = collectionEntry.editAllLicenses();
		collectionEntry = editLicense.setLicense("CC_BY");
		
		boolean licensePresent = collectionEntry.checkLicenseAll("https://creativecommons.org/licenses/by/4.0/");
		Assert.assertTrue(licensePresent, "License is not displayed.");
	}

	/**
	 * IMJ-204
	 */
	@Test(priority = 14, dependsOnMethods = { "createCollection" })
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
	@Test(priority = 15, dependsOnMethods = { "createCollection" })
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
	@Test(priority = 16, dependsOnMethods = { "createCollection" })
	public void logout() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}

	/**
	 * IMJ-22
	 */
	@Test(priority = 17, dependsOnMethods = { "createCollection" })
	public void loginUser2() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(restrUsername), getPropertyAttribute(restrPassword));
	}

	/**
	 * IMJ-46
	 */
	@Test(priority = 18, dependsOnMethods = { "createCollection" })
	public void openCollection() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean shareVisible = collectionEntry.shareIconVisible();
		Assert.assertTrue(shareVisible, "Share icon is not displayed.");
	}

	/**
	 * IMJ-2
	 */
	@Test(priority = 19, dependsOnMethods = { "createCollection" })
	public void logoutUser2() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}

	/**
	 * IMJ-98, IMJ-139, IMJ-45
	 */
	@Test(priority = 20, dependsOnMethods = { "createCollection" })
	public void releaseCollection() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.releaseCollection();
		
		boolean releaseDisplayed = collectionEntry.releasedIconVisible();
		Assert.assertTrue(releaseDisplayed, "Released icon is not displayed.");
	}

	/**
	 * IMJ-115
	 */
	@Test(priority = 21, dependsOnMethods = { "createCollection" })
	public void addCollectionDOI() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.setDOI();
		collectionEntry = collectionEntry.goToCollectionPage().openCollectionByTitle(collectionTitle);
		
		collectionEntry = collectionEntry.openDescription();
		String actualDOI = collectionEntry.getDOI();
		Assert.assertNotEquals(actualDOI, "", "DOIs do not match.");
	}

	/**
	 * IMJ-69
	 */
	@Test(priority = 22, dependsOnMethods = { "createCollection" })
	public void discardItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.selectItem("SampleTXTFile.txt");
		collectionEntry = collectionEntry.discardSelectedItems();
		
		collectionEntry.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean itemInList = collectionEntry.findItem("SampleTXTFile.txt");
		Assert.assertFalse(itemInList, "Discarded item should not be in item list.");
		
		// filter discarded, item should be in list
	}

	/**
	 * (IMJ-2), IMJ-59
	 */
	@Test(priority = 23, dependsOnMethods = { "createCollection" })
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
	@Test(priority = 24, dependsOnMethods = { "createCollection" })
	public void downloadAllNRU() {
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean downloadAllPossible = collectionEntry.downloadAllPossible();
		Assert.assertTrue(downloadAllPossible, "Non-registered user cannot download collection's items.");
	}
	
	/**
	 * Postcondition
	 */
	@Test(priority = 25, dependsOnMethods = { "createCollection" })
	public void checkOU2Correctness() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		
		EditCollectionPage editCollection = collectionEntry.editInformation();
		List<String> organisations = editCollection.getOrganisations();
		Assert.assertEquals(organisations.size(), 2, "An organisation is probably missing");
	}

	/**
	 * IMJ-97
	 */
	@Test(priority = 26, dependsOnMethods = { "createCollection" })
	public void discardCollection() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.discardCollection();
	}

	/**
	 * IMJ-2
	 */
	@AfterClass
	public void afterClass() {
		StartPage startPage = new StartPage(driver);
		startPage.hideMessages();
		homepage = startPage.goToHomepage(homepage);
		homepage.logout();
	}
}
