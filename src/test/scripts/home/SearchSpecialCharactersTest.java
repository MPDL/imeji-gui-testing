package test.scripts.home;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import spot.pages.SearchResultsPage;
import spot.pages.StartPage;
import test.base.BaseSelenium;

/**
 * Testcase #13 (Simple Search, not private mode, NRU, special search terms)
 */
public class SearchSpecialCharactersTest extends BaseSelenium {

private StartPage startPage;
	
	@BeforeMethod
	public void beforeMethodTest() {
		navigateToStartPage();
	}

	@BeforeClass
	public void beforeClassTest() {
		startPage = new StartPage(driver);
	}

	@Test
	public void searchAUmlaut() {
		search("*ä*");
	}
	
	@Test
	public void searchOUmlaut() {
		search("*ö*");
	}
	
	@Test
	public void searchUUmlaut() {
		search("*ü*");
	}
	
	@Test
	public void searchEszett() {
		search("*ß*");
	}
	
	@Test
	public void searchNTilde() {
		search("*ñ*");
	}
	
	private void search(String searchQuery) {
		SearchResultsPage searchQueryPage = startPage.getSearchComponent().searchFor(searchQuery);
		String searchQueryDisplayText = searchQueryPage.getItemSearchQuery();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCount();
		Assert.assertTrue(numResults > 0, "Items were not successfully found.");
	}
}
