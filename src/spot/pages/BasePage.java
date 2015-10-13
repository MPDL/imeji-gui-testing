package spot.pages;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

	@FindBy (xpath=".//*[@id='imj_pageMessageArea']")
	private WebElement pageMessageArea;
	
	public enum MessageType { ERROR, INFO };
		
	@FindBy (xpath=".//*[@id='Header:j_idt59:lnkHome']")
	private WebElement startButton;
	
	@FindBy (xpath=".//*[@id='Header:j_idt59:lnkBrowse']")
	private WebElement itemsButton;
	
	@FindBy (xpath=".//*[@id='Header:j_idt59:lnkCollections']")
	private WebElement collectionsButton;
	
	@FindBy (xpath=".//*[@id='Header:j_idt59:lnkAlbums']")
	private WebElement albumsButton;
	
	@FindBy (xpath=".//*[@id='Header:j_idt59:lnkUpload']")
	private WebElement singleUploadButton;
	
	protected BasePage(WebDriver driver) {
		this.driver = driver;
	}
	
	public SingleUploadPage goToUploadPage() {
		singleUploadButton.click();
		
		return PageFactory.initElements(driver, SingleUploadPage.class);
	}
	
	/**
	 * Used for e.g. Login. 
	 * Login action results into update of page message area in any case (fail/success).
	 * Successful login is associated with message type messageInfo.
	 * Failed login with message type messageError.
	 * 
	 * @return the messageType - either info or error
	 */
	public MessageType getMessageTypeOfPageMessageArea() {
				
		MessageType msgType = MessageType.INFO;			
		
		// Either info or error components, depending on whether login failed or succeeded
		List<WebElement> pageMsgAreaComponents = pageMessageArea.findElements(By.tagName("li"));
		
		for (WebElement we : pageMsgAreaComponents) {
			String messageType = we.getAttribute("class");			
			boolean isError = StringUtils.containsIgnoreCase(messageType, MessageType.ERROR.toString());
			if (isError) {
				msgType = MessageType.ERROR;
				break;
			}
		}
		
		return msgType; 
	}
		
//	public boolean isElementPresent(By selector) {
//		return findElements(selector).size() > 0;		
//	}
//
//	public boolean isElementVisible(By selector) {
//		return findElement(selector).isDisplayed();
//	}
//	
//	private WebElement findElement(By wantedElementXpath) {
//		return driver.findElement(wantedElementXpath);
//	}
//		
//	private List<WebElement> findElements(By wantedElementXpath) {
//		return driver.findElements(wantedElementXpath);
//	}
	
}
