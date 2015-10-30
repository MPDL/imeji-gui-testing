package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionsPage;

public class ItemHitsPerCollectionViewTest extends BaseSelenium {

	private CollectionContentPage collectionContentPage;
	
	private String errorMessage = "Page is not displayed with selected number of items";

	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
		
		CollectionsPage collectionsPage = getStartPage().goToCollectionPage();

		collectionContentPage = collectionsPage.getPageOfLargestCollection();
		
		collectionContentPage.declareTotalItemNumber();
	}

	@AfterMethod
	public void afterMethod() {
	}

	@BeforeClass
	public void beforeClass() {
	}

	@AfterClass
	public void afterClass() {
	}

	public void changeItemHitNumber(int expectedHitNumber) {
		collectionContentPage.changeItemHitNumberCollectionView(expectedHitNumber);
		
		int actualHitNumber = collectionContentPage.getMediaListSize();
		
		if (collectionContentPage.getTotalItemNumber() > expectedHitNumber)
			Assert.assertEquals(actualHitNumber, expectedHitNumber, errorMessage);
		else 
			Assert.assertEquals(actualHitNumber, collectionContentPage.getTotalItemNumber(), errorMessage);
	}
	
	@Test (priority = 1, invocationCount=5)
	public void changeItemHitNumberPerCollectionViewToSixTest() {
		int expectedHitNumber = 6;
				
		changeItemHitNumber(expectedHitNumber);			
	}
	
	@Test (priority = 2, invocationCount=5)
	public void changeItemHitNumberPerCollectionViewToTwelveTest() {
		int expectedHitNumber = 12;

		changeItemHitNumber(expectedHitNumber);
	}
	
	@Test (priority = 3, invocationCount=5)
	public void changeItemHitNumberPerCollectionViewToEighteenTest() {
		int expectedHitNumber = 18;

		changeItemHitNumber(expectedHitNumber);
	}
	
	@Test (priority = 4, invocationCount=5)
	public void changeItemHitNumberPerCollectionViewToTwentyFourTest() {
		int expectedHitNumber = 24;

		changeItemHitNumber(expectedHitNumber);
	}
	
	@Test (priority = 5, invocationCount=5)
	public void changeItemHitNumberPerCollectionViewToSixtyTest() {
		int expectedHitNumber = 60;

		changeItemHitNumber(expectedHitNumber);
	}
	
	@Test (priority = 6, invocationCount=5)
	public void changeItemHitNumberPerCollectionViewToNinetyTest() {
		int expectedHitNumber = 90;

		changeItemHitNumber(expectedHitNumber);
	}
	
	@Test (priority = 7, invocationCount=5)
	public void changeItemHitNumberPerCollectionViewTo240Test() {
		int expectedHitNumber = 240;

		changeItemHitNumber(expectedHitNumber);
	}
}
