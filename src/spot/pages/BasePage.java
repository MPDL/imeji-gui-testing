package spot.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

import spot.components.MainMenuComponent;
import spot.components.MessageComponent;
import spot.components.MessageComponent.MessageType;
import spot.components.SearchComponent;
import spot.components.UserPreferenceComponent;
import spot.pages.admin.AdminHomepage;
import spot.pages.admin.AdministrationPage;
import spot.pages.registered.Homepage;

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
	
	@FindBy (css="#poweredBy a:nth-of-type(2)")
	private WebElement termsOfUse;
	
	@FindBy(css=".fa-star")
	private WebElement activeAlbumMenueLabel;
	
	@FindBy(css=".imj_overlayMenuList li>a[title='View']")
	private WebElement viewActiveAlbumButton;
	
	/* @imejiHomePageLink & @mpdlHomePage only for Imeji */
	@FindBy(css=".imj_poweredby_software_image a")
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
		this.searchComponent = new SearchComponent(driver);
		this.userPreferenceComponent = new UserPreferenceComponent(driver);
		
		wait = new WebDriverWait(this.driver, 20);
	}
	
	public String getSiteContentHeadline() {
		return siteContentHeadline.getText();
	}
	
	public boolean browseAccessible() {
		return searchComponent.browseAccessible();
	}
	
	public BrowseItemsPage navigateToItemPage() {
		return searchComponent.callBrowseSection();
	}
	
	public SingleUploadPage goToSingleUploadPage() {
		return mainMenuComponent.navigateTo(SingleUploadPage.class);
	}
	
	public Homepage goToHomepage(Homepage homepage) {
		if (homepage instanceof AdminHomepage)
			return mainMenuComponent.navigateTo(AdminHomepage.class);
		else if (homepage instanceof Homepage)
			return mainMenuComponent.navigateTo(Homepage.class);
		return null;
	}
	
	public CollectionsPage goToCollectionPage() {
		return mainMenuComponent.navigateTo(CollectionsPage.class);
	}
	
	public AdministrationPage goToAdminPage() {
		return mainMenuComponent.navigateTo(AdministrationPage.class);
	}
	
	public HelpPage goToHelpPage() {
		helpButton.click();
		
		return PageFactory.initElements(driver, HelpPage.class);
	}
	
	public void lookUpDisclaimer() {
		disclaimer.click();
	}
	
	public void lookUpTermsOfUse() {
		termsOfUse.click();
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
	
	public MessageType getPageMessageType() {
		try {
			MessageType messageType = messageComponent.getMessageTypeOfPageMessageArea();
			return messageType;
		}
		catch (NoSuchElementException exc) {
			return MessageType.NONE;
		}
	}
	
	public MainMenuComponent getMainMenuComponent() {
		return mainMenuComponent;
	}
	
	public boolean advancedSearchAccessible() {
		return searchComponent.advancedSearchAccessible();
	}
	
	public boolean advancedSearchUnaccessible() {
		return searchComponent.advancedSearchUnaccessible();
	}
	
	public AdvancedSearchPage goToAdvancedSearch() {
		return searchComponent.navigateToAdvancedSearchPage();
	}

	public boolean retryingFindClick(By by) {
        boolean result = false;
        int attempts = 0;
        while(attempts < 20) {
            try {
                driver.findElement(by).click();
                result = true;
                break;
            } 
            catch(WebDriverException e) {}
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
            } 
            catch(StaleElementReferenceException | NoSuchElementException e) {}
            attempts++;
        }
        return result;
	}
	
	public WebElement retryingElement(By by) {
		int attempts = 0;
        while(attempts < 100) {
            try {
                return driver.findElement(by);
            } 
            catch(StaleElementReferenceException | NoSuchElementException e) {}
            attempts++;
        }
        throw new StaleElementReferenceException("Stale element persists after 100 attempts");
	}
	
	public WebElement retryingNestedElement(WebElement parent, By by) {
		int attempts = 0;
        while(attempts < 100) {
            try {
                return parent.findElement(by);
            } 
            catch(StaleElementReferenceException | NoSuchElementException e) {}
            attempts++;
        }
        throw new StaleElementReferenceException("Stale element persists after 100 attempts");
	}
	
	public String getPageTitle() {
		return driver.getTitle();
	}

	/**
	 * reduce delay while looking for elements that do not exist
	 */
	public boolean isElementPresent(By locator) {
		try {
			wait.withTimeout(3, TimeUnit.SECONDS).until(ExpectedConditions.presenceOfElementLocated(locator));
			return true;
		}
		catch (TimeoutException exc) {
			return false;
		}
	}
	
}
