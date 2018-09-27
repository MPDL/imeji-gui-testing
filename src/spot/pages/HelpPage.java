package spot.pages;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HelpPage extends BasePage {

	@FindBy(xpath = ".//*[@id='imj_ajaxWrapper']/h1")
	private WebElement helpPageSubtitle;

	public HelpPage(WebDriver driver) {
		super(driver);

		PageFactory.initElements(driver, this);
	}

	public String getHelpPageSubtitle() {
		return helpPageSubtitle.getText();
	}

	// IMJ-5
	public List<String> contactSupport() {
		List<String> supportEmails = new LinkedList<String>();
		List<WebElement> contactSupportLinks = driver.findElements(By.partialLinkText("support team"));
		for (WebElement supportLink : contactSupportLinks) {
			String href = supportLink.getAttribute("href");
			supportEmails.add(extractMailDestinationAddressFromLink(href));
		}

		return supportEmails;
	}

	// IMJ-4
	public void lookUpImejiHomePage() {		
		super.lookUpImejiHomePage();
	}

}
