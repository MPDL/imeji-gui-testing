package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionsPage;
import spot.pages.DetailedItemViewPage;
import spot.pages.StartPage;

public class DownloadFileTest extends BaseSelenium {

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
	}
	
	@Test 
	public void downloadFileTest() {
		CollectionsPage collectionPage = new StartPage(driver).goToCollectionPage();
		CollectionContentPage collectionContentPage = collectionPage.getPageOfLargestCollection();
		
		DetailedItemViewPage detailedItemViewPage = collectionContentPage.downloadFirstItemInList();	
		boolean isDownloadPossible = detailedItemViewPage.isDownloadPossible();		
		
		Assert.assertTrue(isDownloadPossible, "Download of a item seems not to be possible. Reason: Download Button most probably not displayed/enabled");	
	}
	
	@AfterClass
	public void afterClass() {
		navigateToStartPage();
	}
	
}
