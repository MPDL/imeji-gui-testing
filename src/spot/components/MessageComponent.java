package spot.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class MessageComponent {

	private WebDriver driver;

	@FindBy(id = "imj_pageMessageArea")
	private WebElement pageMessageArea;

	public enum MessageType {
		ERROR, INFO, NONE
	};

	public MessageComponent(WebDriver driver) {
		this.driver = driver;

		PageFactory.initElements(driver, this);
	}
	
	public boolean messageDisplayed() {
		try {
			return !pageMessageArea.getText().isEmpty();
		}
		catch (NoSuchElementException exc) {
			return false;
		}
	}

	/**
	 * Used for e.g. Login. Login action results into update of page message
	 * area in any case (fail/success). Successful login is associated with
	 * message type messageInfo. Failed login with message type messageError.
	 * 
	 * @return the messageType - either info or error
	 */
	public MessageType getMessageTypeOfPageMessageArea() {
		MessageType msgType = MessageType.INFO;

		// does any class attribute in the message area contain the word "error"?
		List<WebElement> pageMsgAreaComponents = pageMessageArea
				.findElements(By.tagName("div"));

		for (WebElement we : pageMsgAreaComponents) {
			String messageType = we.getAttribute("class");
			boolean isError = StringUtils.containsIgnoreCase(messageType,
					MessageType.ERROR.toString());
			if (isError) {
				msgType = MessageType.ERROR;
				break;
			}
		}
		
		return msgType;
	}

	public String getErrorMessage() {
		List<String> errorMessages = new ArrayList<String>();

		List<WebElement> errorWebElements = pageMessageArea.findElements(By
				.className("imj_messageError"));

		for (WebElement we : errorWebElements) {
			errorMessages.add(we.getText());
		}

		if (errorMessages.size() > 0)
			return errorMessages.get(0).trim();
		else
			return "";
	}

	public String getInfoMessage() {
		List<String> infoMessages = new ArrayList<String>();

		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("imj_messageInfo")));
		
		List<WebElement> errorWebElements = pageMessageArea.findElements(By.className("imj_messageInfo"));

		for (WebElement we : errorWebElements) {
			infoMessages.add(we.getText());
		}

		if (infoMessages.size() > 0)
			return infoMessages.get(0).trim();
		else
			return "";
	}
}
