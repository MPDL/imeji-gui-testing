package spot.scripts.collections;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import spot.BaseSelenium;
import spot.pages.CollectionEntryPage;
import spot.pages.LoginPage;
import spot.pages.StartPage;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.CreateNewCollectionPage;

public class AddOrganizationTest extends BaseSelenium {

	private AdminHomePage adminHomePage;

	@BeforeMethod
	public void beforeMethod() {
		navigateToStartPage();
	}

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
		LoginPage loginPage = new StartPage(driver).openLoginForm();

		adminHomePage = loginPage.loginAsAdmin(
				getPropertyAttribute("aSpotUserName"),
				getPropertyAttribute("aSpotPassword"));
	}

	@AfterClass
	public void afterClass() {
		adminHomePage.logout();
	}

	@Test
	public void addAdditionalAuthorTest() {
		CreateNewCollectionPage createNewCollectionPage = adminHomePage
				.goToCreateNewCollectionPage();

		String collectionTitle = "Donnerstag";
		String collectionDescription = "Leuchte dichten barbara zuliebe es la. Zuliebe weg lie klopfte was geruckt gab endlich mischen. Wohlfeil em spielend pa fu te angenehm rabatten. Ein sehe wei fast noch herd. Geborene gegessen bi schlafen gemessen vorliebe so ei zu. Gesund wissen gib bei frauen wahres harten sie nur keinem.Mannsbild mitkommen ist unendlich schwarzes und wie schlupfte vor. Werdet bat das gut dunner lebtag der allein. Zum sorglosen gib tut wohnstube muhlenrad beneidest. Damit zahne da keine ubers te abend. Mann an je gelt kein fest hast. Madchens schaffte an stabelle heiraten um. Ja stickig konntet richtig du zu ubrigen gewohnt ei.Kindlichen neidgefuhl du nachmittag wu verstehsts se ja. Neu ich ige blatt hin alter nicht. Kleinen reichen lustige ans gut. Kindlichen stockwerke das nie nachmittag ihm geheiratet ige. Eck feinsten gebracht gefallen herunter halbwegs ist oha. Verstehen teilnahme flanierte je zu schnellen em turnhalle ertastete ja. Ten kaute auf das wenig fremd rasch anzug zwirn weg.Nachtessen zaunpfahle ihr als sauberlich hereintrat leuchtturm ihr. Es doch esse gewu gelt an voll es gott am. Bildnis heimweh so hochmut sa zu da nachdem. Grundstuck werkstatte ungerechte sie ans. Kammertur schleiche gewachsen pa mi hochstens. Sie gesichts richtete weg kollegen erschien indessen mag wohlfeil. Feuer suppe zu jahre ab keins nahen. Sto wanderer heiraten hausherr bezahlen sie. Brauchen von hei gerechte heiraten manchmal. ";

		createNewCollectionPage.addOrganization();
		
		CollectionEntryPage collectionEntryPage = createNewCollectionPage
				.fillForm(collectionTitle, collectionDescription,
						getPropertyAttribute("aGivenName"),
						getPropertyAttribute("aFamilyName"), "");
		
		Assert.assertEquals(true, false);
		
	}
}

