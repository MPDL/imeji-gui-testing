package spot.pages.admin;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;
import test.base.SeleniumTestSuite;

/**
 * The class represents the users page, which lists all the users in the admin area.
 * 
 * @author helk
 *
 */
public class BrowseUsersPage extends BasePage{
	
	private static final Logger log4j = LogManager.getLogger(SeleniumTestSuite.class.getName());

	@FindBy(id="#createUser")
	private WebElement createNewUser;
	
	public BrowseUsersPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	public UserProfilePage createNewUser(String newUserEmailAddress) {
		this.createNewUser.click();
		NewUserPage createNewUserPage = new NewUserPage(driver);
		return createNewUserPage.createNewUser(newUserEmailAddress);
	}
	
	public UserProfilePage viewDetailsOfUser(String userEmailAddress) {
		WebElement userSet = this.findUserByEmail(userEmailAddress);
		WebElement viewDetails = userSet.findElement(By.xpath(".//a[contains(@class,'imj_menuButton') and contains(text()[2],'View details')]"));
		viewDetails.click();
		
		return PageFactory.initElements(driver, UserProfilePage.class);
	}
	
	public BrowseUsersPage activateUser(String userEmailAddress) {
		WebElement userSet = this.findUserByEmail(userEmailAddress);
		WebElement activateButton = userSet.findElement(By.xpath(".//a[contains(@class,'imj_menuButton') and contains(text()[2],'Activate')]"));
		activateButton.click();
		
		WebElement confirmationDialog = userSet.findElement(By.xpath(".//div[contains(@class,'imj_modalDialogBox') and contains(@id,'reactivateUser')]"));
		WebElement confirmReactivateButton = confirmationDialog.findElement(By.xpath(".//input[contains(@class,'imj_submitButton') and @value='Activate']"));
		WebElement staleElement =  driver.findElement(By.id("createUser"));
		confirmReactivateButton.click();
		
		wait.until(ExpectedConditions.stalenessOf(staleElement));
		
		return PageFactory.initElements(driver, BrowseUsersPage.class);
	}
	
	public BrowseUsersPage deactivateUser(String userEmailAddress) {
		WebElement userSet = this.findUserByEmail(userEmailAddress);
		WebElement deactivateButton = userSet.findElement(By.xpath(".//a[contains(@class,'imj_menuButton') and contains(text()[2],'deactivate')]"));
		deactivateButton.click();
		
		WebElement confirmationDialog = userSet.findElement(By.xpath(".//div[contains(@class,'imj_modalDialogBox') and contains(@id,'removeUser')]"));
		WebElement confirmDeactivateButton = confirmationDialog.findElement(By.xpath(".//input[contains(@class,'imj_submitButton') and @value='deactivate']"));
		WebElement staleElement =  driver.findElement(By.id("createUser"));
		confirmDeactivateButton.click();
		
		wait.until(ExpectedConditions.stalenessOf(staleElement));
		
		return PageFactory.initElements(driver, BrowseUsersPage.class);
	}
	
	public BrowseUsersPage deleteUser(String userEmailAddress) {
		// The user must be deactivated to delete it.
		this.deactivateUser(userEmailAddress);
		
		WebElement userSet = this.findUserByEmail(userEmailAddress);
		WebElement deleteButton = userSet.findElement(By.xpath(".//a[contains(@class,'imj_menuButton') and contains(text()[2],'Delete')]"));
		deleteButton.click();
		
		WebElement confirmationDialog = userSet.findElement(By.xpath(".//div[contains(@class,'imj_modalDialogBox') and contains(@id,'deleteUser')]"));
		WebElement confirmDeleteButton = confirmationDialog.findElement(By.xpath(".//input[contains(@class,'imj_submitButton') and @value='Delete user']"));
		WebElement staleElement =  driver.findElement(By.id("createUser"));
		confirmDeleteButton.click();
		
		wait.until(ExpectedConditions.stalenessOf(staleElement));
		
		return PageFactory.initElements(driver, BrowseUsersPage.class);
	}
	
	private WebElement findUserByEmail(String userEmailAddress) {
		//FIXME: Find user by EXACT match of familyname, name and email
		//For now the first user-webelement that CONTAINS the  given email address is being returned
		WebElement userLabel = driver.findElement(By.xpath("//div[@class='imj_admindataLabel' and contains(text(),'("+ userEmailAddress +")')]"));
		WebElement userSet = userLabel.findElement(By.xpath(".."));
		return userSet;
	}
	
	public boolean isEmailInUserList(String userEmailAddress) {
		List<WebElement> userLabels = driver.findElements(By.xpath("//div[@class='imj_admindataLabel' and contains(text(),'("+ userEmailAddress +")')]"));
		
		int userCount = userLabels.size();
		
		if(userCount == 1){
			return true;
		} else if(userCount > 1){
			log4j.warn("More than one user with the email-address '"+ userEmailAddress +"' found.");
			return true;
		} else {
			return false;
		}
	}
	
	public int userCount() {
		WebElement usersFound = driver.findElement(By.xpath("//div[@class='imj_admindataLabel' and contains(text()[1],'Users found')]"));
		String usersFoundText = usersFound.getText();
		
		String userCountText = usersFoundText.replace(" Users found", "");
		int userCount = Integer.parseInt(userCountText);
		
		return userCount;
	}
}
