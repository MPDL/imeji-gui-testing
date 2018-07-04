package test.scripts.statements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.ItemViewPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.admin.BrowseStatementsPage;
import spot.pages.registered.EditItemPage;
import spot.pages.registered.EditItemsPage;
import spot.pages.registered.MetadataTablePage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;
import test.base.StatementType;

public class CreateNewStatementTest extends BaseSelenium {

	private Statement statement = new Statement();

	private String collectionTitle = "Collection with default statement: " + TimeStamp.getTimeStamp();
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";
	private List<String> items = new LinkedList<String>();
	
	private AdminHomepage adminHomepage;
	private CollectionEntryPage collectionEntry;
	
	protected static class Statement {
		private StatementType type;
		private String name;
		private boolean isPredefined;
		private String value;
		private String valueEmpty;
		private String valueOverwrite;
		private String oneItemValue;
		private String tableValue;
		private List<String> predefinedValues;
		
		private static StatementType stringToStatement(String statementStr) {
			switch(statementStr) {
				case "StatementType.TEXT":
					return StatementType.TEXT;
				case "StatementType.NUMBER":
					return StatementType.NUMBER;
				case "StatementType.GEOLOCATION":
					return StatementType.GEOLOCATION;
				case "StatementType.DATE":
					return StatementType.DATE;
				case "StatementType.PERSON":
					return StatementType.PERSON;
				case "StatementType.URL":
					return StatementType.URL;
				default:
					return StatementType.TEXT;
			}
		}
		
		private void buildStatement(String type, String statementName, String isPredefined, String statementValue, String statementValueEmpty, 
				String statementValueOverwrite, String statementOneItemValue, String tableValue) {
			this.type = Statement.stringToStatement(type);
			this.name = statementName + ": " + TimeStamp.getTimeStamp();
			this.isPredefined = Boolean.getBoolean(isPredefined);
			this.value = statementValue;
			this.valueEmpty = statementValueEmpty;
			this.valueOverwrite = statementValueOverwrite;
			this.oneItemValue = statementOneItemValue;
			this.tableValue = tableValue;
			buildPredefinedValues();
		}
		
		private void buildPredefinedValues() {
			predefinedValues = new LinkedList<String>();
			predefinedValues.add(value);
			predefinedValues.add(valueEmpty);
		}
	}
	
	private static class ReindexerDataIterator implements Iterator<Object[]> {
		private int index = 0;
		List<Object[]> allData = new ArrayList<>();
		
		public ReindexerDataIterator() {
			
			try (BufferedReader br = new BufferedReader(new FileReader("res/statementData.csv"))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] data = line.split(",");
					allData.add(data);
				}
			}
			catch (IOException exc) {}
		}
		
		@Override
		public boolean hasNext() {
			return index < allData.size() - 1;
		}
		
		@Override
		public Object[] next() {
			return allData.get(index++);
		}
		
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	@DataProvider(name = "statementData")
	public static Iterator<Object[]> reindexerDataProvider() {
		return new ReindexerDataIterator();
	}
	
	@Factory(dataProvider = "statementData")
	public CreateNewStatementTest(String type, String statementName, String isPredefined, String statementValue,
			String statementValueEmpty, String statementValueOverwrite, String statementOneItemValue, String tableValue) {
		statement.buildStatement(type, statementName, isPredefined, statementValue,
				statementValueEmpty, statementValueOverwrite, statementOneItemValue, tableValue);
	}
	
	@Test(priority = 1)
	public void loginAdmin() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
	}
	
	@Test(priority = 2)
	public void initItems() {
		items.add("SamplePDFFile.pdf");
		items.add("SampleTXTFile.txt");
		items.add("SampleTIFFile.tif");
		items.add("SampleMP3File.mp3");
		items.add("SampleSWCFile.swc");
	}
	
	@Test(priority = 3)
	public void createCollection() {
		NewCollectionPage newCollectionPage = adminHomepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollection(collectionTitle, collectionDescription, 
				getPropertyAttribute(adminGivenName), getPropertyAttribute(adminFamilyName), getPropertyAttribute(adminOrganizationName));
	}
	
	@Test(priority = 4)
	public void uploadItems() {
		for (String item : items) {
			uploadItem(item);
		}
	}
	
	private void uploadItem(String title) {
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.uploadFile(getFilepath(title));
		
		boolean uploadSuccessful = collectionEntry.findItem(title);
		Assert.assertTrue(uploadSuccessful, "Item not among uploads.");
	}
	
	/**
	 * IMJ-279
	 */
	@Test(priority = 6)
	public void addMetadataAll() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		
		if (statement.isPredefined) {
			editItems = editItems.addOwnMetadataAll(statement.name, statement.value, statement.predefinedValues);
		}
		else {
			editItems = editItems.addOwnMetadataAll(statement.name, statement.value);
		}
		editItems.hideMessages();
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(statement.name, statement.value);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
	}
	
	@Test(priority = 7)
	public void deleteStatementOneItem() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		ItemViewPage itemView = collectionEntry.openItem("SamplePDFFile.pdf").deleteMetadata(statement.name);
		itemView.hideMessages();
		
		boolean metadataDeleted = !itemView.goToCollectionEntry().metadataDisplayed("SamplePDFFile.pdf", statement.name, statement.value);
		Assert.assertTrue(metadataDeleted, "Metadata was not deleted from item SamplePDFFile.pdf");
	}
	
	/**
	 * IMJ-280
	 */
	@Test(priority = 8)
	public void addMetadataIfEmpty() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.addValueIfEmpty(statement.name, statement.valueEmpty);
		editItems.hideMessages();
		
		Iterator<String> iterator = items.iterator();
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		String currentTitle = iterator.next();
		ItemViewPage itemView = collectionEntry.openItem(currentTitle);
		Assert.assertFalse(itemView.metadataPresent(statement.name, statement.value), currentTitle + ": old metadata still displayed");
		Assert.assertTrue(itemView.metadataPresent(statement.name, statement.valueEmpty), currentTitle + ": new metadata not displayed");
		itemView.goToCollectionEntry();
		
		while (iterator.hasNext()) {
			currentTitle = iterator.next();
			itemView = collectionEntry.openItem(currentTitle);
			Assert.assertFalse(itemView.metadataPresent(statement.name, statement.valueEmpty), currentTitle + ": new metadata is displayed");
			Assert.assertTrue(itemView.metadataPresent(statement.name, statement.value), currentTitle + ": old metadata not displayed");
			itemView.goToCollectionEntry();
		}
	}
	
	/**
	 * IMJ-281
	 */
	@Test(priority = 9)
	public void addMetadataOverwrite() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemsPage editItems = collectionEntry.editAllItems();
		editItems = editItems.overwriteAllValues(statement.name, statement.valueOverwrite);
		editItems.hideMessages();
		
		collectionEntry = editItems.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean metadataDisplayedAll = collectionEntry.metadataDisplayedAll(statement.name, statement.valueOverwrite);
		Assert.assertTrue(metadataDisplayedAll, "Metadata is not displayed on item page.");
	}
	
	@Test(priority = 10)
	public void oneItemWithNewStatement() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		EditItemPage editItem = collectionEntry.openItem(items.get(0)).editItem();
		ItemViewPage itemView = editItem.addMetadata(statement.name, statement.oneItemValue);
		itemView.hideMessages();
		
		boolean newMetadataDisplayed = itemView.goToCollectionEntry().metadataDisplayed(items.get(0), statement.name, statement.oneItemValue);
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean oldMetadataDisplayed = collectionEntry.metadataDisplayed(items.get(0), statement.name, statement.valueOverwrite);
		
		Assert.assertTrue(newMetadataDisplayed, "New metadata is not displayed on item " + items.get(0));
		Assert.assertTrue(oldMetadataDisplayed, "Old metadata is not displayed on item " + items.get(0));
	}
	
	@Test(priority = 11)
	public void metadataTable() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionEntry = collectionEntry.selectItem(items.get(1));
		collectionEntry = collectionEntry.selectItem(items.get(2));
		MetadataTablePage metadataTable = collectionEntry.editSelectedItems();
		
		// pass a list of Strings as argument
		List<String[]> arguments = new LinkedList<>();
		String[] args1 = {items.get(1), statement.name, statement.tableValue};
		String[] args2 = {items.get(2), statement.name, statement.tableValue};
		arguments.add(args1);
		arguments.add(args2);
		collectionEntry = metadataTable.editEntry(arguments);
		collectionEntry.hideMessages();
		
		ItemViewPage itemView = collectionEntry.openItem(items.get(1));
		Assert.assertFalse(itemView.metadataPresent(statement.name, statement.valueOverwrite), items.get(1) + ": old metadata still displayed");
		Assert.assertTrue(itemView.metadataPresent(statement.name, statement.tableValue), items.get(1) + ": new metadata not displayed");
		collectionEntry = itemView.goToCollectionEntry();
		
		itemView = collectionEntry.openItem(items.get(2));
		Assert.assertFalse(itemView.metadataPresent(statement.name, statement.valueOverwrite), items.get(2) + ": old metadata still displayed");
		Assert.assertTrue(itemView.metadataPresent(statement.name, statement.tableValue), items.get(2) + ": new metadata not displayed");
		collectionEntry = itemView.goToCollectionEntry();
	}
	
	@Test(priority = 12)
	public void deleteStatementValues() {
		for (String item : items) {
			adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
			collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
			ItemViewPage itemView = collectionEntry.openItem(item).deleteMetadata(statement.name);
			itemView.hideMessages();
			collectionEntry = itemView.goToCollectionEntry();
			
			boolean metadataDeleted = !collectionEntry.metadataDisplayed(item, statement.name);
			Assert.assertTrue(metadataDeleted, "Metadata was not deleted from item " + item);
		}
	}
	
	@Test(priority = 13)
	public void deleteCollection() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		collectionEntry = adminHomepage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		CollectionsPage collectionsPage = collectionEntry.deleteCollection();
		
		boolean collectionPresent = collectionsPage.collectionPresent(collectionTitle);
		Assert.assertFalse(collectionPresent, "Collection was not deleted.");
	}
	
	@Test(priority = 14)
	public void deleteStatement() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		AdministrationPage adminPage = adminHomepage.goToAdminPage();
		BrowseStatementsPage allStatements = adminPage.deleteStatement(statement.name);
		
		boolean statementPresent = allStatements.isStatementPresent(statement.name);
		Assert.assertFalse(statementPresent, "Statement is still in list.");
	}
	
	@AfterClass
	public void logout() {
		adminHomepage = (AdminHomepage) new StartPage(driver).goToHomepage(adminHomepage);
		adminHomepage.logout();
	}
}
