package spot.pages;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class UserProfilePage extends BasePage {

	@FindBy(xpath =".//*[@id='userForm:inputPassword']")
	private WebElement newPwdTextField;
	
	@FindBy(xpath =".//*[@id='userForm:inputRespeated']")
	private WebElement repeatNewPwdTextField;
	
	@FindBy(xpath =".//*[@id='userForm:j_idt330']")
	private WebElement confirmPwdChangeButton;
	
	public UserProfilePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
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
	
	
}
