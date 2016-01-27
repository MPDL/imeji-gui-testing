package spot.pages.admin;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import spot.pages.BasePage;

public class ViewEditUsersPage extends BasePage {

	@FindBy (xpath ="html/body/div[1]/div[4]/div[3]/div/div[2]")
	private WebElement userList;
	
	
	public ViewEditUsersPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	/**
	 * Deleting an user from database.
	 * 
	 * @param toBeDeletedUsersEmailAddress - to be deleted user is identified by his email address
	 */
	public void deleteUser(String toBeDeletedUsersEmailAddress) {
		
		WebElement toBeDeletedUser = null;
		int index = 0;
		
		for (WebElement user : getAllUsers()) {
			WebElement userWebElement = user.findElement(By.tagName("h2"));
			if (userWebElement.getText().contains(toBeDeletedUsersEmailAddress)) {
				toBeDeletedUser = user;
				break;
			}
			index++;
		}
		
		if (toBeDeletedUser != null) {
			WebElement deleteButton = toBeDeletedUser.findElement(By.cssSelector("a.imj_cancelButton"));
			deleteButton.click();
			
			WebElement deletionConfig = toBeDeletedUser.findElement(By.id("deleteUser"+index));
			WebElement confirmDeletionButton = deletionConfig.findElement(By.className("imj_submitButton"));
			confirmDeletionButton.click();
		}		
	}

	public boolean isUserPresent(String toBeDeletedUsersEmailAddress) {
		
		boolean isUserPresent = false;		
		
		for (WebElement user : getAllUsers()) {
			WebElement userWebElement = user.findElement(By.tagName("h2"));
			if (userWebElement.getText().contains(toBeDeletedUsersEmailAddress)) {
				isUserPresent = true;
				break;
			}
		}
		
		return isUserPresent;
	}
	
	/**
	 * Method returns all registered users.
	 * @return
	 */
	private List<WebElement> getAllUsers() {
		return userList.findElements(By.cssSelector("div.imj_adminPanel.imj_userConfig"));
	}
}
