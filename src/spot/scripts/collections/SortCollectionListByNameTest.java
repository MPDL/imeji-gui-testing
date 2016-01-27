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

public class SortCollectionListByNameTest extends BaseSelenium {

	private SortingComponent sortingComponent;

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
		CollectionsPage collectionsPage = new StartPage(driver)
				.goToCollectionPage();
		sortingComponent = collectionsPage.getSortingComponent();
	}

	@Test
	public void sortByNameInAscendingOrderTest() {

		sortingComponent.sortBy(SortBy.NAME, Order.ASCENDING);
		Assert.assertEquals(true, false);
	}

	@Test
	public void sortByNameInDescendingOrderTest() {

		sortingComponent.sortBy(SortBy.NAME, Order.DESCENDING);
		Assert.assertEquals(true, false);
	}
}
