package spot.pages.admin;

import static test.base.SeleniumWrapper.waitForReloadOfElement;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
	
	public boolean isEmailInUserList(String email) {
		try {
			findUserByEmail(email);
			return true;
		}
		catch (NoSuchElementException exc) {
			return false;
		}
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
	
	private WebElement findUserByEmail(String emailInQuestion) {
		WebElement searchBox = driver.findElement(By.xpath("//input[contains(@id, ':userListForm:filterMd')]"));
		searchBox.clear();
		searchBox.sendKeys(emailInQuestion);
		
		//TODO: Find a working, clear and efficient way to filter + select the users from the list. Maybe use the retryingFindClick-method. Remove the workaround.
		// Workaround: First wait for the whole email to be sent to the searchBox, then wait till the loaderWrapper is not visible any more.
		// This workaround works most of the time. See filterUserList-method for a correct but very slow approach.
		wait.until(ExpectedConditions.attributeToBe(searchBox, "value", emailInQuestion));
		WebElement loaderWrapper = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".loaderWrapper[style]")));
		wait.until(ExpectedConditions.invisibilityOf(loaderWrapper));
		
		if (userList.size() == 1)
			return userList.get(0);
		for (WebElement currentEmail : userList) {
			if (currentEmail.getText().contains(emailInQuestion)) {
				return currentEmail;
			}
		}
		throw new NoSuchElementException("Email " + emailInQuestion + " was not found.");
	}
	
	// IMJ-41
	public void addUserToUserGroup(String userEmail) {
		WebElement userInQuestion = findUserByEmail(userEmail);
		userInQuestion.click();
		
		// Wait until the user group page starts reloading, after clicking userInQuestion (then the userInQuestion element is stale)
		wait.until(ExpectedConditions.stalenessOf(userInQuestion));
	}
	
}
