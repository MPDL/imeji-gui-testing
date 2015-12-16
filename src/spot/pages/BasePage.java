package spot.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import spot.CategoryType;
import spot.components.MainMenuComponent;
import spot.components.MessageComponent;
import spot.components.NewActionComponent;
import spot.components.SearchComponent;
import spot.components.UserPreferenceComponent;
import spot.pages.admin.AdminHomePage;
import spot.pages.notAdmin.HomePage;

/**
 * This class is abstract.
 * PageBase provides the base structure and properties of a page object to extend.
 *  
 * @author kocar
 *
 */
public abstract class BasePage {

	private static final Logger log4j = LogManager.getLogger(BasePage.class.getName());
	
	protected WebDriver driver;
	
	protected WebDriverWait wait; 
	
	/** holds the five menu items: start, items, collections, albums, single upload */
	protected MainMenuComponent mainMenuComponent;
	
	/** success or failure of user actions are reflected in the message area of the current page, e.g. login failed due to bad credentials */
	private MessageComponent messageComponent;
	
	/** class for creating new collections, albums etc. */
	private NewActionComponent newComponent;
	
	/** class for searching activities */
	private SearchComponent searchComponent;
	
	/** editing user preference settings; e.g. language change */
	private UserPreferenceComponent userPreferenceComponent;	

	@FindBy(css=".imj_siteContentHeadline>h1")
	private WebElement siteContentHeadline;
	
	@FindBy (xpath=".//*[@id='Header:lnkHelp']")
	private WebElement helpButton;
	
	@FindBy (xpath="html/body/div[1]/div[4]/div/div[2]/a")
	private WebElement disclaimer;	
	
	@FindBy(xpath="html/body/div[1]/div[4]/div/div[1]/div[2]/a")
	private WebElement imejiHomePageLink;
	
	@FindBy(xpath="html/body/div[1]/div[4]/div/div[3]/div/a")
	private WebElement mpdlHomePage;
	
	@FindBy(css=".fa-star")
	private WebElement activeAlbumMenueLabel;
	
	@FindBy(css=".imj_overlayMenuList li>a[title='View']")
	private WebElement viewActiveAlbumButton;
	
	/**
	 * Constructor
	 * 
	 * @param driver
	 */
	protected BasePage(WebDriver driver) {
		this.driver = driver;
		this.messageComponent = new MessageComponent(driver);
		this.mainMenuComponent = new MainMenuComponent(driver);
		this.newComponent = new NewActionComponent(driver);
		this.searchComponent = new SearchComponent(driver);
		this.userPreferenceComponent = new UserPreferenceComponent(driver);
		
		wait = new WebDriverWait(driver, 50);
	}
	
	public String getSiteContentHeadline() {
		return siteContentHeadline.getText();
	}

	public DetailedItemViewPage navigateToItemPage() {
				
		return mainMenuComponent.navigateTo(DetailedItemViewPage.class);
	}
	
	public SingleUploadPage goToSingleUploadPage() {
		return mainMenuComponent.navigateTo(SingleUploadPage.class);
	}
	
	public HomePage goToHomePage(HomePage homePage) {
		if (homePage instanceof HomePage)
			return mainMenuComponent.navigateTo(HomePage.class);
		else if (homePage instanceof AdminHomePage)
			return mainMenuComponent.navigateTo(AdminHomePage.class);
		return null;
	}
	
	public AlbumPage goToAlbumPage() {
				
		return mainMenuComponent.navigateTo(AlbumPage.class);
	}
	
	public CollectionsPage goToCollectionPage() {
		
		return mainMenuComponent.navigateTo(CollectionsPage.class);
	}
	
	public AdministrationPage goToAdminPage() {
		
		return mainMenuComponent.navigateTo(AdministrationPage.class);
	}
	
	public AlbumEntryPage openActiveAlbumEntryPage() {
		activeAlbumMenueLabel.click();
		viewActiveAlbumButton.click();
		return PageFactory.initElements(driver, AlbumEntryPage.class);
	}
	
	public HelpPage goToHelpPage() {
		helpButton.click();
		
		return PageFactory.initElements(driver, HelpPage.class);
	}
	
	public void lookUpDisclaimer() {
		disclaimer.click();
	}
	
	public void lookUpMpdlHomePage() {
		mpdlHomePage.click();
	}
	
	public void lookUpImejiHomePage() {		
		imejiHomePageLink.click();
	}
	
	public void selectLanguage(String language) {
		
		userPreferenceComponent.selectLanguage(language);
	}
	
	public String getCurrentLanguageSetup() {
		
		return userPreferenceComponent.getCurrentLanguage();		
	}	
	
	protected String extractMailDestinationAddressFromLink(String href) {
		String destination = "";

		String mailto = "mailto:";

		if (href.startsWith(mailto)) {
			String[] split = href.split(mailto);
			if (split.length > 0) {
				List<String> arrayList = new ArrayList<String>(Arrays.asList(split));
				arrayList.removeAll(Arrays.asList("", null));
				destination = arrayList.get(0);
			}
		}
		// TODO contact support link via javascript
		/*
		 * else if (href.startsWith("javascript:")) {
		 * 
		 * }
		 */

		return destination;
	}
	
	protected void waitForInitationPageElements(int waitTime) {
		ElementLocatorFactory elementLocatorFactory =  new AjaxElementLocatorFactory(driver, waitTime);
		PageFactory.initElements(elementLocatorFactory, this);
	}

	public SearchComponent getSearchComponent() {
		return searchComponent;
	}

	public MessageComponent getMessageComponent() {
		return messageComponent;
	}

	public boolean retryingFindClick(By by) {
        boolean result = false;
        int attempts = 0;
        while(attempts < 2) {
            try {
                driver.findElement(by).click();
                result = true;
                break;
            } catch(StaleElementReferenceException | NoSuchElementException e) {
            }
            attempts++;
        }
        return result;
	}
	
	public boolean retryingFinding(By by) {
        boolean result = false;
        int attempts = 0;
        while(attempts < 4) {
            try {
                driver.findElement(by).isDisplayed();
                result = true;
                break;
            } catch(StaleElementReferenceException | NoSuchElementException e) {
            }
            attempts++;
        }
        return result;
	}
}
