package spot.scripts;

import org.testng.Assert;
import org.testng.annotations.*;

import spot.BaseSelenium;
import spot.pages.StartPage;

public class EditLanguageSetupTest extends BaseSelenium {

	private StartPage startPage;
	
	@BeforeMethod
	public void beforeMethod() {
	}

	@AfterMethod
	public void afterMethod() {
	}

	@BeforeClass
	public void beforeClass() {
		super.setup();
		navigateToStartPage();
		startPage = new StartPage(driver);		
	}

	@AfterClass
	public void afterClass() {
		
	}

	@Test (priority = 1)
	public void changeLanguageSetupToEnglishTest() {
		
		startPage.selectLanguage(englishSetup);
		
		Assert.assertEquals(startPage.getCurrentLanguageSetup(), englishSetup);
	}
	
	@Test (priority = 2)
	public void changeLanguageSetupToSpanishTest() {
		startPage.selectLanguage(spanishSetup);
		
		Assert.assertEquals(startPage.getCurrentLanguageSetup(), spanishSetup);
	}
	
	@Test (priority = 3)
	public void changeLanguageSetupToJapaneseTest() {
		startPage.selectLanguage(japaneseSetup);
		
		Assert.assertEquals(startPage.getCurrentLanguageSetup(), japaneseSetup);
	}
	
	@Test (priority = 4)
	public void changeLanguageSetupToGermanTest() {
		startPage.selectLanguage(germanSetup);
		
		Assert.assertEquals(startPage.getCurrentLanguageSetup(), germanSetup);
	}
}
