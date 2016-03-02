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
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.MultipleUploadPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;

public class DiscardCollectionTest extends BaseSelenium {
 	
	private AdminHomePage adminHomePage;
	
	private String collectionTitle;
	
	private HashMap<String, String> files;

	private CollectionContentPage collectionContentPage;

	private MultipleUploadPage multipleUploadPage;

	@BeforeClass
	public void beforeClass() throws AWTException {
		navigateToStartPage();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		
		files = new HashMap<String, String>();
		files.put("Chrysanthemum.jpg", "C:\\Users\\Public\\Pictures\\Sample Pictures\\Chrysanthemum.jpg");
		
//		new StartPage(driver).selectLanguage(englishSetup);
		
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute(spotRUUserName),
				getPropertyAttribute(spotRUPassWord));

		collectionTitle = "Collection doomed to be discarded";

		createAndReleaseCollection(collectionTitle);
	}
	
	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}
	
	@Test
	public void discardCollectionTest() {
		
		ActionComponent actionComponent = multipleUploadPage.getActionComponent();
		
		CollectionEntryPage collectionEntryPage = (CollectionEntryPage)actionComponent.doAction(ActionType.DISCARD);
		
		String actualInfoMessage = collectionEntryPage.getMessageComponent().getInfoMessage();
		String expectedInfoMessage = "Collection discarded successfully";
		
		Assert.assertEquals(actualInfoMessage, expectedInfoMessage, "Discarding collection probably failed");
	}
	
	private void createAndReleaseCollection(String collectionTitle) throws AWTException {
		CreateNewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();

		String collectionDescription = "This collection is doomed to be discarded. For testing purposes.";

		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle,
				collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		multipleUploadPage = collectionEntryPage.uploadContent();

		for (Map.Entry<String, String> file : files.entrySet()) {
			multipleUploadPage.addFile(file.getValue());
		}

		multipleUploadPage.startUpload();
		
		ActionComponent actionComponent = multipleUploadPage.getActionComponent();
		collectionContentPage = (CollectionContentPage) actionComponent.doAction(ActionType.PUBLISH);
	}
}
