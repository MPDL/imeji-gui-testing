package edmondScripts;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.ActionComponent;
import spot.components.ActionComponent.ActionType;
import spot.pages.AlbumEntryPage;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewAlbumPage;
import spot.pages.notAdmin.CreateNewCollectionPage;
import spot.util.TimeStamp;

public class CreateAlbumTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
	private AlbumEntryPage albumEntryPage;
	private MultipleUploadPage multipleUploadPage;
		
	private String collectionTitle;
	
	private HashMap<String, String> files;

	@BeforeClass
	public void beforeClass()  throws AWTException {
		super.setup();
		navigateToStartPage();		
	
		files = new HashMap<String, String>();
		files.put("SampleJPGFile.jpg", getFilepath("SampleJPGFile.jpg"));
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));

	}
	
	@Test
	public void createAlbumTest() throws AWTException {
		CreateNewAlbumPage createNewAlbumPage = adminHomePage.goToCreateNewAlbumPage();		
		
		String albumTitle = "Test Album " +	TimeStamp.getTimeStamp(); 
		String albumDescription = "This album is created for testing purposes.";
		
		albumEntryPage = createNewAlbumPage.createAlbum(albumTitle, albumDescription);
		
		String actualInfoMessage = albumEntryPage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Album created successfully";
		
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Album probably couldn't be be created.");
		
		addPublishedFileToAlbum();
		
	}

	private void addPublishedFileToAlbum() throws AWTException {
		CollectionsPage collectionPage = albumEntryPage.goToCollectionPage();
		
		CollectionContentPage releasedCollectionContentPage = collectionPage.openSomePublishedCollection();
		if (releasedCollectionContentPage == null) {
			collectionTitle = "A collection for testing 'Create Album with a published file'.";
			createAndReleaseCollection(collectionTitle);
			releasedCollectionContentPage = adminHomePage.goToCollectionPage().openSomePublishedCollection();
		}
		
		releasedCollectionContentPage.addFirstItemToAlbum();
		
		String actualInfoMessage = releasedCollectionContentPage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "1 items added to active album.";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Item probably couldn't be added to active album.");
		
	}

	@AfterClass
	public void afterClass() {
		
		adminHomePage.logout();
	}
	
	private void createAndReleaseCollection(String collectionTitle) throws AWTException {
		CreateNewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();

		String collectionDescription = "This collection is for testing purposes.";

		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		multipleUploadPage = collectionEntryPage.uploadContent();

		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}

		multipleUploadPage.startUpload();
		
		ActionComponent actionComponent = multipleUploadPage.getActionComponent();
		actionComponent.doAction(ActionType.PUBLISH);
	}
}
