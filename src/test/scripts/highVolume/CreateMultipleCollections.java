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

public class CreateMultipleCollections extends BaseSelenium{

	private Homepage homepage;
	private CollectionsPage collectionsPage;
	private CollectionEntryPage collectionEntry;

	private String genericCollectionTitle = TimeStamp.getTimeStamp() + " multiple collections _ ";
	private String collectionDescription = "default description 123 äüö ? (ß) μ å";
	
	private final int numberOfCollections = 5;

	/**
	 * IMJ-1
	 */
	@Test(priority = 1)
	public void loginUser() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homepage = loginPage.loginAsNotAdmin(getPropertyAttribute(ruUsername), getPropertyAttribute(ruPassword));
	}
	
	@Test(priority = 2)
	public void createCollections() {
		collectionsPage = homepage.goToCollectionPage();
		
		for(int i=0; i<numberOfCollections; i++) {
			String collectionTitle = genericCollectionTitle + i;
			createCollection(collectionTitle);
		}
	}
	
	/**
	 * IMJ-112, IMJ-113, IMJ-86
	 */
	public void createCollection(String collectionTitle) {
		NewCollectionPage newCollectionPage = collectionsPage.createCollection();
		collectionEntry = newCollectionPage.createCollection(collectionTitle, collectionDescription,
				getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		collectionsPage = collectionEntry.goToCollectionPage();
	}
	
	@Test(priority = 3)
	public void deleteCollections() {
		for(int i=0; i<numberOfCollections; i++) {
			String collectionTitle = genericCollectionTitle + i;
			deleteCollection(collectionTitle);
		}
	}
	
	/**
	 * IMJ-96
	 */
	public void deleteCollection(String collectionTitle) {
		collectionEntry = collectionsPage.openCollectionByTitle(collectionTitle);
		collectionsPage = collectionEntry.deleteCollection();
		
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
