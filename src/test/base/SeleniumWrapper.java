package test.base;

import org.openqa.selenium.WebElement;
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
	 * Wait until the current page is reloaded. <br>
	 * Uses waiting for an element to become stale (not present on the DOM) to detect that the old page doesn't exist anymore. <br>
	 * The staleElement must be a WebElement of the old page. It must be initialized before the event, that led to the page reload. <br>
	 * The staleElement must NOT be a Page Object field, which uses lazy loading.
	 * 
	 * @param wait The WebDriverWait, which defines the wait timeout in seconds
	 * @param staleElement An element of the current page which becomes stale after the page reload 
	 */
	public static void waitForReloadOfCurrentPage(WebDriverWait wait, WebElement staleElement) {
		wait.until(ExpectedConditions.stalenessOf(staleElement));
	}
	
	/**
	 * Wait until a certain component/element of the page is reloaded. <br>
	 * Uses waiting for the element to become stale (not present on the DOM) to detect that the old element doesn't exist anymore. <br>
	 * The elementToBecomeReloaded must be the WebElement that will be reloaded. It must be initialized before the event, that led to the partial reload. <br>
	 * The elementToBecomeReloaded must NOT be a Page Object field, which uses lazy loading.
	 * 
	 * @param wait The WebDriverWait, which defines the wait timeout in seconds
	 * @param elementToBecomeReloaded An element of the current page which will be reload 
	 */
	public static void waitForReloadOfElement(WebDriverWait wait, WebElement elementToBecomeReloaded) {
		wait.until(ExpectedConditions.stalenessOf(elementToBecomeReloaded));
	}
	
	/**
	 * Wait until the new/next page is loaded. <br>
	 * Uses waiting for an old element to become invisible (not present on the DOM) to detect that the old page doesn't exist anymore. <br>
	 * The oldElement must be a WebElement of the old page. It must be initialized before the event, that led to the page reload. <br>
	 * The oldElement must can be a Page Object field, which uses lazy loading.
	 * 
	 * @param wait The WebDriverWait, which defines the wait timeout in seconds
	 * @param oldElement An element of the current page which becomes invisible after the page reload 
	 */
	public static void waitForLoadOfNewPage(WebDriverWait wait, WebElement oldElement) {
		wait.until(ExpectedConditions.invisibilityOf(oldElement));
	}
	
}
