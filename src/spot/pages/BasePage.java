package spot.pages;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import spot.components.MainMenuComponent;
import spot.components.MessageComponent;
import spot.components.MessageComponent.MessageType;
import spot.components.SearchComponent;
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

	@FindBy(css=".imj_siteContentHeadline>h1")
	private WebElement siteContentHeadline;
	
	@FindBy (id="langForm:sel")
	private WebElement languageButton;
	
	@FindBy (css="#lnkHelp")
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
	 * The link to imeji on github.
	 */
	public static final String IMEJI_GITHUB_URL = "https://github.com/MPDL/imeji/";
	
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
		//TODO: Add headerMenu Component with: txtAccountUserName, lnkLogout/lnkLogin, langForm, lnkHelp
		
		wait = new WebDriverWait(this.driver, 20);
	}
	
	public String getSiteContentHeadline() {
		return siteContentHeadline.getText();
	}
	
	public boolean browseAccessible() {
		return searchComponent.browseAccessible();
	}
	
	// IMJ-17
	public BrowseItemsPage navigateToItemPage() {
		return mainMenuComponent.navigateTo(BrowseItemsPage.class);
	}
	
	public StartPage goToStartPage() {
		return mainMenuComponent.navigateTo(StartPage.class);
	}
	
	public Homepage goToHomepage(Homepage homepage) {
		if (homepage instanceof AdminHomepage)
			return mainMenuComponent.navigateTo(AdminHomepage.class);
		else if (homepage instanceof Homepage)
			return mainMenuComponent.navigateTo(Homepage.class);
		return null;
	}
	
	// IMJ-113
	public CollectionsPage goToCollectionPage() {
		return mainMenuComponent.navigateTo(CollectionsPage.class);
	}
	
	// IMJ-226
	public AdministrationPage goToAdminPage() {
		return mainMenuComponent.navigateTo(AdministrationPage.class);
	}
	
	// IMJ-3, IMJ-218
	public HelpPage goToHelpPage() {
		helpButton.click();
		
		return PageFactory.initElements(driver, HelpPage.class);
	}
	
	// IMJ-6
	public void lookUpDisclaimer() {
		disclaimer.click();
	}
	
	// IMJ-20
	public void lookUpTermsOfUse() {
		termsOfUse.click();
	}
	
	public void lookUpMpdlHomePage() {
		mpdlHomePage.click();
	}
	
	// IMJ-7, IMJ-4
	public void lookUpImejiHomePage() {		
		imejiHomePageLink.click();
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
			MessageType messageType = messageComponent.getPageMessageType();
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
	
	// IMJ-219
	public AdvancedSearchPage goToAdvancedSearch() {
		return searchComponent.navigateToAdvancedSearchPage();
	}

	public boolean retryingFindClick(By by) {
        int attempts = 0;
        while(attempts < 20) {
            try {
                driver.findElement(by).click();
                return true;
            } 
            catch(WebDriverException e) {
            	attempts++;
            }
        }

        throw new NoSuchElementException("Element persists after 20 attempts.");
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
        while (attempts < 100) {
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
	 * makes invisible messages in order to access the components they cover
	 */
	public void hideMessages() {
		messageComponent.hideMessages();
	}
	
	/**
	 * Switch the language of imeji to English.
	 */
	public void switchToEnglish() {
	    //languageButton is missing if no other language is selectable -> this would lead to an error!
	    // => check whether the language must be switched without accessing the languageButton
	    // This is done based on the helpButton, because driver.findElements(By.id("langForm")) leads to long waiting times, as long as the implicitlyWait is still be set
	    if(!helpButton.getText().equalsIgnoreCase("HELP")) {
    		Select languageSelect = new Select(languageButton);
    		String actualLanguage = languageSelect.getFirstSelectedOption().getText();
    		
    		if("GERMAN".equals(actualLanguage)) {
    			WebElement staleElement = driver.findElement(By.id("langForm:sel"));
    			languageSelect.selectByValue("en");
    			wait.until(ExpectedConditions.stalenessOf(staleElement));
    		}
    		// else: the language is already English => nothing to do
	    }	
	}
	
	/**
	 * Switch the language of imeji to German.
	 */
	public void switchToGerman() {
	    //languageButton is missing if no other language is selectable -> this would lead to an error!
        // => check whether the language must be switched without accessing the languageButton
        // This is done based on the helpButton, because driver.findElements(By.id("langForm")) leads to long waiting times, as long as the implicitlyWait is still be set
  	    if(!helpButton.getText().equalsIgnoreCase("HILFE")) {
    		Select languageSelect = new Select(languageButton);
    		String actualLanguage = languageSelect.getFirstSelectedOption().getText();
    		
    		if("ENGLISH".equals(actualLanguage)) {
    			WebElement staleElement = driver.findElement(By.id("langForm:sel"));
    			languageSelect.selectByValue("de");
    			wait.until(ExpectedConditions.stalenessOf(staleElement));
    		}
    		// else: the language is already German => nothing to do
  	    }
	}
	
	/**
	 * Sets the waiting-timeout: 
	 * How long to wait for a condition to become true, when using the selenium explicit wait.
	 * 
	 * @param timeOutInSeconds
	 */
	public void setWaitingTime(int timeOutInSeconds) {
		this.wait.withTimeout(Duration.ofSeconds(timeOutInSeconds));
	}
	
}
