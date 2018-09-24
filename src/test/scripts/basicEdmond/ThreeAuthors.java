package test.scripts.basicEdmond;

import java.util.LinkedList;
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
 * Testcase #2
 * 
 * @author helk
 *
 */
public class ThreeAuthors extends BaseSelenium {
	
	private Homepage homepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " 3 authors public mode";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";
	private List<String> organisations = new LinkedList<>();

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
		organisations.add("Max Planck Digital Library");
		organisations.add("MPDL");
		organisations.add("Max Planck Society");
		
		NewCollectionPage newCollectionPage = homepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollection3Authors(collectionTitle, collectionDescription,
				null, getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
		
		List<String> ous = collectionEntry.editInformation().getOrganisations();
		
		for (String organisation : organisations) {
			Assert.assertTrue(ous.contains(organisation), "Organisation " + organisation + " is missing from list.");
		}
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
		collectionEntry = collectionEntry.goToCollectionPage().openFirstCollection();
		
		String pageTitle = collectionEntry.getTitle();
		Assert.assertEquals(pageTitle, collectionTitle, "Title was not changed.");
	}
	
	/**
	 * IMJ-131, IMJ-56
	 */
	private void uploadItem(String title) {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.uploadFile(getFilepath(title));
		
		boolean uploadSuccessful = collectionEntry.findItem(title);
		Assert.assertTrue(uploadSuccessful, "Item not among uploads.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 4, dependsOnMethods = { "createCollection3Authors" })
	public void uploadDOCX() {
		uploadItem("SampleWordFile.docx");
	}

	/**
	 * IMJ-279, IMJ-228
	 */
	@Test(priority = 5, dependsOnMethods = { "createCollection3Authors" })
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
	@Test(priority = 6, dependsOnMethods = { "createCollection3Authors" })
	public void uploadJPG() {
		uploadItem("SampleJPGFile2.jpg");
	}

	/**
	 * IMJ-280, IMJ-140
	 */
	@Test(priority = 7, dependsOnMethods = { "createCollection3Authors" })
	public void metadataIfEmpty() {
		String key = "Description";
		String value = "New value";
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueIfEmpty(key, value);
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayed = collectionEntry.metadataDisplayed("SampleJPGFile2.jpg", key, value);
		Assert.assertTrue(metadataDisplayed, "New metadata is not displayed on JPG item page.");
		
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		metadataDisplayed = collectionEntry.metadataDisplayed("SampleWordFile.docx", key, "Test collection");
		Assert.assertTrue(metadataDisplayed, "Old metadata is not displayed on Word item page.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 8, dependsOnMethods = { "createCollection3Authors" })
	public void uploadMP3() {
		uploadItem("SampleMP3File.mp3");
	}

	/**
	 * IMJ-281, IMJ-229
	 */
	@Test(priority = 9, dependsOnMethods = { "createCollection3Authors" })
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
	@Test(priority = 10, dependsOnMethods = { "createCollection3Authors" })
	public void assignLicense() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditLicensePage editLicense = collectionEntry.editAllLicenses();
		collectionEntry = editLicense.setLicense("CC0");
		
		boolean licensePresent = collectionEntry.checkLicenseAll("https://creativecommons.org/publicdomain/zero/1.0/");
		Assert.assertTrue(licensePresent, "License is not displayed.");
	}

	/**
	 * IMJ-204
	 */
	@Test(priority = 11, dependsOnMethods = { "createCollection3Authors" })
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
	@Test(priority = 12, dependsOnMethods = { "createCollection3Authors" })
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
	@Test(priority = 13, dependsOnMethods = { "createCollection3Authors" })
	public void logout() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}

	/**
	 * IMJ-1, IMJ-22
	 */
	@Test(priority = 14, dependsOnMethods = { "createCollection3Authors" })
	public void loginUser2() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(restrUsername), getPropertyAttribute(restrPassword));
	}

	/**
	 * IMJ-46, IMJ-47
	 */
	@Test(priority = 15, dependsOnMethods = { "createCollection3Authors" })
	public void openCollection() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean shareVisible = collectionEntry.shareIconVisible();
		Assert.assertTrue(shareVisible, "Share icon is not displayed.");
	}

	/**
	 * IMJ-234
	 */
	@Test(priority = 16, dependsOnMethods = { "createCollection3Authors" })
	public void downloadItem() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		// open PDF file
		ItemViewPage itemView = collectionEntry.openItem(0);
		boolean canDownload = itemView.isDownloadPossible();
		Assert.assertTrue(canDownload, "Item cannot be downloaded.");
	}

	/**
	 * IMJ-236
	 */
	@Test(priority = 17, dependsOnMethods = { "createCollection3Authors" })
	public void downloadSelectedItems() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.selectItem(0);
		collectionEntry = collectionEntry.selectItem(1);
		
		boolean downloadPossible = collectionEntry.downloadSelectedPossible();
		Assert.assertTrue(downloadPossible, "Download button is not enabled for selected items.");
	}

	/**
	 * IMJ-232
	 */
	@Test(priority = 18, dependsOnMethods = { "createCollection3Authors" })
	public void downloadAllItems() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry.deselectAllSelectedItems();
		boolean canDownloadAll = collectionEntry.downloadAllPossible();
		Assert.assertTrue(canDownloadAll, "'Download All' button is not displayed or enabled.");
	}

	/**
	 * IMJ-2
	 */
	@Test(priority = 19, dependsOnMethods = { "createCollection3Authors" })
	public void logoutUser2() {
		homepage.logout();
	}

	/**
	 * IMJ-1, IMJ-113, IMJ-98, IMJ-139, IMJ-45
	 */
	@Test(priority = 20, dependsOnMethods = { "createCollection3Authors" })
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
	@Test(priority = 21, dependsOnMethods = { "createCollection3Authors" })
	public void addCollectionDOI() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.setDOI();
		
		collectionEntry = collectionEntry.openDescription();
		String actualDOI = collectionEntry.getDOI();
		Assert.assertNotEquals(actualDOI, "", "DOI should not be empty.");
	}

	/**
	 * IMJ-69
	 */
	@Test(priority = 22, dependsOnMethods = { "createCollection3Authors" })
	public void discardItem() {
		int itemCount = collectionEntry.getTotalItemNumber();
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.selectItem(0);
		collectionEntry = collectionEntry.discardSelectedItems();
		
		collectionEntry.goToCollectionPage().openCollectionByTitle(collectionTitle);
		int newItemCount = collectionEntry.getTotalItemNumber();
		Assert.assertEquals(newItemCount, itemCount - 1, "Discarded item should not be in item list.");
	}

	@Test(priority = 23, dependsOnMethods = { "createCollection3Authors" })
	public void checkOUCorrectness() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		List<String> ous = editCollection.getOrganisations();
		
		for (String organisation : organisations) {
			Assert.assertTrue(ous.contains(organisation), "Organisation " + organisation + " is missing from list.");
		}
	}

	/**
	 * IMJ-2, IMJ-235, IMJ-59
	 */
	@Test(priority = 24, dependsOnMethods = { "createCollection3Authors" })
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
	@Test(priority = 25, dependsOnMethods = { "createCollection3Authors" })
	public void downloadAllNRU() {
		collectionEntry = new StartPage(driver).goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean downloadAllPossible = collectionEntry.downloadAllPossible();
		Assert.assertTrue(downloadAllPossible, "Non-registered user cannot download collection's items.");
	}

	/**
	 * IMJ-97
	 */
	@Test(priority = 26, dependsOnMethods = { "createCollection3Authors" })
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
