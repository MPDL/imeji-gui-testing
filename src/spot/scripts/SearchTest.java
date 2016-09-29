package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.CategoryType;
import spot.pages.AdvancedSearchPage;
import spot.pages.SearchQueryPage;
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
		String searchQuery = "awobsafh";		
		SearchQueryPage searchQueryPage = startPage.getSearchComponent().searchFor(searchQuery);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCount();
		Assert.assertEquals(numResults, 0, "Non-matching results were displayed.");
	}
	
	@Test
	public void asteriskWildcard() {
		String searchQuery = "IMG_*";		
		SearchQueryPage searchQueryPage = startPage.getSearchComponent().searchFor(searchQuery);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCount();
		Assert.assertTrue(numResults > 0, "Items were not successfully found.");
	}
	
	@Test
	public void questionWildcard() {
		String searchQuery = "IMG_????";		
		SearchQueryPage searchQueryPage = startPage.getSearchComponent().searchFor(searchQuery);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCount();
		Assert.assertTrue(numResults > 0, "Items were not successfully found.");
	}
	
	@Test
	public void searchItem() {
		String searchQuery = "test";		
		SearchQueryPage searchQueryPage = startPage.getSearchComponent().searchByCategory(searchQuery, CategoryType.ITEM);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCount();
		Assert.assertTrue(numResults > 0, "Items were not found.");
	}
	
	@Test
	public void searchCollection() {
		String searchQuery = "test";		
		SearchQueryPage searchQueryPage = startPage.getSearchComponent().searchByCategory(searchQuery, CategoryType.COLLECTION);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCountCategory();
		Assert.assertTrue(numResults > 0, "Collections were not found.");
	}
	
	@Test
	public void searchAlbum() {
		String searchQuery = "test";		
		SearchQueryPage searchQueryPage = startPage.getSearchComponent().searchByCategory(searchQuery, CategoryType.ALBUM);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCountCategory();
		Assert.assertTrue(numResults > 0, "Albums were not found.");
	}
	

	@Test
	public void openAdvancedSearchTest() {
		AdvancedSearchPage advancedSearchPage = startPage.getSearchComponent().navigateToAdvancedSearchPage();
		boolean doesSearchQueryMessageAreaExist = advancedSearchPage.doesSearchQueryMessageAreaExist();
		Assert.assertEquals(doesSearchQueryMessageAreaExist, true);
	}
}
