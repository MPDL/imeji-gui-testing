package edmondScripts;

import java.awt.AWTException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.notAdmin.HomePage;

public class DeleteCollectionTest extends BaseSelenium {

	private HomePage homePage;
	private CollectionEntryPage collectionEntryPage;
	
	private String collectionTitle;

	@BeforeClass
	public void beforeClass() throws AWTException {
		super.setup();
		loginCreator();
	}
	
	private void loginCreator() {
		LoginPage loginPage = new StartPage(driver).openLoginForm();

		homePage = loginPage.loginAsNotAdmin(getPropertyAttribute(spotRUUserName), getPropertyAttribute(spotRUPassWord));
	}
	
	@Test
	public void deleteCollectionTest() {
		collectionTitle = getPropertyAttribute(privateCollectionKey);
		homePage = new StartPage(driver).goToHomePage(homePage);
		collectionEntryPage = homePage.goToCollectionPage().openCollectionByTitle(collectionTitle).viewCollectionInformation();
		collectionEntryPage.deleteCollection();
	}
	
	@AfterClass
	public void afterClass() {
		getProperties().remove("sharedPrivateCollection");
		homePage = new StartPage(driver).goToHomePage(homePage);
		homePage.logout();
	}

}
