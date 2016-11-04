package edmondScripts;

import java.awt.AWTException;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.util.TimeStamp;

public class SharedFileToAlbumTest extends BaseSelenium {

	private HomePage homePage;
	private CollectionEntryPage collectionEntry;
	
	private String collectionTitle = "Collection with shared items: " + TimeStamp.getTimeStamp();
	private String albumTitle = "Album with shared file: " + TimeStamp.getTimeStamp();
	private String[] itemTitle = {"SamplePDFFile.pdf", "SampleWordFile.docx","SampleXLSXFile.xlsx" };
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
	}
	
	@Test(priority = 1)
	public void loginRU() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}
	
	@Test(priority = 2)
	public void createAlbum() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.goToCreateNewAlbumPage().createAlbum(albumTitle, "");
	}
	
	@Test(priority = 3)
	public void createCollection() {
		NewCollectionPage newCollection = homePage.goToCreateNewCollectionPage();
		collectionEntry = newCollection.createCollectionWithoutStandardMetaDataProfile(collectionTitle, "", getPropertyAttribute(ruGivenName),
				getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
		
		MultipleUploadPage multipleUpload = collectionEntry.uploadContent();
		try {
			for (String title : itemTitle)
				multipleUpload.addFile(getFilepath(title));
		} catch (AWTException e) {}
		

		boolean uploadSuccessful = multipleUpload.startUpload();
		Assert.assertTrue(uploadSuccessful, "Upload was not successful.");
	}
	
	@Test(priority = 4)
	public void shareItem() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.navigateToItemPage().openItemByTitle(itemTitle[0]).shareItem().shareWithAUser().share(getPropertyAttribute(restrUserName), true);
	}
	
	@Test(priority = 5) 
	public void privateSharedPrivateAlbum() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.navigateToItemPage().openItemByTitle(itemTitle[0]).addToActiveAlbum();
		
		int albumItemCount = new StartPage(driver).openActiveAlbumEntryPage().getItemCount();
		Assert.assertEquals(albumItemCount, 1, "Private shared item was not added to private album.");
	}
	
	@Test(priority = 6)
	public void publicSharedPrivateAlbum() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).publish();
		
		new StartPage(driver).navigateToItemPage().openItemByTitle(itemTitle[1]).shareItem().shareWithAUser().share(getPropertyAttribute(restrUserName), true);
		new StartPage(driver).navigateToItemPage().openItemByTitle(itemTitle[1]).addToActiveAlbum();
		
		int albumItemCount = new StartPage(driver).openActiveAlbumEntryPage().getItemCount();
		Assert.assertEquals(albumItemCount, 2, "Public shared item was not added to private album.");
	}
	
	@Test(priority = 7)
	public void publicSharedPublicAlbum() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.goToAlbumPage().openActiveAlbumEntryPage().publish();
		new StartPage(driver).goToAlbumPage().makeAlbumActive(albumTitle);
		
		new StartPage(driver).navigateToItemPage().openItemByTitle(itemTitle[2]).shareItem().shareWithAUser().share(getPropertyAttribute(restrUserName), true);
		new StartPage(driver).navigateToItemPage().openItemByTitle(itemTitle[2]).addToActiveAlbum();
		
		int albumItemCount = new StartPage(driver).openActiveAlbumEntryPage().getItemCount();
		Assert.assertEquals(albumItemCount, 3, "Public shared item was not added to public album.");
	}
	
	@Test(priority = 8)
	public void discardAlbum() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.goToAlbumPage().openActiveAlbumEntryPage().discardAlbum();
	}
	
	@Test(priority = 9)
	public void discardCollection() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).discardCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
	
}
