package edmondScripts;

import java.awt.AWTException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.SingleUploadPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

public class ItemUploadTest extends BaseSelenium {

	private HomePage homePage;
	private CollectionContentPage collectionContent;
	
	private HashMap<String, String> files;
	private String collectionTitle = "Collection with single uploaded items: " + TimeStamp.getTimeStamp();
	private int itemCount;
	private int previousItemCount;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
	}
	
	@Test(priority = 1)
	public void loginAsRU() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}
	
	@Test(priority = 2)
	public void createPrivateCollection() {
		homePage.goToCreateNewCollectionPage().createCollectionWithoutStandardMetaDataProfile(collectionTitle, "",
				getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName), getPropertyAttribute(ruOrganizationName));
	}
	
	@Test(priority = 3)
	public void uploadItems() {
		files = new HashMap<String, String>();
		files.put("SampleJPGFile.jpg", getFilepath("SampleJPGFile.jpg"));
		files.put("SamplePDFFile.pdf", getFilepath("SamplePDFFile.pdf"));
		files.put("SampleWordFile.docx", getFilepath("SampleWordFile.docx"));
		
		for (Map.Entry<String, String> file : files.entrySet()) {
			String filePath = file.getValue();
			
			try {
				refreshHomePage();
				SingleUploadPage singleUpload = homePage.goToSingleUploadPage();
				singleUpload.upload(filePath, collectionTitle);
			}
			catch (AWTException exc) {}
		}
	}
	
	@Test(priority = 4)
	public void deleteSingleItem() {
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		previousItemCount = collectionContent.getItemListSize();
		collectionContent = collectionContent.deleteItem(0);
		itemCount = collectionContent.getItemListSize();
		
		MessageType messageType = collectionContent.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertEquals(messageType, MessageType.INFO, "Item was not deleted successfully");
		
		Assert.assertEquals(itemCount, previousItemCount - 1, "Item count did not change after delete.");
	}
	
	@Test(priority = 5)
	public void deleteAllItems() {
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionContent = collectionContent.deleteAllItems();
		itemCount = collectionContent.getItemListSize();
		previousItemCount = itemCount;
		
		MessageType messageType = collectionContent.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertEquals(messageType, MessageType.INFO, "Item was not deleted successfully");
		
		Assert.assertEquals(itemCount, 0, "Not all items were deleted.");
	}
	
	@Test(priority = 6)
	public void uploadAndRelease() {
		SingleUploadPage singleUpload = homePage.goToSingleUploadPage();
		try {
			singleUpload.upload(files.get("SampleJPGFile.jpg"), collectionTitle);
		}
		catch (AWTException exc) {}
		
		refreshHomePage();
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		itemCount = collectionContent.getItemListSize();
		Assert.assertEquals(itemCount, previousItemCount + 1, "Item was not uploaded to private collection.");
		
		CollectionEntryPage collectionEntry = collectionContent.viewCollectionInformation().publishCollection();
		MessageType messageType = collectionEntry.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertEquals(messageType, MessageType.INFO, "Message for collection release was not displayed.");
	}
	
	@Test(priority = 7)
	public void singleUploadReleased() {
		SingleUploadPage singleUpload = homePage.goToSingleUploadPage();
		try {
			singleUpload.upload(files.get("SamplePDFFile.pdf"), collectionTitle);
		}
		catch (AWTException exc) {}
		
		previousItemCount = itemCount;
		refreshHomePage();
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		itemCount = collectionContent.getItemListSize();
		Assert.assertEquals(itemCount, previousItemCount + 1, "Item was not uploaded to released collection.");
	}
	
	@Test(priority = 8)
	public void uploadReleased() {
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		previousItemCount = collectionContent.getItemListSize();
		
		try {
			MultipleUploadPage multipleUpload = collectionContent.viewCollectionInformation().uploadContent();
			multipleUpload.addFile(files.get("SampleWordFile.docx"));
			multipleUpload.startUpload();
		}
		catch (AWTException exc) {}
		
		refreshHomePage();
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		itemCount = collectionContent.getItemListSize();
		Assert.assertEquals(itemCount, previousItemCount + 1, "Item was not uploaded to released collection.");
	}
	
	@Test(priority = 9)
	public void repeatUploadWithCheck() {
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		previousItemCount = collectionContent.getItemListSize();
		
		MultipleUploadPage multipleUpload = collectionContent.viewCollectionInformation().uploadContent();
		multipleUpload.checkUniqueness(true);
		
		try {
			multipleUpload.addFile(files.get("SampleWordFile.docx"));
		}
		catch (AWTException exc) {}
		
		multipleUpload.startFailedUpload();
		
		refreshHomePage();
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		itemCount = collectionContent.getItemListSize();
		Assert.assertEquals(itemCount, previousItemCount, "Item was uploaded despite repeating.");
	}
	
	@Test(priority = 10)
	public void repeatUploadWithoutCheck() {
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		previousItemCount = collectionContent.getItemListSize();
		MultipleUploadPage multipleUpload = collectionContent.viewCollectionInformation().uploadContent();
		multipleUpload.checkUniqueness(false);
		
		try {	
			multipleUpload.addFile(files.get("SampleWordFile.docx"));
		}
		catch (AWTException exc) {}
		
		boolean uploadSuccessful = multipleUpload.startUpload();
		Assert.assertTrue(uploadSuccessful, "Upload was not successful.");
		
		refreshHomePage();
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		itemCount = collectionContent.getItemListSize();
		Assert.assertEquals(itemCount, previousItemCount + 1, "Item was not uploaded successfully.");
	}
	
	@Test(priority = 11)
	public void overwriteUpload() {
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		previousItemCount = collectionContent.getItemListSize();
		
		MultipleUploadPage multipleUpload = collectionContent.viewCollectionInformation().uploadContent();
		multipleUpload.overwriteFile(true);
		
		try {
			multipleUpload.addFile(files.get("SamplePDFFile.pdf"));
		}
		catch (AWTException exc) {}
		
		boolean uploadSuccessful = multipleUpload.startUpload();
		Assert.assertTrue(uploadSuccessful, "Upload was not successful.");
		
		refreshHomePage();
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		itemCount = collectionContent.getItemListSize();
		Assert.assertEquals(itemCount, previousItemCount, "Item was not overwritten successfully.");
	}
	
	@Test(priority = 12)
	public void uploadPreviewImage() {
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		previousItemCount = collectionContent.getItemListSize();
		
		MultipleUploadPage multipleUpload = collectionContent.viewCollectionInformation().uploadContent();
		multipleUpload.uploadPreview(true);
		
		try {
			multipleUpload.addFile(files.get("SampleJPGFile.jpg"));
		}
		catch (AWTException exc) {}
		
		boolean uploadSuccessful = multipleUpload.startUpload();
		Assert.assertTrue(uploadSuccessful, "Upload was not successful.");
		
		refreshHomePage();
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		itemCount = collectionContent.getItemListSize();
		Assert.assertEquals(itemCount, previousItemCount, "Preview image was not uploaded successfully.");
	}
	
	@Test(priority = 13)
	public void downloadItemMetadata() {
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		boolean canDownload = collectionContent.downloadItemMetadata();
		Assert.assertTrue(canDownload, "User cannot download RDF metadata.");
	}
	
	@Test(priority = 14)
	public void discardItem() {
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		previousItemCount = collectionContent.getItemListSize();
		collectionContent = collectionContent.openItem(0).discardItem();
		
		MessageType messageType = collectionContent.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertEquals(messageType, MessageType.INFO, "Item was not successfully discarded.");
		
		itemCount = collectionContent.getItemListSize();
		Assert.assertEquals(itemCount, previousItemCount - 1, "Item was not successfully discarded.");
	}
	
	@Test(priority = 15)
	public void discardCollection() {
		collectionContent = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionContent.discardCollection();
	}
	
	@AfterMethod
	public void refreshHomePage() {
		homePage = new StartPage(driver).goToHomePage(homePage);
	}
	
	@AfterClass
	public void afterClass() {
		homePage.logout();
	}
}
