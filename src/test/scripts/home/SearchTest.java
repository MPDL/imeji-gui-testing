package test.scripts.home;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import spot.components.SearchComponent.CategoryType;
import spot.pages.AdvancedSearchPage;
import spot.pages.LoginPage;
import spot.pages.SearchResultsPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomepage;
import test.base.BaseSelenium;

/**
 * Testcase #13 (Simple Search, not private mode, NRU)
 */
public class SearchTest extends BaseSelenium {

	private StartPage startPage;
	
	@BeforeMethod
	public void beforeMethodTest() {
		navigateToStartPage();
	}

	@BeforeClass
	public void beforeClassTest() {
		switchPrivateMode(false);
		startPage = new StartPage(driver);
	}
	
	//FIXME: Create an item or a collection containing the special characters before searching for them

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
		searchItem("Des???.jpg");
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
		boolean advancedSearchDisplayed = advancedSearchPage.advancedSearchDisplayed();
		Assert.assertEquals(advancedSearchDisplayed, true);
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
	
	private void switchPrivateMode(boolean privateMode) {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		AdminHomepage adminHomepage = loginPage.loginAsAdmin(getPropertyAttribute(adminUsername), getPropertyAttribute(adminPassword));
		if (privateMode)
			adminHomepage.goToAdminPage().enablePrivateMode();
		else 
			adminHomepage.goToAdminPage().disablePrivateMode();
		adminHomepage.logout();
	}
}
