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
	public void searchForAlbumsTest() {
		String searchQueryKeyWord = "space";
		searchFor(searchQueryKeyWord, CategoryType.ALBUM);
	}

	@Test
	public void searchForCollectionsTest() {
		
		String searchQueryKeyWord = "test";
		searchFor(searchQueryKeyWord, CategoryType.COLLECTION);
	}

	@Test
	public void searchForItemsTest() {
		
		String searchQueryKeyWord = "awob";		
		searchFor(searchQueryKeyWord, CategoryType.ITEM);		
	}

	@Test
	public void openAdvancedSearchTest() {
		AdvancedSearchPage advancedSearchPage = startPage.getSearchComponent().navigateToAdvancedSearchPage();
		boolean doesSearchQueryMessageAreaExist = advancedSearchPage.doesSearchQueryMessageAreaExist();
		Assert.assertEquals(doesSearchQueryMessageAreaExist, true);
	}
	
	public void searchFor(String searchQueryKeyWord, CategoryType ct) {
		SearchQueryPage searchQueryPage = startPage.getSearchComponent().searchFor(searchQueryKeyWord);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		
		String expectedDisplayText = "";
		
		if (ct == CategoryType.ITEM)
			expectedDisplayText = "items:";
		else if (ct == CategoryType.COLLECTION)
			expectedDisplayText = "collections:";
		else if (ct == CategoryType.ALBUM)
			expectedDisplayText = "albums:";
			
		expectedDisplayText = expectedDisplayText + "(" + searchQueryKeyWord + ")";
		Assert.assertEquals(searchQueryDisplayText, expectedDisplayText);
	}
}
