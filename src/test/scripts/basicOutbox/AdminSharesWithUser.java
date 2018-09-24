package test.scripts.basicOutbox;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.components.MessageComponent.MessageType;
import spot.pages.AdvancedSearchPage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.ItemViewPage;
import spot.pages.LoginPage;
import spot.pages.SearchResultsPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.EditCollectionPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.pages.registered.SharePage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

/**
 * Testcase #9 + #10
 * 
 * @author helk
 *
 */
public class AdminSharesWithUser extends BaseSelenium {
	
	private Homepage homepage;
	private AdminHomepage adminHomepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " Outbox private mode";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";

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
	 * IMJ-191
	 */
	@Test(priority = 2)
	public void enableAutoSuggestions() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.goToAdminPage().setAutosuggestionMP();
	}

	/**
	 * IMJ-240
	 */
	@Test(priority = 3)
	public void enableListView() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.goToAdminPage().enableListView();
	}
	
	@Test(priority = 4)
	public void forceListView() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openFirstCollection();
		collectionEntry = collectionEntry.enableListView();
	}
	
	@Test(priority = 5)
	public void logoutAdmin() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.logout();
	}

	/**
	 * IMJ-19
	 */
	@Test(priority = 6)
	public void loginRU() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}

	/**
	 * IMJ-83
	 */
	@Test(priority = 7)
	public void createCollection() {
		NewCollectionPage newCollection = homepage.goToCreateNewCollectionPage();
		collectionEntry = newCollection.createCollection(collectionTitle, collectionDescription,
				"", "QA Team MPDL", getPropertyAttribute(ruOrganizationName));
		
		MessageType messageType = collectionEntry.getPageMessageType();
		Assert.assertNotEquals(messageType, MessageType.NONE, "No message was displayed.");
		Assert.assertEquals(messageType, MessageType.SUCCESS, "Success message was not displayed.");
	}

	/**
	 * IMJ-133
	 */
	@Test(priority = 8)
	public void uploadLogo() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		editCollection.addLogo(getFilepath("SampleJPGFile.jpg"));
		collectionEntry = editCollection.submitChanges();
		
		boolean hasLogo = collectionEntry.hasLogo();
		Assert.assertTrue(hasLogo, "Logo is not displayed.");
	}

	/**
	 * IMJ-246
	 */
	@Test(priority = 8)
	public void editDescription() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditCollectionPage editCollection = collectionEntry.editInformation();
		collectionDescription += " (revised)";
		editCollection.editDescription(collectionDescription);
		collectionEntry = editCollection.submitChanges();
		
		String actual = collectionEntry.getDescription();
		Assert.assertEquals(actual, collectionDescription, "Description was not changed.");
	}
	
	// IMJ-131
	private void uploadItem(String title) {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.uploadFile(getFilepath(title));
		// to avoid ElementNotVisibleException
		try { Thread.sleep(2500);} catch (InterruptedException e) {}
		
		boolean uploadSuccessful = collectionEntry.findItem(title);
		Assert.assertTrue(uploadSuccessful, "Item not among uploads.");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 9)
	public void uploadTSV() {
		uploadItem("JR1_MAL_MAL_Wissenschaftsgeschichte_2010-2013.tsv");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 10)
	public void uploadExcel() {
		uploadItem("APC_Springer_Psycholinguistik_201609.xlsx");
	}

	/**
	 * IMJ-56
	 */
	@Test(priority = 11)
	public void uploadText() {
		uploadItem("JR1_deGruyter_deGruyter_Menschheitsgeschichte_2014.txt");
	}

	/**
	 * IMJ-196
	 */
	@Test(priority = 12)
	public void shareReadRU() {
		String user2Name = getPropertyAttribute(restrFamilyName) + ", " + getPropertyAttribute(restrGivenName);
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		SharePage sharePage = collectionEntry.share();
		sharePage = sharePage.share(false, false, getPropertyAttribute(restrUsername), true, false, false);
		
		collectionEntry = sharePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		sharePage = collectionEntry.share();
		
		boolean user2InSharedList = sharePage.isSharedPersonInList(user2Name);
		Assert.assertTrue(user2InSharedList, "Second user is not present in shared list.");
		
		boolean grantsCorrect = sharePage.checkUserGrantSelections(false, user2Name, true, false, false);
		Assert.assertTrue(grantsCorrect, "User grants are not correct.");
	}

	/**
	 * IMJ-46
	 */
	@Test(priority = 13)
	public void openCollection() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean shareVisible = collectionEntry.shareIconVisible();
		Assert.assertTrue(shareVisible, "Share icon is not displayed.");
	}

	/**
	 * IMJ-2
	 */
	@Test(priority = 14)
	public void logout() {
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
	 * IMJ-47
	 */
	@Test(priority = 16)
	public void checkShareIcon() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean shareVisible = collectionEntry.shareIconVisible();
		Assert.assertTrue(shareVisible, "Share icon is not displayed.");
	}
	
	// IMJ-183
	private void advancedSearch(String term) {
		AdvancedSearchPage advancedSearch = homepage.goToAdvancedSearch();
		SearchResultsPage searchResults = advancedSearch.advancedSearch(term);
		Assert.assertTrue(searchResults.getResultCount() > 0, "Valid result is not displayed.");
	}

	/**
	 * IMJ-183
	 */
	@Test(priority = 17)
	public void searchTSV() {
		advancedSearch("Antioxidants & Redox Signaling");
	}

	/**
	 * IMJ-183
	 */
	@Test(priority = 18)
	public void searchExcel() {
		advancedSearch("10.3758/s13414-016-1206-4");
	}

	/**
	 * IMJ-183 - TODO will be tested again after milestone 4.2
	 */
	public void searchText() {
		advancedSearch("Botanica Marina (botm)");
	}
	
	// IMJ-234
	private void downloadItem(String title) {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem(title);
		boolean canDownload = itemView.isDownloadPossible();
		Assert.assertTrue(canDownload, "Item cannot be downloaded.");
		
		itemView.download();
	}

	/**
	 * IMJ-234
	 */
	@Test(priority = 20)
	public void downloadTSV() {
		downloadItem("JR1_MAL_MAL_Wissenschaftsgeschichte_2010-2013.tsv");
	}

	/**
	 * IMJ-234
	 */
	@Test(priority = 21)
	public void downloadExcel() {
		downloadItem("APC_Springer_Psycholinguistik_201609.xlsx");
	}

	/**
	 * IMJ-234
	 */
	@Test(priority = 22)
	public void downloadText() {
		downloadItem("JR1_deGruyter_deGruyter_Menschheitsgeschichte_2014.txt");
	}
	
	/**
	 * IMJ-2
	 */
	@Test(priority = 23)
	public void logoutRestricted() {
		homepage.logout();
	}

	/**
	 * IMJ-96
	 */
	@Test(priority = 24)
	public void deleteCollection() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		CollectionsPage collectionsPage = collectionEntry.deleteCollection();
		
		boolean collectionPresent = collectionsPage.collectionPresent(collectionTitle);
		Assert.assertFalse(collectionPresent, "Collection was not deleted.");
	}

	/**
	 * IMJ-2
	 */
	@AfterClass
	public void logoutRU() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}
}
