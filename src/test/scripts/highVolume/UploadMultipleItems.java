package test.scripts.highVolume;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.registered.Homepage;
import spot.pages.registered.NewCollectionPage;
import spot.util.TimeStamp;
import test.base.BaseSelenium;

public class UploadMultipleItems extends BaseSelenium {
	
	private final int timeOutInSeconds = 400;

	private Homepage homepage;
	private CollectionEntryPage collectionEntry;

	private String collectionTitle = TimeStamp.getTimeStamp() + " collection with multiple items";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";

	private final int itemCount = 524;
	
	/**
	 * IMJ-1
	 */
	@Test(priority = 1)
	public void loginUser() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}

	/**
	 * IMJ-112, IMJ-113, IMJ-86
	 */
	@Test(priority = 2)
	public void createCollection() {
		NewCollectionPage newCollectionPage = homepage.goToCreateNewCollectionPage();
		collectionEntry = newCollectionPage.createCollection(collectionTitle, collectionDescription,
				getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
	}
	
	@Test(priority = 3)
	public void uploadItems() {
		//TODO: Commit/Push the res/MultipleItems directory with appropriate items.
		String filespaths = getPathsOfAllFilesInDirectory("MultipleItems");
		
		collectionEntry.setWaitingTime(timeOutInSeconds);
		collectionEntry.uploadFile(filespaths);
		
		Assert.assertEquals(collectionEntry.getTotalItemNumber(), itemCount, "Not all items have been uploaded.");
	}

	/**
	 * IMJ-96
	 */
	@Test(priority = 4)
	public void deleteCollection() {
		collectionEntry.getActionComponent().setWaitingTime(timeOutInSeconds);
		CollectionsPage collectionsPage = collectionEntry.deleteCollection();
		
		boolean collectionPresent = collectionsPage.collectionPresent(collectionTitle);
		Assert.assertFalse(collectionPresent, "Collection was not deleted.");
	}
	
	/**
	 * IMJ-2
	 */
	@AfterClass
	public void afterClass() {
		homepage = new StartPage(driver).goToHomepage(homepage);
		homepage.logout();
	}
	
}
