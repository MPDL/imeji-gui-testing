package spot.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class MessageComponent {

	private WebDriver driver;

	@FindBy(className = "imj_pageMessageArea")
	private WebElement pageMessageArea;

	public enum MessageType {
		ERROR, SUCCESS, NONE
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
	 * Successful action is associated with message type 'success',
	 * failed action - with 'error',
	 * lack of message - with 'none'
	 * Error messages always take priority
	 * 
	 * @return the message type
	 */
	public MessageType getPageMessageType() {
		// does any class attribute in the message area contain the word "error"?
		List<WebElement> pageMsgAreaComponents = pageMessageArea.findElements(By.tagName("div"));
		boolean isSuccess = false;

		for (WebElement we : pageMsgAreaComponents) {
			String messageType = we.getAttribute("class");
			if (StringUtils.containsIgnoreCase(messageType, MessageType.ERROR.toString())) {
				return MessageType.ERROR;
			}
			if (StringUtils.containsIgnoreCase(messageType, MessageType.SUCCESS.toString())) {
				isSuccess = true;
			}
		}
		
		return isSuccess ? MessageType.SUCCESS : MessageType.NONE;
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

	public String getSuccessMessage() {
		List<String> successMessages = new ArrayList<String>();

		WebDriverWait wait = new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("imj_messageSuccess")));
		
		List<WebElement> successWebElements = pageMessageArea.findElements(By.className("imj_messageSuccess"));

		for (WebElement we : successWebElements) {
			successMessages.add(we.getText());
		}

		if (successMessages.size() > 0)
			return successMessages.get(0).trim();
		else
			return "";
	}
	
	public void hideMessages() {
//		List<WebElement> messages = driver.findElements(By.className("imj_message"));
//		for (WebElement message : messages) {
//			((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'none';", message);
//		}
		((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'none';", pageMessageArea);
		
	}
}
