package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.AlbumPage;
import spot.pages.CollectionsPage;
import spot.pages.DetailedItemViewPage;
import spot.pages.StartPage;

public class CategorySelectionTest extends BaseSelenium {

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
	public void openStartPageTest() {
		Assert.assertEquals(true, false);
	}

	@Test
	public void openUploadPageTest() {
		
//		startPage.goToUploadPage();
		Assert.assertEquals(true, false);
	}

	@Test
	public void openItemPageTest() {
		DetailedItemViewPage itemPage = startPage.navigateToItemPage();

		String actualSiteContentHeadline = itemPage.getSiteContentHeadline();

		Assert.assertEquals(actualSiteContentHeadline, "Inhalte", "Site content headline is not correct.");
	}

	@Test
	public void openCollectionPageTest() {
		CollectionsPage collectionsPage = startPage.goToCollectionPage();

		String actualSiteContentHeadline = collectionsPage.getSiteContentHeadline();

		Assert.assertEquals(actualSiteContentHeadline, "Sammlungen", "Site content headline is not correct.");
	}

	@Test
	public void openAlbumPageTest() {
		AlbumPage albumPage = startPage.goToAlbumPage();
		
		String actualSiteContentHeadline = albumPage.getSiteContentHeadline();

		Assert.assertEquals(actualSiteContentHeadline, "Alben", "Site content headline is not correct.");
	}
}
