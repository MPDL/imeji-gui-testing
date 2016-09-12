package spot.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AllUsersOverViewPage extends BasePage {

	private List<WebElement> userList;
	
	public AllUsersOverViewPage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}

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

		deleteButton.click();
		
		wait.until(ExpectedConditions.visibilityOf(toBeDeletedUser));
		
		WebElement confirmDelete = toBeDeletedUser.findElement(By.cssSelector("div:nth-of-type(2)> div[id^=deleteUser] .imj_submitPanel form .imj_submitButton"));

		wait.until(ExpectedConditions.visibilityOf(confirmDelete));
		confirmDelete.click();
	}
	
	public boolean isEmailInUserList(String email) {
		WebElement userInQuestion = findUserByEmail(email);
		if (userInQuestion == null)
			return false;
		else
			return true;
	}

	private WebElement findUserByEmail(String emailInQuestion) {
		
		userList = driver.findElements(By.className("imj_admindataSet"));
		WebElement userInQuestion = null;
		
		for (WebElement user : userList) {
			WebElement userIdentification = user.findElement(By.tagName("h2"));
			String userNamePlusEmail = userIdentification.getText();
			String mail = extractMail(userNamePlusEmail);
			
			if (mail.equals(emailInQuestion)) {
				userInQuestion = user;
				break;
			}
		}
		return userInQuestion;
	}

	private String extractMail(String userNamePlusEmail) {
		// userNamePlusEmail looks sth like this:	familyName, givenName (email@test.de)
		// aiming to get only email-address
		userNamePlusEmail = userNamePlusEmail.trim();
		int tmpIndex = userNamePlusEmail.indexOf('(');
		String email = userNamePlusEmail.substring(tmpIndex+1, userNamePlusEmail.length()-1);
		return email;
	}

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
		
		viewProfileButton.click();
		
		return PageFactory.initElements(driver, UserProfilePage.class);
	}
	
	public void addUserToUserGroup(String userEmail) {
		WebElement userInQuestion = findUserByEmail(userEmail);
		WebElement addToUserGroupButton = userInQuestion.findElement(By.cssSelector("form>a"));
		addToUserGroupButton.click();
		
		
	}
	
	public int userCount() {
		List <WebElement> userCount = driver.findElements(By.className("imj_userConfig"));
		return userCount.size();
	}
	
}
