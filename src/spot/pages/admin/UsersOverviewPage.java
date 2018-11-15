package spot.pages.admin;

import static test.base.SeleniumWrapper.textToBePresentInAllElements;
import static test.base.SeleniumWrapper.waitForReloadOfElement;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

//FIXME: This is not the User-Overview-Page, but the User-List-Component. Refactor/Rename this class: Make it a component and i.a. part of the UserGroupPage.
public class UsersOverviewPage extends BasePage {

	@FindBy(css="#addUser p>a")
	private List<WebElement> userList;
	
	public UsersOverviewPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
		
	/**
	 * Filters the user list for the query string. Returns only the non member user links. <br>
	 * This method has a high runtime => Use this method only to test the filter. <br>
	 * Reason: The user list is reloaded after every single query-input-character, therefore there must be a wait after every sent character.
	 * 
	 * @param query The query string
	 * @return All users whose name or email starts with the query string
	 */
	private List<WebElement> filterUserList(String query){
		WebElement userListElement = driver.findElement(By.xpath("//div[contains(@id, ':userListForm:userList')]"));
		
		WebElement searchBox = driver.findElement(By.xpath("//input[contains(@id, ':userListForm:filterMd')]"));
		searchBox.clear();
		
		char[] charArray = query.toCharArray();
		for (char c : charArray) {
			userListElement = driver.findElement(By.xpath("//div[contains(@id, ':userListForm:userList')]"));
			searchBox.sendKeys(Character.toString(c));
			waitForReloadOfElement(wait, userListElement);
		}
		
		return userList;
	}
	
	private void addUserUsingFilter(String emailInQuestion) {
		WebElement searchBox = driver.findElement(By.xpath("//input[contains(@id, ':userListForm:filterMd')]"));
		searchBox.clear();
		searchBox.sendKeys(emailInQuestion);
		
		// Wait until no more candidates are loaded (by ajax) can only be approximated with waits:
        // 1) Wait until the full email name is typed in the searchBox
		wait.until(ExpectedConditions.attributeToBe(searchBox, "value", emailInQuestion));
        // 2) Wait until all names of all candidates contain the full name-key
        wait.until(textToBePresentInAllElements(By.xpath("//div[@id='addUser']/form/div[contains(@id,'userList')]/descendant::a[contains(@onclick,'addUser')]"), emailInQuestion));
        try {
          // 3) Wait until the searched email is visible (if not: the user with that email is not available => throws TimeoutException)
          wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='addUser']/form/div[contains(@id,'userList')]/descendant::a[contains(@onclick,'addUser') and contains(text(),'" + "(" + emailInQuestion + ")" + "')]")));
          // The element to click can still be stale (if there is another ajax reload). Therefore use retryFindClick to avoid a stale exception.
          retryingFindClick(By.xpath("//div[@id='addUser']/form/div[contains(@id,'userList')]/descendant::a[contains(@onclick,'addUser') and contains(text(),'" + "(" + emailInQuestion + ")" + "')]"));
        } catch (TimeoutException exc) {
          throw new NoSuchElementException("Email " + emailInQuestion + " was not found.");
        }
        
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("addUser")));
	}
	
	// IMJ-41
	public void addUserToUserGroup(String userEmail) {
	    addUserUsingFilter(userEmail);
	}
		
}
