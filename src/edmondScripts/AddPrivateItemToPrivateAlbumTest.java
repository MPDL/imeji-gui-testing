package edmondScripts;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.FilterComponent;
import spot.components.FilterComponent.FilterOptions;
import spot.pages.CollectionEntryPage;
import spot.pages.CollectionsPage;
import spot.pages.DetailedItemViewPage;
import spot.pages.LoginPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;
import spot.util.TimeStamp;

public class AddPrivateItemToPrivateAlbumTest extends BaseSelenium {

	private AdminHomePage adminHomePage;
	
	public final String collectionTitle = "Private test collection with default meta data profile: " + TimeStamp.getTimeStamp();
	
	@Test
	public void addPrivateItemToPrivateAlbum() {
		
		
	}
	
	private void createPrivateCollection() throws AWTException {

		String collectionDescription = "This collection has a meta data profile. It is private.";

		HashMap<String, String> files = new HashMap<String, String>();
		files.put("SampleTIFFFile.tiff", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SampleTIFFFile.tiff");
		
		CreateNewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();

		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollectionWithStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		uploadFiles(files);
	}

	public void uploadFiles(HashMap<String, String> files) throws AWTException {
		navigateToStartPage();
		
		for (Map.Entry<String, String> file : files.entrySet()) {
			String fileTitle = file.getKey();
			String filePath = file.getValue();
			
			uploadFile(fileTitle, filePath);
		}
		navigateToStartPage();
	}
	
	
	private void uploadFile(String fileTitle, String filePath) throws AWTException {
		SingleUploadPage singleUploadPage = adminHomePage.goToSingleUploadPage();
		
		singleUploadPage.uploadAndFillMetaData(filePath, collectionTitle);	
	}
	
	@BeforeClass
	public void beforeClass() throws AWTException {		
		navigateToStartPage();		
	
		new StartPage(driver).selectLanguage(englishSetup);
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));		
		
		createPrivateCollection();
	}
	
	@AfterClass
	public void afterClass() {
		// TODO delete collection
//		navigateToStartPage();
//		
//		CollectionsPage collectionPage = adminHomePage.goToCollectionPage();
//		collectionPage.getFilterComponent().filter(FilterOptions.MY);
//				
//		collectionPage.deleteCollections();
		
		adminHomePage.logout();
	}
	
}
