package spot.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HelpPage extends BasePage {

	@FindBy(xpath = ".//*[@id='imj_ajaxWrapper']/h1")
	private WebElement helpPageSubtitle;

	@FindBy(xpath = ".//*[@id='imj_ajaxWrapper']/a[32]")
	private WebElement contactSupportLink;

	@FindBy(xpath = ".//*[@id='imj_ajaxWrapper']/a[24]")
	private WebElement chapterFiveLink;

	public HelpPage(WebDriver driver) {
		super(driver);

		PageFactory.initElements(driver, this);
	}

	public String getHelpPageSubtitle() {
		return helpPageSubtitle.getText();
	}

	public String contactSupport() {
		clickChapterFive();
		return clickContactSupport();
	}

	private String clickContactSupport() {
		String href = contactSupportLink.getAttribute("href");

		return extractMailDestinationAddressFromLink(href);
	}

	public void lookUpImejiHomePage() {
		clickChapterFive();		
		lookUpImejiHomePage();
	}

	private void clickChapterFive() {
		chapterFiveLink.click();
	}

}
