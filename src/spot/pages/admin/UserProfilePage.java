package spot.pages.admin;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import spot.pages.BasePage;
import spot.pages.registered.SharePage;

public class UserProfilePage extends BasePage {

	@FindBy(xpath=".//*[@id='userForm:userInfos']/div[2]/div[2]/span")
	private WebElement completeNameLabel;
	
	@FindBy(xpath =".//*[@id='userForm:inputPassword']")
	private WebElement newPwdTextField;
	
	@FindBy(xpath =".//*[@id='userForm:inputRespeated']")
	private WebElement repeatNewPwdTextField;
	
	@FindBy(css=".imj_admindataSetEdit .imj_editButton")
	private WebElement confirmPwdChangeButton;
	
	@FindBy(id="userForm:lnkEditUserdata")
	private WebElement editProfileButton;
	
	@FindBy(id="grantForm:admincheck1")
	private WebElement adminCheckbox;
	
	public UserProfilePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public EditUserProfilePage editProfile() {
		editProfileButton.click();
		
		return PageFactory.initElements(driver, EditUserProfilePage.class);
	}
	
	public void changePassword() {
		String newPwd = generateRandomPwd();
		enterNewPassword(newPwd);
		repeatNewPassword(newPwd);
		confirmPwdChange();
	}

	private void confirmPwdChange() {
		confirmPwdChangeButton.click();
	}

	private void repeatNewPassword(String newPwd) {
		repeatNewPwdTextField.sendKeys(newPwd);		
	}

	private void enterNewPassword(String newPwd) {
		newPwdTextField.sendKeys(newPwd);
	}

	private String generateRandomPwd() {
		return RandomStringUtils.randomAscii(15);	
	}

	public boolean checkFamilyName(String newFamilyName) {
		boolean editSucceeded = false;
		
		wait.until(ExpectedConditions.visibilityOf(completeNameLabel));
		String completeName = completeNameLabel.getText();
		
		String familyName = extractFamilyName(completeName);
		
		if (familyName.equals(newFamilyName))
			editSucceeded = true;
		
		return editSucceeded;
	}
	
	private String extractFamilyName(String completeName) {
		// completeName looks like this: [familyName], [givenName]
		String familyName = "";
		int tmpIndex = completeName.indexOf(',');
		familyName = completeName.substring(0, tmpIndex);
		
		return familyName;
	}
	
	public UserProfilePage giveAdminRights() {
		return switchAdminRights(true);
	}
	
	public UserProfilePage withdrawAdminRights() {
		return switchAdminRights(false);
	}
	
	public boolean isAdmin() {
		return adminCheckbox.isSelected();
	}
	
	private UserProfilePage switchAdminRights(boolean administrator) {
		if (administrator) {
			if (!adminCheckbox.isSelected()) {
				adminCheckbox.click();
			}
		}
		else {
			adminCheckbox.click();
		}
		wait.until(ExpectedConditions.elementToBeClickable(By.className("imj_submitButton")));
		driver.findElement(By.className("imj_submitButton")).click();
		
		return PageFactory.initElements(driver, UserProfilePage.class);
	}
	
	public SharePage changeUserGrants(String collectionName) {
	  WebElement collectionSet = driver.findElement(By.xpath("//span[@title='" + collectionName + "']/../.."));
	  WebElement changeButton = collectionSet.findElement(By.xpath(".//a[contains(@title,'Change')]"));
	  
	  changeButton.click();
	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userGroupShare")));
	  return PageFactory.initElements(driver, SharePage.class);
	}
	
	public UserProfilePage revokeUserGrants(String collectionName) {
      WebElement collectionSet = driver.findElement(By.xpath("//span[@title='" + collectionName + "']/../.."));
      WebElement revokeButton = collectionSet.findElement(By.xpath(".//a[contains(@title,'Revoke')]"));
      
      revokeButton.click();
      WebElement revokeConfirmationButton = collectionSet.findElement(By.xpath(".//input[@value='Revoke']"));
      wait.until(ExpectedConditions.visibilityOf(revokeConfirmationButton));
      
      revokeConfirmationButton.click();
      //FIXME: The UserProfilePage is Not reloaded by imeji! Refresh the page by selenium-driver:
      driver.navigate().refresh();
      wait.until(ExpectedConditions.stalenessOf(collectionSet));
      
      return PageFactory.initElements(driver, UserProfilePage.class);
	}
	
}
