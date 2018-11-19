package spot.pages.admin;

import static test.base.SeleniumWrapper.waitForReloadOfElement;

import java.util.List;

import org.openqa.selenium.By;
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
	
	// IMJ-41
	private void selectUserFromUserList(String userEmail) {
	    WebElement userList = driver.findElement(By.id("userList"));
	  
	    WebElement userLink = driver.findElement(By.xpath("//div[@id='addUser']//div[contains(@id,'userList')]//a[contains(@onclick,'addUser') and contains(text(),'" + "(" + userEmail + ")" + "')]"));
	    userLink.click();
	    
	    wait.until(ExpectedConditions.stalenessOf(userList));
	}
	
	// IMJ-41
	public void addUserToUserGroup(String userEmail) {
	    this.selectUserFromUserList(userEmail);
	}
		
}
