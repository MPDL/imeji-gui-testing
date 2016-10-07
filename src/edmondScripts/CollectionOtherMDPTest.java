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

public class CollectionOtherMDPTest extends BaseSelenium {
	
	private String collectionTitle = "Collection with individual MDP as reference/template: " + TimeStamp.getTimeStamp();
	private String metadataIdentifier = "Elefanten Daten Profil";
	
	private HomePage homePage;
	
	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		
		LoginPage loginPage = new StartPage(driver).openLoginForm();
		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}
	
	@Test(priority = 1)
	public void individualMDPReference() {
		NewCollectionPage createNewCollectionPage = homePage.goToCreateNewCollectionPage();
		
		CollectionEntryPage collectionEntryPage = createNewCollectionPage.createCollectionWithMetaDataProfileAsReference(metadataIdentifier, collectionTitle,
				"", getPropertyAttribute(ruGivenName), getPropertyAttribute(ruFamilyName),
				getPropertyAttribute(ruOrganizationName));
		
		String siteContentHeadline = collectionEntryPage.getSiteContentHeadline();
		Assert.assertTrue(siteContentHeadline.equals(collectionTitle), "Collection title not correct");
	}
	
	@Test(priority = 2)
	public void cannotEditMetadata() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		MetadataOverviewPage metaDataOverViewPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).openMetaDataProfile();
		boolean profileCanBeModified = metaDataOverViewPage.profileCanBeModified();
		Assert.assertFalse(profileCanBeModified);
	}
	
	@Test(priority = 3)
	public void individualMDPTemplate() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		MetadataOverviewPage metaDataOverViewPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).openMetaDataProfile();
		MetadataTransitionPage transitionPage = metaDataOverViewPage.changeMetadata();
		metaDataOverViewPage = transitionPage.selectExistingMetadataTemplate(metadataIdentifier);
	}
	
	@Test(priority = 4)
	public void canEditMetadata() {
		homePage = new StartPage(driver).goToHomePage(homePage);
		MetadataOverviewPage metaDataOverViewPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).openMetaDataProfile();
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
