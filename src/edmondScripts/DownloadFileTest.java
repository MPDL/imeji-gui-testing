package edmondScripts;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionsPage;
import spot.pages.StartPage;

public class DownloadFileTest extends BaseSelenium {

	@BeforeMethod
	public void beforeMethod() {
	}

	@AfterMethod
	public void afterMethod() {
	}

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
	}

	@AfterClass
	public void afterClass() {
	}
	
	@Test (groups="dataUploadWithoutMetaDataProfile")
	public void downloadFileTest() {
		CollectionsPage collectionPage = new StartPage(driver).goToCollectionPage();
		CollectionContentPage collectionContentPage = collectionPage.getPageOfLargestCollection();
	}
}
