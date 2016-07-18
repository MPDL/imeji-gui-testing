package spot.scripts.collections;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionsPage;
import spot.pages.StartPage;

public class EditCollectionInformationTest extends BaseSelenium {

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
	}

	@Test
	public void editCollectionInformationTest() {
		CollectionsPage collectionsPage = new StartPage(driver)
				.goToCollectionPage();
		collectionsPage.goToDetailedViewOfRandomCollection();
		
		//
	}
}
