package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.AlbumPage;
import spot.pages.CollectionsPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;

public class CategorySelectionTest extends BaseSelenium {

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
	public void openStartPageTest() {
		
	}

	@Test
	public void openUploadPageTest() {
		
		SingleUploadPage singleUploadPage = startPage.goToSingleUploadPage();
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
