package spot.pages.admin;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;

public class UsersOverviewPage extends BasePage {

	@FindBy(css="#addUser p")
	private List<WebElement> userList;
	
	public UsersOverviewPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

	/**
	 * @throws NoSuchElementException if no email match is found
	 */
	public void deleteUserByEmail(String email) {
		WebElement toBeDeletedUser = findUserByEmail(email);
		List<WebElement> tmpElementList = toBeDeletedUser.findElements(By.tagName("a"));
		
		WebElement deleteButton = null;
		
		for (WebElement tmpWE : tmpElementList) {
			String titleName = tmpWE.getAttribute("title");
			if (titleName.equals("Delete user")) {
				deleteButton = tmpWE;
				break;
			}
		}
		
		if (deleteButton == null)
			throw new NoSuchElementException("No user email matches were found.");

		deleteButton.click();
		wait.until(ExpectedConditions.visibilityOf(toBeDeletedUser));
		
		WebElement confirmDelete = toBeDeletedUser.findElement(By.cssSelector("div:nth-of-type(2)> div[id^=deleteUser] .imj_submitPanel form .imj_submitButton"));
		wait.until(ExpectedConditions.visibilityOf(confirmDelete));
		confirmDelete.click();
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

	private WebElement findUserByEmail(String emailInQuestion) {
		WebElement searchBox = driver.findElement(By.xpath("//input[contains(@id, ':userListForm:filterMd')]"));
		searchBox.clear();
		searchBox.sendKeys(emailInQuestion);
		try { Thread.sleep(2000); } catch (InterruptedException e) {}
		userList = driver.findElements(By.cssSelector("#addUser p>a"));
		if (userList.size() == 1)
			return userList.get(0);
		for (WebElement currentEmail : userList) {
			if (currentEmail.getText().contains(emailInQuestion)) {
				return currentEmail;
			}
		}
		throw new NoSuchElementException("Email " + emailInQuestion + " was not found.");
	}

	/**
	 * @throws NoSuchElementException if no user name matches userName
	 */
	public UserProfilePage viewDetails(String userName) {
		WebElement userInQuestion = findUserByEmail(userName);
		
		List<WebElement> tmpElementList = userInQuestion.findElements(By.tagName("a"));
		
		WebElement viewProfileButton = null;
		
		for (WebElement tmpWE : tmpElementList) {
			String href = tmpWE.getAttribute("href");
			if (href.startsWith("http://qa-edmond.mpdl.mpg.de/imeji/user?id=")) {
				viewProfileButton = tmpWE;
				break;
			}
		}
		
		if (viewProfileButton == null)
			throw new NoSuchElementException("No user matches the given username.");
			
		viewProfileButton.click();
		
		return PageFactory.initElements(driver, UserProfilePage.class);
	}
	
	public void addUserToUserGroup(String userEmail) {
		// sleep() does not help here
		try {
			WebElement userInQuestion = findUserByEmail(userEmail);
			userInQuestion.click();
		}
		catch (StaleElementReferenceException exc) {
			userList = driver.findElements(By.cssSelector("#addUser p>a"));
			userList.get(0).click();
		}
	}
	
	public int userCount() {
		List <WebElement> userCount = driver.findElements(By.className("imj_userConfig"));
		return userCount.size();
	}
	
}
