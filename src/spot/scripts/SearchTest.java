package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
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
	public void searchForItemsTest() {
		String searchQueryKeyWord = "awob";		
		SearchQueryPage searchQueryPage = startPage.getSearchComponent().searchFor(searchQueryKeyWord);
		String searchQueryDisplayText = searchQueryPage.getSearchQueryDisplayText();
		Assert.assertEquals(searchQueryDisplayText, searchQueryKeyWord);	
	}

	@Test
	public void openAdvancedSearchTest() {
		AdvancedSearchPage advancedSearchPage = startPage.getSearchComponent().navigateToAdvancedSearchPage();
		boolean doesSearchQueryMessageAreaExist = advancedSearchPage.doesSearchQueryMessageAreaExist();
		Assert.assertEquals(doesSearchQueryMessageAreaExist, true);
	}
}
