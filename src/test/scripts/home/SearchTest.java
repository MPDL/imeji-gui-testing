package test.scripts.home;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.components.SearchComponent.CategoryType;
import spot.pages.AdvancedSearchPage;
import spot.pages.SearchResultsPage;
import spot.pages.StartPage;
import test.base.BaseSelenium;

public class SearchTest extends BaseSelenium {

	private StartPage startPage;
	
	@BeforeMethod
	public void beforeMethodTest() {
		navigateToStartPage();
	}

	@BeforeClass
	public void beforeClassTest() {
		super.setup();
		startPage = new StartPage(driver);
	}

	@Test
	public void inexistentItemTest() {
		String searchQuery = "awobsafhtui5we5t57zuo77izufh";		
		SearchResultsPage searchQueryPage = startPage.getSearchComponent().searchFor(searchQuery);
		String searchQueryDisplayText = searchQueryPage.getItemSearchQuery();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCount();
		Assert.assertEquals(numResults, 0, "Non-matching results were displayed.");
	}
	
	@Test
	public void inexistentCollectionTest() {
		String searchQuery = "aseirh47382whsuiofidfbhuoidhfid";
		SearchResultsPage searchQueryPage = startPage.getSearchComponent().searchByCategory(searchQuery, CategoryType.COLLECTION);
		String searchQueryDisplayText = searchQueryPage.getCollectionSearchQuery();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCount();
		Assert.assertEquals(numResults, 0, "Non-matching results were displayed.");
	}
	
	@Test
	public void asteriskWildcard() {
		searchItem("Sample*");
	}
	
	@Test
	public void questionWildcard() {
		searchItem("T???r.jpg");
	}
	
	@Test
	public void searchItem() {
		searchItem("File");
	}
	
	@Test
	public void searchCollection() {
		searchCollection("collection");
	}

	@Test
	public void openAdvancedSearchTest() {
		AdvancedSearchPage advancedSearchPage = startPage.getSearchComponent().navigateToAdvancedSearchPage();
		boolean doesSearchQueryMessageAreaExist = advancedSearchPage.doesSearchQueryMessageAreaExist();
		Assert.assertEquals(doesSearchQueryMessageAreaExist, true);
	}
	
	private void searchItem(String searchQuery) {
		SearchResultsPage searchQueryPage = startPage.getSearchComponent().searchFor(searchQuery);
		String searchQueryDisplayText = searchQueryPage.getItemSearchQuery();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCount();
		Assert.assertTrue(numResults > 0, "Items were not successfully found.");
	}
	
	private void searchCollection(String searchQuery) {
		SearchResultsPage searchQueryPage = startPage.getSearchComponent().searchByCategory(searchQuery, CategoryType.COLLECTION);
		String searchQueryDisplayText = searchQueryPage.getCollectionSearchQuery();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCount();
		Assert.assertTrue(numResults > 0, "Collections were not found.");
	}
}
