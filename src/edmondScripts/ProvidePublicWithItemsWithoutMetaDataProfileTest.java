package edmondScripts;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;


public class ProvidePublicWithItemsWithoutMetaDataProfileTest extends BaseSelenium {

	private LoginPage loginPage;
	private AdminHomePage adminHomePage;
	private CollectionEntryPage collectionEntryPage;
	
	private HashMap<String, String> files;
	private MultipleUploadPage multipleUploadPage;

	public final String collectionTitle = "Published test collection without meta data profile";	

	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}
	
	@BeforeClass
	public void beforeClass() {
//		System.out.println("Opening login page for: " + username);
		
		navigateToStartPage();		
		
		loginPage = new StartPage(driver).openLoginForm();
		
		files = new HashMap<String, String>();
		files.put("Chrysanthemum.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
		files.put("SamplePDFFile.pdf", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SamplePDFFile.pdf");
//		files.put("Wüste.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Wüste.jpg");
//		files.put("Hortensien.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Hortensien.jpg");
//		files.put("Qualle.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Qualle.jpg");
//		files.put("Koala.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Koala.jpg");
//		files.put("Leuchtturm.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Leuchtturm.jpg");
//		files.put("Pinguine.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Pinguine.jpg");		
//		files.put("SamplePNGFile.png", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SamplePNGFile.png");
//		files.put("SampleSWCFile.swc", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SampleSWCFile.swc");
//		files.put("SampleTIFFFile.tiff", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SampleTIFFFile.tiff");
//		files.put("SampleWordFile.docx", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SampleWordFile.docx");
//		files.put("SampleXLSXFile.xlsx", "C:\\Users\\Public\\Pictures\\Sample Pictures\\SampleXLSXFile.xlsx");
//		files.put("Tulpen.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Tulpen.jpg");
		
		
		
		
		
		new StartPage(driver).selectLanguage(englishSetup);
	}

	@Test (priority = 1)/*(groups = {"login", "dataUploadWithoutMetaDataProfile"})*/
	public void loginTest() {
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));

		String adminFullName = getPropertyAttribute(ruFamilyName) + ", " + getPropertyAttribute(ruGivenName);
		Assert.assertEquals(adminHomePage.getLoggedInUserFullName(), adminFullName, "User name doesn't match");
	
	}

	@Test(priority = 2)/*(groups={"collectionCreated", "dataUploadWithoutMetaDataProfile"}, dependsOnGroups = "login")*/
	public void createCollectionWithoutMetaDataProfileTest() {
		System.out.println("Attempt to create collection without meta data");
		CreateNewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();
		
		String collectionDescription = "This collection has no meta data profile. It is being published.";

		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));

		Assert.assertTrue(
				collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO,
				"Collection couldn't be created");

		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	
	}

	@Test (priority = 3)/*(groups="dataUploadWithoutMetaDataProfile", dependsOnGroups="collectionCreated")*/
	public void uploadFilesTest() throws AWTException {

		multipleUploadPage = collectionEntryPage.uploadContent();
		
		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}
		
		List<String> fileNames = new ArrayList<String>(files.keySet());
		
		multipleUploadPage.startUpload();
		
		boolean isVerificationSuccessfull = multipleUploadPage.verifyUploadedFiles(fileNames);
		
		Assert.assertTrue(isVerificationSuccessfull, "The list of uploaded files is probably incomplete.");
	}

//	@Test (priority = 4)/*(groups = "dataUploadWithoutMetaDataProfile")*/
	public void publishCollectionTest() {
		
			
		multipleUploadPage.publishCollection();

		String actualInfoMessage = multipleUploadPage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Collection released successfully";
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage,
				"Something went wrong with the release of the collection.");
	
	}
}
