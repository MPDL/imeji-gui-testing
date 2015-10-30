package spot.components;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MessageComponent {

	private WebDriver driver;
	
	@FindBy (xpath=".//*[@id='imj_pageMessageArea']")
	private WebElement pageMessageArea;
	
	public enum MessageType { ERROR, INFO };
	
	public MessageComponent(WebDriver driver) {
		this.driver = driver;
		
		PageFactory.initElements(driver, this);
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
}
