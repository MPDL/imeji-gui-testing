package spot.scripts;

import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.components.MessageComponent.MessageType;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;

import org.testng.annotations.BeforeClass;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class NewActionTest extends BaseSelenium {

	private AdminHomePage adminHomePage;

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		
		adminHomePage = loginPage.loginAsAdmin(getPropertyAttribute("aSpotUserName"), getPropertyAttribute("aSpotPassword"));
			
	}

	@AfterClass
	public void afterClass() {
		logout(PageFactory.initElements(driver, AdminHomePage.class));
	}

	@Test
	public void createCollectionTest() {
		CreateNewCollectionPage createNewCollectionPage = adminHomePage.goToCreateNewCollectionPage();
		
		String collectionTitle = "Testsammlung Montag";
		String collectionDescription = "This is a test description for a new collection.";
		
		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollectionWithoutStandardMetaDataProfile(collectionTitle, collectionDescription, getPropertyAttribute("aGivenName"), getPropertyAttribute("aFamilyName"),
				getPropertyAttribute("aOrganizationName"));
		
		Assert.assertTrue(collectionEntryPage.getMessageComponent().getMessageTypeOfPageMessageArea() == MessageType.INFO, "Collection couldn't be created");
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();		
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");		
	}

}
