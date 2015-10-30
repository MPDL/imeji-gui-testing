package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;

public class EditLanguageSetupTest extends BaseSelenium {

	private String germanSetup = "de - German";
	private String englishSetup = "en - English";
	private String spanishSetup = "es - Spanish";
	private String japaneseSetup = "ja - Japanese";	
	
	@BeforeMethod
	public void beforeMethod() {
	}

	@AfterMethod
	public void afterMethod() {
	}

	@BeforeClass
	public void beforeClass() {
		navigateToStartPage();
	}

	@AfterClass
	public void afterClass() {
	}

	@Test (priority = 1)
	public void changeLanguageSetupToEnglishTest() {
		getStartPage().selectLanguage(englishSetup);
		
		Assert.assertEquals(getStartPage().getCurrentLanguageSetup(), englishSetup);
	}
	
	@Test (priority = 2)
	public void changeLanguageSetupToSpanishTest() {
		getStartPage().selectLanguage(spanishSetup);
		
		Assert.assertEquals(getStartPage().getCurrentLanguageSetup(), spanishSetup);
	}
	
	@Test (priority = 3)
	public void changeLanguageSetupToJapaneseTest() {
		getStartPage().selectLanguage(japaneseSetup);
		
		Assert.assertEquals(getStartPage().getCurrentLanguageSetup(), japaneseSetup);
	}
	
	@Test (priority = 4)
	public void changeLanguageSetupToGermanTest() {
		getStartPage().selectLanguage(germanSetup);
		
		Assert.assertEquals(getStartPage().getCurrentLanguageSetup(), germanSetup);
	}
}
