package edmondScripts;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.FilterComponent.FilterOptions;
import spot.pages.AlbumEntryPage;
import spot.pages.AlbumPage;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.DiscardedAlbumEntryPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewAlbumPage;
import spot.pages.notAdmin.CreateNewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class DiscardAlbumTest extends BaseSelenium {

	private LoginPage loginPage;
	private HomePage homePage;
	private AlbumEntryPage albumEntryPage;
	private CollectionsPage collectionPage;
	
	private String albumTitle;
	private String albumDescription;
	private String collectionTitle;
	private HashMap<String, String> files;
	
	@BeforeClass
	public void beforeClass() throws AWTException {
		super.setup();
		enableAlbumsAsAdmin();
		logInAsNotAdmin();
	}
	
	private void enableAlbumsAsAdmin() {
		loginPage = new StartPage(driver).openLoginForm();
		AdminHomePage adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotAdminUserName),
				getPropertyAttribute(spotAdminPassWord));
		adminHomePage.goToAdminPage().enableAlbums();
		adminHomePage.logout();
	}
	
	private void logInAsNotAdmin() {
		loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}
	
	@Test(priority = 1)
	public void createAndPublishAlbum() throws AWTException {
		CreateNewAlbumPage createNewAlbumPage = homePage.goToCreateNewAlbumPage();
		
		albumTitle = "Published album to be discarded: " + TimeStamp.getTimeStamp();
		albumDescription = "For testing purposes";
		
		albumEntryPage = createNewAlbumPage.createAlbum(albumTitle, albumDescription);
		
		String siteContentHeadline = albumEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(albumTitle), "Album title not correct");
	}
	
	@Test(priority = 2)
	private void addPublishedFileToAlbum() throws AWTException {
		collectionPage = albumEntryPage.goToCollectionPage();
		
		CollectionContentPage releasedCollectionContentPage = collectionPage.openSomePublishedCollection();
		if (releasedCollectionContentPage == null) {
			collectionTitle = "A collection for testing discarding albums: " + TimeStamp.getTimeStamp();
			createAndReleaseCollection(collectionTitle);
			releasedCollectionContentPage = homePage.goToCollectionPage().openSomePublishedCollection();
		}
		releasedCollectionContentPage.addFirstItemToAlbum();
		homePage = new StartPage(driver).goToHomePage(homePage);
		
	}
	
	private void createAndReleaseCollection(String collectionTitle) throws AWTException {
		homePage = collectionPage.goToHomePage(homePage);
		CreateNewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();

		String collectionDescription = "This collection is for testing purposes.";

		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		MultipleUploadPage multipleUploadPage = collectionEntryPage.uploadContent();
		
		files = new HashMap<String, String>();
		files.put("SampleJPGFile.jpg", "file:" + getClass().getResource("/SampleJPGFile.jpg").getPath());
		
		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}

		multipleUploadPage.startUpload();
		multipleUploadPage.publishCollection();
	}
	
	@Test(priority = 3)
	public void releaseAlbum() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		albumEntryPage = homePage.openActiveAlbumEntryPage();
		albumEntryPage = albumEntryPage.publish();
	}
	
	@Test(priority = 4)
	public void discardAlbumTest() {
		DiscardedAlbumEntryPage discardedAlbumEntryPage = albumEntryPage.discardAlbum();
		AlbumPage albumPage = discardedAlbumEntryPage.goToAlbumPage();
		boolean albumShowsUpInAlbums = albumPage.isAlbumPresent(albumTitle);
		Assert.assertFalse(albumShowsUpInAlbums);
		
		// need to hover over filter options first
		WebElement filter = driver.findElement(By.className("fa-filter"));
		filter.click();
		albumPage.getFilterComponent().filter(FilterOptions.ONLY_DISCARDED);
		albumPage = PageFactory.initElements(driver, AlbumPage.class);
		
		boolean albumShowsUpInDiscarded = albumPage.isAlbumPresent(albumTitle);
		Assert.assertTrue(albumShowsUpInDiscarded);
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		if (collectionTitle != null)
			homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).discardCollection();
		homePage.logout();
	}
	
	
}
