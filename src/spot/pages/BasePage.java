package spot.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

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

	protected WebDriver driver;
	
	protected WebDriverWait wait; 
	
	/** holds the four menu items: start, collections, albums, single upload */
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
	
	@FindBy (css="#Header\\:lnkHelp")
	private WebElement helpButton;
	
	@FindBy (css="#poweredBy a")
	private WebElement disclaimer;	
	
	@FindBy(css=".fa-star")
	private WebElement activeAlbumMenueLabel;
	
	@FindBy(css=".imj_overlayMenuList li>a[title='View']")
	private WebElement viewActiveAlbumButton;
	
	/* @imejiHomePageLink & @mpdlHomePage only for Imeji */
	@FindBy(css=".imj_poweredby_software_image")
	protected WebElement imejiHomePageLink;
	
	@FindBy(css=".imj_poweredby_institution")
	protected WebElement mpdlHomePage;
	
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
		
		wait = new WebDriverWait(this.driver, 15);
	}
	
	public String getSiteContentHeadline() {
		return siteContentHeadline.getText();
	}
	
	public BrowseItemsPage navigateToItemPage() {
		return searchComponent.callBrowseSection();
	}
	
	public SingleUploadPage goToSingleUploadPage() {
		return mainMenuComponent.navigateTo(SingleUploadPage.class);
	}
	
	public HomePage goToHomePage(HomePage homePage) {
		if (homePage instanceof AdminHomePage)
			return mainMenuComponent.navigateTo(AdminHomePage.class);
		else if (homePage instanceof HomePage)
			return mainMenuComponent.navigateTo(HomePage.class);
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
	
	public void enableDarkMode() {
		userPreferenceComponent.enableDarkMode();
	}
	
	public void enableLightMode() {
		userPreferenceComponent.enableLightMode();
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
	
	public String getPageTitle() {
		return driver.getTitle();
	}
	
}
