package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SharePage extends BasePage {

	@FindBy(id="share:emailInput")
	private WebElement emailTextField;
	
	@FindBy(id="share:selSendEmail")
	private WebElement checkBoxSendMail;
	
	@FindBy(xpath=".//*[@id='share:j_idt174']/div/div[4]/input")
	private WebElement sendMailButton;
	
	public SharePage(WebDriver driver) {
		super(driver);
	}

	public void sendEmailtoUser(String emailUser) {
		emailTextField.sendKeys(emailUser);
		if (!checkBoxSendMail.isSelected())
			checkBoxSendMail.click();
		sendMailButton.click();
	}

}
