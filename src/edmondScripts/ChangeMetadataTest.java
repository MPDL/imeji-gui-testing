package edmondScripts;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionContentPage;
import spot.pages.CollectionEntryPage;
import spot.pages.MetadataTransitionPage;
import spot.pages.LoginPage;
import spot.pages.MetadataOverviewPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;
import spot.pages.notAdmin.NewCollectionPage;
import spot.util.TimeStamp;

public class ChangeMetadataTest extends BaseSelenium {

	private String newMetadataTitle = "Own test profile";
	private String collectionTitle = "Colllection with referenced metadata profile: " + TimeStamp.getTimeStamp();
	private String collectionDescription = "Metadata profile to be changed";
	
	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	private MetadataOverviewPage metaDataOverViewPage;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}
	
	@Test(priority = 1)
	private void newCollectionReferencedMDP() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		collectionEntryPage = createNewCollectionPage.createCollectionWithStandardMetaDataProfile(collectionTitle, 
						collectionDescription, getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
						getPropertyAttribute(ruOrganizationName));
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
	
	@Test(priority = 2)
	public void canEditStandardProfile() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		metaDataOverViewPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).openMetaDataProfile();
		boolean profileCanBeModified = metaDataOverViewPage.profileCanBeModified();
		Assert.assertFalse(profileCanBeModified);
	}
	
	@Test(priority = 3)
	public void changeMetadataProfile() {
		MetadataTransitionPage transitionPage = metaDataOverViewPage.changeMetadata();
		metaDataOverViewPage = transitionPage.selectExistingMetadata(newMetadataTitle);
	}
	
	@Test(priority = 4)
	public void canEditOwnProfile() {
		boolean profileCanBeModified = metaDataOverViewPage.profileCanBeModified();
		Assert.assertTrue(profileCanBeModified);
	}
	
	@Test(priority = 5)
	public void deleteCollection() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		CollectionContentPage collectionContentPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle);
		collectionContentPage.viewCollectionInformation().deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}
}
