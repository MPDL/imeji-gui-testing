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
	public void asteriskWildcard() {
		search("IMG_*");
	}
	
	@Test
	public void questionWildcard() {
		search("IMG_????");
	}
	
	@Test
	public void searchItem() {
		search("test");
	}
	
	@Test
	public void searchCollection() {
		search("test", CategoryType.COLLECTION);
	}
	
	@Test
	public void searchAlbum() {
		search("test", CategoryType.ALBUM);
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
		
		int numResults = searchQueryPage.getResultCountCategory();
		Assert.assertTrue(numResults > 0, "Items were not found.");
	}
}
