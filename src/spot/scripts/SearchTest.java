package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.CategoryType;
import spot.pages.AdvancedSearchPage;
import spot.pages.SearchResultsPage;
import spot.pages.StartPage;

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
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCount();
		Assert.assertEquals(numResults, 0, "Non-matching results were displayed.");
	}
	
	@Test
	public void inexistentCollectionTest() {
		String searchQuery = "aseirh47382whsuiofidfbhuoidhfid";
		SearchResultsPage searchQueryPage = startPage.getSearchComponent().searchByCategory(searchQuery, CategoryType.COLLECTION);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCountCollection();
		Assert.assertEquals(numResults, 0, "Non-matching results were displayed.");
	}
	
	@Test
	public void inexistentAlbumTest() {
		String searchQuery = "ajaz89w4z583wrfehigfd8ifjidj";
		SearchResultsPage searchQueryPage = startPage.getSearchComponent().searchByCategory(searchQuery, CategoryType.ALBUM);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCountAlbum();
		Assert.assertEquals(numResults, 0, "Non-matching results were displayed.");
	}
	
	@Test
	public void asteriskWildcard() {
		search("Sample*");
	}
	
	@Test
	public void questionWildcard() {
		search("Sample???File.jpg");
	}
	
	@Test
	public void searchItem() {
		search("File");
	}
	
	@Test
	public void searchCollection() {
		search("published", CategoryType.COLLECTION);
	}
	
	@Test
	public void searchAlbum() {
		search("album", CategoryType.ALBUM);
	}

	@Test
	public void openAdvancedSearchTest() {
		AdvancedSearchPage advancedSearchPage = startPage.getSearchComponent().navigateToAdvancedSearchPage();
		boolean doesSearchQueryMessageAreaExist = advancedSearchPage.doesSearchQueryMessageAreaExist();
		Assert.assertEquals(doesSearchQueryMessageAreaExist, true);
	}
	
	private void search(String searchQuery) {
		SearchResultsPage searchQueryPage = startPage.getSearchComponent().searchFor(searchQuery);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCount();
		Assert.assertTrue(numResults > 0, "Items were not successfully found.");
	}
	
	private void search(String searchQuery, CategoryType category) {
		SearchResultsPage searchQueryPage = startPage.getSearchComponent().searchByCategory(searchQuery, category);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCountCollection();
		Assert.assertTrue(numResults > 0, "Items were not found.");
	}
}
