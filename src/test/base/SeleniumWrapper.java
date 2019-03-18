package test.base;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Utility class for wrapping Selenium methods.
 * 
 * @author helk
 *
 */
public class SeleniumWrapper {

	/**
	 * Wait until the (next) page is loaded. <br>
	 * Uses waiting for an element to become stale (not present on the DOM) to detect that the old page doesn't exist anymore. <br>
	 * The staleElement must be a WebElement of the old page. It must be initialized before the event, that led to the page reload. <br>
	 * The staleElement must NOT be a Page Object field, which uses lazy loading.
	 * 
	 * @param wait The WebDriverWait, which defines the wait timeout in seconds
	 * @param staleElement An element of the current page which becomes stale after the page load 
	 */
	public static void waitForPageLoad(WebDriverWait wait, WebElement staleElement) {
		wait.until(ExpectedConditions.stalenessOf(staleElement));
		wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
	}
	
	/**
	 * Wait until a certain component/element of the page is (re)loaded. <br>
	 * Uses waiting for the element to become stale (not present on the DOM) to detect that the old element doesn't exist anymore. <br>
	 * The elementToBecomeReloaded must be the WebElement that will be reloaded. It must be initialized before the event, that led to the partial reload. <br>
	 * The elementToBecomeReloaded must NOT be a Page Object field, which uses lazy loading.
	 * 
	 * @param wait The WebDriverWait, which defines the wait timeout in seconds
	 * @param elementToBecomeReloaded An element of the current page which will be reload 
	 */
	public static void waitForAjaxLoad(WebDriverWait wait, WebElement elementToBecomeReloaded) {
		wait.until(ExpectedConditions.stalenessOf(elementToBecomeReloaded));
		//TODO: Wait for Ajax request finished loading the Element -> xhr.readyState == 4
	}
	
	/**
  	 * An expectation for checking if the given text is present in ALL specified elements. <br>
  	 * 
  	 * Equal to ExpectedConditions.textToBePresentInElementLocated() but checking all specified elements and ignoring case <br>
  	 * 
	 * This method can be used to wait for the ExpectedCondition to become true: <br>
	 * -> wait.until(textToBePresentInAllElements(locator, text));
	 * 
	 * @param locator used to find the elements
	 * @param text to be present all elements
	 * @return true once all elements contain the given text
	 */
	public static ExpectedCondition<Boolean> textToBePresentInAllElements(final By locator, final String text) {
      return new ExpectedCondition<Boolean>() {
          @Override
          public Boolean apply(WebDriver driver) {
              try {
                  List<WebElement> elements = driver.findElements(locator);
                  for (WebElement webElement : elements) {
                      String elementText = webElement.getText();
                      if (elementText != null) {
                          if(!elementText.toLowerCase().contains(text.toLowerCase())) {
                              return false;
                          }
                      }
                  }
                  return true;
              } catch (StaleElementReferenceException e) {
                  return null;
              }
          }

          @Override
          public String toString() {
              return String.format("text ('%s') to be present in all elements located by %s ignoring case", text, locator);
          }
      };
  }
	
	/**
  	 * An expectation for checking if the given text is present in the specified element. <br>
  	 * 
  	 * Equal to ExpectedConditions.textToBePresentInElementLocated() but ignoring case <br>
  	 * 
	 * This method can be used to wait for the ExpectedCondition to become true: <br>
	 * -> wait.until(textToBePresentInAllElements(locator, text));
	 * 
	 * @param locator used to find the element
	 * @param text to be present the element
	 * @return true once the element contains the given text
	 */
	public static ExpectedCondition<Boolean> textToBePresentInElementIgnoreCase(final By locator, final String text) {
      return new ExpectedCondition<Boolean>() {
          @Override
          public Boolean apply(WebDriver driver) {
              try {
                  String elementText = driver.findElement(locator).getText();
                  return elementText.toLowerCase().contains(text.toLowerCase());
              } catch (StaleElementReferenceException e) {
                  return null;
              }
          }

          @Override
          public String toString() {
              return String.format("text ('%s') to be present in element located by %s ignoring case", text, locator);
          }
      };
  }
	
}
