package test.scripts.basicOutbox;

import org.openqa.selenium.StaleElementReferenceException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.ItemViewPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class MoveItems extends BaseSelenium {

	private AdminHomepage adminHomepage;
	private Homepage homepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle1 = "Collection for moving files (private) 1: " + TimeStamp.getTimeStamp();
	private String collectionTitle2 = "Collection for moving files (private) 2: " + TimeStamp.getTimeStamp();
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
	public void logoutAdmin() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.logout();
	}
	
	/**
	 * IMJ-19
	 */
	@Test(priority = 5)
	public void loginRegistered() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}
	
	private void createCollection(String collectionTitle) {
		NewCollectionPage newCollectionPage = homepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollection(collectionTitle, collectionDescription, 
				getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
	}
	
	private void uploadItem(String collectionTitle, String title) {
		try {
			collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
			collectionEntry = collectionEntry.uploadFile(getFilepath(title));
		}
		catch (StaleElementReferenceException exc) {
			collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
			boolean uploadSuccessful = collectionEntry.findItem(title);
			if (!uploadSuccessful) {
				uploadItem(collectionTitle, title);
			}
		}
	}
	
	/**
	 * IMJ-83
	 */
	@Test(priority = 6)
	public void createFirstCollection() {
		createCollection(collectionTitle1);
	}
	
	/**
	 * IMJ-56
	 */
	@Test(priority = 7)
	public void uploadItemsToFirstCollection() {
		String[] items = {"APC_Springer_Psycholinguistik_201609.xlsx", "APC_Springer_Strafrecht_201608.xlsx", 
				"JR1_deGruyter_deGruyter_Menschheitsgeschichte_2014.txt", "JR1_MAL_MAL_Wissenschaftsgeschichte_2010-2013.tsv",
				"JR2_SpringerProtocols_Springer_Mathematik_2009.csv", "JR2_SpringerProtocols_Springer_Wissenschaftsgeschichte_2009.csv",
				"outbox_doku_apcreports.pdf", "outbox_doku_dateiformate.pdf",
				"SampleCSVFile.csv"};
		for (String item : items) {
			uploadItem(collectionTitle1, item);
		}
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle1);
		int uploadedCount = collectionEntry.getItemListSize();
		Assert.assertEquals(uploadedCount, items.length, "An item was not uploaded");
	}
	
	/**
	 * IMJ-83
	 */
	@Test(priority = 8)
	public void createSecondCollection() {
		createCollection(collectionTitle2);
	}
	
	/**
	 * IMJ-277
	 */
	@Test(priority = 9)
	public void moveItemToPrivateCollection() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle1);
		collectionEntry = collectionEntry.displayMaximalItems();
		ItemViewPage itemView = collectionEntry.openItem("APC_Springer_Psycholinguistik_201609.xlsx");
		itemView = itemView.moveItemToPrivateCollection(collectionTitle2);

		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle2);
		collectionEntry = collectionEntry.displayMaximalItems();
		Assert.assertTrue(collectionEntry.findItem("APC_Springer_Psycholinguistik_201609.xlsx"),
				"Item was not moved to released collection " + collectionTitle2);
		
		itemView = collectionEntry.openItem("APC_Springer_Psycholinguistik_201609.xlsx");
		String collection = itemView.getCollectionTitle();
		Assert.assertEquals(collection, collectionTitle2, "Item was not moved to released collection " + collectionTitle2);
		Assert.assertNotEquals(collection, collectionTitle1, "Item was not moved from private collection " + collectionTitle1);
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle1);
		Assert.assertFalse(collectionEntry.findItem("APC_Springer_Psycholinguistik_201609.xlsx"),
				"Item was not moved from private collection " + collectionTitle1);
	}
	
	@Test(priority = 10)
	public void repeatMovingItemToPrivateCollection() {
		uploadItem(collectionTitle1, "APC_Springer_Psycholinguistik_201609.xlsx");
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle1);
		ItemViewPage itemView = collectionEntry.openItem("APC_Springer_Psycholinguistik_201609.xlsx");
		itemView = itemView.moveItemToPrivateCollection(collectionTitle2);
		
		MessageType messageType = itemView.getPageMessageType();
		Assert.assertEquals(messageType, MessageType.ERROR, "Error message for repeated item is not displayed.");
	}
	
	/**
	 * IMJ-275
	 */
	@Test(priority = 11)
	public void moveSelectedItemsToPrivateCollection() {
		String[] items = {"APC_Springer_Strafrecht_201608.xlsx", "JR1_deGruyter_deGruyter_Menschheitsgeschichte_2014.txt",
				"JR1_MAL_MAL_Wissenschaftsgeschichte_2010-2013.tsv", "JR2_SpringerProtocols_Springer_Mathematik_2009.csv",
				"JR2_SpringerProtocols_Springer_Wissenschaftsgeschichte_2009.csv", "outbox_doku_apcreports.pdf",
				"outbox_doku_dateiformate.pdf", "SampleCSVFile.csv"};
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle1);
		for (String item : items) {
			collectionEntry = collectionEntry.selectItem(item);
		}
		
		collectionEntry = collectionEntry.moveSelectedItemsToPrivateCollection(collectionTitle2);
		
		for (String item : items) {
			Assert.assertFalse(collectionEntry.findItem(item));
		}
		
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle2);
		for (String item : items) {
			Assert.assertTrue(collectionEntry.findItem(item));
		}
	}
	
	@Test(priority = 12)
	public void repeatMovingSelectedItemsToPrivateCollection() {
		uploadItem(collectionTitle1, "APC_Springer_Strafrecht_201608.xlsx");
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle1);
		collectionEntry = collectionEntry.selectItem("APC_Springer_Strafrecht_201608.xlsx");
		collectionEntry = collectionEntry.moveSelectedItemsToReleasedCollection(collectionTitle2);
		
		MessageType messageType = collectionEntry.getPageMessageType();
		Assert.assertEquals(messageType, MessageType.ERROR, "Error message for repeated item is not displayed.");
	}
	
	@Test(priority = 13)
	public void removeCollections() {
		homepage.goToCollectionPage().openCollectionByTitle(collectionTitle1).deleteCollection();
		homepage.goToCollectionPage().openCollectionByTitle(collectionTitle2).deleteCollection();
	}
	
	@AfterClass
	public void logout() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}
}
