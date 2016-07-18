package spot.scripts.collections;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.SortingComponent;
import spot.components.SortingComponent.Order;
import spot.components.SortingComponent.SortBy;
import spot.pages.CollectionsPage;
import spot.pages.StartPage;

public class SortCollectionListByStatusTest extends BaseSelenium {

	private SortingComponent sortingComponent;

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		CollectionsPage collectionsPage = new StartPage(driver)
				.goToCollectionPage();
		sortingComponent = collectionsPage.getSortingComponent();
	}

	@Test
	public void sortByStatusInAscendingOrderTest() {

		sortingComponent.sortBy(SortBy.STATUS, Order.ASCENDING);
		Assert.assertEquals(true, false);
	}

	@Test
	public void sortByStatusInDescendingOrderTest() {

		sortingComponent.sortBy(SortBy.STATUS, Order.DESCENDING);
		Assert.assertEquals(true, false);
	}
}
