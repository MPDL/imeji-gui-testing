package test.scripts.basicEdmond;

import java.util.LinkedList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.ItemViewPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.registered.Homepage;
import spot.pages.registered.MetadataTablePage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

//TODO: This Test class has no Test-ID and is not described in the keyword-usage-table. -> Add Test-ID + Description
public class MetadataSelectedItems extends BaseSelenium {

	private Homepage homepage;
	private AdminHomepage adminHomepage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = TimeStamp.getTimeStamp() + " metadata of selected items";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";
	String[] items = {"SamplePDFFile.pdf", "SampleCSVFile.csv", "SampleJPGFile.jpg", "SampleJPGFile2.jpg",
			"SampleTXTFile.txt", "SampleWordFile.docx", "SampleXLSXFile.xlsx"};
	
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
	
	@Test(priority = 5)
	public void loginUser1() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}

	@Test(priority = 6)
	public void createDefaultCollection() {
		NewCollectionPage newCollectionPage = homepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollection(collectionTitle, collectionDescription, 
				getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
	}
	
	private void uploadItem(String title) {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.uploadFile(getFilepath(title));
		collectionEntry = collectionEntry.goToCollectionPage().openCollectionByTitle(collectionTitle);
		
		boolean uploadSuccessful = collectionEntry.findItem(title);
		Assert.assertTrue(uploadSuccessful, "Item " + title + " not among uploads.");
	}

	@Test(priority = 7, dependsOnMethods = { "createDefaultCollection" })
	public void uploadAll() {
		for (String item : items) {
			uploadItem(item);
		}
	}
	
	private MetadataTablePage goToMetadataTable(String[] items) {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		for (String item : items) {
			collectionEntry = collectionEntry.selectItem(item);
		}
		return collectionEntry.editSelectedItems();
	}
	
	private List<String[]> composeArguments(String[] titleItems, String titleKey, String titleValue) {
		List<String[]> arguments = new LinkedList<>();
		for (String titleItem : titleItems) {
			String[] titleItemArgs = {titleItem, titleKey, titleValue};
			arguments.add(titleItemArgs);
		}
		return arguments;
	}
	
	private void validateMetadata(String[] titleItems, String titleKey, String titleValue) {
		for (String titleItem : titleItems) {
			collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
			ItemViewPage itemView = collectionEntry.openItem(titleItem);
			Assert.assertTrue(itemView.metadataPresent(titleKey), "Metadata '" + titleKey + "' is not present in '" + titleItem + "'.");
			Assert.assertTrue(itemView.metadataPresent(titleKey, titleValue),
					"Value '" + titleValue + "' of metadata '" + titleKey + "' is not present in '" + titleItem + "'." );
		}
	}
	
	@Test(priority = 8, dependsOnMethods = { "createDefaultCollection" })
	public void addMetadataSelectedTitle() {
	    //TODO: Create Metadata=Statement "Title" before calling this method
		String[] titleItems = {items[0], items[1], items[2]};
		String titleKey = "Title";
		String titleValue = "Test title \"8.-:;";
		
		List<String[]> arguments = composeArguments(titleItems, titleKey, titleValue);
		MetadataTablePage metadataTable = goToMetadataTable(titleItems);
		metadataTable.addColumn(titleKey);
		collectionEntry = metadataTable.editEntry(arguments);
		collectionEntry.deselectAllSelectedItems();
		validateMetadata(titleItems, titleKey, titleValue);
	}
	
	@Test(priority = 9, dependsOnMethods = { "createDefaultCollection" })
	public void addMetadataSelectedText() {
	    //TODO: Create Metadata=Statement "Text (predefined)" before calling this method
		String[] titleItems = {items[3], items[4], items[5]};
		String titleKey = "Text (predefined)";
		String titleValue = "Value B";
		
		List<String[]> arguments = composeArguments(titleItems, titleKey, titleValue);
		MetadataTablePage metadataTable = goToMetadataTable(titleItems);
		metadataTable.addColumn(titleKey);
		collectionEntry = metadataTable.editEntry(arguments);
		collectionEntry.deselectAllSelectedItems();
		validateMetadata(titleItems, titleKey, titleValue);
	}
	
	@Test(priority = 10, dependsOnMethods = { "createDefaultCollection" })
	public void addMetadataSelectedNumber() {
	    //TODO: Create Metadata=Statement "Number" before calling this method
		String[] titleItems = {items[5], items[6], items[0]};
		String titleKey = "Number";
		String titleValue = "2.718";
		
		List<String[]> arguments = composeArguments(titleItems, titleKey, titleValue);
		MetadataTablePage metadataTable = goToMetadataTable(titleItems);
		metadataTable = metadataTable.addColumn(titleKey);
		collectionEntry = metadataTable.editEntry(arguments);
		collectionEntry.deselectAllSelectedItems();
		validateMetadata(titleItems, titleKey, titleValue);
	}
	
	@Test(priority = 11, dependsOnMethods = { "createDefaultCollection" })
	public void addMetadataSelectedDate() {
	    //TODO: Create Metadata=Statement "Date" before calling this method
		String[] titleItems = {items[2], items[4], items[6]};
		String titleKey = "Date";
		String titleValue = "2018-01-01";
		
		List<String[]> arguments = composeArguments(titleItems, titleKey, titleValue);
		MetadataTablePage metadataTable = goToMetadataTable(titleItems);
		metadataTable = metadataTable.addColumn(titleKey);
		collectionEntry = metadataTable.editEntry(arguments);
		collectionEntry.deselectAllSelectedItems();
		validateMetadata(titleItems, titleKey, titleValue);
	}
	
	@Test(priority = 12, dependsOnMethods = { "createDefaultCollection" })
	public void addMetadataSelectedPerson() {
	    //TODO: Create Metadata=Statement "PERSON 31" before calling this method
		String[] titleItems = {items[2], items[4], items[6]};
		String titleKey = "PERSON 31";
		String titleValue = "MPDL";
		
		List<String[]> arguments = composeArguments(titleItems, titleKey, titleValue);
		MetadataTablePage metadataTable = goToMetadataTable(titleItems);
		metadataTable = metadataTable.addColumn(titleKey);
		collectionEntry = metadataTable.editEntry(arguments);
		collectionEntry.deselectAllSelectedItems();
		validateMetadata(titleItems, titleKey, "MPDL, MPDL (MPDL)");
	}
	
	@Test(priority = 13, dependsOnMethods = { "createDefaultCollection" })
	public void addMetadataSelectedPersonPreselected() {
	    //TODO: Create Metadata=Statement "PERSON 31" before calling this method
		String[] titleItems = {items[1], items[3], items[5]};
		String titleKey = "PERSON 31";
		String titleValue = "MPDL";
		
		List<String[]> arguments = composeArguments(titleItems, titleKey, titleValue);
		MetadataTablePage metadataTable = goToMetadataTable(titleItems);
		metadataTable = metadataTable.addColumn(titleKey);
		collectionEntry = metadataTable.editEntry(arguments);
		collectionEntry.deselectAllSelectedItems();
		validateMetadata(titleItems, titleKey, "MPDL, MPDL (MPDL)");
	}
	
	@Test(priority = 14, dependsOnMethods = { "createDefaultCollection" })
	public void deleteCollection() {
		collectionEntry = homepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		CollectionsPage collectionsPage = collectionEntry.deleteCollection();
		
		boolean collectionPresent = collectionsPage.collectionPresent(collectionTitle);
		Assert.assertFalse(collectionPresent, "Collection was not deleted.");
	}

	@AfterClass
	public void afterClass() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}
	
}
