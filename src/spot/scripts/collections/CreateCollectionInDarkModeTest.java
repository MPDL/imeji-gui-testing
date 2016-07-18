package spot.scripts.collections;

import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.CreateNewCollectionPage;
import spot.pages.notAdmin.HomePage;
import spot.util.TimeStamp;

import org.testng.annotations.BeforeClass;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class CreateCollectionInDarkModeTest extends BaseSelenium {

	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private final String collectionTitle = "Collection created in dark mode: " + TimeStamp.getTimeStamp();
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();

		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName),
				getPropertyAttribute(spotRUPassWord));
		
		homePage.enableDarkMode();
	}
	
	@Test(priority = 1)
	public void createCollectionInDarkModeTest() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CreateNewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		
		String collectionDescription = "Testing functionality of dark mode";
		
		collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle, 
				collectionDescription, getPropertyAttribute("aGivenName"), getPropertyAttribute("aFamilyName"),
				getPropertyAttribute("aOrganizationName"));

		MessageType messageType = collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea();
		Assert.assertTrue(messageType == MessageType.INFO, "Collection couldn't be created");
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();		
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");		
	}
	
	@Test(priority = 2)
	public void deleteCollection() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		collectionEntryPage.deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}

}
