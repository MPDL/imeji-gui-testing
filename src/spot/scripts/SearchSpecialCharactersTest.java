package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.SearchResultsPage;
import spot.pages.StartPage;

public class SearchSpecialCharactersTest extends BaseSelenium {

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
	public void searchAUmlaut() {
		search("*�*");
	}
	
	@Test
	public void searchOUmlaut() {
		search("*�*");
	}
	
	@Test
	public void searchUUmlaut() {
		search("*�*");
	}
	
	@Test
	public void searchEszett() {
		search("*�*");
	}
	
	@Test
	public void searchNTilde() {
		search("*�*");
	}
	
	private void search(String searchQuery) {
		SearchResultsPage searchQueryPage = startPage.getSearchComponent().searchFor(searchQuery);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQuery);
		
		int numResults = searchQueryPage.getResultCount();
		Assert.assertTrue(numResults > 0, "Items were not successfully found.");
	}
}
